package jdbc.xadatasource;

import java.util.Properties;

import javax.sql.DataSource;

import com.atomikos.jdbc.AtomikosDataSourceBean;

public class AtomikosXADataSourcePerfTest extends AbstractPerfClass {

	@Override
	protected DataSource getDataSource() {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
		Properties xaProperties = new Properties();
		xaProperties.put("url", "jdbc:h2:mem:test_mem");
		xaProperties.put("user", "sa");
		ds.setXaProperties(xaProperties);
		// REQUIRED: properties to set on the XA datasource class
		// ds.getXaProperties().setProperty("user", "demo");
		// REQUIRED: unique resource name for transaction recovery
		// configuration
		ds.setUniqueResourceName("Atomikos");
		// OPTIONAL: what is the pool size?
		ds.setMaxPoolSize(10);
		// OPTIONAL: how long until the pool thread checks liveness of
		// connections?
		ds.setBorrowConnectionTimeout(60);

		return ds;
	}

}
