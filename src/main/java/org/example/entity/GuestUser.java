package org.example.entity;
import java.sql.*;
// GUI elements will be added later.
public class GuestUser {
    public GuestUser() throws SQLException {
        String url = "jdbc:postgresql:dam"; //Arbitrary
        String user = "guest_user"; //Arbitrary
        String password = "password"; //Arbitrary

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to database");
            PreparedStatement preparedStatement = connection.prepareStatement("select * from guest_user where object_name = ?");
            preparedStatement.setString(1, "Mona Lisa"); //X is arbitrary.
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("object_name"));

            }

        } catch (SQLException e) {
           System.out.println("Failed to connect to a database."); //Will be looked later.
        }

    }
}
