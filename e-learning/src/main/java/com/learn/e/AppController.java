package com.learn.e;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AppController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private FeedbackRepository feedbackRepo;
	
	@Autowired
	private CourseRepository courseRepo;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String registrationForm( Model model ) {
		model.addAttribute("user", new User());
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegisteration(User user) {
		BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setRegistration_date(java.time.LocalDate.now().toString());
		userRepo.save(user);
		
		return "register_success";
	}
	
	@GetMapping("/home")
	public String viewAllUsers() {
		return "home";
	}
	
	@GetMapping("admin/user_list")
	public String viewUsersList(Model model){
		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("admin/dashboard")
	public String viewAdminDashboard() {
		return "dashboard";
	}
	
	@GetMapping("/home/feedback")
	public String feedbackForm( Model model ) {
		model.addAttribute("feedback", new Feedback());
		return "feedback_form";
	}
	
	@PostMapping("/process_feedback")
	public String processFeedback(@AuthenticationPrincipal CustomUserDetails user , Feedback feedback) {
		feedback.setName(user.getName());
		feedback.setUser_id(user.getUser_Id());
		feedback.setEmail(user.getEmail());
		feedbackRepo.save(feedback);
		return "feedback_filled";
	}
	
	@GetMapping("admin/feedback_list")
	public String viewFeedbackList(Model model){
		List<Feedback> listFeedbacks = feedbackRepo.findAll();
		model.addAttribute("listFeedbacks", listFeedbacks);
		return "feedbacks";
	}
	
	@GetMapping("admin/add_course")
		public String addCourses(Model model) {
			model.addAttribute("course", new Course());
			return "course_form";
		}
	
	@PostMapping("/process_course")
	public String processRegisteration(Course course) {
		courseRepo.save(course);
		return "course_added";
	}
	
	@GetMapping("/home/course_enroll")
	public String feedbackForm( @AuthenticationPrincipal CustomUserDetails user, Model model  ) {
		if(user.getCourse_id() != null)
			return "already_enrolled";
		Course_id course_id = new Course_id();
		List<Course> listCourses = (List<Course>) courseRepo.findAll();
		model.addAttribute("listCourses", listCourses);
		model.addAttribute("course_id", course_id);
		return "enroll_course";
	}
	
	@PostMapping("/enroll_course")
	public String processFeedback(@AuthenticationPrincipal CustomUserDetails currentUser , Course_id course_id) {
		CustomUserDetailsService service;
		User user = userRepo.getById(currentUser.getUser_Id());
		user.setCourse_id(course_id.getCourse_id());
		userRepo.save(user);
		return "course_enrolled";
	}
	
}
