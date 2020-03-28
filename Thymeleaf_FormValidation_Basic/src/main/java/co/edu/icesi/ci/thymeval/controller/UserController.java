package co.edu.icesi.ci.thymeval.controller;

import java.util.Optional;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.icesi.ci.thymeval.model.EditableAtributte;
import co.edu.icesi.ci.thymeval.model.ParteOne;
import co.edu.icesi.ci.thymeval.model.ParteTwo;
import co.edu.icesi.ci.thymeval.model.User;
import co.edu.icesi.ci.thymeval.service.UserService;
import lombok.val;
import net.bytebuddy.implementation.bind.MethodDelegationBinder.BindingResolver;

@Controller
public class UserController {

	UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
		;
	}

	@GetMapping("/users/")
	public String indexUser(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users/index";
	}

	@GetMapping("/users/add")
	public String addUser(Model model) {
		User aux = new User();
		model.addAttribute("user", aux);
		model.addAttribute("genders", userService.getGenders());
		model.addAttribute("types", userService.getTypes());
		return "users/add-user";
	}

	@PostMapping("/users/add")
	public String saveUser(@Validated(ParteOne.class) User user,BindingResult bindingResult,Model model, @RequestParam(value = "action", required = true) String action) {
		if(action.equals("Cancel")) {
			return "users/index";
		}
		else {
			if(bindingResult.hasErrors()) {
				
				return "users/add-user";
			}
			
			userService.save(user);
			model.addAttribute("genders", userService.getGenders());
			model.addAttribute("types", userService.getTypes());
			model.addAttribute("user",user);
			System.out.println("User id part1"+user.getId());
			
			return "users/add-user2";
		}
		
	}
	
	@PostMapping("/users/add2")
	public String saveUser2(@Validated(ParteTwo.class) User user,BindingResult bindingResult,Model model, @RequestParam(value = "action", required = true) String action) {
		System.out.println("User id part2"+user.getId());
		
		if (user == null)
			throw new IllegalArgumentException("Invalid User");
		if(action.equals("Cancel")) {
			return "users/index";
		}
		else {
			if(bindingResult.hasErrors()) {
				model.addAttribute("genders", userService.getGenders());
				model.addAttribute("types", userService.getTypes());
				return "users/add-user2";
			}
			
			User userSaved=userService.findById(user.getId()).get();
			
			user.setBirthDate(userSaved.getBirthDate());
			user.setPassword(userSaved.getPassword());
			user.setUsername(userSaved.getUsername());
			
			userService.save(user);
			return "redirect:/users/";
		}
	}

	@GetMapping("/users/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Optional<User> user = userService.findById(id);
		if (user == null)
			throw new IllegalArgumentException("Invalid user Id:" + id);
		model.addAttribute("user", user.get());
		model.addAttribute("genders", userService.getGenders());
		model.addAttribute("types", userService.getTypes());
		return "users/update-user";
	}

	@PostMapping("/users/edit/{id}")
	public String updateUser(@PathVariable("id") long id,
			@RequestParam(value = "action", required = true) String action,@Validated(EditableAtributte.class) User user, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("genders", userService.getGenders());
			model.addAttribute("types", userService.getTypes());
			return "users/update-user";
		}
		if (action != null && !action.equals("Cancel")) {
			userService.save(user);
		}
		return "redirect:/users/";
	}

	@GetMapping("/users/del/{id}")
	public String deleteUser(@PathVariable("id") long id) {
		User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userService.delete(user);
		return "redirect:/users/";
	}
}
