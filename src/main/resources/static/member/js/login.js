// join.js
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$(function(){

});

//중복, null체크 후 검증완료데이터만 전송
function login(){	
    $.ajax({
		type: 'POST',
        data : {
            "checks" : checks,
            "value" : inputed
        },
        url : "checking",
        beforeSend: function(xhr){
  			  xhr.setRequestHeader(header, token);
		 },
        success : function(json) {
			if(json=="1"){			
				$(element).next('div:first').addClass( 'x' );
				$(element).next('div:first').css("display", "block");

			}else{
				$(element).next('div:first').removeClass( 'x' );						
				$(element).next('div:first').css("display", "none");
			}   
        },
        error: function() {
        console.log("error");
      	},
    });		
}
	