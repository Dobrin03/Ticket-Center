package org.example.ticketcenter.database;
import java.sql.*;
public class DBConnection {
    protected Connection connection;
    private static DBConnection databaseInstance;

    public static DBConnection getInstance() throws SQLException {
        if(databaseInstance==null){
            databaseInstance=new DBConnection();
        }

        return databaseInstance;
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        connection=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "project_manager", "DobiTBG#03");
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
