package foodrestaurant.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.cj.jdbc.Driver;

import foodrestaurant.dto.User;

public class FoodrestaurantCrud {
	
	public Properties fileCreate() throws IOException {
		FileInputStream stream=new FileInputStream("foodLogin.properties");
		Properties properties=new Properties();
		properties.load(stream);
		return properties;
	}
	public void createTable() throws SQLException, IOException{
		DriverManager.registerDriver(new Driver());
		Properties properties = fileCreate();
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/food?createDatabaseIfNotExist=true",properties);
		Statement st=con.createStatement();
		st.execute("create table if not exists customers(name varchar(45),email varchar(45),password varchar(45),wallet double,phone int(10) primary key)");
		st.close();
		con.close();
		System.out.println("welcome....\n");
	}
	
	public void login(User user) throws SQLException, IOException {
		DriverManager.registerDriver(new Driver());
		Properties properties=fileCreate();
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/food",properties);
		PreparedStatement ps=con.prepareStatement("insert into customers values(?,?,?,?,?)");
		ps.setString(1,user.getName());
		ps.setString(2,user.getEmail());
		ps.setString(3,user.getPassword());
		ps.setDouble(4,user.getWallet());
		ps.setLong(5,user.getPhone());
		ps.execute();
		ps.close();
		con.close();
		System.out.println("\n login done.....");
		
	}
	public double bill(int f1, int quantity,String email) throws SQLException, IOException {
		double bill=0;
		String item="";
		DriverManager.registerDriver(new Driver());
		Properties properties=fileCreate();
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food",properties);
		PreparedStatement ps=con.prepareStatement("select * from fooditems");
		ResultSet res=ps.executeQuery();
		while(res.next()) {
			if(f1==res.getInt(1)) {
				item=res.getString(2);
				bill=res.getDouble(3)*quantity;
			}
		}
		boolean amount=walletCheck(bill,email);
		if(amount==true) {
			paymentCut(bill,email);
			System.out.println("Bill is:");
			System.out.println("item:"+item+"\nquantity="+quantity);
			return bill;
		} 
		return 0;
	}
	public boolean walletCheck(double bill, String email) throws SQLException, IOException {
		double wallet=0;
		DriverManager.registerDriver(new Driver());
		Properties properties=fileCreate();
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food",properties);
		PreparedStatement ps=con.prepareStatement("select * from customers ");
		ResultSet res=ps.executeQuery();
		while(res.next()) {
			if(email.equalsIgnoreCase(res.getString(2))) {
				wallet=res.getDouble(4);
			}
		}
		if(wallet>=bill) {
			return true;
		} else {
			return false;
		}

	}
	public void paymentCut(double bill,String customer) throws SQLException, IOException {
		DriverManager.registerDriver(new Driver());
		Properties properties=fileCreate();
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food",properties);
		PreparedStatement ps=con.prepareStatement("UPDATE food.customers SET wallet = wallet - ? WHERE email = ?");
		ps.setDouble(1, bill);
		ps.setString(2, customer);
		ps.execute();
		ps.close();
		con.close();
	}
	public void addMoney(double money) throws SQLException, IOException {
		DriverManager.registerDriver(new Driver());
		Properties properties=fileCreate();
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/food",properties);	
		CallableStatement cs=con.prepareCall("call food.money(?)");
		cs.setDouble(1,money);
		cs.execute();
		cs.close();
		con.close();
	}
	
}
