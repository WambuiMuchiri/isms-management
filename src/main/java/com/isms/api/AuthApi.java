package com.isms.api;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.catalina.User;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.isms.dto.LoginDto;
import com.isms.dto.UserDTO;
import com.isms.model.AuditIdentifier;
import com.isms.model.Roles;
import com.isms.model.Users;
import com.isms.repository.RolesRepository;
import com.isms.repository.UserRepository;
import com.isms.security.MyUserDetailsService;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.RequestService;
import com.isms.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApi {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuditIdentifierService auditIdentifierService;

	@Autowired
	private AuditEventsService auditEventsService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private AuditTypesService auditTypesService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RolesRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	private MyUserDetailsService userDetailsService;

	public AuthApi(MyUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto, HttpServletRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			Users appUser = userRepository.findByUsername(userDetails.getUsername());
			if (appUser == null) {
				throw new RuntimeException("User not found");
			}

			// 🪙 Generate access token with custom claims
			String accessToken = JWT.create().withSubject(appUser.getUsername())
					.withExpiresAt(new Date(System.currentTimeMillis() + 7 * 60 * 60 * 1000)) // 7 days
					.withIssuer(request.getRequestURL().toString())
					.withClaim("roles",
							userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
									.collect(Collectors.toList()))
					.withClaim("email", appUser.getEmail()).withClaim("firstName", appUser.getFirstName())
					.withClaim("lastName", appUser.getLastName()).withClaim("userImage", appUser.getUserLogoPath()) // <--
																													// add
																													// this
																													// field
																													// in
																													// entity
					.sign(algorithm);

			// ♻️ Refresh token with minimal claims
			String refreshToken = JWT.create().withSubject(appUser.getUsername())
					.withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
					.withIssuer(request.getRequestURL().toString()).sign(algorithm);

			Map<String, String> tokens = new HashMap<>();
			tokens.put("access_token", accessToken);
			tokens.put("refresh_token", refreshToken);

			return ResponseEntity.ok(tokens);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("error", "Invalid username or password"));
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult,
			@RequestParam("userLogo") MultipartFile multipartFile) throws IOException {
		// Validate input first
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
		}

		// Confirm password check
		if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
			return ResponseEntity.badRequest().body(Map.of("error", "Passwords do not match"));
		}

		// Check if Username or Email already exists
		if (userService.getUser(userDTO.getUsername()) != null) {
			return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
		}

		if (userService.getUserByEmail(userDTO.getEmail()) != null) {
			return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));
		}

		// Validate role
		Roles role = roleRepository.findByName("USER");
		if (role == null) {
			return ResponseEntity.badRequest().body(Map.of("error", "Default user role not found"));
		}

		// Create audit identifier
		AuditIdentifier auditIdentifier = new AuditIdentifier();
		auditIdentifierService.saveAuditIdentifier(auditIdentifier);

		// Create user object
		Users user = new Users();

		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setStatus(userDTO.getStatus());
		user.setRemarks(userDTO.getRemarks());
		user.setRoles(Collections.singleton(role));

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		user.setUserLogo(fileName);

		user.setAuditIdentifierId(auditIdentifier);

		// Save user
		try {
			Users savedUser = userService.saveUser(user);

			String uploadDir = "./user-logos/" + savedUser.getId();
//		     String uploadDir = "/var/spring/user-logos/"+savedUser.getId();
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				System.out.println(filePath.toString());
				System.out.println(filePath.toFile().getAbsolutePath());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new IOException("Could not save the uploaded User Logo File : " + fileName, e);
			}

			return ResponseEntity.ok(savedUser);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Registration failed: " + e.getMessage()));
		}
	}
//	@PostMapping(value = "/signup", consumes = "multipart/form-data")
//	public ResponseEntity<?> registerUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult,
//			@RequestParam("userLogo") MultipartFile multipartFile) throws IOException {
//Log.info("User Data:"+userDTO.toString());
//		if (bindingResult.hasErrors()) {
//			return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().stream()
//					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
//		}
//
//		if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
//			return ResponseEntity.badRequest().body(Map.of("error", "Passwords do not match"));
//		}
//
//		if (userService.getUser(userDTO.getUsername()) != null) {
//			return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
//		}
//
//		if (userService.getUserByEmail(userDTO.getEmail()) != null) {
//			return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));
//		}
//
//		Roles role = roleRepository.findByName("ROLE_USER");
//		if (role == null) {
//			return ResponseEntity.badRequest().body(Map.of("error", "Default user role not found"));
//		}
//
//		AuditIdentifier auditIdentifier = new AuditIdentifier();
//		auditIdentifierService.saveAuditIdentifier(auditIdentifier);
//
//		Users user = new Users();
//		user.setFirstName(userDTO.getFirstName());
//		user.setLastName(userDTO.getLastName());
//		user.setUsername(userDTO.getUsername());
//		user.setEmail(userDTO.getEmail());
//		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//		user.setStatus(userDTO.getStatus());
//		user.setRemarks(userDTO.getRemarks());
//		user.setRoles(Collections.singleton(role));
//		user.setAuditIdentifierId(auditIdentifier);
//
//		// Save user first (so we get the ID)
//		Users savedUser = userService.saveUser(user);
//
//		// Save logo
//		if (!multipartFile.isEmpty()) {
//			String fileName = UUID.randomUUID().toString() + "_"
//					+ StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			String uploadDir = "./user-logos/" + savedUser.getId();
//			Path uploadPath = Paths.get(uploadDir);
//
//			if (!Files.exists(uploadPath)) {
//				Files.createDirectories(uploadPath);
//			}
//
//			try (InputStream inputStream = multipartFile.getInputStream()) {
//				Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
//				savedUser.setUserLogo(fileName);
//				userService.saveUser(savedUser);
//			}
//		}
//
//		// Return safe DTO
//		Map<String, Object> response = new HashMap<>();
//		response.put("id", savedUser.getId());
//		response.put("username", savedUser.getUsername());
//		response.put("email", savedUser.getEmail());
//		response.put("firstName", savedUser.getFirstName());
//		response.put("lastName", savedUser.getLastName());
//		response.put("userLogo", savedUser.getUserLogoPath());
//
//		return ResponseEntity.ok(response);
//	}

	@PostMapping("/token/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refreshToken = authorizationHeader.substring("Bearer ".length());

				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refreshToken);

				String username = decodedJWT.getSubject();
				Users appUser = userRepository.findByUsername(username);
				if (appUser == null) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							.body(Map.of("error_message", "User not found"));
				}

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				// ✅ Null-safe roles extraction
				var authorities = userDetails.getAuthorities() != null ? userDetails.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).collect(Collectors.toList()) : Collections.emptyList();

				// ✅ Generate access token with full claims
				String accessToken = JWT.create().withSubject(userDetails.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
						.withIssuer(request.getRequestURL().toString()).withClaim("roles", authorities)
						.withClaim("email", appUser.getEmail()).withClaim("firstName", appUser.getFirstName())
						.withClaim("lastName", appUser.getLastName()).withClaim("userImage", appUser.getUserLogoPath())
						.sign(algorithm);

				// ✅ Return new tokens + user info
				Map<String, Object> responseBody = new HashMap<>();
				responseBody.put("access_token", accessToken);
				responseBody.put("refresh_token", refreshToken);
				responseBody.put("username", appUser.getUsername());
				responseBody.put("roles", authorities);
				responseBody.put("email", appUser.getEmail());
				responseBody.put("firstName", appUser.getFirstName());
				responseBody.put("lastName", appUser.getLastName());
				responseBody.put("userImage", appUser.getUserLogoPath());

				return ResponseEntity.ok(responseBody);

			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(Map.of("error_message", "Invalid refresh token"));
			}
		}

		return ResponseEntity.badRequest().body(Map.of("error_message", "Missing refresh token"));
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT jwt = verifier.verify(token);

				Users appUser = userRepository.findByUsername(jwt.getSubject());
				if (appUser == null) {
					throw new RuntimeException("User not found");
				}

				Map<String, Object> userDetails = new HashMap<>();
				userDetails.put("username", appUser.getUsername());
				userDetails.put("roles", jwt.getClaim("roles").asList(String.class));
				userDetails.put("email", appUser.getEmail());
				userDetails.put("firstName", appUser.getFirstName());
				userDetails.put("userLogo", appUser.getUserLogoPath());

				return ResponseEntity.ok(userDetails);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing token"));
	}

}
