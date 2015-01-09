package holweb2sec;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping(value="/hol/v.{departmentName}/seek", method=RequestMethod.GET)
	public String departmentSeek(@PathVariable String departmentName, Model model, HttpServletRequest req) {
		System.out.println("/hol/v-"+departmentName);
		logger.debug("/hol/v."+departmentName);
		final String seek = req.getParameter("seek");
		System.out.println("seek = "+seek);
		model.addAttribute("seek", seek);
		addDepartment(departmentName, model);
		addDepartmentModel(departmentName, model);
		return "dSeek";
	}
	@RequestMapping(value="/hol/v.{departmentName}/regal", method=RequestMethod.GET)
	public String departmentDecree(@PathVariable String departmentName, Model model) {
		System.out.println("/hol/v-"+departmentName);
		logger.debug("/hol/v."+departmentName);
		model.addAttribute("orders2", "regal");
		addDepartment(departmentName, model);
		addDepartmentModel(departmentName, model);
		return "departmentRegal";
	}
	private void addDepartmentModel(String departmentName, Model model) {
		final Map<String, Object> department = departmentModel(departmentName);
		System.out.println(department);
		model.addAttribute("department", department);
	}
	@RequestMapping(value="/hol/v.{departmentName}", method=RequestMethod.GET)
	public String department(@PathVariable String departmentName, Model model) {
		System.out.println("/hol/v-"+departmentName);
		logger.debug("/hol/v."+departmentName);
		addDepartment(departmentName, model);
		addDepartmentModel(departmentName, model);
		return "department";
	}
	@RequestMapping(value="/hol/e-v.{departmentName}", method=RequestMethod.GET)
	public String departmentEdit(@PathVariable String departmentName, Model model) {
		System.out.println("/hol/e-v."+departmentName);
		logger.debug("/hol/e-v."+departmentName);
		addDepartment(departmentName, model);
		return "departmentEdit";
	}
	private void addDepartment(String departmentName, Model model) {
		model.addAttribute("departmentName", departmentName);
	}

	@RequestMapping(value="/hol/vid", method=RequestMethod.GET)
	public String departments( Model model) {
		System.out.println("/hol/v");
		logger.debug("/hol/v");
		return "departments";
	}

	@RequestMapping(value="/model/v.{departmentName}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> departmentModel(@PathVariable String departmentName) {
		logger.debug("/model/department/v."+departmentName);
		return holweb2secController.readDepartment(departmentName);
	}

}
