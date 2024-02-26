// findPw.js
//이메일로 pw 발송
let emailresult = 0
function sendNumber(id){
var emailForm = $('#customEmail').val()
var Email = $('#email').val() + "@" + emailForm;
       $.ajax({
           url:"/mail/pw",
           type:"post",
           dataType:"json",
           data:{"mail" : Email,
           "id":id
           },
           success: function(data){
               alert("이메일로 초기화 비밀번호를 발송했습니다. 확인 후 로그인하세요");
				window.location.replace("/member/login");	               
           },
           error:function(){
            alert("잠시 후 다시 실행하세요.");
         }
           });

}
