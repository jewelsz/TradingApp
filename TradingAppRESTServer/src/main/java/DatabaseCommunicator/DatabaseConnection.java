package DatabaseCommunicator;

import java.sql.*;
import java.util.Properties;

public class DatabaseConnection implements AutoCloseable
{
    static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/";
    private final String DATABASE_NAME = "tradingappdb";
    private static final String USERNAME = "jowelle";
    private static final String PASSWORD = "admin";
    private static final String MAX_POOL = "250";

    private Connection connection;
    private Properties properties;


    private Properties getProperties() {
        if (properties == null)
        {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    public Connection connect() {
        try
        {
            Class.forName(DATABASE_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL + DATABASE_NAME, getProperties());
        } catch (ClassNotFoundException | SQLException e)
        {
            // Java 7+
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void close() throws Exception
    {
        if (connection != null)
        {
            try
            {
                connection.close();
                connection = null;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
