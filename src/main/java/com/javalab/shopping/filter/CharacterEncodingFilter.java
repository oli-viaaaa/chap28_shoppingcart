package com.javalab.shopping.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * [필터] 이 필터는 캐릭터 인코딩 작업용
 *  1. 필터는 사용자의 요청으로 서블릿이 실행되기 전에 해야할 작업 정의
 *  2. 로그인 필터를 적용하여 로그인 유무를 확인할 수 있다.
 *  3. 기타 다양한 작업을 진행할 수 있다.
 *  4. 필터 체인을 통해서 여러 필터를 다중으로 적용할 수 있다. 
 *  5. 처리가 끝나면 chain.doFilter(request, response); 호출해줘야
 *     다음 필터가 있으면 처리되고 없으면 실제 요청한 서블릿으로 제어가 넘어간다.
 *  6. @WebFilter 방식 or web.xml 설정 중에서 하나 사용  
 */
// @WebFilter 방식
@WebFilter(
		urlPatterns="/*",
		initParams={@WebInitParam(name="encoding", value="UTF-8")})
public class CharacterEncodingFilter implements Filter {

	private String encoding; // 전역변수
	private String responseEncoding; // 전역변수

	/*
	 * [필터 초기화 메소드 init]
	 *  1. 어플리케이션이 구동
	 *  2. web.xml 파일에서 <init-param>으로 설정한 value값이  
	 *     서블릿 컨텍스트에 저장되고 FilterConfig를 통해서 주어짐.
	 *  3. @WebFilter 방식은 @WebInitParam에서 설정한 값이
	 *  	FilterConfig로 주어짐
	 *  4. 필터 객체 생성하면서 최초 1회 호출됨.
	 */
	  @Override
	  public void init(FilterConfig filterConfig) throws ServletException {
		  // 디버깅 코드
		  System.out.println("여기는 CharacterEncodingFilter init 메소드");
		  
		  // filterConfig를 통해서 web.xml 파일 또는 @WebFilter에 encoding이라는 
		  // 이름으로 설정해놓은 값 읽어옴
		  encoding = filterConfig.getInitParameter("encoding");
		  responseEncoding = filterConfig.getInitParameter("responseEncoding");
	    
	    if (encoding == null) {
	      encoding = "UTF-8"; 
	    }
	    if (responseEncoding == null) {
	    	responseEncoding = "text/html; charset:utf-8"; 
	    }
	  }
	
	/*
	 * [필터기능 수행]
	 *  1. 필터에서 지정한 url-pattern에 정한("/*") 요청이 올때마다 실행됨.
	 *  2. 필터의 구체적인 기능을 수행하는 메소드
	 *  3. doFilter()메소드를 통해서 필터 체인의 다음 필터로 진행하거나
	 *     다음 필터가 없으면 원래 요청된 서블릿을 실행한다.
	 *  4. FilterChain : 등록된 모든 필터에 대한 정보 갖고 있음
	 */
	public void doFilter(ServletRequest request, 
						ServletResponse response, 
						FilterChain chain) 
						throws IOException, ServletException {

		System.out.println("모든 서블릿 호출전 거쳐가는 필터입니다.");

		/*
		 * [서블릿이 실행되기 전에 해야할 작업]
		 *  1. config.getInitParameter("encoding") 값은
		 *   - web.xml에서 <param-name>으로 설정한 값 즉, utf-8 이거나
		 */
	    // request에 대한 캐릭터 인코딩(utf-8) 설정
	    request.setCharacterEncoding(encoding);
	    
	    // response에 대한 캐릭터 인코딩(utf-8) 및 contentType 설정
	    response.setContentType("text/html; charset=" + encoding);
	    response.setCharacterEncoding(encoding);

	    
	    // 다음 필터 호출, 없으면 실제 호출된 서블릿의 service() 메소드 호출
	    chain.doFilter(request, response);
	    
	}

	// 필터가 웹컨테이너(톰캣, WAS)에서 삭제될 때 호출됨.
	public void destroy() {
	}
}
