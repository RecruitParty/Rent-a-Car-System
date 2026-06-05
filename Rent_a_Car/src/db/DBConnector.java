package db;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnector {
    private static final Properties props = new Properties();

    static {
        try {
        	File externalFile = new File("db.properties");
        	if(externalFile.exists()) {
                props.load(new FileInputStream(externalFile));
            } else {
                InputStream is = DBConnector.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");
                props.load(is);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password")
        );
    }
}