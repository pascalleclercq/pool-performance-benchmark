package jdbc.xadatasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractPerfClass {


	DataSource ds;
	/**
	 * Setup DB tables if needed.
	 */

	@Before
	public void checkTables() throws Exception {
		boolean error = false;
		Connection conn = null;
		try {
			ds = getDataSource();
			conn = getConnection();
		} catch (Exception noConnect) {
			noConnect.printStackTrace();
			System.err.println("Failed to connect.");
			System.err
					.println("PLEASE MAKE SURE THAT DERBY IS INSTALLED AND RUNNING");
			throw noConnect;
		}
		try {

			Statement s = conn.createStatement();
			try {
				s.executeQuery("select * from Accounts");
			} catch (SQLException ex) {
				// table not there => create it
				System.err.println("Creating Accounts table...");
				s.executeUpdate("create table Accounts ( "
						+ " account VARCHAR ( 20 ), owner VARCHAR(300), balance DECIMAL (19,0) )");
				for (int i = 0; i < 100; i++) {
					s.executeUpdate("insert into Accounts values ( "
							+ "'account" + i + "' , 'owner" + i + "', 10000 )");
				}
			}
			s.close();
		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			closeConnection(conn, error);

		}

		// That concludes setup

	}

	protected abstract  DataSource getDataSource();
	

	/**
	 * Utility method to start a transaction and get a connection.
	 *
	 * @return Connection The connection.
	 */

	private  Connection getConnection() throws Exception {

		Connection conn = ds.getConnection();

		return conn;

	}

	/**
	 * Utility method to close the connection and terminate the transaction.
	 * This method does all XA related tasks and should be called within a
	 * transaction. When it returns, the transaction will be terminated.
	 *
	 * @param conn
	 *            The connection.
	 * @param error
	 *            Indicates if an error has occurred or not. If true, the
	 *            transaction will be rolled back. If false, the transaction
	 *            will be committed.
	 */

	private  void closeConnection(Connection conn, boolean error)
			throws Exception {
		if (conn != null)
			conn.close();
	}

	public  long getBalance(int account) throws Exception {
		long res = -1;
		boolean error = false;
		Connection conn = null;

		try {
			conn = getConnection();
			Statement s = conn.createStatement();
			String query = "select balance from Accounts where account='"
					+ "account" + account + "'";
			ResultSet rs = s.executeQuery(query);
			if (rs == null || !rs.next())
				throw new Exception("Account not found: " + account);
			res = rs.getLong(1);
			s.close();
		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			closeConnection(conn, error);
		}
		return res;
	}

	public  void withdraw(int account, int amount) throws Exception {
		boolean error = false;
		Connection conn = null;

		try {
			conn = getConnection();
			Statement s = conn.createStatement();

			String sql = "update Accounts set balance = balance - " + amount
					+ " where account ='account" + account + "'";
			s.executeUpdate(sql);
			s.close();
		} catch (Exception e) {
			error = true;
			throw e;
		} finally {
			closeConnection(conn, error);

		}

	}

	private  final int DEFAULT_ACCOUNT_ID = 50;

	@Test
	public void perfTest() throws Exception {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			deposit50OnAccount50();
			withdraw50OnAccount50();
		}
		System.err.println("Time spent " + (System.currentTimeMillis() - start));
	}

	
	public void deposit50OnAccount50() throws Exception {
		long amount1 = getBalance(DEFAULT_ACCOUNT_ID);

		withdraw(DEFAULT_ACCOUNT_ID, -50);

		long amount2 = getBalance(DEFAULT_ACCOUNT_ID);

		Assert.assertEquals(amount1 + 50, amount2);

	}

	public void withdraw50OnAccount50() throws Exception {
		long amount1 = getBalance(DEFAULT_ACCOUNT_ID);

		withdraw(DEFAULT_ACCOUNT_ID, 50);

		long amount2 = getBalance(DEFAULT_ACCOUNT_ID);

		Assert.assertEquals(amount1 - 50, amount2);

	}
}
