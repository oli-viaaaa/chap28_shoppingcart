package com.javalab.shopping.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javalab.shopping.dao.CartDao;
import com.javalab.shopping.dao.ProductDao;
import com.javalab.shopping.model.Product;


@WebServlet("/detailView")
public class DetailViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductDao productDao = ProductDao.getInstance();          

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 파라미터 받기
		//2. 받은 파라미터를 dao로 보내기
		//3. 결과 받기, 받은 결과를 request에 담아서 결과 jsp로 보내기
		
		String productId = ""; 
		String pageName = "";
				
		if (request.getParameter("productId") != null)		
			productId = request.getParameter("productId").trim();
		
		Product product = productDao.getProduct(productId);
		request.setAttribute("product", product);

		pageName = request.getRequestURI();		// 자신의 요청경로명을 기억(만약 로그인 페이지로 이동할 경우 달고가려고  ex) ShoppingCart/detailView
		System.out.println("pageName : " + pageName);		
		request.setAttribute("pageName", pageName);
		
		//[방법1] HttpServletRequest 객체 사용
		//RequestDispatcher rds = request.getRequestDispatcher("/WEB-INF/views/product/detailView.jsp");
		//rds.forward(request, response);
		
		//[방법2] ServletContext 객체 사용
		ServletContext ctx = this.getServletContext();
		RequestDispatcher rds = ctx.getRequestDispatcher("/WEB-INF/views/product/detailView.jsp");
		rds.forward(request, response);
	}

}
