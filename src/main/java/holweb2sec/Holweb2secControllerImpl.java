package holweb2sec;

import holweb2sec.util.Cyrillic2english;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Component("Holweb2secController")
public class Holweb2secControllerImpl {
	private static final Logger logger = LoggerFactory.getLogger(Holweb2secControllerImpl.class);
	
	@Autowired private HolEihJdbc holEihJdbc;
	
	public Map<String, Object> addDepartmentIndex(final Map<String, Object> department, String departmentName) {
		final Map<String, Object> personalListHolWeb = getPersonalListHolWeb();
		List<Map<String, Object>> pl = (List<Map<String, Object>>) personalListHolWeb.get("pl");
		List<Map<String, Object>> s2List = (List<Map<String, Object>>) department.get("docBlock");
		for (int s2idx = 0; s2idx < s2List.size(); s2idx++) {
			final Map<String, Object> s2 = s2List.get(s2idx);
			if(s2.containsKey("chief"))
			{
				final Integer personalId = (Integer) s2.get("personal_id");
				System.out.println("personalId = "+personalId);
				for (Map<String, Object> personal : pl) {
					System.out.println("personal = "+personal);
					final Integer personalIdFromPL = (Integer) personal.get("personal_id");
					if(personalId.equals(personalIdFromPL))
					{
						s2.put("spkPersonal", personal);
						break;
					}
				}
			}
			addDocBlockIndex(s2idx, s2);
		}
		String fullPathToFile = getDepartmentFilePath(departmentName);
		writeToFileFullPath(department, fullPathToFile);
		return department;
	}

	void addDocBlockIndex(int s2idx, final Map<String, Object> s2) {
		s2.put("sectionIdx","section-"+s2idx);
		List<Map<String, Object>> s3List = (List<Map<String, Object>>) s2.get("docBlock");
		if(s3List != null)
		for (int s3idx = 0; s3idx < s3List.size(); s3idx++) {
			final Map<String, Object> s3 = s3List.get(s3idx);
			s3.put("sectionIdx","section-"+s2idx+"-"+s3idx);
			s3.put("pIdx","p-"+s2idx+"-"+s3idx);
			List<Map<String, Object>> s4List = (List<Map<String, Object>>) s3.get("docBlock");
//				if(!s4List.isEmpty())
			if(s4List != null)
			for (int s4idx = 0; s4idx < s4List.size(); s4idx++) {
				final Map<String, Object> s4 = s4List.get(s4idx);
				s4.put("pIdx","p-"+s2idx+"-"+s3idx+"-"+s4idx);
			}
		}
	}

	public Map<String, Object> addPL() {
		final Map<String, Object> personalListHolWeb = getPersonalListHolWeb();
		if(false)
			return personalListHolWeb;
		System.out.println("---------------------0");
		addLatNameUrl(personalListHolWeb);
		System.out.println("---------------------1");
		createNullImage(personalListHolWeb);
		System.out.println("---------------------2");
//		System.out.println(readJsonDbFile2map);
		writeToFileInnerPath(personalListHolWeb, Holweb2secConfig.innerModelFolderPfad
						+"/personalListHolWeb.json.js");
		System.out.println("---------------------3");
		return personalListHolWeb;
	}

	private Map<String, Object> getPersonalListHolWeb() {
		String pathToFile = Holweb2secConfig.applicationFolderPfad + (Holweb2secConfig.innerModelFolderPfad
				+"/personalListHolWeb.json.js");
		File file = new File(pathToFile);
		final Map<String, Object> readJsonDbFile2map = readJsonDbFile2map(file);
		return readJsonDbFile2map;
	}
	private void createNullImage(Map<String, Object> readJsonDbFile2map) {
		final List<Map<String, Object>> pl = (List<Map<String, Object>>) readJsonDbFile2map.get("pl");
		final String imgPath = "/src/main/webapp/img/";
		final String noImageJpg = Holweb2secConfig.applicationFolderPfad+imgPath
				+ "noimage.jpg";
		System.out.println(noImageJpg);
		for (Map<String, Object> person : pl) {
			final Object personalUrl = person.get("personal_url");
			
			final String personalJpg = Holweb2secConfig.applicationFolderPfad+imgPath
					+"spk/"+ personalUrl
					+ ".jpg";
			System.out.println(personalJpg);
			try {
				Files.copy(new File(noImageJpg).toPath()
						, new File(personalJpg).toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	private void addLatNameUrl(Map<String, Object> readJsonDbFile2map) {
		final List<Map<String, Object>> pl = (List<Map<String, Object>>) readJsonDbFile2map.get("pl");
		final Cyrillic2english cyrillic2english = new Cyrillic2english();
		for (Map<String, Object> person : pl) {
			final String pun = (String) person.get("personal_username");
			final String convert = cyrillic2english.convert(pun);
			final String personalUrl = convert.replaceAll(" ", "").replaceAll("'", "").replaceAll("\\.", "");
			person.put("personal_url", personalUrl);
			person.put("p1", pun.substring(0,1));
		}
		
	}
	public List<Map<String, Object>> personalList() {
		final List<Map<String, Object>> personalList = holEihJdbc.personalList();
		writeToFileInnerPath(personalList, Holweb2secConfig.innerModelFolderPfad + "personalList.json.js");
		return personalList;
	}

	void writeToFileFullPath(Object objectForJson, String dbFullPathFile) {
		File file = new File(dbFullPathFile);
		logger.debug("write to file = " + file);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writerWithDefaultPrettyPrinter = mapper.writerWithDefaultPrettyPrinter();
		try {
//			logger.warn(writerWithDefaultPrettyPrinter.writeValueAsString(objectForJson));
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			writerWithDefaultPrettyPrinter.writeValue(fileOutputStream, objectForJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void writeToFileInnerPath(Object objectForJson, String dbInnerPathFile) {
		final String dbFullPathFile = Holweb2secConfig.applicationFolderPfad + dbInnerPathFile;
		writeToFileFullPath(objectForJson, dbFullPathFile);
	}

	public Map<String, Object> readModelFile(String modelFileName) {
		String pathToFile = getModelFileName(modelFileName);
		File file = new File(pathToFile);
		return readJsonDbFile2map(file);
	}

	String getModelFileName(String modelFileName) {
		String pathToFile = Holweb2secConfig.applicationFolderPfad 
				+ Holweb2secConfig.innerModelFolderPfad
				+ modelFileName + ".json.js";
		return pathToFile;
	}
	public Map<String, Object> readDepartment(String departmentName) {
		String pathToFile = getDepartmentFilePath(departmentName);
		File file = new File(pathToFile);
		return readJsonDbFile2map(file);
	}
	private String getDepartmentFilePath(String departmentName) {
		return Holweb2secConfig.applicationFolderPfad 
				+ Holweb2secConfig.innerDepartmentModelFolderPfad
				+"/v."+ departmentName + ".json.js";
	}

	Map<String, Object> readJsonDbFile2map(File file) {
		logger.debug(" o - "+file);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> readJsonDbFile2map = null;// = new HashMap<String, Object>();
		try {
			readJsonDbFile2map = mapper.readValue(file, Map.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return readJsonDbFile2map;
	}
}
