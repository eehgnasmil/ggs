// join.js
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$(function(){

});

//패스워드 수정폼 토글
function modifyPw(){
    $(".modifyPwForm").toggle();
}

//닉네임 수정폼 토글
function modifyNick(){
    $(".modifyNickForm").toggle();
}

//닉네임 수정폼 토글
function modifyAddress(){
    $(".postAddressM").toggle();
}

function deleteImg(idx){
    $.ajax({
        type: 'POST',
        data: {
            "fileIdx": idx
        },
        url: "/mypage/deleteImg",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            alert(json)
            location.reload();


        },
        error: function () {
            console.log("error");
        },
    });
}
function triggerFileInput() {
    document.getElementById('fileInput').click();
}

// 파일이 선택되면 실행되는 함수
function handleFileUpload() {
    const fileInput = document.getElementById('fileInput');
    const uploadImage = document.getElementById('uploadImage');
    const fileIdx = document.getElementById("fileIdx").value;

    // 선택된 파일이 존재하면 이미지를 업데이트
    if (fileInput.files && fileInput.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
            uploadImage.src = e.target.result;
        };


        reader.readAsDataURL(fileInput.files[0]);
        const file = fileInput.files[0];
        const formData = new FormData();
        formData.append('file', file);
        formData.append('idx', fileIdx);

        $.ajax({
            type: 'POST',
            data: formData,
            url: '/mypage/changeImg',
            contentType: false, // 필수: FormData 사용시 false로 설정
            processData: false, // 필수: FormData 사용시 false로 설정
            url: "/mypage/changeImg",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
                alert(json)
                location.reload();
            },
            error: function () {
                console.log("error");
            },
        });

    }

}



//onblur !null&!duplication일 때 다른 인풋 선택 가능
function NullCheck(element) {
   var checks = $(element).attr('id');

    var inputValue = document.getElementById(checks).value;

   if(inputValue ==''){         
      $(element).nextAll('div:first').next('div').css("display", "block");
   }else{
      if(checks=='password2'||checks=='password1'){
         if($('#password2').val() !== $('#password1').val()){
            $(element).nextAll('div:first').addClass( 'x' );			 
            $(element).nextAll('div:first').css("display", "block");            
         }else{
            $(element).nextAll('div:first').removeClass( 'x' );     			 
            $(element).nextAll('div:first').css("display", "none");   
         }   
      }         
      $(element).nextAll('div:first').next('div').css("display", "none");
   }
}

//실시간 중복체크   
function check(element){
   var inputed = $(element).val();
   var checks = $(element).attr('id');
   if(checks=="emailForm"|| checks=="customEmail" ||checks=="email"){
      inputed =  $('#email').val() + "@" + $('#customEmail').val();
   }   
    $.ajax({
      type: 'POST',
        data : {
            "checks" : checks,
            "value" : inputed
        },
        url : "/member/checking",
        beforeSend: function(xhr){
             xhr.setRequestHeader(header, token);
       },
        success : function(json) {
         if(json=="1"){
		        
            $(element).nextAll('div:first').addClass( 'x' );
            $(element).nextAll('div:first').css("display", "block");

         }else{
		 	
            $(element).nextAll('div:first').removeClass( 'x' );                  
            $(element).nextAll('div:first').css("display", "none");
         }   
        },
        error: function() {
        console.log("error");
         },
    });      
}
function join() {
    var nullId = $('#id').val();
    var nullPw1 = $('#password1').val();
    var nullPw2 = $('#password2').val();
    var nullNick = $('#nick').val();
    var nullName = $('#name').val();
    var nullPhone = document.getElementById("phone1").value + document.getElementById("phone2").value + document.getElementById("phone3").value;
    var nullEmail = $('#email').val();
    var emailForm = $('#customEmail').val();
    var Email = $('#email').val() + "@" + emailForm;

    if (nullId !== "" && nullPw1 !== "" && nullPw2 !== "" && nullNick !== "" && nullName !== "" && nullEmail !== "" && emailForm !== "") {
        var duplicatePw = $('#npw').attr("class");
        var duplicateId = $('#nid').attr("class");
        var duplicateNick = $('#nnick').attr("class");
        var duplicateEmail = $('#nemail').attr("class");
        var emailResult = $('#Confirm').val();

        if (duplicatePw == "x" || duplicateId == "x" || duplicateNick == "x" || duplicateEmail == "x" || emailResult == "0") {
            alert("유효하지 않은 값이 있습니다.");
        } else if (emailResult == "") {
            alert("이메일 인증이 완료되지 않았습니다.");
        } else {
            // FormData 객체 생성
            var formData = new FormData();
			// 파일과 상태 정보를 FormData에 추가
			for (var i = 0; i < fileIdx; i++) {
			    var status = document.getElementById("fileDtos" + i + ".status").value;
			    var file = document.getElementById("fileDtos" + i + ".file").files[0];
			
			    // 파일이 선택된 경우에만 추가
			    if (file) {
			        formData.append('fileDtos[' + i + '].status', status);
			        formData.append('fileDtos[' + i + '].file', file);
			    }
			}

            formData.append("id", nullId);
            formData.append("pw", nullPw2);
            formData.append("name", nullName);
            formData.append("phone", nullPhone);
            formData.append("nick", nullNick);
            formData.append("email", Email);

            // AJAX를 사용하여 FormData를 서버로 전송
            $.ajax({
                type: 'POST',
                data: formData,
                processData: false,  // 필수: FormData 사용시 false로 설정
                contentType: false,  // 필수: FormData 사용시 false로 설정
                url: "/member/join",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function (json) {
                    window.location.replace("/member/login");
                },
                error: function () {
                    console.log("error");
                },
            });
        }
    } else {
        alert("빈값있음");
    }
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


//이메일 인증
let emailresult = 0
function sendNumber(){
var emailForm = $('#customEmail').val()
var Email = $('#email').val() + "@" + emailForm;
   if($('#nemail').attr('class')=="x"||$('#email').val()==""){
      alert("이메일을 다시 확인해주세요");
   }else{      
       $("#mail_number").css("display","block");
       $.ajax({
           url:"/mail",
           type:"post",
           dataType:"json",
           data:{"mail" : Email},
           success: function(data){
               alert("인증번호 발송");
			   console.log(data)
            emailresult = data;
           },
           error:function(){
            alert("이메일 양식이 아닙니다. 확인하세요");
         }
           }
          
       )};
}

function confirmNumber(){
    var number1 = $("#number").val();
   var rs =0;
    if(number1 == emailresult && number1!==""){
      rs = "1";
      $("#Confirm").attr("value",rs);
      alert("인증성공");
    }else{
      rs = "0";
      $("#Confirm").attr("value",rs);
      alert("인증실패");      
    }
}

// resign.js
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

$(function(){

});

function saveAddress(){
	var postcode = $('#sample6_postcode').val();
	var postaddress = $('#sample6_address').val();
	var detailaddress = $('#sample6_detailAddress').val();
	var idx = $('#memberName').attr('name');
	var data = {
		"idx":idx,
		"postcode" : postcode,
		"postaddress" : postaddress,
		"detailaddress" : detailaddress
	};
	console.log(data)
	if(addressNullCheck()){
		 	$.ajax({
				type: 'POST',
		        data : data,
		        url : "/mypage/saveAddress",
		        beforeSend: function(xhr){
		  			  xhr.setRequestHeader(header, token);
				 },
		        success : function(json) {
					location.reload();					
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

