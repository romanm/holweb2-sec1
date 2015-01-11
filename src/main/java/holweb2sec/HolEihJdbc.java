package holweb2sec;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

@Component("holEihJdbc")
public class HolEihJdbc {
	
	private static final Logger logger = LoggerFactory.getLogger(HolEihJdbc.class);
	
	private JdbcTemplate jdbcTemplate;

	public HolEihJdbc(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/hol?useUnicode=true&characterEncoding=utf-8");
		dataSource.setUsername("hol");
		dataSource.setPassword("hol");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Map<String, Object>> personalList() {
		String sql = "select pe.personal_username, pe.personal_surname, pe.personal_name, pe.personal_patronymic, pe.personal_id , pe.personal_surgeon, pe.personal_anesthetist, d.department_id, d.department_name, po.position_id, po.position_name from personal_department pd, position po, department d, personal pe where po.position_id=pd.position_id and d.department_id=pd.department_id and pe.personal_id=pd.personal_id";
		logger.debug("\n"+sql);
		System.out.println("\n"+sql);
		List<Map<String, Object>> patient1sList = jdbcTemplate.queryForList(sql);
		return patient1sList;
	}
	
}
