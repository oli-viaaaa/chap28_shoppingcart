package com.javalab.shopping.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LogoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String contextPath = request.getContextPath();
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			System.out.println("무효화 할 세션 " + session.getAttribute("user").toString());
			session.invalidate();
			//session.removeAttribute("user");	// 세션을 전체 지우지 않고 특정 이름의 것만 지울경우
			System.out.println("세션이 존재하여 무효화 하였습니다.");
		}
		
		response.sendRedirect(contextPath + "/login");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
	}

}
