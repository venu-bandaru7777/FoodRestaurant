package foodrestaurant.dto;

public class User {
	private String name;
	private String email;
	private String password;
	private double wallet;
	private long phone;
	


	public User(String name, String email, String password, double wallet, long phone) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.wallet = wallet;
		this.phone = phone;
	}

	
	
	//getter and setter

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public double getWallet() {
		return wallet;
	}
	public void setWallet(double wallet) {
		this.wallet = wallet;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	
	
}
