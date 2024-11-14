public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int foodItemId;
    private int quantity;
    private double price;
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getFoodItemId() {
		return foodItemId;
	}
	public void setFoodItemId(int foodItemId) {
		this.foodItemId = foodItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public OrderItem(int orderItemId, int orderId, int foodItemId, int quantity, double price) {
		super();
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.foodItemId = foodItemId;
		this.quantity = quantity;
		this.price = price;
	}
    
    
	

}
