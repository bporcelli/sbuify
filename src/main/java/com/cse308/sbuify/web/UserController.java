package com.cse308.sbuify.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/users")
public class UserController {
	@RequestMapping("/hello")
	@ResponseBody
	String home() {
		return "User Controller!";
	}
}
