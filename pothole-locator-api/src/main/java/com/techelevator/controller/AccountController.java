package com.techelevator.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class AccountController {

	@RequestMapping("/greeting")
	public String displayGreeting() {
		
		return "greeting";
	}
}
