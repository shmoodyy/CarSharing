package carsharing;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class CarSharingDB {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    private static String DB_URL;
    static Connection connection = null;
    static Statement companyStatement = null;
    static Statement carStatement = null;
    static Statement customerStatement = null;
    static boolean isConnected;

    public static String getDB_URL() {
        return DB_URL;
    }

    public static void setDB_URL(String DB_URL) {
        CarSharingDB.DB_URL = DB_URL;
    }

    public static Connection serverConnect() {

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            if (!isConnected) System.out.println("Connecting to database...");
            connection = DriverManager.getConnection("jdbc:h2:" + getDB_URL());
            connection.setAutoCommit(true);
            if (connection != null) isConnected = true;


            //STEP 3: Create file if it doesn't exist
            createFile();

        } catch(SQLException se) {
            se.printStackTrace(); //Handle errors for JDBC
        } catch(Exception e) {
            e.printStackTrace(); //Handle errors for Class.forName
        }
        System.out.println();
        return connection;
    }

    public static void createTables() throws SQLException {
        connection = serverConnect();
        //Execute a query
        System.out.println("Creating tables in given database...");
        customerStatement = connection.createStatement();
//        customerStatement.executeUpdate("DROP TABLE IF EXISTS customer CASCADE;");
        carStatement = connection.createStatement();
//        carStatement.executeUpdate("DROP TABLE IF EXISTS car CASCADE;");
        companyStatement = connection.createStatement();
//        companyStatement.executeUpdate("DROP TABLE IF EXISTS company CASCADE;");

        String companySQL = "CREATE TABLE IF NOT EXISTS company (" +
                "id INTEGER NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR_IGNORECASE(255) NOT NULL UNIQUE, " +
                "PRIMARY KEY (id));";
        companyStatement.executeUpdate(companySQL);

        String carSQL = "CREATE TABLE IF NOT EXISTS car (" +
                "id INTEGER NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR_IGNORECASE(255) NOT NULL UNIQUE, " +
                "company_id INTEGER NOT NULL, " +
                "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company(id), " +
                "PRIMARY KEY (id));";
        carStatement.executeUpdate(carSQL);

        String customerSQL = "CREATE TABLE IF NOT EXISTS customer (" +
                "id INTEGER NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR_IGNORECASE(255) NOT NULL UNIQUE, " +
                "rented_car_id INTEGER, " +
                "CONSTRAINT fk_car FOREIGN KEY (rented_car_id) REFERENCES car(id), " +
                "PRIMARY KEY (id));";
        carStatement.executeUpdate(customerSQL);

        customerStatement.executeUpdate("ALTER TABLE customer ALTER COLUMN id RESTART WITH 1");
        carStatement.executeUpdate("ALTER TABLE car ALTER COLUMN id RESTART WITH 1");
        companyStatement.executeUpdate("ALTER TABLE company ALTER COLUMN id RESTART WITH 1");

        System.out.println("Created tables in given database...\n");
        closeStatement(companyStatement);
        closeStatement(carStatement);
        closeStatement(customerStatement);
        closeConnection(connection);
    }
    public static void createFile() {
        File file = new File(getDB_URL());
        try {
            if (file.createNewFile()) System.out.println("File saved!");
        } catch (IOException e) {
            System.out.println("Cannot create the file: " + file.getPath());
        }
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public static void closeStatement(Statement statement) throws SQLException {
        statement.close();
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }
}