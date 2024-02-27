var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

function toggleTextbox(checkbox, textboxId) {
    var textbox = document.getElementById(textboxId);
    if (checkbox.checked) {
        textbox.style.display = 'block'; // 텍스트 필드를 보여줍니다.
        textbox.disabled = false; // 텍스트 필드를 활성화합니다.
        textbox.focus(); // 텍스트 필드에 포커스를 줍니다.
    } else {
        textbox.style.display = 'none'; // 텍스트 필드를 숨깁니다.
        textbox.disabled = true; // 텍스트 필드를 비활성화합니다.
        textbox.value = ''; // 텍스트 필드의 내용을 초기화합니다.
    }
}

function updatesave() {
    var idx = $("#idx").val(); // 게시물 ID
    var title = $("#title").val();
    var detail = window.editor.getHTML();
    var category = $("#category").val();
    var outerwear = $('#chkOuterwear').is(':checked') ? $("#outerwearInput").val() : null;
    var top = $('#chkTop').is(':checked') ? $("#topInput").val() : null;
    var bottom = $('#chkBottom').is(':checked') ? $("#bottomInput").val() : null;
    var shoes = $('#chkShoes').is(':checked') ? $("#shoesInput").val() : null;
    var acc = $('#chkAcc').is(':checked') ? $("#accInput").val() : null;

    var hashtags = window.tagify.value.map(function(tag) {
        var trimmedValue = tag.value.replace(/\s+/g, ''); // 문자열 내의 모든 공백을 제거
        return trimmedValue.startsWith('#') ? trimmedValue : '#' + trimmedValue.toLowerCase(); // #이 없다면 앞에 추가 있으면 그냥 넘김 공백제거해줌
    });


    // 콘텐츠 입력 유효성 검사
    if (window.editor.getMarkdown().length < 1) {
        alert('에디터 내용을 입력해 주세요.');
        throw new Error('editor content is required!');
    }

    if (title.length < 1) {
        alert('제목을 입력해 주세요.');
        throw new Error('editor content is required!');
    }

    if (category == null || category == '') {
        alert('카테고리를 선택해 주세요.');
        throw new Error('editor content is required!');
    }

    // API 호출을 위한 파라미터 세팅
    var url = '/board/board_update/' + idx;
    var params = {
        idx: idx,
        title: title,
        detail: detail,
        category: category,
        outerwear: outerwear,
        top: top,
        bottom: bottom,
        shoes: shoes,
        acc: acc,
        hashtags:hashtags
    }

    // AJAX를 사용한 API 호출
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(params),
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            alert('게시글이 수정되었습니다.');
            location.href = '/board/board_detail/' + idx; // 수정한 게시물의 detail 페이지로 이동
        },
        error: function (error) {
            console.error('수정 실패 : ', error);
        },
    });
}
