// join.js
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$(function(){

});

function resign(){
	var result = confirm("정말 탈퇴 하시겠습니까?");
	if (result) {
		window.location.replace("/mypage/myResign");		
	} else {
		window.location.replace("/mypage/myList");		
	}	
}

