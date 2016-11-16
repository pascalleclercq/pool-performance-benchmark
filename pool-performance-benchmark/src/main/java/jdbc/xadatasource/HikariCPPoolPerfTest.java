package jdbc.xadatasource;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPPoolPerfTest extends AbstractPerfClass {

	@Override
	protected DataSource getDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:h2:mem:test_mem");
		config.setDriverClassName("org.h2.Driver");
		config.setUsername("sa");
		return new HikariDataSource(config);
	}

}
