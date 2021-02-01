package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.repo.*;

@Controller
public class HomeController {
	
	@Autowired
	UserRepo iUserRepo;
	
	@GetMapping("/")
	public String homePage(Model model) {
		model.addAttribute("users", iUserRepo.findAll());
		return "index";
	}
	
	@GetMapping("/add")
	public String addPage() {
		return "add";
	}
}