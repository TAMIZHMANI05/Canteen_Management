import java.sql.*;

public class User {
    private int userId;
    private String name;
    private String role;
    private String email;
    private String password;
	public User(int userId, String name, String role, String email, String password) {
		super();
		this.userId = userId;
		this.name = name;
		this.role = role;
		this.email = email;
		this.password = password;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static User findUserByName(String name) {
        String query = "SELECT * FROM users WHERE name=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {

                return new User(rs.getInt("user_id"),
                                rs.getString("name"),
                                rs.getString("role"),
                                rs.getString("email"), // Fixed typo here
                                rs.getString("password"));
                
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as necessary
        }

        return null;
    }
    

}
