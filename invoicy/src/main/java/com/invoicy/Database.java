package com.invoicy;

import java.sql.*;

public class Database {

    private Connection connection;

    public Database() {
        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/invoicy", "root", "");

            // Create table if it doesn't exist
            createInvoiceTableIfNotExists();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create 'invoice' table if it doesn't exist
    private void createInvoiceTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS invoice ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255) NOT NULL, "
                + "products TEXT NOT NULL, "
                + "totalAmount DOUBLE NOT NULL, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
                
        String createProfile = "CREATE TABLE IF NOT EXISTS profile ("
                + "company_name VARCHAR(255) NOT NULL, "
                + "zipcode VARCHAR(255) NOT NULL, "
                + "address VARCHAR(255) NOT NULL, "
                + "name VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255) NOT NULL, "
                + "phone VARCHAR(255) NOT NULL "
                + ")";
                
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            System.out.println("Invoice table checked/created successfully.");
            statement.execute(createProfile);
            System.out.println("Profile is ready!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to save invoice and return the generated ID
    public int saveInvoice(String customerName, String customerEmail, String productsData, double totalAmount) {
        int invoiceId = -1; // Default value if insert fails
        try {
            String query = "INSERT INTO invoice (name, email, products, totalAmount, created_at) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customerName);
            statement.setString(2, customerEmail);
            statement.setString(3, productsData);
            statement.setDouble(4, totalAmount);

            int affectedRows = statement.executeUpdate();

            // Get the generated invoice ID if insert was successful
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        invoiceId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceId; // Return the generated invoice ID
    }

    public void setProfile(String company, String zipcode, String address, String name, String email, String phone){
        try {
            String deleteProfileQuery = "DELETE FROM profile";
            Statement statement0 = connection.createStatement();
            statement0.execute(deleteProfileQuery);

            String query = "INSERT INTO profile (company_name, zipcode, address, name, email, phone) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, company);
            statement.setString(2, zipcode);
            statement.setString(3, address);
            statement.setString(4, name);
            statement.setString(5, email);
            statement.setString(6, phone);
            statement.executeUpdate();
            System.out.println("Settings Updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   

    // Method to retrieve all invoices and return a ResultSet
    public ResultSet getAllInvoices() {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM invoice";
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSettings(){
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM profile";
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Method to close the database connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
