package com.isms.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequestMapping("/github-webhook")
public class GitHubWebhookController {

	 @PostMapping
	    public String handleWebhook(
	            @RequestBody String payload,
	            @RequestHeader(value = "X-GitHub-Event", required = false) String event
	    ) {
	        System.out.println("Received GitHub event: " + event);
	        System.out.println("Payload: " + payload);

	        return "Webhook received successfully!";
	    }
	 
//    @Value("${github.webhook.secret}")
//    private String githubSecret;
//
//    @PostMapping
//    public ResponseEntity<String> handleWebhook(
//            @RequestBody String payload,
//            @RequestHeader("X-GitHub-Event") String event,
//            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature
//    ) {
//        // Verify HMAC signature
//        if (!isValidSignature(payload, signature)) {
//            return ResponseEntity.status(403).body("Invalid signature");
//        }
//
//        // Log or route based on event type
//        switch (event) {
//            case "push":
//                System.out.println("Received a push event!");
//                break;
//            case "issues":
//                System.out.println("Received an issue event!");
//                break;
//            case "issue_comment":
//                System.out.println("Received an issue comment!");
//                break;
//            default:
//                System.out.println("Received event: " + event);
//        }
//
//        System.out.println("Payload: " + payload);
//
//        return ResponseEntity.ok("Webhook received");
//    }
//
//    private boolean isValidSignature(String payload, String signature) {
//        try {
//            if (signature == null || !signature.startsWith("sha256=")) {
//                return false;
//            }
//
//            String expected = "sha256=" + hmacSha256(payload, githubSecret);
//            return Objects.equals(expected, signature);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private String hmacSha256(String data, String secret) throws Exception {
//        Mac hmac = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
//        hmac.init(secretKey);
//        byte[] rawHmac = hmac.doFinal(data.getBytes());
//        StringBuilder sb = new StringBuilder();
//        for (byte b : rawHmac) {
//            sb.append(String.format("%02x", b));
//        }
//        return sb.toString();
//    }
}
