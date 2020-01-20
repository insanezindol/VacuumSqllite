import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

public class DataReader {

	private Connection connection;
	private boolean isOpened = false;
	private final static String QUERY_SELECT = "SELECT COUNT(1) CNT FROM OFFSETS WHERE TIMESTAMP <= ?";
	private final static String QUERY_DELETE = "DELETE FROM OFFSETS WHERE TIMESTAMP <= ?";
	private final static String QUERY_VACUUM = "VACUUM";

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean open(String path) {
		try {
			SQLiteConfig config = new SQLiteConfig();
			config.setReadOnly(false);
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + path, config.toProperties());
			System.out.println("open");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		isOpened = true;
		return true;
	}

	public boolean close() {
		if (this.isOpened == false) {
			return true;
		}
		try {
			this.connection.close();
			System.out.println("closed");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void selectData(long timestamp) {
		try {
			PreparedStatement prep = this.connection.prepareStatement(QUERY_SELECT);
			prep.setLong(1, timestamp);
			ResultSet row = prep.executeQuery();
			if (row.next()) {
				int cnt = row.getInt(1);
				System.out.println("select : " + cnt);
			}
			row.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteData(long timestamp) {
		try {
			PreparedStatement prep = this.connection.prepareStatement(QUERY_DELETE);
			prep.setLong(1, timestamp);
			int cnt = prep.executeUpdate();
			System.out.println("delete : " + cnt);
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void vacuumData() {
		try {
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate(QUERY_VACUUM);
			stmt.close();
			System.out.println("VACUUM");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
