package foodrestaurant.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import com.mysql.cj.jdbc.Driver;

import foodrestaurant.dao.FoodrestaurantCrud;
import foodrestaurant.dto.User;

public class FoodController {
	static Scanner scan=new Scanner(System.in);
	static FoodrestaurantCrud crud=new FoodrestaurantCrud();
	public static void main(String[] args) throws SQLException, IOException {
		crud.createTable();
		
		boolean check=true;
		
		do {
			System.out.println("Enter your choice \n 1.register \n 2.login \n 3.exit");
			int choice=scan.nextInt();
			switch(choice) {
			case 1:{
				System.out.println("Enter your name:");
				String name=scan.next();
				System.out.println("Enter email:");
				String email=scan.next();
				System.out.println("Enter password:");
				String password=scan.next();
				System.out.println("Enter wallet amount here:");
				double wallet=scan.nextDouble();
				System.out.println("Enter phone no:");
				long phone=scan.nextLong();
				User user=new User(name, email, password, wallet, phone);
				crud.login(user);
			}
			break;
			
			case 2:{
				System.out.println("Enter your email:");
				String email=scan.next();
				System.out.println("Enter password:");
				String password=scan.next();
				DriverManager.registerDriver(new Driver());
				Properties properties=crud.fileCreate();
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/food",properties);
				PreparedStatement ps=con.prepareStatement("select * from customers");
				ResultSet res=ps.executeQuery();
				boolean correct=false;
				while(res.next()) {
					String em = res.getString(2);
					String p=res.getString(3);
					if(email.equalsIgnoreCase(em) && password.equalsIgnoreCase(p)) {
						correct=true;
					}
				}
				ps.close();
				con.close();
				
				if(correct==true) {
					display();
					
					double bill=0;
					int n=1;
					while(true) {
						
						System.out.println("Enter item:");
						int f1=scan.nextInt();
						System.out.println("Enter quantity:");
						int quantity=scan.nextInt();
						bill=bill+crud.bill(f1,quantity,email);
						if(bill==0.0) {
							System.out.println("Insufficient balence.....\ndo you want add money in your wallet");
							String c=scan.next();
							if(c.equals("yes")) {
								System.out.println("Enter amount:");
								double money=scan.nextDouble();
								crud.addMoney(money);
							}
							break;
						}
						System.out.println("Anything.....");
						String any=scan.next();
						if(any.equalsIgnoreCase("no")) {
							break;
						}
						n++;
					}
					if(bill>0) {
						System.out.println("total item"+n+" bill is:"+bill+"\n");
					}
					
				} else {
					System.out.println("Email or Password incorrect........\n");
		
				}
			}
			break;
			
			case 3:{
				check=false;
			}
			break;
			
			default:{
				System.out.println("Please choose correct option....");
			}
			}
			
		}while(check);
	}
	public static void display() throws SQLException, IOException {
		System.out.println("Credentials correct.....");
		DriverManager.registerDriver(new Driver());
		Properties properties1=crud.fileCreate();
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/food",properties1);
		PreparedStatement ps1=con.prepareStatement("select * from fooditems");
		ResultSet food=ps1.executeQuery();
		while(food.next()) {
			System.out.print(food.getInt(1)+" ");
			System.out.print(food.getString(2)+" ");
			System.out.println(food.getDouble(3));
		}
		System.out.println("choose your food items");
		ps1.close();
		con.close();
	}

}
