// findId.js

$(function(){

});
//이메일로 아이디 발송
let emailresult = 0
function sendNumber(id){
var emailForm = $('#customEmail').val()
var Email = $('#email').val() + "@" + emailForm;
       $.ajax({
           url:"/mail/id",
           type:"post",
           dataType:"json",
           data:{"mail" : Email,
           "id" :id
           },
           success: function(data){
               alert("이메일로 아이디를 발송했습니다. 확인 후 로그인하세요");
				window.location.replace("/member/login");	               
           },
           error:function(){
            alert("이메일 양식이 아닙니다. 확인하세요");
         }
           });

}   