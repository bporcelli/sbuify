package com.cse308.sbuify.servlets;

import java.util.Optional;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import com.cse308.sbuify.security.SecurityUtils;

@WebServlet(name = "LoginServlet", description = "Log in/out Servlet", urlPatterns = { "/log" })
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private HttpSession httpSession;

	@Bean
	public Servlet loginServlet() {
		return new LoginServlet();
	}

	@GetMapping(path = "/log/in")
	public String doPost(@RequestBody User attemptingUser, HttpServletResponse res) {
		Optional<User> userOptional = userRepo.findByEmail(attemptingUser.getEmail());

		if (!userOptional.isPresent()) { // if email is invalid
			return "invalid credentials";
		}

		User user = userOptional.get();

		String trueHashPw = user.getPassword();

		String hashed = SecurityUtils.hash(attemptingUser.getPassword());

		if (trueHashPw != hashed) {
			return "invalid credentials";
		}

		httpSession.setAttribute("user", user);

		return "success";
	}

	public void doReset(String email, HttpServletResponse res) {

	}

	@GetMapping(path = "/log/out")
	public String doLogout(HttpServletRequest req, HttpServletResponse res) {
		HttpSession sess = req.getSession();
		sess.invalidate();

		return "success";
	}
}
