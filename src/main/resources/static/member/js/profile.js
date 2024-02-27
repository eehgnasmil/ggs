// emailCheck.js

$(function(){

});

//실시간 중복체크   
function check(){
   var inputed = $('#email').val();
   var checks = $('#email').attr('id');
   if(checks=="emailForm"|| checks=="customEmail" ||checks=="email"){
      inputed =  $('#email').val() + "@" + $('#customEmail').val();
   }   
    $.ajax({
      type: 'POST',
        data : {
            "checks" : checks,
            "value" : inputed
        },
        url : "/member/email/checking",
        beforeSend: function(xhr){
             xhr.setRequestHeader(header, token);
       },
        success : function(json) {
			 if(confirm(inputed+"가 가입한 이메일이 맞습니까?")){
				sendNumber(json); 
			 }			 
        },
        error: function() {
			alert("이메일을 확인하세요");
         },
    });      
}


// 이메일 폼 만들기, 페이지 로드 시 초기화
document.addEventListener("DOMContentLoaded", function() {
    updateInputValue();
});

function updateInputValue() {
    var selectBox = document.getElementById("emailForm");
    var inputField = document.getElementById("customEmail");

    // 선택된 옵션의 값을 가져옴
    var selectedOption = selectBox.value;

    // 선택된 옵션이 "직접입력"이 아닌 경우에는 인풋에 해당 옵션의 값을 나타냄
    if (selectedOption !== "") {
        inputField.value = selectedOption;
        check(selectBox);
    } else {
        // 선택된 옵션이 "직접입력"인 경우에는 인풋을 비움
        inputField.value = "";
        
    }

    // 인풋을 표시 또는 숨김
    inputField.style.display = "inline-block";
}