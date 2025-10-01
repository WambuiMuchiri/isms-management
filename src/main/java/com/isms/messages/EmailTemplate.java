package com.isms.messages;

import java.io.IOException;
import java.util.Map;

public class EmailTemplate {

	private String template;
	private Map<String, String> replacementParams;

	public EmailTemplate(String customtemplate) {

		try {
			this.template = loadTemplate(customtemplate);
		} catch (Exception e) {
			this.template = "Empty";
		}

	}

	private String loadTemplate(String customtemplate) throws Exception {
		try (var is = getClass().getClassLoader().getResourceAsStream(customtemplate)) {
			if (is == null) {
				throw new Exception("Template file not found: " + customtemplate);
			}
			return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new Exception("Could not read template = " + customtemplate, e);
		}
	}

	public String getTemplate(Map<String, String> replacements) {

		String cTemplate = this.template;
		// Replace the String
		for (Map.Entry<String, String> entry : replacements.entrySet()) {
			cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
		}
		return cTemplate;
	}
}
