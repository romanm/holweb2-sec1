package holweb2sec;

import holweb2sec.util.Cyrillic2english;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
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
	private static final Logger logger = LoggerFactory.getLogger(Holweb2secRest.class);
	
	@Autowired private HolEihJdbc holEihJdbc;
	
	public Map<String, Object> addPL() {
		final String innerPfadFileName = Holweb2secConfig.innerModelFolderPfad
						+"/personalListHolWeb.json.js";
		String pathToFile = Holweb2secConfig.applicationFolderPfad + innerPfadFileName;
		File file = new File(pathToFile);
		final Map<String, Object> readJsonDbFile2map = readJsonDbFile2map(file);
		if(false)
			return readJsonDbFile2map;
		System.out.println("---------------------0");
		addLatNameUrl(readJsonDbFile2map);
		System.out.println("---------------------1");
		createNullImage(readJsonDbFile2map);
		System.out.println("---------------------2");
//		System.out.println(readJsonDbFile2map);
		writeToFile(readJsonDbFile2map, innerPfadFileName);
		System.out.println("---------------------3");
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
		writeToFile(personalList, Holweb2secConfig.innerModelFolderPfad + "personalList.json.js");
		return personalList;
	}

	private void writeToFile(Object objectForJson, String dbPathFile) {
		File file = new File(Holweb2secConfig.applicationFolderPfad + dbPathFile);
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

	public Map<String, Object> readDepartment(String departmentName) {
		String pathToFile = Holweb2secConfig.applicationFolderPfad 
				+ Holweb2secConfig.innerDepartmentModelFolderPfad
				+"/v."+ departmentName + ".json.js";
		File file = new File(pathToFile);
		return readJsonDbFile2map(file);
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
