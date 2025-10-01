package com.isms.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailTemplateService {
    private final TemplateEngine templateEngine;
    public MailTemplateService(TemplateEngine templateEngine) { this.templateEngine = templateEngine; }

    public String renderAssignment(Map<String, Object> model) {
        Context ctx = new Context();
        ctx.setVariables(model);
        return templateEngine.process("mail/assignment_notification", ctx);
    }
}
