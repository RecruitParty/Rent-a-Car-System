package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/rental_car"; //localhost부분IP수정하셔서 사용하시면 됩니다  
	private static final String USER = "root"; //
 	private static final String PASS = ""; //여기에 각자 비밀번호 입력해서 사용하시면 됩니다

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}
	
	public DBConnector() {}
}