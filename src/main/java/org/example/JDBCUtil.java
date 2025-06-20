package org.example;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

    private static final Properties props= new Properties();
    static {
        try(InputStream in = JDBCUtil.class.getClassLoader().getResourceAsStream(("db.properties"))) //el recurso creado llamado db.properties
        {
            if (in==null){
                throw new RuntimeException("no se encuentra el archivo");
            }
            props.load(in);
        }catch (IOException e)
        {
            throw new ExceptionInInitializerError("errror al cargar el archivo: "+e.getMessage());
        }
    }
    public static Connection getConection() throws SQLException {
        String url = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String password = props.getProperty("jdbc.password");
        return DriverManager.getConnection(url,user,password);
    }
}
