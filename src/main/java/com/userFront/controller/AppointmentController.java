package com.userFront.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userFront.domain.Appointment;
import com.userFront.domain.User;
import com.userFront.service.AppointmentService;
import com.userFront.service.UserService;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
	@Autowired
	private UserService userService;
	@Autowired
	private AppointmentService appointmentService;
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createAppointment(Model model) {
		Appointment appointment = new Appointment();
		model.addAttribute("appointment", appointment);
		model.addAttribute("dateString","");
		return "appointment";
	}
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public String createAppointmentPOST(@ModelAttribute("appointment") Appointment appointment,
			@ModelAttribute("dateString") String dateString, Model model, Principal principal) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date date = simpleDateFormat.parse(dateString);
		appointment.setDate(date);
		User user = userService.findByUsername(principal.getName());
		appointment.setUser(user);
		appointmentService.createAppointment(appointment);
		return "redirect:/userFront";
	}
}
