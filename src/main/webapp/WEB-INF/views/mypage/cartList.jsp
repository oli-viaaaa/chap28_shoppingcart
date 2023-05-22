<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	// 세션이 있으면 이미 로그인을 했기 때문에 로그인 페이지는 보여줄 필요 없음.
	/* if(request.().getAttribute("user") != null){
		//response.sendRedirect("index.jsp");	// index.jsp 페이지로 이동
	} */
	
%>
<!DOCTYPE html>
<html>
<head>
<title>쇼핑카트</title>
	<!-- Cart List  전담 CSS -->
	<style type="text/css">
		.table tbody td{
			vertical-align: middle;
		}
		.table thead tr th{
			text-align: center;
		}
		.btn-incre, .btn-decre{
			font-size: 35px;
			box-shadow: none;
		}
	</style>
	
	<%@include file="/WEB-INF/includes/header.jsp" %>

	<!-- 자바스크립트를 외부 파일로 빼고 include해줌(WebContent/js/cartList.js) -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/cartList.js"></script>

</head>
<body>
	<%@include file="/WEB-INF/includes/navbar.jsp"%>
	
	<div class="container">
		<div class="d-flex py-3">
			<h3>총 금액 : 200,000원</h3>
			<a class="btn btn-primary" href="#" onclick="location.href='${pageContext.request.contextPath}/checkout'">주문</a>
		</div>
		<form name="formm" id="formm" action="${pageContext.request.contextPath}/mypage/checkout" method="post">
			<input type="hidden" name="userId" value="${userId}">
			<div id="wrap" align="center">
			
				<table id="datatable"
					class="table table-striped table-lought dt-responsive nowrap"
					style="width: 100%">
					<thead>
						<tr>
							<th scope="col">선택</th>
							<th scope="col">상품명</th>
							<th scope="col">가격</th>
							<th scope="col">수량</th>
							<th scope="col">금액</th>
							<th scope="col">주문일자</th>
							<th scope="col">삭제</th>
							<!-- <th scope="col" style="display:none;">히든</th> -->
						</tr>
					</thead>
		
					<tbody>
					<c:choose>
						<c:when test="${carts.size() <= 0}">
							<tr>
								<td colspan="9" align="center" height="23">장바구니가 비었습니다.</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${carts}" var="cart" varStatus="status">
								<tr>
									<td>	<!-- 선택 체크박스 -->
										<input type="checkbox" class="chkCart" id="chkCart[${status.index }]" name="chkCart" value="${cart.productId}" onchange="fnChkChange(${status.index })" checked>	<!-- 유저ID-상품ID key -->
									</td>
									<td>	<!-- 상품명 -->
										<a href="${pageContext.request.contextPath}/detailView?productId=${cart.productId }">${cart.productName }</a>
									</td>
									<td> <!-- 가격(단가) -->
										<label id="unitPrice[${status.index }]" class="label unitPrice">
											<fmt:formatNumber value="${cart.unitPrice}" />
										</label>
									</td>
									<td>	<!-- 수량 -->
										<div class="form-group d-flex justify-content-between">
											<a class="btn btn-sm btn-decre" id="minusQty[${status.index }]" href="#" style="color:red"  
												onclick="return minusBtnClick(${status.index })">
												<i class="fas fa-minus-square"></i>
											</a>
											<input type="number" name="quantity" id="quantity[${status.index }]" class="form-control" 
												style="width: 100px;" onchange="changeQuantity(${status.index})" required 
												value="${cart.quantity}"><br>
											<a class="btn btn-sm btn-incre" id="plusQty[${status.index }]" href="#" style="color:blue" 
												onclick="return plusBtnClick(${status.index })">
												<i class="fas fa-plus-square"></i>
											</a>
										</div>
									</td>
									<td class="amt">	<!-- 금액(수량 * 단가) -->
										<label id="amt[${status.index }]">
											<fmt:formatNumber value="${cart.unitPrice * cart.quantity}" />
										</label>
									</td>
									<td>	<!-- 저장일자 -->
										<fmt:formatDate value="${cart.createDate}" type="date" />
									</td>
									<td style="text-align:center;">	<!-- 삭제버튼 -->
										<a href="#" class="btn btn-warning btnDelete" >삭제</a>
									</td>
									<td>	<!-- 단가 -->
										<input type="hidden" name="unitPrice" id="unitPrice[${status.index }]" value="${cart.unitPrice}" >	
									</td>
									<td>	<!-- 상품ID -->
										<input type="hidden" name="productId" class="productId" id="productId[${status.index }]" value="${cart.productId}" >	<!-- 상품ID -->
									</td>									
																		
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
					</tbody>
					<tfoot>
						<tr>
							<th colspan="4" style="text-align:center;">총 액</th>
							<th colspan="2">
								<label id="lblTotalAmt">
									<fmt:formatNumber value="${totalAmt}" />
								</label>
							</th>
							<c:if test="${cartList.size() != 0}">
								<th><a href="#" class="btn btn-primary btnOrder" >주문하기</a></th>
							</c:if>								
						</tr>
					</tfoot>
				</table>
			</div>
			
			<div id="buttons" style="float: right">
			<input type="button" value="쇼핑하기" class="btn btn-primary" onclick="location.href='${pageContext.request.contextPath}/main'">			
			<c:if test="${msg}">
				<p class="check_font text-danger" id="order_error">${msg}</p>
			</c:if>
		</div>
		</form>
	</div>  <!-- end container -->
	

	<%@include file="/WEB-INF/includes/footer.jsp" %>
</body>
</html>