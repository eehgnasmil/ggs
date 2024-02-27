// resign.js
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$(function(){

});


//prevPwCheck 기존 비밀번호 체크 후 true면 save, false면 alert
function prevPwCheck2() {
   var id = $('#password1').attr('name');
   var pw = $('#password1').val();      
    //비밀번호 빈칸 or  2,3이 일치하지 않을 떄,
    if(pw ==""){
   
   }else{
      $.ajax({
            type: 'POST',
            data: {
                "id": id,
                "prevPw": pw,
            },
            url: "/mypage/prevPwCheck",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
                // JSON으로부터 전달된 boolean 값에 따라 처리
                if (json) {
                    resign();
                } else {
                    // json이 false이거나 rs가 'false'일 경우
                    alert("비밀번호를 다시 확인 하세요");
                }
            },
            error: function () {
                console.log("error");
            },
        });
    }
}


function resign(){
   var id = $('#password1').attr('name');   
      $.ajax({
            type: 'POST',
            data: {
                "id": id
            },
            url: "/mypage/resign",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
                 alert("이용해 주셔서 감사합니다.");
                 window.location.replace("/");  
            },
            error: function () {
                console.log("error");
            },
        });   
}