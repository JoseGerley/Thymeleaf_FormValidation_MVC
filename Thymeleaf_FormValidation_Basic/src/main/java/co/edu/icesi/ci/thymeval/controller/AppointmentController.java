package co.edu.icesi.ci.thymeval.controller;

import java.util.Optional;

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

import co.edu.icesi.ci.thymeval.model.Appointment;
import co.edu.icesi.ci.thymeval.service.AppointmentService;
import co.edu.icesi.ci.thymeval.service.AppointmentServiceInt;
import co.edu.icesi.ci.thymeval.service.UserService;
import co.edu.icesi.ci.thymeval.service.UserServiceInt;

@Controller
public class AppointmentController {

	AppointmentServiceInt appointmentService;
	UserServiceInt userService;

	@Autowired
	public AppointmentController(AppointmentServiceInt appointmentService, UserServiceInt userService) {
		this.userService = userService;
		this.appointmentService = appointmentService;
		;
	}

	@GetMapping("/apps/")
	public String indexApp(Model model) {
		model.addAttribute("apps", appointmentService.findAll());
		return "apps/index";
	}

	@GetMapping("/apps/add")
	public String addApp(Model model,  @ModelAttribute("appointment") Appointment appointment) {
		model.addAttribute("appointment", new Appointment());
		model.addAttribute("doctors", userService.findAllDoctors());
		model.addAttribute("patients", userService.findAllPatients());
		return "apps/add-app";
	}

	@PostMapping("/apps/add")
	public String saveApp(@RequestParam(value = "action", required = true) String action,@Validated Appointment appointment, BindingResult bindingResult,Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("doctors", userService.findAllDoctors());
			model.addAttribute("patients", userService.findAllPatients());
			return "apps/add-app";
		}
		if (!action.equals("Cancel"))
			appointmentService.save(appointment);
		return "redirect:/apps/";
	}

	@GetMapping("/apps/edit/{id}")
	public String showUpdateApp(@PathVariable("id") long id, Model model) {
		Optional<Appointment> appointment = appointmentService.findById(id);
		if (appointment == null)
			throw new IllegalArgumentException("Invalid appointment Id:" + id);
		model.addAttribute("appointment", appointment.get());

		model.addAttribute("doctors", userService.findAllDoctors());
		model.addAttribute("patients", userService.findAllPatients());
		return "apps/update-app";
	}

	@PostMapping("/apps/edit/{id}")
	public String updateApp(@PathVariable("id") long id, Model model, @RequestParam(value = "action", required = true) String action,
			@Validated Appointment appointment, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("doctors", userService.findAllDoctors());
			model.addAttribute("patients", userService.findAllPatients());
			return "apps/update-app";
		}
		if (!action.equals("Cancel")) {
			appointmentService.save(appointment);
		}
		return "redirect:/apps/";
	}

	@GetMapping("/apps/del/{id}")
	public String deleteApp(@PathVariable("id") long id) {
		Appointment app = appointmentService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		appointmentService.delete(app);
		return "redirect:/apps/";
	}
}
