package holweb2sec;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("Holweb2secController")
public class Holweb2secControllerImpl {
	private static final Logger logger = LoggerFactory.getLogger(Holweb2secRest.class);

	public Map<String, Object> readDepartment(String departmentName) {
		String pathToFile = Holweb2secConfig.applicationFolderPfad + Holweb2secConfig.innerDepartmentModelFolderPfad
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
