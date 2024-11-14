import java.awt.Choice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CanteenManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the College Canteen Management System");
        while(true)
		{
			System.out.println("Main Menu");
			System.out.println("1. Admin Login");
			System.out.println("2. User Login");
			System.out.println("3. Exit");
            System.out.println("=======================================");
            System.out.print("Please select an option (1-3): ");
			
			System.out.println("Enter Choice:");
			int choice = scanner.nextInt();
			
			if(choice ==1)
			{
				adminlogin();
				
			}
			else if(choice == 2)
			{
				userlogin();
			}
			else if(choice == 3)
			{
				System.out.println("Exiting.........");
				System.exit(0);
				
			}
			else
			{
				System.out.println("Invalid Statement");
			}
			}
		}
        
        private static void userlogin() {
            Scanner scanner = new Scanner(System.in);
			System.out.println("Enter Username");
			String name = scanner.next();
			System.out.println("Enter Password");
			String password = scanner.next();
			User user = User.findUserByName(name);
			if(user != null && "user".equalsIgnoreCase(user.getRole())&& user.getPassword().equals(password))
			{
				System.out.println("Login Successful!");
				userMenu(scanner,user.getUserId());
				
			}
			else
			{
				System.out.println("Login Unsuccessful");
			}
		}
		private static void adminlogin() {
	        Scanner sc= new Scanner(System.in);
			System.out.println("Enter Username");
			String name = sc.next();
			System.out.println("Enter Password");
			String password = sc.next();
			User admin = User.findUserByName(name);
			if(admin != null && "Admin".equalsIgnoreCase(admin.getRole())&& admin.getPassword().equals(password))
			{
				System.out.println("Login Successful!");
				adminMenu(sc);
				
				
			}
			else
			{
				System.out.println("Login Unsuccessful");
			}
		}
	
    private static void adminMenu(Scanner scanner) {
        int choice;
		do {
			System.out.println("Admin Menu:");
			System.out.println("1. View Menu");
			System.out.println("2. Add Food Item");
			System.out.println("3. Update Stock");
			System.out.println("4. Generate Stock Report");
			System.out.println("5. Generate Order Report");
			System.out.println("6. Create New User");
			System.out.println("7. Logout");
            System.out.println("=======================================");
            System.out.print("Please select an option (1-7): ");
			choice = scanner.nextInt();
			switch (choice) {
			case 1:
				displayFoodMenu();
				break;
			case 2:
				addFoodItem(scanner);
				break;
			case 3:
				AdminFunctions.updateStock(scanner);
				break;
			case 4:
				AdminFunctions.generateStockReport();
				break;
			case 5:
				AdminFunctions.generateAdminOrderReport();
			case 6:
				createUser(scanner);
				break;
			case 7:
				System.out.println("Logging out...");
				break;
			default:
				System.out.println("Invalid choice.");
			}
		} while (choice!=7);
    }

    private static void addFoodItem(Scanner scanner) {
        System.out.print("Enter food item name: ");
        String name = scanner.next();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter category: ");
        String category = scanner.next();
        System.out.print("Enter stock quantity: ");
        int stockQuantity = scanner.nextInt();

        FoodItem foodItem = new FoodItem(0, name, price, category, stockQuantity);
        AdminFunctions.addFoodItem(foodItem);
    }
    
    public static void displayFoodMenu() {
        String sql = "SELECT item_id, name, category, price, stock_quantity FROM fooditems WHERE stock_quantity > 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Available Food Menu:");
            System.out.println("-------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-15s %-10s %-10s%n", "ID", "Name", "Category", "Price", "Stock");
            System.out.println("-------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("item_id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock_quantity");

                System.out.printf("%-5d %-20s %-15s %-10.2f %-10d%n", id, name, category, price, stock);
            }

            System.out.println("-------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createUser(Scanner scanner) {
        System.out.print("Enter new username: ");
        String username = scanner.next();
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        System.out.print("Enter role (Admin/User): ");
        String role = scanner.next();

        User newUser = new User(0, username,role,email, password);
        AdminFunctions.addUser(newUser);
        System.out.println("New user created successfully!");
    }
    
        private static void userMenu(Scanner scanner,int userId) {
        System.out.println("User Menu:");
        int choice;

        do {
            System.out.println("========= Canteen System Menu =========");
            System.out.println("1. View Menu Items");
            System.out.println("2. Place an Order");
            System.out.println("3. View Order History");
            System.out.println("4. Logout");
            System.out.println("=======================================");
            System.out.print("Please select an option (1-4): ");
            
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Call method to view menu items
                    System.out.println("Displaying menu items...");
                    displayFoodMenu();
                    break;
                case 2:
                    // Call method to place an order
                    System.out.println("Placing an order...");
                    orderFood(scanner, userId);
                    break;
                case 3:
                    // Call method to view order history
                    System.out.println("Displaying order history...");
                    orderHistory(userId);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);

        System.out.println("Thank you for using the Canteen System!");
    }
        

        public static void orderFood(Scanner scanner, int userId) {
            List<OrderItem> orderItems = new ArrayList<>();
            double totalAmount = 0;

            System.out.println("Available Food Menu:");
            displayFoodMenu();

            // Let user choose items to order
            while (true) {
                System.out.print("Enter the ID of the food item to order (or 0 to finish): ");
                int foodItemId = scanner.nextInt();
                if (foodItemId == 0) break;

                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();

                FoodItem foodItem = getFoodItemById(foodItemId);
                if (foodItem == null) {
                    System.out.println("Food item not found. Please try again.");
                    continue;
                }

                if (quantity > foodItem.getStockQuantity()) {
                    System.out.println("Insufficient stock for " + foodItem.getName() + ". Available: " + foodItem.getStockQuantity());
                    continue;
                }

                // Calculate cost for this item and add to total
                double itemCost = foodItem.getPrice() * quantity;
                totalAmount += itemCost;

                // Add item to the order list
                orderItems.add(new OrderItem(0,0,foodItemId, quantity, itemCost));

                System.out.println("Added " + quantity + " x " + foodItem.getName() + " to order. Subtotal: " + itemCost);
            }

            if (orderItems.isEmpty()) {
                System.out.println("No items selected. Order canceled.");
                return;
            }

            System.out.println("Total order amount: " + totalAmount);
            System.out.print("Confirm order (yes/no): ");
            String confirm = scanner.next();
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("Order canceled.");
                return;
            }

            // Place the order
            saveOrder(userId, orderItems, totalAmount);
            System.out.println("Order placed successfully!");
        }

        private static FoodItem getFoodItemById(int foodItemId) {
            String sql = "SELECT item_id, name, price,category, stock_quantity FROM fooditems WHERE item_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, foodItemId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new FoodItem(
                            rs.getInt("item_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getString("category"),
                            rs.getInt("stock_quantity")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static void saveOrder(int userId, List<OrderItem> orderItems, double totalAmount) {
            String orderSql = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
            String orderItemSql = "INSERT INTO orderitems (order_id, food_item_id, quantity, price) VALUES (?, ?, ?, ?)";
            String updateStockSql = "UPDATE fooditems SET stock_quantity = stock_quantity - ? WHERE item_id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement orderItemStmt = conn.prepareStatement(orderItemSql);
                 PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql)) {

                conn.setAutoCommit(false); // Start transaction

                // Insert into orders table and retrieve the order ID
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, totalAmount);
                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    // Insert each item into order_items and update stock quantity
                    for (OrderItem item : orderItems) {
                        // Insert into order_items table
                        orderItemStmt.setInt(1, orderId);
                        orderItemStmt.setInt(2, item.getFoodItemId());
                        orderItemStmt.setInt(3, item.getQuantity());
                        orderItemStmt.setDouble(4, item.getPrice());
                        orderItemStmt.addBatch();

                        // Update stock in food_items table
                        updateStockStmt.setInt(1, item.getQuantity());
                        updateStockStmt.setInt(2, item.getFoodItemId());
                        updateStockStmt.addBatch();
                    }

                    orderItemStmt.executeBatch();
                    updateStockStmt.executeBatch();

                    conn.commit(); // Commit transaction
                } else {
                    conn.rollback(); // Rollback transaction if order ID is not generated
                    System.out.println("Failed to place order.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        

        public static void orderHistory(int userId) {
            String sql = "SELECT o.order_id, o.total_price, oi.food_item_id, f.name AS food_name, oi.quantity, oi.price " +
                         "FROM orders o " +
                         "JOIN orderitems oi ON o.order_id = oi.order_id " +
                         "JOIN fooditems f ON oi.food_item_id = f.item_id " +
                         "WHERE o.user_id = ? " +
                         "ORDER BY o.order_id";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("User Order Report:");
                System.out.println("-------------------------------------------------------------");
                System.out.printf("%-10s %-40s %-10s\n", "Order ID", "Items (Name x Quantity)", "Total Amount");
                System.out.println("-------------------------------------------------------------");

                int currentOrderId = -1;
                StringBuilder items = new StringBuilder();
                double totalAmount = 0;

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String foodName = rs.getString("food_name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    // Check if we moved to a new order ID
                    if (orderId != currentOrderId) {
                        // Print the previous order's details if this is a new order ID
                        if (currentOrderId != -1) {
                            System.out.printf("%-10d %-40s %-10.2f\n", currentOrderId, items.toString(), totalAmount);
                        }
                        
                        // Reset for the new order
                        currentOrderId = orderId;
                        items = new StringBuilder();
                        totalAmount = rs.getDouble("total_price");
                    }

                    // Append the item details to items StringBuilder
                    items.append(foodName).append(" x ").append(quantity).append(", ");
                }

                // Print the last order's details after the loop
                if (currentOrderId != -1) {
                    System.out.printf("%-10d %-40s %-10.2f\n", currentOrderId, items.toString(), totalAmount);
                }

                System.out.println("-------------------------------------------------------------");

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    
   

}

    