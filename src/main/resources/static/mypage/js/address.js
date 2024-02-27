// resign.js
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$(function(){

});
function saveAddress(){
	var postcode = $('#sample6_postcode').val();
	var postaddress = $('#sample6_address').val();
	var detailaddress = $('#sample6_detailAddress').val();
	var idx = $('.idForm').attr('name');
	var data = {
		"idx":idx,
		"postcode" : postcode,
		"postaddress" : postaddress,
		"detailaddress" : detailaddress
	};
	if(addressNullCheck()){
		 	$.ajax({
				type: 'POST',
		        data : data,
		        url : "/mypage/saveAddress",
		        beforeSend: function(xhr){
		  			  xhr.setRequestHeader(header, token);
				 },
		        success : function(json) {
					window.location.replace("/mypage/myList");						
		        },
		        error: function() {
					alert("잠시 후 다시 시도하세요.")
		      	},
		    });	
	}
	
}

function addressNullCheck(){
	var postCode = $('#sample6_postcode').val();
	if(postCode==''){
		alert("주소는 필수 입력사항입니다.")
	}else{
		return true
	}
}