<!-- header.jsp : tagligs / vo 클래스 / 부트스트랩 link / jquery / 
	custom css 정의 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.javalab.shopping.model.*"%>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<!-- 클라이언트의  브라우저에 컨텍스트 패스를 저장(https://ko.javascript.info/localstorage)-->
	<!-- jsp 화면에서 저장해놓으면 모든 화면과 js파일에서 사용가능함. -->
	<script type="text/javascript" charset="utf-8">
		sessionStorage.setItem("contextpath", "${pageContext.request.contextPath}");
	</script>

	<!-- jQuery CDN 포털사이트에서 제공하는 jquery 함수 사용 -->
	<script src="http://code.jquery.com/jquery-latest.min.js"></script> 
	
	<style>
	
	html {
	    font-size: 100%;
	}
	
	body, input, select, textarea, button, span, i {
	    font-size: 1.0rem !important; 
	    line-height: 1.3;
	}
	
	.t_main {
	    margin-top:80px;    /* 원래는 90 */
	    z-index: -1;        /* 화면이 길어져도 헤더 밑으로 들어가도록 하기 위함 */
	}
	
	body {
	    background: #fefefe;
	    line-height:160%;
	}
	
	#t_header {
	    background: #46c3e9;
	    position:fixed;
	    z-index:9999;
	    color: #fff;
	    padding: 10px 10px 10px 10px;
	    top:0;
	    left:0;
	    right:0;    
	}
	
	#t_header {
	    font-size: 1.1em;
	    vertical-align: baseline;
	}
	
	#t_header .title {
	    font-size: 1.1em;
	    font-weight: 600;
	}
	
	
	#t_header i.far-bell:after {
	    position: absolute;
	    display: block;
	    content: '';
	    height: 7px;
	    width: 7px;
	    background: #faaac7;
	    border-radius: 50%;
	    right: 12px;
	    bottom: 17px;
	}
	
	/** ios용 헤더(푸시알람뷰)*/
	#t_header_ios {
	    background: #46c3e9;
	    position:fixed;
	    z-index:9999;
	    color: #fff;
	    padding: 10px 10px 10px 10px;
	    top:0;
	    left:0;
	    right:0;
	}
	
	#t_header_ios {
	    font-size: 1.1em;
	    vertical-align: baseline;
	}
	
	#t_header_ios .title {
	    font-size: 1.1em;
	    font-weight: 600;
	}
	
	
	#t_header_ios i.far-bell:after {
	    position: absolute;
	    display: block;
	    content: '';
	    height: 7px;
	    width: 7px;
	    background: #faaac7;
	    border-radius: 50%;
	    right: 12px;
	    bottom: 17px;
	}
	
	/* 화면 경로 표시(미사용)*/
	.ema_header_path {    
	    background: #366775;
	    color: #d8ecf2;
	    border-radius: 0px;
	    /*margin-top: 20px;*/
	    text-align: left;
	    padding: 10px 10px 10px;
	    font-size: 0.8em;            
	}
	
	.card {
	    /*border: 0;*/
	    box-shadow: 0 2px 3px 0 rgb(0 0 0 / 5%); /* 좌우 상하 흐려짐정도 크기 색상 */
	}
	
	.card-header {
	    background-color: rgba(0,0,0,0);
	    border-bottom: 1px solid rgba(0,0,0,0);
	}
	
	.footer {
	    position: fixed;
	    left: 0;
	    bottom: 0;
	    width: 100%;
	    background-color: red;
	    color: #fefefe;
	    text-align: center;
	    font-size: 1.1em;
	}
	
	.spinner-border{
	    display: block;
	    position: fixed;
	    font-size: 1.5em;    
	    width: 4rem; 
	    height: 4rem;
	    z-index: 1031;
	    top:calc(50% - (58px/2));
	    right:calc(50% - (58px/2));
	}
	
	</style>