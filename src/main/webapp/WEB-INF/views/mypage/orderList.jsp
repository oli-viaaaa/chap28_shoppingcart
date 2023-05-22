<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<title>주문서</title>
	<!-- 주문서 리스트 전용 CSS -->
	<style type="text/css">
		.table tbody td {
			vertical-align: middle;
		}
		.table thead tr th {
			text-align: center;
		}
		.btn-incre, .btn-decre {
			font-size: 35px;
			box-shadow: none;
		}
	</style>
	
	<%@include file="/WEB-INF/includes/header.jsp"%>
	
</head>
<body>
	<%@include file="/WEB-INF/includes/navbar.jsp"%>

	<div class="container">
		<form name="formm" id="formm"
			action="${pageContext.request.contextPath}/mypage/insertOrder" method="post">
			<input type="hidden" name="userId" value="${userId}">
	
			<main style="margin-top: 80px;">
					<h3 class="text-center">주문내역(Order List)</h3>
					<!-- 주문목록 시작 -->
					<div class="row">
						<div>
							<table id="datatable"
								class="table table-striped table-lought dt-responsive nowrap"
								style="width: 100%">
								<thead>
									<tr>
										<th scope="col">상품명</th>
										<th scope="col">판매가</th>
										<th scope="col">수량</th>
										<th scope="col">주문금액</th>
										<!-- <th scope="col">상품ID</th> -->
									</tr>
								</thead>
	
								<tbody>
									<c:choose>
										<c:when test="${orderItems.size() <= 0}">
											<tr>
												<td colspan="9" align="center" height="23">주문한 상품이 없습니다.</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${orderItems}" var="orderItem"
												varStatus="status">
												<tr>
													<td>
														<!-- 상품명 --> <a
														href="${pageContext.request.contextPath}/detailView?productId=${orderItem.productId }">${orderItem.productName }</a>
														<input type="hidden" name="productId"
														value="${orderItem.productId}">
													</td>
													<td style="text-align: right;">
														<!-- 가격(단가) --> <label id="unitPrice[${status.index }]">
															<fmt:formatNumber value="${orderItem.unitPrice}" />
													</label> <input type="hidden" class="form-control" name="unitPrice"
														value="${orderItem.unitPrice}">
													</td>
													<td style="text-align: right;">
														<!-- 수량 --> <label> <fmt:formatNumber
																value="${orderItem.quantity}" />
													</label> <input type="hidden" class="form-control" name="quantity"
														value="${orderItem.quantity}">
													</td>
													<td style="text-align: right;">
														<!-- 금액(수량 * 단가) --> <label id="amt[${status.index }]">
															<fmt:formatNumber
																value="${orderItem.unitPrice * orderItem.quantity}" />
													</label> <input type="hidden" class="form-control" name="amt"
														value="${orderItem.unitPrice * orderItem.quantity}">
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
						<!-- 						
						<div id="buttons" style="float: right">
							<input type="button" value="쇼핑 계속하기" class="cancel" onclick="location.href='ShoppingServlet?command=index'">
							<c:if test="${cartList.size() != 0}">
								<input type="button" value="주문하기" class="submit" onclick="go_order_insert()">
							</c:if>
						</div>
						-->
					</div>
					<!-- 주문목록 종료 -->
					<!-- 주소입력 결재금액 시작 -->
					<div class="row">
						<div class="d-flex justify-content-between mt-3">
							<div class="col-6">
								<div class="box">
									<div>
										<label style="font-weight: 600;">배송정보</label> <br />
									</div>
									<div class="d-flex mt-2">
										<div class="col-2">
											<label class="px-3">주소지</label>
										</div>
										<div class="col-10">
											<input type="text" name="address" class="form-control"
												value="${orderHeader.address}">
										</div>
									</div>
	
									<div class="mt-4">
										<label style="font-weight: 600;">결제방법</label>
									</div>
									<div class="d-flex mt-2">
										<c:set var="pm" value="${fn:trim(orderHeader.paymentMethod)}" />
										<div class="form-check px-5">
											<input class="form-check-input" type="radio"
												name="paymentMethod" id="paymentMethod1"
												<c:if test="${pm eq '1'}">checked </c:if>> 
											<label class="form-check-label" for="paymentMethod1">현금 </label>
										</div>
										<div class="form-check px-5">
											<input class="form-check-input" type="radio"
												name="paymentMethod" id="paymentMethod2"
												<c:if test="${pm eq '2'}">checked </c:if>> 
											<label class="form-check-label" for="paymentMethod2"> 카드 </label>
										</div>
										<div class="form-check px-5">
											<input class="form-check-input" type="radio"
												name="paymentMethod" id="paymentMethod3"
												<c:if test='${pm eq "3"}'>checked </c:if>> 
											<label class="form-check-label" for="paymentMethod3"> 휴대폰 </label>
										</div>
									</div>
								</div>
							</div>
							<div class="col-1"></div>
							<div class="col-5">
								<div class="box">
									<div>
										<label style="font-weight: 600;">결제금액</label>
									</div>
									<div class="card bg-white rounded mt-2">
										<div class="card-body" style="line-height: 30px;">
											<div class="row d-flex">
												<div class="col-3">
													<label>상품금액</label>
												</div>
												<div class="col-9" style="text-align: right;">
													<label id="lblTotalAmt"> <fmt:formatNumber
															value="${orderHeader.totalAmt}" />
													</label>
												</div>
											</div>
											<div class="row d-flex">
												<div class="col-3">
													<label>할인금액</label>
												</div>
												<div class="col-9" style="text-align: right;">
													<label id="lblTotalAmt" style="color: red;">0 </label>
												</div>
											</div>
											<div class="row d-flex">
												<div class="col-3">
													<label>배송비</label>
												</div>
												<div class="col-9" style="text-align: right;">
													<label id="lblTotalAmt">2,500 </label>
												</div>
											</div>
											<div class="row d-flex">
												<div class="col-3">
													<label>총 결제금액</label>
												</div>
												<div class="col-9" style="text-align: right;">
													<label id="lblTotalAmt"
														style="color: red; font-weight: 600;"> <fmt:formatNumber
															value="${orderHeader.totalAmt + 2500}" />
													</label> <input type="hidden" class="form-control" name="totalAmt"
														value="${orderHeader.totalAmt + 2500}">
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- 주소입력 종료 -->
					<!-- 결제하기 -->
					<div class="d-flex mt-5">
						<div class="col-4"></div>
						<div class="col-4" style="text-align: center;">
							<a href="${pageContext.request.contextPath}/main"
								class="btn btn-primary">쇼핑하기</a>
						</div>
						<div class="col-4"></div>
					</div>
			</main>
		</form>
	</div> <!-- end container -->
	
	<%@include file="/WEB-INF/includes/footer.jsp"%>
</body>
</html>