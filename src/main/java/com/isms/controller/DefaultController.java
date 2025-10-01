package com.isms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @RequestMapping("/403")
	public String unauthorisedPage() {
		return "layouts/exception_page";
	}

}
