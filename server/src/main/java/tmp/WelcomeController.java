package tmp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/hello")
public class WelcomeController {
	
	@RequestMapping("/123")
	public String welcome() {
		return "welcome";
	}
}
