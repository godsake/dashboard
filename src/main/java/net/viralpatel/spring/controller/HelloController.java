package net.viralpatel.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

	@GetMapping("/")
	public String hello(Model model) {

		model.addAttribute("name", "hello feature!!");

		return "welcome";
	}
}
