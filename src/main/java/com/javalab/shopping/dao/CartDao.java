package com.javalab.shopping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalab.shopping.model.Cart;

/**
 * Cart 관련 Dao
 *  - 싱글턴 패턴 적용
 *
 */
public class CartDao {
	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	private String sql = "";
	private DataSource dataSource;	
	private static CartDao instance;
	
	// 기본 생성
	private CartDao() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 싱글톤 팬턴으로 생성
	public static CartDao getInstance() {
		if (instance == null)
			instance = new CartDao();
		return instance;
	}
	
	// 카트 저장
	public int insertCart(Cart cart) {
		sql = "insert into cart(product_id, user_id, quantity, unit_price) values(?,?,?,?)";
		int rows = 0;
		
		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기

			psmt = conn.prepareStatement(sql);
			psmt.setString(1, cart.getProductId());
			psmt.setString(2, cart.getUserId());
			psmt.setInt(3, cart.getQuantity());
			psmt.setInt(4, cart.getUnitPrice());
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
	
	//모든 상품 조회
	public List<Cart> getAllCarts(String userId) {
		List<Cart> carts = new ArrayList<Cart>();
		Cart cart = null;
		
		sql =  "SELECT p.product_id, p.product_name, p.image, c.quantity, c.unit_price , p.image , c.user_id, c.create_date";
		sql += " From cart c inner join product p on c.product_id = p.product_id";
		sql += " inner join users u on c.user_id = u.user_id";
		sql += " where c.user_id = ?";
		sql += " Order BY p.create_date DESC";

		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기

			psmt = this.conn.prepareStatement(sql);
			psmt.setString(1, userId);
			rs = psmt.executeQuery();
			while(rs.next()) {
				cart = new Cart();
				// 상품명, 가격(단가), 수량, 총금액, 사용자ID (숨김필드 product_id)
				cart.setProductId(rs.getString("product_id"));
				cart.setProductName(rs.getString("product_name"));
				cart.setQuantity(rs.getInt("quantity"));
				cart.setUnitPrice(rs.getInt("unit_price"));
				cart.setUserId(rs.getString("user_id"));
				cart.setImage(rs.getString("image"));
				cart.setCreateDate(rs.getDate("create_date"));
				carts.add(cart);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close();
		}
		
		return carts;
	}	
	
	// Cart 삭제
	public void deleteCart(String userId, String productId) {
		
		sql =  "delete from cart where user_id = ? and product_id = ?";

		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기

			psmt = this.conn.prepareStatement(sql);
			psmt.setString(1, userId);
			psmt.setString(2, productId);
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}		
	}


	/**
	 * 사용자가 선택한 상품에 대한 카트 목록이 있는지 체크
	 * @return
	 */
	public boolean cartExist(Cart cart) {
		boolean exist = false;
		sql = "Select * From cart where user_id = ? and product_id = ?";
		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기

			psmt = this.conn.prepareStatement(sql);
			psmt.setString(1, cart.getUserId());
			psmt.setString(2, cart.getProductId());
			rs = psmt.executeQuery();
			if (rs.next()) {	
				exist = true;
			} 		

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return exist;
	}

	// Cart update
	public void updateCart(Cart cart) {
		
		sql =  "update cart set quantity = ?, unit_price = ? where user_id = ? and product_id = ?";

		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기

			psmt = this.conn.prepareStatement(sql);
			psmt.setInt(1, cart.getQuantity());
			psmt.setInt(2, cart.getUnitPrice());
			psmt.setString(3, cart.getUserId());
			psmt.setString(4, cart.getProductId());
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}		
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
