package tmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.user.Preferences;

@Controller
@RequestMapping(path = "/customers/{id}/preferences")

//TODO
public class PreferenceController {
	@Autowired
	private PreferenceRepository preferenceRepo;
	
	@GetMapping
	public @ResponseBody
	Preferences findById(@PathVariable int id) {
		Preferences pref = new Preferences();
		return pref;
	}
	
	@PatchMapping
	public @ResponseBody
	Preferences updateById(@PathVariable int id) {
		//TODO
		return null;
	}
}
