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

import java.util.Scanner;
import static productsinformationsystem.Products.*;

public class ProductApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        while (choice != 4) {
            printMenu(); // Print the menu options

            String userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                System.out.println("Please choose an option from the menu."); // Display a message  if the input  was  empty
                continue; // Skip to the next iteration of the loop
            }

            choice = Integer.parseInt(userInput);

            switch (choice) {
                case 1:
                    addProduct(scanner); // Call the addProduct() method to add a product
                    break;
                case 2:
                    Products.searchProduct(scanner); // Call the searchProduct() method to search for products
                    break;
                case 3:
                    Products.deleteProduct(scanner); // Call the deleteProduct() method to delete a product
                    break;
                case 4:
                    System.out.println("Finishing..."); // Display a message indicating the program is finishing
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option."); // Display a message for an invalid choice
            }
        }

        scanner.close();
    }


    
    private static void printMenu() { //  menu 
        System.out.println("Menu:");
        System.out.println("1. Add a product");
        System.out.println("2. Search for products by model or type");
        System.out.println("3. Delete a product by ID");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }
}
