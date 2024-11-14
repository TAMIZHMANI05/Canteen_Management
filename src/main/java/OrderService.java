import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderService {
    public static void placeOrder(Order order) {
        String orderSql = "INSERT INTO Orders (user_id, total_price, order_status) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {

            conn.setAutoCommit(false);

            // Insert order details
            orderStmt.setInt(1, order.getUserId());
            orderStmt.setDouble(2, order.getTotalPrice());
            orderStmt.setString(3, "pending");
            orderStmt.executeUpdate();

            // Insert each order item (this example assumes methods to insert each order item)
            for (OrderItem item : order.getOrderItems()) {
                insertOrderItem(conn, item);
            }

            conn.commit();
            System.out.println("Order placed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertOrderItem(Connection conn, OrderItem item) throws SQLException {
        String sql = "INSERT INTO OrderItems (order_id, food_item_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getOrderId());
            pstmt.setInt(2, item.getFoodItemId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getPrice());
            pstmt.executeUpdate();
        }
    }
}
