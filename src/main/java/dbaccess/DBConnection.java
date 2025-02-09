package dbaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() {
        String dbURL = "jdbc:postgresql://ep-dry-cherry-a1setuqo-pooler.ap-southeast-1.aws.neon.tech/neondb";
        String dbUser = "neondb_owner";
        String dbPassword = "D0bQopwUH1Wr";
        String dbClass = "org.postgresql.Driver";
        String dbSSL = "sslmode=require";

        Connection connection = null;
        try {
            Class.forName(dbClass);
            connection = DriverManager.getConnection(dbURL + "?" + dbSSL, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: PostgreSQL driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: Database connection failed");
            e.printStackTrace();
        }
        return connection;
    }
}