package com.example.demo;

import com.example.demo.entity.oracle.TempResult;
import com.example.demo.entity.postgres.User;
import com.example.demo.repository.oracle.TempResultRepository;
import com.example.demo.repository.postgres.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication  {


	@Qualifier("oracleDataSource")
	@Autowired
	public DataSource oracleDataSource;

	/*@Qualifier("postgresDataSource")
	@Autowired
	public DataSource postgresDataSource;*/

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TempResultRepository tempResultRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



	@GetMapping("/test")
	public void run(String... args) throws Exception {

		tempResultRepository.save(new TempResult(26L, "karim"));
		List<TempResult> all = tempResultRepository.findAll();
		for (TempResult tempResult : all) {
			System.out.println(tempResult.getConcatenatedString());
		}

		System.out.println("fffffffffffffffff");
	}

	/*@GetMapping("/procedure")
	public String res() {
		String sql = "{call MAMANPAPA.concat_strings(?, ?, ?)}";

		// Define variables to hold the output parameter value
		final String[] result = new String[1];

		// Execute the stored procedure
		jdbcTemplate.execute(sql, (CallableStatementCallback<String>) (callableStatement) -> {
			// Set the input parameters
			callableStatement.setString(1, "yassin ");
			callableStatement.setString(2, "omar");

			// Register the output parameter
			callableStatement.registerOutParameter(3, Types.VARCHAR);

			// Execute the stored procedure
			callableStatement.execute();

			// Get the output parameter value
			result[0] = callableStatement.getString(3);

			return result[0];
		});



		return result[0];
	}*/


	@GetMapping("/procedure2")
	public String res2() {

		JdbcTemplate jdbcTemplate = new JdbcTemplate(oracleDataSource);

		String sql = "{call MAMANPAPA.last_name(?, ?, ?, ?)}";

		// Define variables to hold the output parameter value
		final String[] result = new String[2];

		// Execute the stored procedure
		String execute = jdbcTemplate.execute(sql, (CallableStatementCallback<String>) (callableStatement) -> {
			// Set the input parameters
			callableStatement.setString(1, "yassin ");
			callableStatement.setString(2, "mamat");

			// Register the output parameter
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			callableStatement.registerOutParameter(4, Types.VARCHAR);

			// Execute the stored procedure
			callableStatement.execute();

			// Get the output parameter value
			result[0] = callableStatement.getString(3);
			result[1] = callableStatement.getString(4);

			return result[0] + " " + result[1];
		});




		return execute;
	}

	@GetMapping("postgres")
	public String userName() {
		User user = new User();
		user.setUserName("omar");
		user.setId((int)(Math.random()*120));
		userRepository.save(user);
		return userRepository.findById(2).get().getUserName();
	}

	@GetMapping("postg")
	public List<User> fgfg() {
		return userRepository.findAll();
	}

	@GetMapping("oracle")
	public List<TempResult> fgffg() {
		return tempResultRepository.findAll();
	}


}
