package com.isms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isms.dto.CommonSyncResponse;
import com.isms.dto.InternalB2CTransactionRequest;
import com.isms.dto.PaymentsDTO;
import com.isms.helper.DataForDatatableHelper;
import com.isms.helper.SaveEventsHelper;
import com.isms.messages.EmailTemplate;
import com.isms.model.AuditIdentifier;
import com.isms.model.B2C_C2B_Entries;
import com.isms.model.Clients;
import com.isms.model.Payments;
import com.isms.service.AuditEventsService;
import com.isms.service.AuditIdentifierService;
import com.isms.service.AuditTypesService;
import com.isms.service.ClientsService;
import com.isms.service.DarajaApiService;
import com.isms.service.EmailService;
import com.isms.service.PaymentJobItemService;
import com.isms.service.PaymentsService;
import com.isms.service.ReceiptNumberGeneratorService;
import com.isms.service.RequestService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/payments")
public class PaymentsController {

	@Autowired
	private PaymentsService paymentsService;
	@Autowired
	private AuditIdentifierService auditIdentifierService;
	@Autowired
	private AuditEventsService auditEventsService;
	@Autowired
	private AuditTypesService auditTypesService;
	@Autowired
	private RequestService requestService;
	@Autowired
	private ClientsService clientsService;
	@Autowired
	private PaymentJobItemService paymentJobItemService;
	@Autowired
	private DarajaApiService darajaApiService;
	@Autowired
	public EmailService emailService;
	@Autowired
	public ReceiptNumberGeneratorService receiptNumberGeneratorService;

	@GetMapping(value = { "/index", "/", "" })
	public String index(Model model) {
		this.paymentJobItemService.refreshPaymentJobItem();
		return "payments/index";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("paymentsdto", new PaymentsDTO());
		model.addAttribute("listClients", clientsService.getAllClients());
		return "payments/create";
	}

	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("paymentsdto") PaymentsDTO paymentsdto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @RequestHeader(value = "User-Agent") String userAgent,
			HttpServletRequest request, Model model, @RequestParam("paymentProof") MultipartFile multipartFile)
			throws IOException, MessagingException {
		if (!bindingResult.hasErrors()) {
			AuditIdentifier auditIdentifier = new AuditIdentifier();
			auditIdentifierService.saveAuditIdentifier(auditIdentifier);

			Payments payment = new Payments();
			payment.setPaymentDate(paymentsdto.getPaymentDate());
			payment.setAmountPaid(paymentsdto.getAmountPaid());
			payment.setPaidBy(paymentsdto.getPaidBy());

//		    payment.setClients(paymentsdto.getClients());

			Clients client = clientsService.getClient(paymentsdto.getClients());
			if (client == null) {
				throw new RuntimeException("Client not found for id : " + client);
			}
			payment.setClients(client);

			// generate unique receipt no
			payment.setReceiptNo(receiptNumberGeneratorService.next());

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			payment.setPaymentProof(fileName);

			payment.setAuditIdentifierId(auditIdentifier);
			Payments savedPayment = paymentsService.savePayment(payment);

			SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(),
					"New Payment for Client No:  " + payment.getClients().getClientName() + "  has been created.",
					requestService.getClientIp(request), userAgent, auditIdentifier,
					auditTypesService.findAuditTypesByName("CREATE"));
			auditEventsService.saveAuditEvents(saveEventsHelper);

			// Save file to disk (if provided)
			java.io.File storedFile = null;
			if (fileName != null) {
				String uploadDir = "./payment-proofs/" + savedPayment.getId();

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
					throw new IOException("Could not save the uploaded Payment Proof File : " + fileName, e);
				}
			}

			redirectAttributes.addFlashAttribute("success", "Payment saved successfully!!");

			// Generate The Template to send OTP
			EmailTemplate template = new EmailTemplate("templates/ReceivedCashPayment.html");
			Map<String, String> replacements = new HashMap<>();
			replacements.put("clients", client.getClientName());
			replacements.put("receiptNo", payment.getReceiptNo());
			replacements.put("balanceBF", String.valueOf(paymentsdto.getBalanceBF()));
			replacements.put("amountPaid", String.valueOf(paymentsdto.getAmountPaid()));
			replacements.put("balanceCF", String.valueOf(paymentsdto.getBalanceCF()));
			String message = template.getTemplate(replacements);

			// Send with attachment/inline
			emailService.sendAssignmentNotificationMessage(client.getEmailAddress(),
					"PAYMENT - ISMS" , message);

			System.out.println("This is the string --->>" + message);

			return "redirect:/payments/index";
		} else {
			return "payments/create";
		}
	}

	@GetMapping("/update/{id}")
	public String update(@PathVariable(value = "id") int id, Model model) {
		Payments payment = paymentsService.getPayment(id);
		PaymentsDTO paymentdto = new PaymentsDTO(payment.getId(), payment.getPaymentDate(), payment.getAmountPaid(),
				payment.getPaidBy(), payment.getPaymentProof(), payment.getClients().getId());
		model.addAttribute("paymentsdto", paymentdto);
		model.addAttribute("listClients", clientsService.getAllClients());
		return "payments/update";
	}

	@PostMapping("/update/{id}")
	public String update(@PathVariable(value = "id") int id, @Valid @ModelAttribute("payment") PaymentsDTO paymentdto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes,
			@RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request, Model model,
			@RequestParam("paymentProof") MultipartFile multipartFile) throws IOException {
		if (!bindingResult.hasErrors()) {
			Payments payment = paymentsService.getPayment(paymentdto.getId());
			payment.setPaymentDate(paymentdto.getPaymentDate());
			payment.setAmountPaid(paymentdto.getAmountPaid());
			payment.setPaidBy(paymentdto.getPaidBy());
			payment.setPaymentProof(paymentdto.getPaymentProof());
//		    payment.setClients(paymentdto.getClients());

			Clients client = clientsService.getClient(paymentdto.getClients());
			if (client == null) {
				throw new RuntimeException("Client not found for id : " + paymentdto.getClients());
			}
			payment.setClients(client);

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			payment.setPaymentProof(fileName);

			// paymentsService.savePayment(payment);
			Payments savedPayment = paymentsService.savePayment(payment);

			SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(),
					"Payment for Client No:  " + payment.getClients().getClientName() + "  has been updated.",
					requestService.getClientIp(request), userAgent, payment.getAuditIdentifierId(),
					auditTypesService.findAuditTypesByName("UPDATE"));
			auditEventsService.saveAuditEvents(saveEventsHelper);

			String uploadDir = "./payment-proofs/" + savedPayment.getId();

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
				throw new IOException("Could not save the uploaded Payment Proof : " + fileName, e);
			}

			redirectAttributes.addFlashAttribute("success", "Payment updated successfully!!");
			return "redirect:/payments/index";
		} else {
			return "payments/update";
		}
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable(value = "id") int id, RedirectAttributes redirectAttributes,
			@RequestHeader(value = "User-Agent") String userAgent, HttpServletRequest request) {
		Payments payment = paymentsService.getPayment(id);

		SaveEventsHelper saveEventsHelper = new SaveEventsHelper(new Date(),
				"Payment for Client No:  " + payment.getClients().getClientName() + "  has been deleted.",
				requestService.getClientIp(request), userAgent, payment.getAuditIdentifierId(),
				auditTypesService.findAuditTypesByName("DELETE"));
		auditEventsService.saveAuditEvents(saveEventsHelper);

		this.paymentsService.deletePayment(id);
		redirectAttributes.addFlashAttribute("warning", "Payment has been deleted successfully!!");
		return "redirect:/payments/index";
	}

	@RequestMapping(value = "/index/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getDataForDatatable(@RequestParam Map<String, Object> params) {
		int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
		DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
		Pageable pageRequest = dataForDatatable.getPageable();
		String queryString = (String) (params.get("search[value]"));
		Page<Payments> payments = paymentsService.getPaymentsForDatatable(queryString, pageRequest);
		return this.getJsonData(payments, draw);
	}

	private String getJsonData(Page<Payments> payments, int draw) {
		long totalRecords = payments.getTotalElements();
		List<Map<String, Object>> cells = new ArrayList<>();
		payments.forEach(payment -> {
			Map<String, Object> cellData = new HashMap<>();
			cellData.put("id", payment.getId());
			cellData.put("paymentDate", payment.getPaymentDate());
			cellData.put("clients", payment.getClients().getClientName());
			cellData.put("amountPaid", payment.getAmountPaid());
			cellData.put("paidBy", payment.getPaidBy());
			cellData.put("receiptNo", payment.getReceiptNo());
			cellData.put("paymentProof", payment.getPaymentProofPath());
			cells.add(cellData);
		});

		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("draw", draw);
		jsonMap.put("recordsTotal", totalRecords);
		jsonMap.put("recordsFiltered", totalRecords);
		jsonMap.put("data", cells);
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
		}
		return json;
	}

	// MPESA
	@GetMapping("/b2c-new")
	public String newB2C(Model model) {
		model.addAttribute("internalB2CRequestDTO", new InternalB2CTransactionRequest());
		// model.addAttribute("listUsers", usersService.getAllUsers());
		return "mobileMoney/b2c_new";
	}

	// Redirects
	@GetMapping(value = { "/mobile-money/index", "/mobile-money/" })
	public String index_2(HttpSession session, Model model) {
		CommonSyncResponse commonSyncResponse = (CommonSyncResponse) session.getAttribute("commonSyncResponse");
		model.addAttribute("syncResponse", commonSyncResponse);
		return "mobileMoney/index";
	}

	@RequestMapping(value = "/mobile-money/data-for-datatable", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getMobileMoneyDataforDatatable(@RequestParam Map<String, Object> params) {
		int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
		DataForDatatableHelper dataForDatatable = new DataForDatatableHelper(params);
		Pageable pageRequest = dataForDatatable.getPageable();
		String queryString = (String) (params.get("search[value]"));
		Page<B2C_C2B_Entries> mpesaPayments = darajaApiService.getPaymentsDataForDatatable(queryString, pageRequest);
		return this.getJsonDatas(mpesaPayments, draw);
	}

	private String getJsonDatas(Page<B2C_C2B_Entries> mpesaPayments, int draw) {
		long totalRecords = mpesaPayments.getTotalElements();
		List<Map<String, Object>> cells = new ArrayList<>();
		mpesaPayments.forEach(mpesaPayment -> {
			Map<String, Object> cellData = new HashMap<>();
			cellData.put("internalId", mpesaPayment.getInternalId());
			cellData.put("entryDate", mpesaPayment.getEntryDate());
			cellData.put("transactionType", mpesaPayment.getTransactionType());
			cellData.put("msisdn", mpesaPayment.getMsisdn());
			cellData.put("amount", mpesaPayment.getAmount());
			cellData.put("conversationId", mpesaPayment.getConversationId());
			cells.add(cellData);
		});

		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("draw", draw);
		jsonMap.put("recordsTotal", totalRecords);
		jsonMap.put("recordsFiltered", totalRecords);
		jsonMap.put("data", cells);
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
		}
		return json;
	}
}