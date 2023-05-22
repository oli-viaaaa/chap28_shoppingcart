package com.javalab.shopping.servlet;
	
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javalab.shopping.dao.CartDao;
import com.javalab.shopping.dao.ProductDao;
import com.javalab.shopping.model.Product;
import com.javalab.shopping.model.User;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// ProductDao 멤버 변수, 싱글턴 패턴으로 생성된 인스턴스 얻기 
	private ProductDao productDao = ProductDao.getInstance();         

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String url = "/WEB-INF/views/main/main.jsp";

		List<Product> proList = new ArrayList<Product>();
		
		// 모든 상품
		proList = productDao.getAllProducts();
		// 최근 신상품
		List<Product> recentProducts = productDao.getRecentProducts();
		// 인기 상품
		List<Product> hitProducts = productDao.getHitProducts();
		
		//1. 세션에서 사용자 아이디 갖고오기
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		
		request.setAttribute("products", proList);
		request.setAttribute("recentProducts", recentProducts);
		request.setAttribute("hitProducts", hitProducts);
		request.setAttribute("user", user);
		
		RequestDispatcher rds = request.getRequestDispatcher(url);
		rds.forward(request, response);		

	}

}
