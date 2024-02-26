$(function(){

});


//닉네임 변경
function saveNick(element){
    var idx = $('.idForm').attr('name');
    var nick = $('#nick').val();
    var nnick = $('#nnick').attr("class");
    
    if(nnick == "x"){
		alert("이미 사용중인 닉네임입니다.");
	}else if(nick ==""){
		alert("닉네임을 작성하세요");		
	}else{
		    $.ajax({
				type: 'POST',
		        data : {
					"idx" : idx,
		            "nick" : nick
		        },
		        url : "/mypage/modifyNick",
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

};

//prevPwCheck 기존 비밀번호 체크 후 true면 save, false면 alert
function prevPwCheck() {
	var id = $('#password2').attr('name');
    var prevPw = $('#password0').val();
    var pw2 = $('#password1').val();
    var pw3 = $('#password2').val();
    //기존 비밀번호 빈칸 처리
    if (prevPw == "") {
        alert("기존 비밀번호를 입력해주세요.");
        
    //비밀번호 빈칸 or  2,3이 일치하지 않을 떄,
    }else if(pw2 =="" || pw3 =="" || pw2 !== pw3){
			
    //기존비밀번호와 일치, 비밀번호 2,3이 일치 할 때 저장.
	}else{
		$.ajax({
            type: 'POST',
            data: {
                "id": id,
                "prevPw": prevPw,
            },
            url: "/mypage/prevPwCheck",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
		        // 서버에서 반환한 결과값이 true인 경우에 실행
		        if (json) {
		            savePw();
		        }else{
					alert("기존 비밀번호를 다시 확인하세요")
				}
            },
            error: function () {
                console.log("error");
            },
        });
    }
}


function savePw(){
	var idx = $('.idForm').attr('name');
    var pw = $('#password2').val();
		    $.ajax({
				type: 'POST',
		        data : {
					"idx" : idx,
		            "pw" : pw
		        },
		        url : "/mypage/modifyPw",
		        beforeSend: function(xhr){
		  			  xhr.setRequestHeader(header, token);
				 },
		        success : function(json) {
					window.location.replace("/mypage/myList");
		        },
		        error: function() {
		        console.log("error");
		      	},
		    });	

};