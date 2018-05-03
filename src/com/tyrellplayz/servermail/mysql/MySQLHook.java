package com.tyrellplayz.servermail.mysql;

import com.tyrellplayz.servermail.ServerMail;
import com.tyrellplayz.servermail.configs.MainConfig;
import com.tyrellplayz.servermail.utils.LogMessagesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLHook {

    private ServerMail sm;
    public MySQLHook(ServerMail sm) { this.sm = sm; mySQLSetUp(); mySQLOpenConnection(); setUpTables();}

    private Connection connection;
    private String host, database, username, password;
    private int port;

    public Connection getConnection() { return connection; }
    public void setConnection(Connection connection) { this.connection = connection; }

    /**
     * Sets up the connecting to the SQL database
     */
    private void mySQLSetUp(){
        host = MainConfig.getSQLHost();
        database = MainConfig.getSQLDatabase();
        username = MainConfig.getSQLUsername();
        password = MainConfig.getSQLPassword();
        port = MainConfig.getSQLPort();
    }

    /**
     * Opens a connection to the database
     */
    private void mySQLOpenConnection(){
        try{
            synchronized (this){
                if(connection != null && !connection.isClosed()){
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, username, password);
                sm.getLogger().info("Connected to MySQL database");
            }
        } catch (SQLException ex) {
            LogMessagesUtil.errorMessage("Failed to connect to mysql database on host: " + host, "MySQL Error", "Incorrect MySQL connection information");
            ex.printStackTrace();
            sm.getPluginLoader().disablePlugin(sm);
        } catch (ClassNotFoundException ex) {
            LogMessagesUtil.errorMessage("Class not found: com.mysql.jdbc.Driver", "MySQL Driver Missing", "Check to make sure you have the driver");
            ex.printStackTrace();
            sm.getPluginLoader().disablePlugin(sm);
        }
    }

    /**
     * Closes the connection to the database
     */
    public void mySQLCloseConnection(){
        try {
            synchronized (this){
                if(connection != null && !connection.isClosed()){
                    connection.close();
                    sm.getLogger().info("Connection to MySQL database closed");
                }
            }
        } catch (SQLException ex) {
            LogMessagesUtil.errorMessage("Failed to close connection to host: " + host, "MySQL Error", "No need to close a connection if there isnt one.");
            ex.printStackTrace();
            sm.getPluginLoader().disablePlugin(sm);
        }
    }

    public void setUpTables(){
        try{
            Connection con = getConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS `mailDisabled` ( `uuid` INT NOT NULL ) ENGINE = InnoDB;");
            create.executeUpdate();
            PreparedStatement create2 = con.prepareStatement("CREATE TABLE IF NOT EXISTS `playerNames` ( `uuid` TEXT NOT NULL , `name` TEXT NOT NULL ) ENGINE = InnoDB;");
            create2.executeUpdate();
        }catch (NullPointerException ex){
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new table in the database for a new {@link org.bukkit.entity.Player}
     * @return
     */
    public boolean createNewPlayerData(UUID uuid){
        try{
            Connection con = getConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS `"+uuid.toString()+"` ( `message` TEXT NOT NULL , `sender` TEXT NOT NULL , `read` BOOLEAN NOT NULL DEFAULT FALSE , `item` TEXT NOT NULL , `itemReceived` BOOLEAN NOT NULL DEFAULT FALSE , `money` DOUBLE NOT NULL DEFAULT '0' , `moneyReceived` BOOLEAN NOT NULL DEFAULT FALSE ) ENGINE = InnoDB;");
            create.executeUpdate();
            return true;
        }catch (NullPointerException ex){
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

}
