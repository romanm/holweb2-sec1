package holweb2sec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
