package org.example.ticketcenter.database;
import java.sql.*;
import org.apache.log4j.Logger;
public class DBConnection {
    private static Logger logger=Logger.getLogger(DBConnection.class);
    protected Connection connection;
    private static DBConnection databaseInstance;

    public static DBConnection getInstance() throws SQLException {
        if(databaseInstance==null){
            databaseInstance=new DBConnection();
        }

        return databaseInstance;
    }

    public void connect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "project_manager", "DobiTBG#03");
        }catch (Throwable e){
            logger.error("Database not found" + e);
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void closeConnection() throws SQLException {
        if(connection!=null){
            connection.close();
        }
    }
}
