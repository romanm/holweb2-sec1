package holweb2sec;

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

	//-------------------Holweb2sec---------------------------------------------
	@Autowired private Holweb2secControllerImpl holweb2secController;

	@RequestMapping(value="/hol2", method=RequestMethod.GET)
	public String holhome(Model model) {
		System.out.println("/hol2");
		System.out.println(" -> /hol2/holhome");
		logger.debug("/hol2/holhome");
		addModelAll(model);
		return "hol2/holhome";
	}

	//-------------------department---------------------------------------------
	@RequestMapping(value="/hol2/v.{departmentName}", method=RequestMethod.GET)
	public String department(@PathVariable String departmentName, Model model) {
		System.out.println("/hol2/v-"+departmentName);
		logger.debug("/hol2/v."+departmentName);
		addModelDepartmentModel(model, departmentName);
		return "hol2/department";
	}

	@RequestMapping(value="/hol2/v.{departmentName}/personal", method=RequestMethod.GET)
	public String departmentPersonal(@PathVariable String departmentName, Model model) {
		final String url = "/hol2/v."+departmentName+"/personal";
		System.out.println(url);
		logger.debug(url);
		model.addAttribute("orders2", "personal");
		addModelDepartmentModel(model, departmentName);
		return "hol2/departmentPersonal";
	}

	@RequestMapping(value="/hol2/v.{departmentName}/regal/doc", method=RequestMethod.GET)
	public String departmentRegalDoc(@PathVariable String departmentName, Model model) {
		System.out.println("/hol2/v-"+departmentName);
		logger.debug("/hol2/v."+departmentName);
		model.addAttribute("orders2", "regal");
		addModelDepartmentModel(model, departmentName);
		return "hol2/departmentRegal";
	}

	@RequestMapping(value="/hol2/v.{departmentName}/regal", method=RequestMethod.GET)
	public String departmentRegal(@PathVariable String departmentName, Model model) {
		System.out.println("/hol2/v-"+departmentName);
		logger.debug("/hol2/v."+departmentName);
		model.addAttribute("orders2", "regal");
		addModelDepartmentModel(model, departmentName);
		return "hol2/departmentRegal";
	}
	private void addModelDepartmentModel(Model model, String departmentName) {
		final Map<String, Object> department = departmentModel(departmentName);
		System.out.println(department);
		holweb2secController.pullOutDepartmentPersonal(department);
		model.addAttribute("department", department);
		model.addAttribute("departmentName", departmentName);
		addModelAll(model);
	}
	private void addModelAll(Model model) {
		model.addAttribute("devProdUrlPrefix", Holweb2secConfig.devProdUrlPrefix);
	}
	
	@RequestMapping(value="/hol2/e-v.{departmentName}", method=RequestMethod.GET)
	public String departmentEdit(@PathVariable String departmentName, Model model) {
		System.out.println("/hol2/e-v."+departmentName);
		logger.debug("/hol2/e-v."+departmentName);
		addModelDepartmentModel(model,departmentName);
		return "hol2/departmentEdit";
	}
	

	@RequestMapping(value="/hol2/v.{departmentName}-addidx", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> addDepartmentIndex(@PathVariable String departmentName, Model model) {
		logger.debug("/v."+departmentName+"-addidx");
		final Map<String, Object> department = departmentModel(departmentName);
		addModelAll(model);
		return holweb2secController.addDepartmentIndex(department, departmentName);
	}
	
	@RequestMapping(value="/model/v.{departmentName}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> departmentModel(@PathVariable String departmentName) {
		logger.debug("/model/department/v."+departmentName);
		return holweb2secController.readDepartment(departmentName);
	}
	@RequestMapping(value="/hol2/vid", method=RequestMethod.GET)
	public String departments( Model model) {
		System.out.println("/hol2/vid");
		logger.debug("/hol2/vid");
		addModelAll(model);
		return "hol2/departments";
	}

	//----------------------------seek------------------------------------------
	@RequestMapping(value="/hol2/v.{departmentName}/seek", method=RequestMethod.GET)
	public String departmentSeek(@PathVariable String departmentName, Model model, HttpServletRequest req) {
		System.out.println("/hol2/v-"+departmentName);
		logger.debug("/hol2/v."+departmentName);
		addModelSeek(model, req);
		addModelDepartmentModel(model, departmentName);
		return "hol2/seekInDepartment";
	}

//-------------------department----------------------------------------------END

	@RequestMapping(value="/hol2/seek", method=RequestMethod.GET)
	public String seekInAll( Model model, HttpServletRequest req) {
		System.out.println("/hol2/seek");
		logger.debug("/hol2/seek");
		addModelSeek(model, req);
		addModelAll(model);
		return "hol2/seekInAll";
	}
//----------------------------seek-------------------------------------------END

	private void addModelSeek(Model model, HttpServletRequest req) {
		final String seek = req.getParameter("seek");
		System.out.println("seek = "+seek);
		model.addAttribute("seek", seek);
	}

	@RequestMapping(value="/hol2/about-addidx", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> addIdxAbout(Model model) {
		logger.debug("/about-addidx");
		System.out.println("/about-addidx");
		final Map<String, Object> generalInfo = holweb2secController.readModelFile("generalInfo");
		final Set<String> keySet = generalInfo.keySet();
		for (String key : keySet) {
			System.out.println(key);
			final Object object = generalInfo.get(key);
			if(object instanceof List)
				continue;
			final Map<String, Object> docBlockContainer = (Map<String, Object>) object;
			final List<Map<String, Object>> s2List = (List<Map<String, Object>>) docBlockContainer.get("docBlock");
			if(null != s2List)
			for (int s2idx = 0; s2idx < s2List.size(); s2idx++) {
				final Map<String, Object> s2 = s2List.get(s2idx);
				holweb2secController.addDocBlockIndex(s2idx, s2);
			}
		}
		final String modelFileName = holweb2secController.getModelFileName("generalInfo");
		System.out.println(109);
		System.out.println(modelFileName);
		holweb2secController.writeToFileFullPath(generalInfo, modelFileName);
		addModelAll(model);
		return generalInfo;
	}

//------------------------------про лікарню------------------------------------

	@RequestMapping(value="/hol2/news", method=RequestMethod.GET)
	public String news( Model model) {
		System.out.println("/hol2/news");
		logger.debug("/hol2/news");
		addGeneralInfoModel(model);
		return "hol2/news";
	}

	@RequestMapping(value="/hol2/policlinic", method=RequestMethod.GET)
	public String policlinic( Model model) {
		System.out.println("/hol2/policlinic");
		logger.debug("/hol2/policlinic");
		addGeneralInfoModel(model);
		return "hol2/policlinic";
	}

	@RequestMapping(value="/hol2/about", method=RequestMethod.GET)
	public String about( Model model) {
		System.out.println("/hol2/about");
		logger.debug("/hol2/about");
		addGeneralInfoModel(model);
		return "hol2/about";
	}
	@RequestMapping(value="/hol2/admin", method=RequestMethod.GET)
	public String admin( Model model) {
		System.out.println("/hol2/admin");
		logger.debug("/hol2/admin");
		addGeneralInfoModel(model);
		return "hol2/admin";
	}

	private void addGeneralInfoModel(Model model) {
		getGeneralInfo(model);
		addModelAll(model);
	}
	@RequestMapping(value="/hol2/history", method=RequestMethod.GET)
	public String history( Model model) {
		System.out.println("/hol2/history");
		logger.debug("/hol2/history");
		addGeneralInfoModel(model);
		return "hol2/history";
	}

	@RequestMapping(value="/hol2/telefon", method=RequestMethod.GET)
	public String telefon( Model model, HttpServletRequest req) {
		System.out.println("/hol2/telefon");
		final String remoteAddr = req.getRemoteAddr();
		System.out.println(remoteAddr);
		logger.debug("/hol2/telefon");
		addGeneralInfoModel(model);
		return "hol2/telefon";
	}

//------------------------------про лікарню----------------------------------END

	private void getGeneralInfo(Model model) {
		final Map<String, Object> generalInfo = holweb2secController.readModelFile("generalInfo");
		model.addAttribute("generalInfo", generalInfo);
	}
	
	@RequestMapping(value="/hol2/liklife", method=RequestMethod.GET)
	public String hospitalLife( Model model) {
		System.out.println("/hol2/liklife");
		logger.debug("/hol2/liklife");
		addModelAll(model);
		return "hol2/hospitalLife";
	}

	@RequestMapping(value="/hol2/spivrobitnik", method=RequestMethod.GET)
	public String spivrobitnik( Model model, HttpServletRequest req) {
		final long currentTimeMillis = System.currentTimeMillis();
		System.out.println("/hol2/spivrobitnik");
		logger.debug("/hol2/spivrobitnik");
		addModelAll(model);
		model.addAttribute("parameterMap", req.getParameterMap());
		final String abc = req.getParameter("abc");
		final Map<String, Object> personalListSort;
		if(abc != null){
			personalListSort = holweb2secController.readPersonalListSort(abc);
		}else{
			personalListSort = holweb2secController.readPersonalListSort();
		}
		model.addAttribute("personalList", personalListSort);
		logger.debug(""+currentTimeMillis);
		System.out.println(System.currentTimeMillis()-currentTimeMillis);
		return "hol2/spivrobitnik";
	}

	@RequestMapping(value="/hol2/spivrobitnik-addsortidx", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> spivrobitnikAddsortidx(Model model) {
		holweb2secController.makePersonalListSort();
		final Map<String, Object> personalListSort = holweb2secController.readPersonalListSort();
		return personalListSort;
	}

	@RequestMapping(value="/hol2/spk/{personalUrl}", method=RequestMethod.GET)
	public String personalUrl(@PathVariable String personalUrl, Model model) {
		System.out.println("/hol2/spk/"+personalUrl);
		logger.debug("/hol2/spk/"+personalUrl);
		//помилка шукає відділення з іменем лікаря
//		addModelDepartmentModel(model,personalUrl);
		addModelAll(model);
		return "hol2/spkPersonal";
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
	//-------------------Holweb2sec------------------------------------------END

}
