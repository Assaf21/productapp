/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productsinformationsystem;

/**
 *
 * @author assaf
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Products {

// ---------------------  addProduct  ---------------------
    static void addProduct(Scanner scanner) {
        String type = getUserInput(scanner, "Enter product type: ", "[a-zA-Z0-9\\s]+");  // Prompt the user for product type and validate the input (accepts upper and lower case letters, numbers, and spaces)
        String model = getUserInput(scanner, "Enter product model: ", "[a-zA-Z0-9\\s]+");    // Prompt the user for product model and validate the input (accepts upper and lower case letters, numbers, and spaces)

        // Prompt the user for product price and validate the input
        float price = getPositiveFloatInput(scanner, "Enter product price: ");

        // Prompt the user for product count and validate the input
        int count = getPositiveIntegerInput(scanner, "Enter product count: ");

        // Get a valid delivery date from the user
        String deliveryDate = getValidDeliveryDate(scanner);

        try (Connection connection = DBConnector.getConnection()) {
            // SQL statement to insert the product into the database
            String sql = "INSERT INTO productstbl_marwan_assaf (type, model, price, count, deliveryDate) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(sql);  // Prepare the insert statement
            insertStatement.setString(1, type);
            insertStatement.setString(2, model);
            insertStatement.setDouble(3, price);
            insertStatement.setInt(4, count);
            insertStatement.setString(5, deliveryDate);
            int rowsInserted = insertStatement.executeUpdate();    // Execute the SQL statement and get the number of rows inserted

            if (rowsInserted > 0) {  // Check if the product was successfully inserted
                System.out.println("Product added successfully!");
            } else {
                System.out.println("Failed to add product.");     // Print error message if the product failed to be added

            }
        } catch (SQLException e) {

            System.out.println("Error inserting product: " + e.getMessage());  // Print the error message if there's an error inserting the product
        }
    }

// Method to get user input with validation
    static String getUserInput(Scanner scanner, String prompt, String regex) {
        String input = null;

        while (input == null || !input.matches(regex)) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (!input.matches(regex)) {
                System.out.println("Error : Only an integer value made up of letters or numbers");
            }
        }
        return input;
    }

// Method to get positive float input from the user
    static float getPositiveFloatInput(Scanner scanner, String prompt) {
        float input = 0;
        while (input <= 0) {  // Keep prompting the user until a positive float input is provided 
            System.out.print(prompt);
            String inputString = scanner.nextLine();

            try {
                input = Float.parseFloat(inputString);

                if (input <= 0) { // Check if the input is less than or equal to 0 
                    System.out.println("Error :  Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Error :  Please enter a valid positive number.");
            }
        }
        return input;
    }

// Method to get a positive integer input from the user
    static int getPositiveIntegerInput(Scanner scanner, String prompt) {
        int input = 0;

        // Continue looping until a valid positive integer is entered
        while (input <= 0) {
            System.out.print(prompt);  // Prompt the user for input
            String inputString = scanner.nextLine();

            try {
                input = Integer.parseInt(inputString);

                if (input <= 0) {
                    System.out.println(" Error :  Please enter a positive number.");  // Display error message for non-positive input
                }
            } catch (NumberFormatException e) {
                System.out.println(" Error :  Please Enter  Numbers .");  // Display error message for invalid input format 
            }
        }

        return input;  // Return the valid positive integer input
    }

// Method to get a valid delivery date from the user
    static String getValidDeliveryDate(Scanner scanner) {
        String deliveryDate = null;
        boolean validDate = false;

        // Keep prompting the user until a valid delivery date is provided
        while (!validDate) {
            System.out.print("Enter product delivery date (YYYY-MM-DD): ");
            deliveryDate = scanner.nextLine();

            // Check if the delivery date matches the specified format (YYYY-MM-DD)
            if (deliveryDate.matches("^(201[5-9]|20[2-9][0-9]|2030)-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
                validDate = true;
            } else {
                System.out.println("Error: Date should be between 2015 and 2030. by this way (YYYY-MM-DD) ");
            }
        }

        return deliveryDate;
    }

    // ---------------------  searchProduct  ---------------------
    static void searchProduct(Scanner scanner) {
        boolean validInput = false; // Flag to indicate if the user input is valid
        String errorMessage = " Error:   Please enter a valid type or model."; // Error message for invalid input

        while (!validInput) {
            System.out.print("Enter product type or model to search (leave blank to show all products): ");
            String searchValue = scanner.nextLine();

            // Check if the search value is empty
            if (searchValue.trim().isEmpty()) {
                displayAllProducts();
                return;
            }

            // Check if the search value contains only alphabetic characters
            if (!searchValue.matches("[a-zA-Z]+")) {
                System.out.println(errorMessage);
                continue;
            }

            try (Connection connection = DBConnector.getConnection()) {
                String sql = "SELECT * FROM productstbl_marwan_assaf WHERE type LIKE ? OR model LIKE ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, searchValue);
                statement.setString(2, searchValue);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Display table header
                    printTableComponent("header");

                    do {
                        // Display product information in a formatted manner
                        System.out.format("| %6d | %14s | %30s | %21.2f | %7d | %18s |\n",
                                resultSet.getInt("id"),
                                resultSet.getString("type"),
                                resultSet.getString("model"),
                                resultSet.getDouble("price"),
                                resultSet.getInt("count"),
                                resultSet.getDate("deliveryDate")
                        );
                    } while (resultSet.next());

                    // Display table footer
                    printTableComponent("line");

                    validInput = true;  // Set validInput to true if results are found
                } else {
                    System.out.println("  Error : No matching products found.");
                }
            } catch (SQLException e) {
                System.out.println("Error searching for products: " + e.getMessage());
            }
        }
    }

// Displays all products in the database
    static void displayAllProducts() {
        try (Connection connection = DBConnector.getConnection()) {
            String sql = "SELECT * FROM productstbl_marwan_assaf";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Display table header
                printTableComponent("header");

                do {
                    // Display product information in a formatted manner
                    System.out.format("< %6d >< %14s >< %30s >< %21.2f >< %7d >< %18s >\n",
                            resultSet.getInt("id"),
                            resultSet.getString("type"),
                            resultSet.getString("model"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("count"),
                            resultSet.getDate("deliveryDate")
                    );
                } while (resultSet.next());

                // Display table footer
                printTableComponent("line");
            } else {
                System.out.println("The product database is empty.");
            }
        } catch (SQLException e) {
            System.out.println("Error displaying products: " + e.getMessage());
        }
    }

    // ---------------------  deleteProduct  ---------------------
    static void deleteProduct(Scanner scanner) {
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter product ID to delete: ");
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("Error :  Please enter a valid product ID.");
                continue;  // Go back to the start of the loop and request input again
            }

            int productId;

            try {
                productId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error : Please enter a valid product ID.");
                continue;  // Go back to the start of the loop and request input again
            }

            try (Connection connection = DBConnector.getConnection()) {
                String selectSql = "SELECT * FROM productstbl_marwan_assaf WHERE id = ?";  // to show this id want to delete  and say yes or no 
                String deleteSql = "DELETE FROM productstbl_marwan_assaf WHERE id = ?"; // to delete it after say yes 

                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setInt(1, productId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    // If a product is found, display its details
                    displayProductDetails(resultSet);

                    System.out.print("Are you sure you want to delete this product? (Y/N): ");  // ask the user if u sure  to delete this  product  and answer by yes or no (y/n)
                    String confirmation = scanner.nextLine();

                    if (confirmation.equalsIgnoreCase("Y")) {
                        // If confirmed, execute the SQL statement to delete the product
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                        deleteStatement.setInt(1, productId);
                        int rowsDeleted = deleteStatement.executeUpdate();

                        System.out.println(rowsDeleted + " product(s) deleted successfully.");  // message show up if the delete done  successfully if user select yes 
                    } else {
                        System.out.println("Deletion canceled."); // message  show up  if the delete canceled by user  if user select no  
                    }

                    validInput = true;  // Set validInput to true if the product is found
                } else {
                    System.out.println("No product found with ID: " + productId);
                }
            } catch (SQLException e) {
                System.out.println("Error deleting product: " + e.getMessage());
            }
        }
    }

    // This method displays the details of products retrieved from the database in a formatted table format.
    static void displayProductDetails(ResultSet resultSet) throws SQLException {

        printTableComponent("header");

        do {
            // Display product information in a formatted manner
            System.out.format("< %6d >< %14s >< %30s >< %21.2f >< %7d >< %18s >\n",
                    resultSet.getInt("id"),
                    resultSet.getString("type"),
                    resultSet.getString("model"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("count"),
                    resultSet.getDate("deliveryDate")
            );
        } while (resultSet.next());
        printTableComponent("line");

    }

//   This method prints a specific component of the table.
    static void printTableComponent(String component) {
        switch (component) {
            case "header":
                // Print the table header with column names
                System.out.println(".....................................................................................................................");
                System.out.println("<   ID   <>      Type      <>             Model              <>          Price        <>  Count  <>   Delivery Date    > ");
                System.out.println(".....................................................................................................................");
                break;
            case "line":
                // Print a separating line between table rows
                System.out.println(".....................................................................................................................");
                break;
            default:
                // Invalid table component provided
                System.out.println("Invalid table component.");
        }
    }
}
