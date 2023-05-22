package com.javalab.shopping.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalab.shopping.model.OrderHeader;
import com.javalab.shopping.model.OrderItem;

/**
 * Order 관련 Dao
 *  - 싱글턴 패턴 적용
 *
 */
public class OrderDao {
	private Connection conn;
	private PreparedStatement psmt;
	private Statement stmt;
	private ResultSet rs;
	private String sql = "";
	private DataSource dataSource;	
	private static OrderDao instance;

	
	// 기본 생성
	private OrderDao() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 싱글톤 팬턴으로 생성
	public static OrderDao getInstance() {
		if (instance == null)
			instance = new OrderDao();
		return instance;
	}
	
	// 카트 저장
	public int insertOrderHeader(OrderHeader orderHeader) {
		int rows = 0;
		sql = "insert into order_header(order_id, user_id, address, total_amt, payment_method) values(seq_order_header.nextval, ?,?,?,?)";
		
		try {			
			conn = dataSource.getConnection();	//커넥션 객체 얻기

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, orderHeader.getUserId());
			psmt.setString(2, orderHeader.getAddress());
			psmt.setInt(3, orderHeader.getTotalAmt());
			psmt.setString(4, orderHeader.getPaymentMethod());
			rows = psmt.executeUpdate();
			
			if(rows > 0) {
				System.out.println("성공적으로 저장되었습니다.");
				
				//방금 저장한 Order Header의 order_id 조회
				sql = "";
				sql = "select Max(order_id) as order_id from order_header";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				
				if(rs.next()) {
					rows = rs.getInt("order_id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return rows;
	}
	
	
	public int insertOrderItem(OrderItem orderItem) {
		int rows = 0;
		sql = "insert into order_item(order_item_id, order_id, product_id, quantity, unit_price, amt) values(seq_order_item.nextval, ?, ?, ?, ?, ?)";
		
		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기
			
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, orderItem.getOrderId());
			psmt.setString(2, orderItem.getProductId());
			psmt.setInt(3, orderItem.getQuantity());
			psmt.setInt(4, orderItem.getUnitPrice());
			psmt.setInt(5, orderItem.getAmt());
			rows = psmt.executeUpdate();
			
			if(rows > 0) {
				System.out.println("성공적으로 저장되었습니다.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return rows;
	}
	
	//Order Header 조회
	public OrderHeader getOrderByOrderId(int orderId) {
		OrderHeader orderHeader = null;
		
		sql =  "SELECT order_id, user_id, address, total_amt, status, payment_method, create_date";
		sql += " From order_header";
		sql += " where order_id = ?";
		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기
			
			psmt = this.conn.prepareStatement(sql);
			psmt.setInt(1, orderId);
			rs = psmt.executeQuery();
			while(rs.next()) {
				orderHeader = new OrderHeader();
				orderHeader.setOrderId(rs.getInt("order_id"));
				orderHeader.setUserId(rs.getString("user_id"));
				orderHeader.setAddress(rs.getString("address"));
				orderHeader.setTotalAmt(rs.getInt("total_amt"));
				orderHeader.setPaymentMethod(rs.getString("payment_method"));
				orderHeader.setCreateDate(rs.getString("create_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return orderHeader;
	}	
	
	
	//Order Item 조회
	public List<OrderItem> getOrderItems(int orderId) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		OrderItem orderItem = null;
		
		sql =  "SELECT o.product_id, p.product_name, o.unit_price, o.quantity, o.amt";
		sql += " From order_item o inner join product p on o.product_id = p.product_id";
		sql += " where o.order_id = ?";
		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기
		
			psmt = this.conn.prepareStatement(sql);
			psmt.setInt(1, orderId);
			rs = psmt.executeQuery();
			while(rs.next()) {
				orderItem = new OrderItem();
				orderItem.setProductId(rs.getString("product_id"));
				orderItem.setProductName(rs.getString("product_name"));
				orderItem.setUnitPrice(rs.getInt("unit_price"));
				orderItem.setQuantity(rs.getInt("quantity"));
				orderItem.setAmt(rs.getInt("amt"));
				orderItems.add(orderItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return orderItems;
	}
	
	//가장 최근 Order Header 조회(userId로 헤더 조회)
	public OrderHeader getOrderByUserId(String userId) {
		OrderHeader orderHeader = null;
		
		sql =  "select m.order_id, m.user_id, m.address, m.total_amt, m.status, m.payment_method,m. create_date";
		sql += " from (";
		sql += " 	select order_id, user_id, address, total_amt, status, payment_method, create_date";
		sql += " 	from order_header";
		sql += " 	where user_id = ?";
		sql += " 	order by create_date desc";
		sql += " )m";		
		sql += " where rownum = 1";
		
		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기

			psmt = this.conn.prepareStatement(sql);
			psmt.setString(1, userId);
			rs = psmt.executeQuery();
			while(rs.next()) {
				orderHeader = new OrderHeader();
				orderHeader.setOrderId(rs.getInt("order_id"));
				orderHeader.setUserId(rs.getString("user_id"));
				orderHeader.setAddress(rs.getString("address"));
				orderHeader.setTotalAmt(rs.getInt("total_amt"));
				orderHeader.setPaymentMethod(rs.getString("payment_method"));
				orderHeader.setCreateDate(rs.getString("create_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return orderHeader;
	}		
	
	// DB 자원해제
	private void close()
	{
		try {
			if (rs != null ){ 
				rs.close(); 
				rs=null; 
			}
			if (psmt != null ){ 
				psmt.close(); 
				psmt=null; 
			}
			if (conn != null ){ 
				conn.close(); 
				conn=null;	
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	} // end close()	
	
	
}
