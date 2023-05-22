/**
 * 장바구니에서 주문처리 자바스크립트
 */
$(document).ready(function(){
	/*
	* [삭제 버튼 클릭 핸들러(처리기, 메소드)]
	* 본 jsp 페이지는 전체를 감싸는 form이 있고 그 안에서 행별로 지워야 한다
	* 행별로 삭제할 때는 현재 jsp페이지 전체를 submit()하는 것이 아니기 때문에
	* 삭제 버튼이 눌린 그 행만 submit()을 시켜야 한다.
	* 그래서 별도의 form을 creation하고 거기에 name 속성을 만들어서 넣고
	* value 속성도 만들어서 넣는다.
	* 최종적으로 만든 form을 submit()하면 post 방식으로 submit()할수있다.
	* 이 방식은 난이도가 좀 있는 편이다.
	*/
  	$('.btnDelete').on('click', function(){
  		
  		if(!confirm('정말 삭제하시겠습니까?')){
  			return false;
  		}
  		
  		// 각 행의 체크박스에 상품ID값을 value 속성에 달아놓음
  		// $(this) : 현재 삭제 버튼이 눌린 버튼 자신을 가리킴
  		// 클릭된 삭제 버튼의 행을 먼저 찾고 그 행에 속한 체크박스 값을 취함
  		// 이방식으로 하면 굳이 각 행마다 상품ID를 hidden tag로 달아줄 필요 없음
  		var productId = $(this).closest('tr').find('input:checkbox').val(); // 체크박스값(상품ID)
  		
  		// 자바스크립트 변수 선언 방식 (let vs var) let은 변수명 중복 선언 막아줌
		let url = "${pageContext.request.contextPath}/mypage/deleteCart";
		
		let virtualForm = document.createElement('form');	//실제로 화면에는 없는 가상 <form>을 생성
	    
	    let obj;
	    obj = document.createElement('input');
	    obj.setAttribute('type', 'hidden');
	    obj.setAttribute('name', 'productId');
	    obj.setAttribute('value', productId);
	    
	    virtualForm.appendChild(obj);
	    virtualForm.setAttribute('method', 'post');
	    virtualForm.setAttribute('action', url);
	    document.body.appendChild(virtualForm);	//body에 가상폼을 요소로 추가함
	    virtualForm.submit(); //전송	  		
		
  	});	// end click event
  	
	// 주문 버튼 클릭 핸들러(메소드, 처리기)
	$('.btnOrder').on('click', function(){


		if(!confirm('주문하시겠습니까?')){
  			return false;
  		}else{
			fnCheckoutJquery();	// 주문 진행 메소드 콜
  		}
		
	}); // end btnOrder click
  	
  	
});	// end document ready	
	

//컨텍스트 패스 경로를 얻어오는 메소드
function getContextPath() {
	return sessionStorage.getItem("contextpath");
}

// 주문 진행 메소드
function fnCheckoutJquery(){
	
	// 체크된 행이 하나라도 있어야 주문 진행
	let count = 0;
	
	// 상품ID, 수량, 금액을 콤머로 구분해서 담기 위한 임시 변수
	let strProductId = "";
	let strQuantity = "";
	let strUnitPrice = "";
	
	// 모든 행을 반복하면서 체크된 행들의 상품ID, 수량, 금액을 문자열로 만듦
	$('#datatable tbody tr').each(function (index, ele) {
		
		// 체크박스 체크 여부 확인(체크된 행만 주문)
		// 상품ID, 수량, 단가를 콤머(",") 구분해서 서블릿으로 전송함(서블릿에서 split해서 사용하도록)
		let checked = $(this).closest('tr').find('input:checkbox').is(":checked");
		if(checked == true){ 					
			
			let _productId = $(this).closest('tr').find('input:checkbox').val(); // 체크박스에 달아놓은 상품ID
			strProductId += _productId + ",";	//콤머(,)로 연결해서 서버로 전송하기 위해서
			
			let _quantity = $("input[type=number][name=quantity]").val(); // 주문 수량(input text)
			strQuantity += fnRemoveComma(_quantity) + ",";

			let _unitPrice = $(this).closest('tr').find(".label.unitPrice").text().trim();
			strUnitPrice += fnRemoveComma(_unitPrice) + ",";
			
			count++;
		}
	}); // end table each loop
	
	if(count <= 0){
		alert('주문할 항목이 없거나 선택된 행이 없습니다.');
		return false;
	}

	// Total Amt 총 주문 금액
	let _totalAmt = $("#lblTotalAmt").text();
	let totalAmt = fnRemoveComma(_totalAmt);	
	
	/*
	 [가상폼]
	 1. 단순하게 폼태그의 값을 전송하는 것이 아님, 한 행씩 체크해서 값을 넘기기 때문에 복잡
	 2. 위에서 만든 콤머로 구분된 문자열(상품ID, 수량, 단가)과 총금액을 가상폼에 저장
	*/			

	// 자바스크립트를 별도의 파일로 뺄경우 ${pageContext.request.contextPath} 사용불가
	let url = getContextPath() + "/mypage/checkout";
	
	let f = document.createElement('form');	//실제로 화면에는 없는 가상의 <form>을 생성해서 거기에 값들을 담는다.
    
    let objProdudctId;	// productId용
    objProdudctId = document.createElement('input');
    objProdudctId.setAttribute('type', 'hidden');
    objProdudctId.setAttribute('name', 'productId');
    objProdudctId.setAttribute('value', strProductId);
    
    let objQuantity;	// Quantity용
    objQuantity = document.createElement('input');
    objQuantity.setAttribute('type', 'hidden');
    objQuantity.setAttribute('name', 'quantity');
    objQuantity.setAttribute('value', strQuantity);

    let objUnitPirce;	// UnitPirce용
    objUnitPirce = document.createElement('input');
    objUnitPirce.setAttribute('type', 'hidden');
    objUnitPirce.setAttribute('name', 'unitPrice');
    objUnitPirce.setAttribute('value', strUnitPrice);

    let objTotalAmt;	// total amt
    objTotalAmt = document.createElement('input');
    objTotalAmt.setAttribute('type', 'hidden');
    objTotalAmt.setAttribute('name', 'totalAmt');
    objTotalAmt.setAttribute('value', totalAmt);		    
    
    f.appendChild(objProdudctId);
    f.appendChild(objQuantity);
    f.appendChild(objUnitPirce);
    f.appendChild(objTotalAmt);
    
    f.setAttribute('method', 'post');
    f.setAttribute('action', url);
    document.body.appendChild(f);	// 가상폼을 body에 추가
    f.submit();			// 서버로 전송
}	

// 마이너스 버튼 클릭 이벤트 핸들러
function minusBtnClick(row) {
	//alert("minusBtnClick");
	//alert(this);
	//alert(row);
	
	let quantity = 0;
	let unitPrice = 0;
	let amt = 0;
	
	let qty = document.getElementById('quantity['+row+']'); 	// input tag()
	let price = document.getElementById('unitPrice['+row+']');	// lable tag
	
	//한 row에 대한 금액 계산
	if (qty.value == "" || qty.value == null) {
		alert('구매 수량을 입력하세요.');
		qty.focus();
		return false;
	}else {
		quantity = parseInt(qty.value);
		if(quantity <= 1){
			alert('구매 수량은 1개 이상이어야 합니다.');
			qty.focus();
			return false;
		}else{
			quantity = quantity - 1;	// 기존 수량 - 1
			qty.value = quantity;		// 차감한 수량 세팅
			
			//가격(단가)
			unitPrice = price.innerText;
			
			//금액계산(수량 * 단가)
			amt = parseInt(quantity) * parseInt(fnRemoveComma(unitPrice));
			document.getElementById('amt['+row+']').innerText = fnAddComma(amt);
		}
	}
	// 모든 행들의 amt 합계 계산
	fnCalTotalAmt();
	
	return false;
}

// 모든 행들의 주문금액 합계 계산
function fnCalTotalAmt(){
	//let totalRows = document.formm.chkCart.length;	// 테이블의 행수(체크박스 갯수)
	let count = 0;
	let totalAmt = 0;
				
	//for문 돌면서 모든 행들의 금액을 합산해서 총금액에 세팅
	for (var i = 0; i < tbodyRows; i++) {
		if (document.getElementById('chkCart[' + i + ']').checked == true) {
			totalAmt += parseInt(fnRemoveComma(document.getElementById('amt[' + i + ']').innerText));
		}
	}						
	document.getElementById('lblTotalAmt').innerText = fnAddComma(totalAmt);			
}

//각 행의 체크박스 체크/언체크시 이벤트 핸들러(전체 금액 계산)
function fnChkChange(row){
	let totalAmt = 0;
	let amt = document.getElementById('amt['+row+']').innerText;		// 해당 행의 금액(amt) lable tag
	let checkFlag = document.getElementById('chkCart['+row+']').checked;// 해당 행의 체크 여부
	
	totalAmt = document.getElementById('lblTotalAmt').innerText; 	// 모든 행의 전체 합계 금액 label tag
	
	if(checkFlag == true){
		totalAmt = parseInt(fnRemoveComma(totalAmt)) + parseInt(fnRemoveComma(amt));
	}else{
		// 총금액 - 언체크된 행의 금액
		totalAmt = parseInt(fnRemoveComma(totalAmt)) - parseInt(fnRemoveComma(amt));
	}
	
	document.getElementById('lblTotalAmt').innerText = fnAddComma(totalAmt);
}

// 수량 증가 버튼 클릭 핸들러
function plusBtnClick(row) {
	let quantity = 0;
	let unitPrice = 0;
	let amt = 0;
	
	let qty = document.getElementById('quantity['+row+']'); 	// input tag()
	let price = document.getElementById('unitPrice['+row+']');	// lable tag
	
	//한 row에 대한 금액 계산
	if (qty.value == "" || qty.value == null) {
		alert('구매 수량을 입력하세요.');
		qty.focus();
		return false;
	}else {
		quantity = parseInt(qty.value);
		if(quantity > 100){
			alert('구매 수량은 100개 이하여야 합니다.');
			qty.focus();
			return false;
		}else{
			quantity = quantity + 1;	// 기존 수량 + 1
			qty.value = quantity;		// 차감한 수량 세팅

			//가격(단가)
			unitPrice = price.innerText;
			
			//금액계산(수량 * 단가)
			amt = parseInt(quantity) * parseInt(fnRemoveComma(unitPrice));
			document.getElementById('amt['+row+']').innerText = fnAddComma(amt);
		}
	}
	
	// 모든 행들의 amt 합계 계산
	fnCalTotalAmt();
	
	return false;
}

// 수량 변경 핸들러
function changeQuantity(row) {
	
	let quantity = 0;
	let unitPrice = 0;
	let amt = 0;
	
	let qty = document.getElementById('quantity['+row+']'); 	// input tag()
	let price = document.getElementById('unitPrice['+row+']');	// lable tag
	
	//한 row에 대한 금액 계산
	if (qty.value == "" || qty.value == null) {
		alert('구매 수량을 입력하세요.');
		qty.focus();
		return false;
	}else {
		quantity = parseInt(qty.value);
		if(quantity < 1){
			alert('구매 수량은 1개 이상이어야 합니다.');
			qty.value = 1;
			qty.focus();
			return false;
		}
		if(quantity > 100){
			alert('구매 수량은 100개 미만이어야 합니다.');
			qty.value = 100;
			qty.focus();
			return false;
		}else{
			//가격(단가) 추출
			unitPrice = price.innerText;
			
			//금액계산(수량*단가)
			amt = parseInt(quantity) * parseInt(fnRemoveComma(unitPrice));
			document.getElementById('amt['+row+']').innerText = fnAddComma(amt); //금액 세팅
		}
	}
	// 모든 행들의 amt 합계 계산
	fnCalTotalAmt();
	
	return false;
}

//주문 핸들러(세션 확인해서 없으면 로그인 화면으로 유도함.)
function fnGoCart(userId){
	var user = '${sessionScope.user}'; // 이 방법도 됨
	
	console.log(user);
	if(user == ""){
		alert("로그인을 하세요.");
		location.href="";	// 로그인 서블릿으로 이동 필요
		return false;
	}
	
	return true;
}			

//수량, 금액에 콤머 붙여주는 역할
function fnAddComma(number) {
    var number_string = number.toString();
    var number_parts = number_string.split('.');
    var regexp = /\B(?=(\d{3})+(?!\d))/g;
    if (number_parts.length > 1) {
        return number_parts[0].replace(regexp, ',') + '.' + number_parts[1];
    }
    else
    {
        return number_string.replace(regexp, ',');
    }
}

//계산하거나 또는 서버로 보내기 위해서는 수량, 금액에 붙은 콤머를 제거해야 함.
function fnRemoveComma(number) {
    if (typeof number == "undefined" || number == null || number == "") {
        return "";
    }
    var txtNumber = '' + number;
    return txtNumber.replace(/(,)/g, "");
}			
		
