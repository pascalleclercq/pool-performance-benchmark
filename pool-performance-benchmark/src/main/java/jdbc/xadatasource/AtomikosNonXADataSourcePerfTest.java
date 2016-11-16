package jdbc.xadatasource;

import javax.sql.DataSource;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;

/**
 *
 *
 * A simple program that uses JDBC-level integration with
 * TransactionsEssentials. Although only one database is accessed, it shows all
 * important steps for programming with TransactionsEssentials.
 *
 * Usage: java Account <account number> <operation> [<amount>]<br>
 * where:<br>
 * account number is an integer between 0 and 99<br>
 * and operation is one of (balance, owner, withdraw, deposit).<br>
 * In case of withdraw and deposit, an extra (integer) amount is expected.
 */

public class AtomikosNonXADataSourcePerfTest extends AbstractPerfClass {

	@Override
	protected DataSource getDataSource() {
			// Find or construct a datasource instance;
			// this could equally well be a JNDI lookup
			// where available. To keep it simple, this
			// demo merely constructs a new instance.
			AtomikosNonXADataSourceBean ds = new AtomikosNonXADataSourceBean();
			ds.setDriverClassName("org.h2.Driver");
			// REQUIRED: the full name of the XA datasource class
			ds.setUrl("jdbc:h2:mem:test_mem");
			ds.setUser("sa");

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

			// NOTE: the resulting datasource can be bound in JNDI where
			// available
		return ds;
	}

}
