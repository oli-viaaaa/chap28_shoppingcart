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

@WebServlet("/mypage/cartList")
public class CartListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// CartDao 멤버 변수, 싱글턴 패턴으로 생성된 인스턴스 얻기 
	private CartDao cartDao = CartDao.getInstance();        

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = "";
		String url = "/WEB-INF/views/mypage/cartList.jsp";
		
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
		// 세션을 통해서 로그인 유무 확인, 안했으면 로그인 페이지로 이동
		User user = (User)request.getSession().getAttribute("user");
		
		// 로그인 안한 사용자는 로그인 페이지로 이동
		if(user == null || user.getUserId().equals("")) {	
			//response.sendRedirect(contextPath + "/login"); // 로그인 서블릿 호출
			//return;
		}else {
			userId = user.getUserId();
			
			// 사용자의 카트 조회
			List<Cart> carts = cartDao.getAllCarts(userId);
			int totalAmt = 0;
			for (Cart cart : carts) {
				totalAmt += cart.getUnitPrice() * cart.getQuantity();
			}
			System.out.println("총합계금액 : totalAmt : " + totalAmt);
			
			request.setAttribute("carts", carts);
			request.setAttribute("totalAmt", totalAmt);
			request.setAttribute("userId", userId);						
		}		

		// 카트 리스트 페이지로 이동
		RequestDispatcher rds = request.getRequestDispatcher(url);
		rds.forward(request, response);	
	}

	/**
	 * 미사용
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("여기는 CartListServlet의 doPost()메소드입니다.");

		// 1. 세션에서 로그인 유무 확인 
		// 2. 파라미터 받기
		// 3. 받은 파라미터를 비즈니스 로직에 보내서 원하는 결과 받아오기(카트에 저장후 조회)
		// 4. 받아온 결과를 최종 화면으로 보내기
		
		/*

		String userId = "";
		
		// 로그인 유무 확인(로그인 안했으면 userId가 없어서 cart 테이블에 저장 불가)
		if(request.getSession().getAttribute("user") != null) {
			User user = (User)request.getSession().getAttribute("user");
			userId = user.getUserId();
		}else {		// 세션이 없으면 login.jsp 페이지로 이동
			response.sendRedirect(request.getContextPath() + "/WEB-INF/views/user/login.jsp");
			return;
		}

		// 비지니스 로직으로 파라미터 전달해서 저장
		CartDao cartDao = new CartDao();

		// 해당 사용자의 전체 cart 조회		
		List<Cart> carts = cartDao.getAllCarts(userId);
		request.setAttribute("carts", carts);
		
		RequestDispatcher rds = request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/views/mypage/cartList.jsp");
		rds.forward(request, response);
			
		*/
	}
	
}
