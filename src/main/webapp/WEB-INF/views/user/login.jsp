<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%
	// login.jsp 페이지는 navbar를 import하지 않기 때문에 jstl 라이브러리를 직접 import해야
	// <c:if test 문구를 사용하려면 jstl 라이브러리가 있어야 한다.
	
	
%> 
<!DOCTYPE html>
<html>
<head>
<title>로그인</title>
<%@include file="/WEB-INF/includes/header.jsp" %>
<style>
#login_error{
	.font-color:red;
}
</style>
</head>
<body>
	<div class="container">
		<div class="card w-50 mx-auto my-5">
			<div class="card-haeder text-center">로그인
			</div>
			<div class="card-body">
				<form action="${pageContext.request.contextPath}/login" method="post" autocomplete="on">
					<input type="hidden" name="pageName" value="${pageName }">
					<input type="hidden" name="productId" value="${productId }">
					<div class="form-group">
						<label>아이디</label>
						<input type="text" class="form-control" name="userId" value="${param.userId }"  autofocus>
						<c:if test="${userIdErr}" >
							<p class="check_font text-danger">아이디를 입력하세요.</p>
						</c:if>
					</div>
					<div class="form-group">
						<label>비밀번호</label>
						<input type="password" class="form-control" name="userPwd" value="${param.userPwd }" >
						<c:if test="${userPwdErr}">
							<p class="check_font text-danger">비밀번호를 입력하세요.</p>
						</c:if>
					</div>
					<c:if test="${idPwdNotMatch}">
						<p class="check_font text-danger" id="login_error" >아이디와 비밀번호가 맞지 않습니다.</p>
					</c:if>
					<div class="text-center mt-3">
						<button type="submit" class="btn btn-primary">로그인</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
<%@include file="/WEB-INF/includes/footer.jsp" %>
</body>
</html>