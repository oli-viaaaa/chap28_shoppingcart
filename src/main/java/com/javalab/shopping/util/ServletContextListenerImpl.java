package com.javalab.shopping.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextListenerImpl implements ServletContextListener {

	public ServletContextListenerImpl() {
		System.out.println("여기는 ServletContextListenerImpl 생성자입니다. ");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		ServletContextListener.super.contextDestroyed(sce);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContextListener.super.contextInitialized(sce);
		System.out.println("여기는 ServletContextListenerImpl의 contextInitialized() 메소드로 여기서는 서블릿 컨텍스트 초기화 작업을 할 수 있다. sce : " + sce.getSource());
	}
	
	
}
