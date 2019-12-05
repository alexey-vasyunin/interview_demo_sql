import java.sql.*;
import java.util.Scanner;

/**
 * Database (POSTGRESQL):
 *
 * create table users
 * (
 * 	id serial not null constraint users_pk 	primary key,
 * 	login varchar(128) not null,
 * 	fio varchar(255)
 * );
 *
 * create unique index users_login_u index on users (login);
 */


public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection(Configure.CONNECTION_URL);
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            String command = scan.nextLine();
            if (command.trim().equalsIgnoreCase("ADD")){
                // Adding user
                System.out.println("Enter login, please:");
                String login = scan.nextLine();
                System.out.println("Enter name, please:");
                String fio = scan.nextLine();
                if (login.isBlank() || fio.isBlank()) {
                    System.out.println("Login and name must be not empty");
                }
                PreparedStatement ps = conn.prepareStatement("insert into users (login, fio) values (?, ?)");
                ps.setString(1, login);
                ps.setString(2, fio);
                try {
                    ps.execute();
                    System.out.println("New user was added");
                    ps.close();
                } catch (SQLException e){
                    System.out.println("Error. Possibly login is already exists in the database");
                }
            } else if (command.trim().equalsIgnoreCase("GET")) {
                // Getting user
                System.out.println("Enter ID of user, please:");
                int id;
                try {
                    id = Integer.parseInt(scan.nextLine());
                } catch (NumberFormatException e){
                    System.out.println("Something wrong with ID. Use only numbers, please");
                    continue;
                }
                PreparedStatement ps = conn.prepareStatement("select * from users where id=?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next())  System.out.println("Login: " + rs.getString("login") + "; name: " + rs.getString("fio"));
            } else if (command.trim().equalsIgnoreCase("END")) {
                // Exit
                break;
            } else {
                // Help
                System.out.println("Using: ADD, GET, END");
            }
        }
        conn.close();
    }
}
