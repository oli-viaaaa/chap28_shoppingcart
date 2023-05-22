package com.javalab.shopping.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javalab.shopping.dao.CartDao;
import com.javalab.shopping.model.Cart;
import com.javalab.shopping.model.User;


@WebServlet("/mypage/deleteCart")
public class CartDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// CartDao 멤버 변수, 싱글턴 패턴으로 생성된 인스턴스 얻기 
	private CartDao cartDao = CartDao.getInstance();      

    public CartDeleteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = "";
		String contextPath = request.getContextPath();
		
		/**
		 * [세션을 통해서 로그인 유무 확인]
		 *  - 이제는 로그인 체크 필터에서 로그인 유무를 확인하므로
		 *    미로그인 사용자를 로그인 페이지로 이동시키는
		 *    코드가 필요 없어짐.
		 *  - 로그인을 안한 사용자는 필터 차원에서 걸러지고 여기는 로그인한
		 *    사용자로 가정하고 코드를 구현하면 됨.  
		 *  - 아래 코드에서 if문은 필요 없어짐.  
		 */	
		User user = (User)request.getSession().getAttribute("user");
		if(user == null) {
			//response.sendRedirect("/WEB-INF/views/user/login.jsp");
			//return;			
		}else {
			userId = user.getUserId();		
		
			// 사용자의 카트 조회
			List<Cart> carts = cartDao.getAllCarts(userId);
			int totalAmt = 0;
			for (Cart cart : carts) {
				totalAmt += cart.getUnitPrice() * cart.getQuantity();
			}
			
			String url = "/WEB-INF/views/mypage/cart.jsp";
			request.setAttribute("carts", carts);
			request.setAttribute("totalAmt", totalAmt);
			
			RequestDispatcher rds = request.getRequestDispatcher(url);
			rds.forward(request, response);	
		}		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 세션에서 로그인 유무 확인 
		// 2. 파라미터 받기
		// 3. 받은 파라미터를 비즈니스 로직에 보내서 원하는 결과 받아오기(카트에 저장후 조회)
		// 4. 받아온 결과를 최종 화면으로 보내기
		
		System.out.println("여기는 CartDeleteServlet의 doPost()메소드입니다.");
		String productId = "";
		String userId = "";
		String contextPath = request.getContextPath();
		
		// 로그인 유무 확인(로그인 안했으면 userId가 없어서 cart 테이블에 저장 불가)
		if(request.getSession().getAttribute("user") == null) {
			//response.sendRedirect(contextPath + "/login");	// 필터를 통해서 로그인 체크하기 때문에 필요 없어짐
			//return;
		}else {
			// 세션에서 userId 조회
			User user = (User)request.getSession().getAttribute("user");
			userId = user.getUserId();
		
			// 파라미터 받기  productId
			if(request.getParameter("productId") != null) {
				productId = request.getParameter("productId").trim();
			}
	
			// cart 삭제
			cartDao.deleteCart(userId, productId);
			
			System.out.println("카트가 정상적으로 삭제되었습니다.");
			
			String url = contextPath + "/mypage/cartList";	
			response.sendRedirect(url);	//cart list servlet 호출
		}
	}

}
