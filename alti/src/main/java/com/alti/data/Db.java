package com.alti.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Db implements DbInterface{

	@Override
	public void sendToDb(String inoviceNum, String inDate, String cPo, String addressTo, String total) {
		Connection conn = null;
		PreparedStatement pStatement = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tails", "root", "root");

			String query = " insert into details (InvoiceNo, InvoiceDate, customerPo, address, total)"
					+ " values (?, ?, ?, ?, ?)";

			pStatement = conn.prepareStatement(query);
			pStatement.setString(1, inoviceNum);
			pStatement.setString(2, inDate);
			pStatement.setString(3, cPo);
			pStatement.setString(4, addressTo);
			pStatement.setString(5, total);
			pStatement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}

	@Override
	public void displayDb() {
		Connection conn = null;
		PreparedStatement eStatment = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tails", "root", "root");

			eStatment = conn.prepareStatement("select * from details");
			ResultSet rs = eStatment.executeQuery();
			Scanner sc = new Scanner(System.in);

			while (rs.next()) {
				System.out.print("InvoiceNo " + rs.getString("InvoiceNo"));
				System.out.println("InvoiceDate " + rs.getString("InvoiceDate"));
				System.out.print(rs.getString("customerPo"));
				System.out.print(rs.getString("address"));
				System.out.print(rs.getString("total"));
				System.out.println("---------------------------");
				System.out.print("Do you want to approve? y/n: ");
				String ch = sc.nextLine();
				if (ch.equals("y")) {
					updateStatusYes(rs.getString("InvoiceNo"));

				} else {
					updateStatusNo(rs.getString("InvoiceNo"));
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	private void updateStatusNo(String string) {
		Connection conn = null;
		PreparedStatement pStatement = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tails", "root", "root");

			pStatement = conn.prepareStatement("update details set status = 'No' where InvoiceNo = ?");
			pStatement.setString(1, string);
			pStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	private static void updateStatusYes(String string) {
		Connection conn = null;
		PreparedStatement pStatement = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tails", "root", "root");

			pStatement = conn.prepareStatement("update details set status = 'Yes' where InvoiceNo = ?");
			pStatement.setString(1, string);
			pStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
