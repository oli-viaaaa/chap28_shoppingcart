package com.javalab.shopping.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class HttpSessionListenerImpl implements HttpSessionListener {

	/**
	 * 어플리케이션이 구동되는 시점에 호출된다.
	 */
	public HttpSessionListenerImpl() {
		System.out.println("여기는 HttpSessionListenerImpl 생성자입니다.");
	}

	/**
	 * 어플리케이션에 클라이언트가 최초로 접속하는 시점에 호출된다.
	 * 크롬으로 접속하고 나서 엣지로 접속하면 또 다른 세션이 만들어지면서 본 메소드가 호출된다.
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSessionListener.super.sessionCreated(se);
		System.out.println("여기는 HttpSessionListenerImpl 클래스의 sessionCreated() 메소드 - 새로운 세션 객체 생성");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		HttpSessionListener.super.sessionDestroyed(se);
	}
	
	
}
