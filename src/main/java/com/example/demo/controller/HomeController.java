package com.example.demo.controller;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.CategoryDao;
import com.example.demo.dao.PollDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dao.VoteDao;
import com.example.demo.model.MyUserDetails;
import com.example.demo.model.selectedValue;
import com.example.demo.repo.*;

@Controller
public class HomeController {
	
	@Autowired
	UserRepo iUserRepo;
	@Autowired
	CategoryRepo iCategoryRepo;
	@Autowired
	PollRepo iPollRepo;
	@Autowired
	VoteRepo iVoteRepo;
	
	@GetMapping("/")
	public String homePage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream()
		          .anyMatch(r -> r.getAuthority().toString().equals("ROLE_USER"));
		System.out.println(authentication.getAuthorities().stream().collect(Collectors.toList()));
		if(hasUserRole) {
			return "redirect:/user";
		}else {
			return "redirect:/admin";
		}
	}
	
//	admin pages
	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("users", iUserRepo.findAll());
		return "admin";
	}
	
	@GetMapping("/admin/addcategory")
	public String addCategory(Model model) {
		model.addAttribute("errorDisplay", "none");
		model.addAttribute("correctDisplay", "none");
		model.addAttribute("errorMessage","");
		model.addAttribute("correctMessage", "");
		return "addCategory";
	}
	
	@PostMapping("/admin/addcategory")
	public String submitCategory(@ModelAttribute("category") CategoryDao category,Model model){
		CategoryDao c =new CategoryDao();
		c.setCategoryName(category.getCategoryName());
		c.setDescription(category.getDescription());
		Optional<CategoryDao> name = iCategoryRepo.findById(category.getCategoryName());
		if(name.isEmpty()) {
			iCategoryRepo.save(c);
			model.addAttribute("errorDisplay", "none");
			model.addAttribute("correctDisplay", "block");
			model.addAttribute("correctMessage", "Category has been added!");
		}else {
			model.addAttribute("errorDisplay", "block");
			model.addAttribute("correctDisplay", "none");
			model.addAttribute("errorMessage", "Category exists already!");
		}
		
		return "addCategory";
	}
	
	
//	adding poll 
	@GetMapping("/admin/addpoll")
	public String addPoll(Model model) {
		List<CategoryDao> list = (List<CategoryDao>) iCategoryRepo.findAll();
		model.addAttribute("errorDisplay", "none");
		model.addAttribute("correctDisplay", "none");
		model.addAttribute("errorMessage","");
		model.addAttribute("correctMessage", "");
		model.addAttribute("category", list);
		return "addPoll";
	}
	@PostMapping("/admin/addpoll")
	public String addNewPoll(@ModelAttribute("pollDetails") PollDao pollDetails,Model model) {
		Optional<PollDao> list = iPollRepo.findById(pollDetails.getPollName());
		if(list.isEmpty()) {
			iPollRepo.save(pollDetails);
			model.addAttribute("errorDisplay", "none");
			model.addAttribute("correctDisplay", "block");
			model.addAttribute("correctMessage", "Poll has been added!");			
		}else {
			model.addAttribute("errorDisplay", "block");
			model.addAttribute("correctDisplay", "none");
			model.addAttribute("errorMessage", "Poll name exists already!");
		}
		List<CategoryDao> c = (List<CategoryDao>) iCategoryRepo.findAll();
		model.addAttribute("category", c);
		return "addPoll";
	}
	
	
//	user pages
	@GetMapping("/user")
	public String users(Model model) {
		
		Iterable<PollDao> p = iPollRepo.findAll();
		model.addAttribute("pollData", p);
		return "user";
	}
	
	@PostMapping("/user/{id}/{select}")
	public String userPost(@PathVariable String id, Model model,@PathVariable String select) {
		System.out.println(select);
		VoteDao v = new VoteDao();
		v.setPollName(id);
		v.setVoterName(getLoggedUser());
		Iterable<PollDao> p = iPollRepo.findAll();
		model.addAttribute("pollData", p);
		return "user";
	}
	//functions
	public String getLoggedUser() {
		MyUserDetails user = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      String name = user.getEmail();
	    return name;
	}
}