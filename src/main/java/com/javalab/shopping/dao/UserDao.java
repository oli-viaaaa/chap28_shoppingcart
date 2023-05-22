package com.javalab.shopping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalab.shopping.model.User;

/**
 * User 관련 Dao
 *  - 싱글턴 패턴 적용
 *
 */
public class UserDao {

	private Connection conn;
	private String sql;
	private PreparedStatement psmt;
	private ResultSet rs;
	private DataSource dataSource;
	private static UserDao instance;
	
	private UserDao() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 싱글톤 팬턴으로 생성
	public static UserDao getInstance() {
		if (instance == null)
			instance = new UserDao();
		return instance;
	}
		
	public User login(String userId, String userPwd) {
		User user = null;
		
		try {
			conn = dataSource.getConnection();	//커넥션 객체 얻기			
			sql = "Select * From users Where user_id = ? and user_pwd = ?";
			//파라미터 세팅
			psmt = this.conn.prepareStatement(sql);
			psmt.setString(1, userId);			
			psmt.setString(2, userPwd);
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setUserId(rs.getString("user_id"));
				user.setUserPwd(rs.getString("user_pwd"));	
				user.setUserName(rs.getString("user_name"));
				user.setUserEmail(rs.getString("user_email"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}finally {
			close();
		}
		return user;
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
