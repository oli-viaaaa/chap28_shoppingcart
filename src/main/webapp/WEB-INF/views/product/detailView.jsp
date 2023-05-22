<%@page import="com.javalab.shopping.dao.ProductDao"%>
<%@ page import="com.javalab.shopping.model.*"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%

%>

<!DOCTYPE html>
<html lang="ko">
<head>
<title>쇼핑몰에 오신걸 환영합니다.</title>

	<%@include file="/WEB-INF/includes/header.jsp"%>

</head>
<body>
	<%@include file="/WEB-INF/includes/navbar.jsp"%>

	<div class="container mt-5">
		<form action="${pageContext.request.contextPath}/insertCart" name="formm" method="post">
			<!-- 히든 필드 -->
			<input type="hidden" name="pageName" value="${pageName}">	<!-- DetailViewServlet에서 request 객체에 담아놓은 product 객체 사용 -->
			<input type="hidSden" name="productId" id="productId" value="${product.productId}">	<!-- DetailViewServlet에서 request 객체에 담아놓은 product 객체 사용 -->
			<input type="hidden" name="userId" id="userId" value="${user.userId}">	<!-- navbar에서 세션에 저장한 User객체를 가져와서 세팅해놓은 걸 사용 -->
			<input type="hidden" name="unitPrice" value="${product.unitPrice}">	<!-- navbar에서 세션에 저장한 User객체를 가져와서 세팅해놓은 걸 사용 -->
			
	         <!--card(상품 사진 + 정보) 시작-->
	         <div class="card mt-5 p-3 mb-5 bg-white rounded">
	             <div class="card-body">
	                 <div class="d-flex">
                         <!-- 사진 시작-->
	                     <div class="col-lg-5 col-md-5 col-sm-5 col-5">
	                         <div id="photo">
	                             <div class="row">
	                                 <a class="btn">
	                                     <img id="imgphoto" class="img-fluid" data-toggle="modal" data-target="#originalImage" 
	                                     	style="border-radius:5px;" src="${pageContext.request.contextPath}/images/${product.image}" alt="" />
	                                 </a>
	                             </div>        
	                             <div class="row">
	                                 <label></label>
	                             </div>
	                         </div>
	                     </div>
	                     <!-- 사진 종료 -->
	                     <!-- 정보 시작-->
	                     <div class="col-lg-7 col-md-7 col-sm-7 col-7">
	                     	<div id="productDetail" style="line-height:40px;">
	                            <div class="fw-bold" style="font-size:1.3rem;">
									<label>${product.productName}</label>
								</div>
	                            <div class="fw-bold">
	                                 	가격&nbsp;<label class="text-danger" id="unitPrice" >${product.unitPrice}</label>
	                                 <label> 원</label>
	                            </div>
								<div class="input-group" style="width:50%">
									<div class="input-group-prepend">
										<span class="input-group-text">
											<a class="btn btn-sm" onclick="return minusBtnClick()" href="#"><i class="fa fa-minus" aria-hidden="true"></i></a>
										</span>
									</div>
									<input type="number" class="form-control" id="quantity" name="quantity" style="width: 10px;" onchange="changeQuantity()" aria-label="quantity" value="1" required autofocus>
									<div class="input-group-append">
										<span class="input-group-text">
											<a class="btn btn-sm " onclick="return plusBtnClick()"  href="#"><i class="fa fa-plus" aria-hidden="true"></i></a>
										</span>
									</div>
								</div>
	                            <div class="fw-bold">
	                                 <label> 총결제금액&nbsp;</label>
	                                 <label class="text-danger" id="totalAmt">${product.unitPrice}</label>	                                 
	                                 <label> 원</label>
	                            </div>								
							</div>
							<div class="mt-4" id="buttons">
								<input type="button" value="장바구니" class="btn btn-primary" onclick="return fnInsertCart();"> 
								<input type="button" value="구매하기"	class="btn btn-warning" onclick="return fnCheckout();"> 
								<input type="reset" value="목록" class="cancel btn btn-secondary">
							</div>
						</div>
	                 	 <!-- 정보 종료 -->
	                 </div>
	             </div>
	         </div>
	         <!-- 상세 정보 -->
			<div>
				<div id="detail">
					<div class="row">
						<label style="text-align:center; font-weight:600; font-size:1.5rem;">
							상세 정보
						</label>
						<a class="btn mt-3"> 
							<img id="imgphoto" style="border-radius: 5px;width:500px; height:400px;" src="${pageContext.request.contextPath}/images/${product.image}" alt="" />
						</a>
					</div>
					<div class="row">
						<label></label>
					</div>
				</div>
			</div>
		</form>
		<!-- card -->	
	</div>
	<!-- container -->

<script type="text/javascript">

	/*
	 [카트에 저장]
		- 1. 로그인 한 상태 : 카트에 저장 작업
		- 2. 로그인 안 한 상태 : 현재 상품 상세 페이지 이름을 로그인 페이지로 전달해서 로그인 성공 후에 그 페이지로 다시 이동할 수 있도록 한다.
	*/
	function fnInsertCart() {
	
		let user = '${sessionScope.user}'; // 세션에서 user 객체 조회
		//로그인 전이면 현재 페이지 이름(detailView)을 로그인 페이지로 전달해서 로그인 후에 다시 이 페이지로 올 수 있도록
		let url = "${pageContext.request.contextPath}/login?pageName=" + "${pageName}&productId=${product.productId}";
		if(user == "" || user == null){
			if(confirm("로그인 전입니다. 로그인 하시겠습니까?")){
				location.href = url;	// 여기서 바로 화면이 전환되는 것이 아니라 아래로 계속 진행됨. 그래서 return false 해줘야 이 함수가 끝남.
			}
			return false;
			
		}
		
		var productId = "";
		var quantity = 0;
		var unitPrice = 0;

		if (document.getElementById('quantity').value == "") {
			alert('구매 수량을 입력하세요.');
			return false;
		}else{	
			quantity = Number(document.getElementById('quantity').value);
			if (quantity <= 0) {
				alert("구매 수량을 확인하세요.");
				return false;
			}
		}
		
		document.formm.submit();
	}

	/*
	 [카트 저장]
		- 1. 주문할 상품 정리해서 보여주는 페이지로 이동
	*/
	function fnCheckout() {
		alert('장바구니 기능을 이용하세요.');
		return false;
		
		let user = '${sessionScope.user}'; // 세션에서 user 객체 조회
		//로그인 전이면 현재 페이지 이름(detailView)을 로그인 페이지로 전달해서 로그인 후에 다시 이 페이지로 올 수 있도록
		let url = "${pageContext.request.contextPath}/login?pageName=" + "${pageName}&productId=${product.productId}";
		if(user == "" || user == null){
			if(confirm("로그인 전입니다. 로그인 하시겠습니까?")){
				location.href = url;	// 여기서 바로 화면이 전환되는 것이 아니라 아래로 계속 진행됨. 그래서 return false 해줘야 이 함수가 끝남.
			}
			return false;
		}

		let productId = document.getElementById("productId").value;
		let quantity = document.getElementById("quantity").value;
		let unitPrice = document.getElementById("unitPrice").innerText;
		let totalAmt = document.getElementById("totalAmt").innerText;
		
		
		if (quantity == "") {
			alert('주문 수량을 입력하세요.');
			return false;
		}else{	
			quantity = Number(quantity);
			if (quantity <= 0) {
				alert("주문 수량을 확인하세요.");
				return false;
			}
		}
		
		url = "${pageContext.request.contextPath}/checkout";	//결제페이지
		
		let f = document.createElement('form');	//실제로 화면에는 없는 <form>을 생성
		
	    let objProductId;
	    objProductId = document.createElement('input');
	    objProductId.setAttribute('type', 'hidden');
	    objProductId.setAttribute('name', 'productId');		//가상폼의 바디에 변수 저장(post)
	    objProductId.setAttribute('value', productId);
	    
	    let objQuantity;
	    objQuantity = document.createElement('input');
	    objQuantity.setAttribute('type', 'hidden');
	    objQuantity.setAttribute('name', 'quantity');		//가상폼의 바디에 변수 저장(post)
	    objQuantity.setAttribute('value', quantity);

	    let objUnitPrice;
	    objUnitPrice = document.createElement('input');
	    objUnitPrice.setAttribute('type', 'hidden');
	    objUnitPrice.setAttribute('name', 'unitPrice');		//가상폼의 바디에 변수 저장(post)
	    objUnitPrice.setAttribute('value', unitPrice);	    
	    
	    let objTotalAmt;
	    objTotalAmt = document.createElement('input');
	    objTotalAmt.setAttribute('type', 'hidden');
	    objTotalAmt.setAttribute('name', 'totalAmt');		//가상폼의 바디에 변수 저장(post)
	    objTotalAmt.setAttribute('value', totalAmt);	    	    
	    
	    
	    f.appendChild(objProductId);
	    f.appendChild(objQuantity);
	    f.appendChild(objUnitPrice);
	    f.appendChild(objTotalAmt);
	    
	    f.setAttribute('method', 'post');
	    f.setAttribute('action', url);
	    document.body.appendChild(f);

	    f.submit();

	}	
	
	// 상품 수량 감소 버튼
	function minusBtnClick() {
		var quantity = 0;
	
		if (document.formm.quantity.value == ""
				|| document.formm.quantity.value == null) {
			alert('구매 수량을 입력하세요.');
			document.formm.quantity.focus();
			return false;
		}

		quantity = parseInt(document.formm.quantity.value);

		if (quantity <= 1) {
			alert('구매 수량은 한 개 이상이어야 합니다.');
			document.formm.quantity.focus();
			return false;
		}

		quantity = quantity - 1;
		document.formm.quantity.value = quantity;

		//총 결제금액 계산
		var totalAmt = 0;
		var unitPrice = 0;

		if (document.getElementById('unitPrice').innerText != "") {
			unitPrice = Number(document.getElementById('unitPrice').innerText);
		}

		totalAmt = quantity * unitPrice;

		document.getElementById('totalAmt').innerText = totalAmt; // label에 값을 세팅할때

		return false;
	}

	// 상품 수량 증가
	function plusBtnClick() {
		var quantity = 0;

		if (document.formm.quantity.value == ""
				|| document.formm.quantity.value == null) {
			alert('구매 수량을 입력하세요.');
			document.formm.quantity.focus();
			return false;
		}

		quantity = parseInt(document.formm.quantity.value);

		if (quantity >= 100) {
			alert('구매 수량은 100개 까지만 구매할 수 있습니다.');
			document.formm.quantity.value = 100;
			document.formm.quantity.focus();
			return false;
		}

		quantity = quantity + 1;
		document.formm.quantity.value = quantity;

		//총 결제금액 계산
		var totalAmt = 0;
		var unitPrice = 0;

		if (document.getElementById('unitPrice').innerText != "") {
			unitPrice = Number(document.getElementById('unitPrice').innerText);
		}

		totalAmt = quantity * unitPrice;

		document.getElementById('totalAmt').innerText = totalAmt; // label에 값을 세팅할때

		return false;
	}

	//상품 수량 변경 이벤트 핸들러
	function changeQuantity() {
		var quantity = 0;

		if (document.formm.quantity.value == ""
				|| document.formm.quantity.value == null) {
			alert('구매 수량을 입력하세요.');
			document.formm.quantity.focus();
			return false;
		}

		quantity = parseInt(document.formm.quantity.value);

		if (quantity < 1) {
			alert('구매 수량은 한 개 이상이어야 합니다.');
			document.formm.quantity.focus();
			return false;
		}

		//총 결제금액 계산
		var totalAmt = 0;
		var unitPrice = 0;

		if (document.getElementById('unitPrice').innerText != "") {
			unitPrice = Number(document.getElementById('unitPrice').innerText);
		}

		totalAmt = quantity * unitPrice;

		document.getElementById('totalAmt').innerText = totalAmt; // label에 값을 세팅할때		

		return false;
	}	

</script>

	<%@include file="/WEB-INF/includes/footer.jsp"%>
</body>
</html>