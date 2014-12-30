package holweb2sec;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Holweb2secRest {
	private static final Logger logger = LoggerFactory.getLogger(Holweb2secRest.class);

	@Autowired private Holweb2secControllerImpl holweb2secController;

	@RequestMapping(value="/hol/v.{departmentName}", method=RequestMethod.GET)
	public String readPatient(@PathVariable String departmentName, Model model) {
		System.out.println("/read/v-"+departmentName);
		logger.debug("/read/v."+departmentName);
		model.addAttribute("departmentName", departmentName);
		return "department";
	}
	
	@RequestMapping(value="/model/v.{departmentName}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> readDepartment(@PathVariable String departmentName) {
		logger.debug("/model/department/v."+departmentName);
		return holweb2secController.readDepartment(departmentName);
	}
}
