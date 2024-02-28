package supplement;

import java.sql.*;
import java.util.Map;
import java.util.Set;

import systemModel.*;

public class ExportExclusiveSignalsToDatabase {
	/**
	 * Creates a Table in a database where the exclusive signals should be
	 * exported to.
	 * 
	 * @param dbURL
	 *            URL of the database
	 * @param tableName
	 *            Name of the table
	 */
	public void createTableInDatabase(String dbURL, String tableName) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(dbURL);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName + ";");
			String sql = "CREATE TABLE " + tableName + " (ID TEXT PRIMARY KEY NOT NULL," + " NAME TEXT NOT NULL, " + " SENDER TEXT NOT NULL, "
					+ " RECEIVER TEXT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");

	}

	/**
	 * Fills the table with the Signals. Throws an Exception
	 * 
	 * @param aMap
	 *            Map which contains the exclusive signals.
	 * @param dbURL
	 *            URL of the database
	 * @param tableName
	 *            Name of the table
	 */
	public void fillTableWithEntries(Map<String, CommunicationMedium> aMap, String dbURL, String tableName) throws Exception {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(dbURL);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		Set<String> keySet = aMap.keySet();
		for (String s : keySet) {

			CommunicationMedium cm = aMap.get(s);
			String id = s.toUpperCase();
			String name = cm.getName().toUpperCase();
			String sender = null;
			// throw exception if there is more or less than 1 sender
			if (cm.getSoftwareWritingOnto().size() != 1) {
				throw new Exception();
			}
			for (Software sw : cm.getSoftwareWritingOnto()) {
				sender = sw.getName().toUpperCase();
			}

			String receiver = null;
			// throw exception if there is more or less than 1 receiver
			if (cm.getReadingSoftware().size() != 1) {
				throw new Exception();
			}
			for (Software sw : cm.getReadingSoftware()) {
				receiver = sw.getName().toUpperCase();
			}

			String sql = "INSERT INTO " + tableName + " (ID, NAME, SENDER, RECEIVER) " + "VALUES ('" + id + "', '" + name + "', '" + sender + "', '" + receiver
					+ "')";
			stmt.executeUpdate(sql);
		}

		stmt.close();
		c.commit();
		c.close();

		System.out.println("Table filled successfully");
	}

	/**
	 * Prints every signal which is in the table. Note: fillTablewithEntries
	 * must be called, otherwise the table is empty
	 * 
	 * @param dbURL
	 *            URL of the Database
	 * @param tableName
	 *            Name of the Table
	 */
	public void printTable(String dbURL, String tableName) {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(dbURL);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			String sql = "SELECT * FROM " + tableName + ";";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String id = rs.getString("ID");
				String name = rs.getString("NAME");
				String sender = rs.getString("SENDER");
				String receiver = rs.getString("RECEIVER");
				System.out.println("ID = " + id);
				System.out.println("NAME = " + name);
				System.out.println("SENDER = " + sender);
				System.out.println("RECEIVER = " + receiver);
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully");
	}

}
