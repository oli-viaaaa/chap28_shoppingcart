<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 메뉴 관련 정의 -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary" id="t_header">
	<div class="container">
		<a class="navbar-brand" href="${pageContext.request.contextPath}/">Membership ShoppingMall</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mx-auto">
				<li class="nav-item">
					<a class="nav-link active"	aria-current="page" href="${pageContext.request.contextPath}/main">Home</a></li>
				<li class="nav-item">
					<a class="nav-link" id="cart" 
						href="${pageContext.request.contextPath}/mypage/cartList">Cart
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" id="order"  
						href="${pageContext.request.contextPath}/mypage/orderList">Order
					</a>
				</li>
			</ul>
		</div>
		<div class="flex-grow-1">
			<div class="navbar-collapse collapse w-100 order-3 dual-collapse2">
				<ul class="navbar-nav ms-auto">
					<c:if test="${not empty sessionScope.user.userId}" >
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/userUpdate?userId=${sessionScope.user.userId}">(${sessionScope.user.userName}님) </a></li>
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a></li>
					</c:if>
					<c:if test="${empty sessionScope.user.userId}" >
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
						</li>
					</c:if>	
				</ul>
			</div>
		</div>
	</div>
</nav>