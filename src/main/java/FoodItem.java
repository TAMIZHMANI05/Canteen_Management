public class FoodItem {
    private int itemId;
    private String name;
    private double price;
    private String category;
    private int stockQuantity;
    
    
	public FoodItem(int itemId, String name, double price, String category, int stockQuantity) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.price = price;
		this.category = category;
		this.stockQuantity = stockQuantity;
	}


	public int getItemId() {
		return itemId;
	}


	public void setItemId(int itemId) {
		this.itemId = itemId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public int getStockQuantity() {
		return stockQuantity;
	}


	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	
	
}