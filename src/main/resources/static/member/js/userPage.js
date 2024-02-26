// userPage.js
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$(function(){

});

//소개글 수정확인
function introCheck(){
	var result = confirm("수정 하시겠습니까?");
	if (result) {
		createIntro()				
	} else {
		history.go();		
	}	
}

//소개글 수정
function createIntro(){
	var intro= $('#intro').val();
	var id= $('#id').text();
	var toUserId = $('#id').attr('name');	
	  $.ajax({
      type: 'POST',
        data : {
            "intro" : intro,
            "id" : id
        },
        url : "/member/intro",
        beforeSend: function(xhr){
             xhr.setRequestHeader(header, token);
       },
        success : function(json) {
				alert("수정 되었습니다.");
			window.location.replace("/member/userPage/"+toUserId);					
        },
        error: function() {
				alert("잠시 후 다시 시도하세요");			
        	console.log("error");
         },
    });      
}	

//follow 구독
function follow(){
	var toUserId = $('#id').attr('name');
	var fromUserId = $('#followBtn').attr('name');
	  $.ajax({
      type: 'POST',
        data : {
            "toUserId" : toUserId,
            "fromUserId" : fromUserId
        },
        url : "/member/follow",
        beforeSend: function(xhr){
             xhr.setRequestHeader(header, token);
       },
        success : function(json) {
			window.location.replace("/member/userPage/"+toUserId);					
        },
        error: function() {

         },
    });  	
}
	
//unFollow
function unFollow(){
	var toUserId = $('#id').attr('name');
	var fromUserId = $('#followBtn').attr('name');
	  $.ajax({
      type: 'POST',
        data : {
            "toUserId" : toUserId,
            "fromUserId" : fromUserId
        },
        url : "/member/unFollow",
        beforeSend: function(xhr){
             xhr.setRequestHeader(header, token);			
       },
        success : function(json) {
			window.location.replace("/member/userPage/"+toUserId);			
        },
        error: function() {

         },
    });  	
	
}