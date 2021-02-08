package com.example.demo.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.CategoryDao;
import com.example.demo.dao.PollDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dao.VoteDao;
import com.example.demo.model.MyUserDetails;
import com.example.demo.model.ResultModel;
import com.example.demo.model.SaveUser;
import com.example.demo.model.selectedValue;
import com.example.demo.repo.*;

@Controller
public class HomeController {
	
	private final String UPLOAD_DIR =  System.getProperty("user.dir")  + "/src/main/resources/";
	
	@Autowired
	UserRepo iUserRepo;
	@Autowired
	CategoryRepo iCategoryRepo;
	@Autowired
	PollRepo iPollRepo;
	@Autowired
	VoteRepo iVoteRepo;
	@Autowired
    JobLauncher jobLauncher;
    
    @Autowired
    Job job;
    
//    @Autowired
//    FileUploadService fileuploadService;
	
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
//	result page controller
	@GetMapping("/admin/result")
	public String showAdmin(Model model) {
		int total = ((Collection<? extends GrantedAuthority>) iUserRepo.findAll()).size();
		List<ResultModel> list = new LinkedList<ResultModel>();
		if(total>0){
			for(PollDao p:iPollRepo.findAll()) {
				ResultModel r = new ResultModel();
				int voted = ((Collection<? extends GrantedAuthority>) iVoteRepo.findAllByPollName(p.getPollName())).size();
				float f=((float)voted/(float)total)*100;
				r.setPollName(p.getPollName());
				r.setPercentage((int)f);
				list.add(r);
			}
		}
		model.addAttribute("data", list);
		return "result";
	}
	
//	bulk adding users
	@PostMapping("/admin/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException {

// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/";
		}

// normalize the file path
		String fileName = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());

// save the file on the local file system
		try {
		Path path = Paths.get(UPLOAD_DIR + fileName);
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
		e.printStackTrace();
		}

		Map<String, JobParameter> maps = new HashMap<>();

		maps.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters parameters = new JobParameters(maps);
		JobExecution jobExecution = jobLauncher.run(job, parameters);
		System.out.println("Batch is running");
		while ((jobExecution).isRunning()) {
			System.out.println("...");
		}

		System.out.println("Job Execution Status " + jobExecution.getStatus());

// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
		
		try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            if(Files.deleteIfExists(path)) {
            	System.out.println("Deletion of file after upload successful");
            } else {
            	System.out.println("Deletion of file after upload Failed");
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return "redirect:/";
	}
	
	
//	user pages
	@GetMapping("/user")
	public String users(Model model) {
		
		Iterable<VoteDao> v = iVoteRepo.findAllByVoterName(getLoggedUser());
		Iterable<PollDao> temp = iPollRepo.findAll();
		List<PollDao> a = new LinkedList<PollDao>();
		for(PollDao x:temp) {
			boolean flag = true;
			for(VoteDao i:v) {
				if(i.getPollName().toString().equals(x.getPollName())) {
					flag=false;
					break;
				}
			}
			if(flag)
				a.add(x);
		}
		model.addAttribute("pollData", a);
		return "user";
	}

	@GetMapping("/user/{id}")
	public String votePage(@PathVariable String id,Model model) {
		model.addAttribute("id", id);
		Optional<PollDao> p = iPollRepo.findById(id);
		model.addAttribute("vote", new selectedValue());
		model.addAttribute("data", p.get());
		return "votePage";
	}
	@PostMapping("/user/{id}")
	public String postVotePage(@ModelAttribute("voteresult") selectedValue v,@PathVariable String id) {
		VoteDao vote = new VoteDao();
		vote.setPollName(id);
		vote.setVoterName(getLoggedUser());
		switch(Integer.parseInt(v.getSelectedValue().toString())) {
		case 1:
			vote.setChoice1(1);
			break;
		case 2:
			vote.setChoice2(1);
			break;
		case 3:
			vote.setChoice3(1);
			break;
		default:
			vote.setChoice4(1);
			break;
		}
		iVoteRepo.save(vote);
		System.out.println(id.toString());
		System.out.println(v.getSelectedValue());
		return "redirect:/user";
	}
	
//	edit user information
	@GetMapping("/user/edit")
	public String editInfo(Model model) {
		Optional<UserDao> u = iUserRepo.findById(getLoggedUser());
		model.addAttribute("warning", "none");
		model.addAttribute("success", "none");
		model.addAttribute("user", u.get());
		model.addAttribute("warningMessage", "");
		return "edit";
	}
	
	@PostMapping("/user/save/{id}")
	public String saveInfo(@ModelAttribute("edit") SaveUser e,Model model,@PathVariable String id){
		if(e.getPassword().equals(e.getConfirmpassword())) {
			model.addAttribute("warning", "none");
			model.addAttribute("success", "block");
			Optional<UserDao> u = iUserRepo.findById(getLoggedUser());
			model.addAttribute("user", u.get());
			UserDao a = new UserDao();
			a.setActive(true);
			a.setDepartment(e.getDepartment());
			a.setEmail(id);
			a.setName(e.getName());
			a.setPassword(e.getConfirmpassword());
			a.setPhone(e.getPhone());
			a.setSalary(e.getSalary());
			a.setRoles("ROLE_USER");
			iUserRepo.save(a);
			return "edit";		
		}else {
			System.out.println(e);
			model.addAttribute("success", "none");
			model.addAttribute("warning", "block");
			Optional<UserDao> u = iUserRepo.findById(getLoggedUser());
			model.addAttribute("user", u.get());
			return "edit";
		}
	}
	
	
	//functions
	public String getLoggedUser() {
		MyUserDetails user = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      String name = user.getEmail();
	    return name;
	}
}