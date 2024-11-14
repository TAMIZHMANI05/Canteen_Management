import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminFunctions {
    public static void addFoodItem(FoodItem foodItem) {
        String sql = "INSERT INTO fooditems (name, price, category, stock_quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, foodItem.getName());
            pstmt.setDouble(2, foodItem.getPrice());
            pstmt.setString(3, foodItem.getCategory());
            pstmt.setInt(4, foodItem.getStockQuantity());
            pstmt.executeUpdate();
            System.out.println("Food item added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public static void addUser(User newUser) {
String sql = "INSERT INTO users ( name, role, email, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newUser.getName());
            pstmt.setString(2, newUser.getRole());
            pstmt.setString(3, newUser.getEmail());
            pstmt.setString(4, newUser.getPassword());
            pstmt.executeUpdate();
            System.out.println("User Created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}
	
	public static void generateStockReport() {
		String sql = "SELECT item_id, name, category, price, stock_quantity FROM fooditems";
        String filePath = "canteen_stock_report.csv";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             FileWriter csvWriter = new FileWriter(filePath)) {

            // Write CSV header
            csvWriter.append("ID,Name,Category,Price,Stock Quantity\n");

            // Write data rows
            while (rs.next()) {
                int id = rs.getInt("item_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock_quantity");

                csvWriter.append(id + "," + name + "," + category + "," + price + "," + stock + "\n");
            }

            System.out.println("Report generated successfully: " + filePath);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }
	
	 public static void generateAdminOrderReport() {
	        String sql = "SELECT o.order_id, u.name AS customer_name, o.total_price, oi.food_item_id, f.name AS food_name, oi.quantity, oi.price " +
	                     "FROM orders o " +
	                     "JOIN users u ON o.user_id = u.user_id " +
	                     "JOIN orderitems oi ON o.order_id = oi.order_id " +
	                     "JOIN fooditems f ON oi.food_item_id = f.item_id " +
	                     "ORDER BY o.order_id";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery();
	             FileWriter csvWriter = new FileWriter("admin_order_report.csv")) {

	            // Write CSV header
	            csvWriter.append("Order ID,Customer Name,Total Amount,Food Item ID,Food Name,Quantity,Price\n");

	            while (rs.next()) {
	                int orderId = rs.getInt("order_id");
	                String customerName = rs.getString("customer_name");
	                double totalAmount = rs.getDouble("total_price");
	                int foodItemId = rs.getInt("food_item_id");
	                String foodName = rs.getString("food_name");
	                int quantity = rs.getInt("quantity");
	                double price = rs.getDouble("price");

	                // Write data to CSV
	                csvWriter.append(orderId + "," + customerName + "," + totalAmount + "," +
	                                 foodItemId + "," + foodName + "," + quantity + "," + price + "\n");
	            }

	            System.out.println("Admin order report generated successfully: admin_order_report.csv");

	        } catch (SQLException e) {
	            System.err.println("Database error: " + e.getMessage());
	        } catch (IOException e) {
	            System.err.println("File writing error: " + e.getMessage());
	        }
	    }
    

	
	
	public static void updateStock(Scanner scanner) {
        System.out.print("Enter the ID of the food item to update: ");
        int foodItemId = scanner.nextInt();
        
        System.out.print("Enter the new stock quantity: ");
        int newStockQuantity = scanner.nextInt();
        
        String sql = "UPDATE fooditems SET stock_quantity = ? WHERE item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newStockQuantity);
            pstmt.setInt(2, foodItemId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Stock updated successfully.");
            } else {
                System.out.println("Food item with ID " + foodItemId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
