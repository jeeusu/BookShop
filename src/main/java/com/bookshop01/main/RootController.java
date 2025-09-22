package com.bookshop01.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {
	
	@RequestMapping("/")
	public String redirectToMain() {
		return "redirect:/main/main.do";
	}
	
	
}
