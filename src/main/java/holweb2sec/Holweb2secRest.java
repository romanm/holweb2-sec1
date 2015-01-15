package holweb2sec;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	
//-------------------department-------------------------------------------------
	@RequestMapping(value="/hol2/v.{departmentName}", method=RequestMethod.GET)
	public String department(@PathVariable String departmentName, Model model) {
		System.out.println("/hol2/v-"+departmentName);
		logger.debug("/hol2/v."+departmentName);
		addDepartment(departmentName, model);
		addDepartmentModel(departmentName, model);
		return "department";
	}

	@RequestMapping(value="/hol2/v.{departmentName}/seek", method=RequestMethod.GET)
	public String departmentSeek(@PathVariable String departmentName, Model model, HttpServletRequest req) {
		System.out.println("/hol2/v-"+departmentName);
		logger.debug("/hol2/v."+departmentName);
		final String seek = req.getParameter("seek");
		System.out.println("seek = "+seek);
		model.addAttribute("seek", seek);
		addDepartment(departmentName, model);
		addDepartmentModel(departmentName, model);
		return "dSeek";
	}

	@RequestMapping(value="/hol2/v.{departmentName}/personal", method=RequestMethod.GET)
	public String departmentPersonal(@PathVariable String departmentName, Model model) {
		final String url = "/hol2/v."+departmentName+"/personal";
		System.out.println(url);
		logger.debug(url);
		model.addAttribute("orders2", "personal");
		addDepartment(departmentName, model);
		addDepartmentModel(departmentName, model);
		return "departmentPersonal";
	}

	@RequestMapping(value="/hol2/v.{departmentName}/regal", method=RequestMethod.GET)
	public String departmentDecree(@PathVariable String departmentName, Model model) {
		System.out.println("/hol2/v-"+departmentName);
		logger.debug("/hol2/v."+departmentName);
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
	
	@RequestMapping(value="/hol2/e-v.{departmentName}", method=RequestMethod.GET)
	public String departmentEdit(@PathVariable String departmentName, Model model) {
		System.out.println("/hol2/e-v."+departmentName);
		logger.debug("/hol2/e-v."+departmentName);
		addDepartment(departmentName, model);
		return "departmentEdit";
	}
	

	@RequestMapping(value="/hol2/addidx-v.{departmentName}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> addDepartmentIndex(@PathVariable String departmentName) {
		logger.debug("/addidx-v."+departmentName);
		final Map<String, Object> department = departmentModel(departmentName);
		return holweb2secController.addDepartmentIndex(department, departmentName);
	}
	private void addDepartment(String departmentName, Model model) {
		model.addAttribute("departmentName", departmentName);
	}
//-------------------department----------------------------------------------END

	@RequestMapping(value="/hol2/addidx-about", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> addIdxAbout() {
		logger.debug("/addidx-about");
		System.out.println("/addidx-about");
		final Map<String, Object> generalInfo = holweb2secController.readModelFile("generalInfo");
		final Set<String> keySet = generalInfo.keySet();
		for (String string : keySet) {
			final Map<String, Object> docBlockContainer = (Map<String, Object>) generalInfo.get(string);
			final List<Map<String, Object>> s2List = (List<Map<String, Object>>) docBlockContainer.get("docBlock");
			for (int s2idx = 0; s2idx < s2List.size(); s2idx++) {
				final Map<String, Object> s2 = s2List.get(s2idx);
				holweb2secController.addDocBlockIndex(s2idx, s2);
			}
		}
		final String modelFileName = holweb2secController.getModelFileName("generalInfo");
		System.out.println(109);
		System.out.println(modelFileName);
		holweb2secController.writeToFileFullPath(generalInfo, modelFileName);
		return generalInfo;
	}

	@RequestMapping(value="/hol2/about", method=RequestMethod.GET)
	public String about( Model model) {
		System.out.println("/hol2/about");
		logger.debug("/hol2/about");
		getGeneralInfo(model);
		return "about";
	}
	@RequestMapping(value="/hol2/history", method=RequestMethod.GET)
	public String history( Model model) {
		System.out.println("/hol2/history");
		logger.debug("/hol2/history");
		getGeneralInfo(model);
		return "history";
	}
	@RequestMapping(value="/hol2/telefon", method=RequestMethod.GET)
	public String telefon( Model model) {
		System.out.println("/hol2/telefon");
		logger.debug("/hol2/telefon");
		getGeneralInfo(model);
		return "telefon";
	}

	private void getGeneralInfo(Model model) {
		final Map<String, Object> generalInfo = holweb2secController.readModelFile("generalInfo");
		model.addAttribute("generalInfo", generalInfo);
	}
	@RequestMapping(value="/hol2/vid", method=RequestMethod.GET)
	public String departments( Model model) {
		System.out.println("/hol2/vid");
		logger.debug("/hol2/vid");
		return "departments";
	}
	@RequestMapping(value="/hol2/lklife", method=RequestMethod.GET)
	public String hospitalLife( Model model) {
		System.out.println("/hol2/lklife");
		logger.debug("/hol2/lklife");
		return "hospitalLife";
	}

	@RequestMapping(value="/hol2/spivrobitnik", method=RequestMethod.GET)
	public String spivrobitnik( Model model) {
		System.out.println("/hol2/spivrobitnik");
		logger.debug("/hol2/spivrobitnik");
		return "spivrobitnik";
	}
	@RequestMapping(value="/hol2/spk/{personalUrl}", method=RequestMethod.GET)
	public String personalUrl(@PathVariable String personalUrl, Model model) {
		System.out.println("/hol2/spk/"+personalUrl);
		logger.debug("/hol2/spk/"+personalUrl);
		addDepartment(personalUrl, model);
		return "spkPersonal";
	}

	@RequestMapping(value="/model/v.{departmentName}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> departmentModel(@PathVariable String departmentName) {
		logger.debug("/model/department/v."+departmentName);
		return holweb2secController.readDepartment(departmentName);
	}

	@RequestMapping(value = "/hol2/personalList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> personalList() {
		logger.debug("/personalList");
		return holweb2secController.personalList();
	}


	@RequestMapping(value = "/hol2/addPL", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> addPL() {
		logger.debug("/addPL");
		return holweb2secController.addPL();
	}

}
