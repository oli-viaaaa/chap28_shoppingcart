package com.javalab.shopping.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * [로그인 체크 필터]
 *  - 이전까지 사용자의 로그인 유무를 체크하는 로직을 서블릿에 모두 넣었다. 코드 중복!
 *  - 로그인이 필요한 특정 서블릿/페이지에 대한 요청이 오면 로그인 체크 필터가 가로채서
 *    확인하고 로그인 안했으면 로그인 페이지로 강제 이동시킨다.
 *    e.g url : ~/mypage/* 즉, mypage는 반드시 로그인이 필요한 페이지이므로 
 *    로그인 체크 필터를 태운다. 
 *  - web.xml에 설정되어 있음.
 */
public class LoginCheckFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 디버깅 코드
		System.out.println("여기는 LoginCheckFilter init 메소드");		
	}	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("여기는 LoginCheckFilter doFilter 메소드");		

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(false);
		
		// 기본은 로그인 안했음으로 설정
		boolean login = false;
		
		// 세션이 널 아니고
		if(session != null) {
			// 세션에 사용자 정보 있음. 로그인한 사용자
			if(session.getAttribute("user") != null) { 
				login = true;
			}
		}
		
		// 로그인 했으면 원래 가고자 했던 목적지로 이동
		if(login) {
			System.out.println("로그인 체크 필터 - 로그인 사용자이므로 요청한 페이지로 이동");			

			chain.doFilter(request, response);
		}else { // 아니면 로그인 페이지로 강제 이동
			System.out.println("로그인 체크 필터 - 미로그인 사용자이므로 로그인 페이지로 이동");			
			RequestDispatcher rds = request.getRequestDispatcher("/WEB-INF/views/user/login.jsp");
			rds.forward(request, response);
		}

	}
}
