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
import com.javalab.shopping.model.Cart;
import com.javalab.shopping.model.Product;
import com.javalab.shopping.model.User;


@WebServlet("/mypage/checkout")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// CartDao 멤버 변수, 싱글턴 패턴으로 생성된 인스턴스 얻기 
	private CartDao cartDao = CartDao.getInstance();
	// Product 관련 쿼리를 하기 때문에 필요함.
	private ProductDao productDao = ProductDao.getInstance();  
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("여기는 CheckoutServlet의 doGet()메소드입니다.");
		String userId = "";
		String url = "/WEB-INF/views/mypage/cartList.jsp";
		
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
		
		if(user == null || user.getUserId().equals("")) {	// 세션이 없으면 로그인 페이지로 이동
			//url = "/WEB-INF/views/user/login.jsp";		// 필터에서 처리함
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

		RequestDispatcher rds = request.getRequestDispatcher(url);
		rds.forward(request, response);	
	}

	/**
	 * 카트리스트 화면에서 주문할 상품들을 받아서 정리해서 보여주는 [주문서]역할을 담당한다.
	 * 테이블에 저장되지는 않는다. 임시로 보여주는 역할만 한다.
	 * [주문서] 화면에서 [결제하기] 버튼을 눌러야 주문 테이블에 저장된다. 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("여기는 CheckoutServlet의 doPost()메소드입니다.");

		// 1. 세션에서 로그인 유무 확인 
		// 2. 파라미터 받기
		// 3. 받은 파라미터를 비즈니스 로직에 보내서 원하는 결과 받아오기(카트에 저장후 조회)
		// 4. 받아온 결과를 최종 화면으로 보내기
		
		String contextPath = request.getContextPath();
		String url = "/WEB-INF/views/mypage/checkout.jsp";	// 전달된 값이 올바르지 않으면 포워딩될 페이지 호출
		String userId = "";
		
		//1. 세션에서 사용자 아이디 갖고오기
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		
		//2. 로그인 유무 확인(로그인 안했으면 userId가 없어서 cart 테이블에 저장 불가)
		if(user != null) {
			userId = user.getUserId();
		}else {		// 세션이 없으면 login.jsp 페이지로 이동
			//response.sendRedirect(contextPath + "/WEB-INF/views/user/login.jsp");	//필터에서 처리하므로 필요 없어짐
			//return;
		}

		//3. 파라미터 받기 - 카트리스트에서 전달된 상품ID, 수량, 단가 배열 받기
		String arrProduct[] = null;
		String arrQuantity[] = null;
		String arrUnitPrice[] = null;
		int totalAmt = 0;
		String productId = "";
		String quantity = "";
		String unitPirce = "";
		
		//3-1 카트리스트에서 주문하기 요청한 경우
		// productId
		if(request.getParameterValues("productId") != null){
			arrProduct = request.getParameter("productId").split(",");
		}
		// quantity
		if(request.getParameterValues("quantity") != null){
			arrQuantity = request.getParameter("quantity").split(",");
		}
		// unitPrice
		if(request.getParameterValues("unitPrice") != null){
			arrUnitPrice = request.getParameter("unitPrice").split(",");
		}
		// totalAmt
		if(request.getParameterValues("totalAmt") != null){
			totalAmt = Integer.parseInt(request.getParameter("totalAmt").trim());
		}
		
		//3-2 상품 상세 페이지에서 주문하기 버튼을 누른경우
		if(request.getParameter("productId") != null) {
			productId = request.getParameter("productId").trim();
		}
		if(request.getParameter("quantity") != null) {
			quantity = request.getParameter("quantity").trim();
		}
		if(request.getParameter("unitPirce") != null) {
			unitPirce = request.getParameter("unitPirce").trim();
		}		
		
		//4. 전달된 값의 갯수가 올바른지 체크하고 갯수가 틀리면 이전 카트리스트로 돌아감
		if(arrProduct.length != arrProduct.length || arrProduct.length != arrProduct.length) {
			System.out.println("전달된 값의 숫자가 올바르지 않습니다.");
			response.sendRedirect(contextPath + url);	//카트리스트 화면으로 이동시킴
			return;
		}
		
		//5. 넘어온 상품의 갯수 만큼 화면에 보여지도록 반복 작업
		List<Cart> carts = new ArrayList<Cart>();
		if(arrProduct.length > 0) {	// 카트리스트에서 요청
			for(int i=0; i<arrProduct.length; i++) {
				// 세개의 배열에서 인덱스에 맞게 Cart 객체에 세팅
				Cart cart = new Cart(userId, arrProduct[i], productName(arrProduct[i]), Integer.parseInt(arrQuantity[i]), Integer.parseInt(arrUnitPrice[i]));	// productName(arrProduct[i]) 결제페이지에 상품명을 보여줘야 되기 때문에
				carts.add(cart);
			}
		}else {		// 상세 페이지에서 요청(상품 한 개만 주문)
			Cart cart = new Cart(userId, productId, productName(productId), Integer.parseInt(quantity), Integer.parseInt(unitPirce));
			carts.add(cart);
		}
		
		//6. 만들어진 카트  ArrayList를 request객체에 담아서 checkout.jsp[주문서]에 전달
		request.setAttribute("carts", carts);
		request.setAttribute("totalAmt", totalAmt);
		RequestDispatcher rds = request.getRequestDispatcher(url);
		rds.forward(request, response);
		
	}
	
	/**
	 * 단순하게 productId를 인자로 받아서 상품을 찾아주는 역할
	 */
	public String productName(String productId) {
		Product product = productDao.getProduct(productId);
		return product.getProductName();
	}
	
}
