var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');
// 색상 선택
function toggleColor(clickbtn) {
    goodsIdx = $(clickbtn).attr('name');
    clickbtn.classList.toggle("selected");

    // sizeBtn 초기화
    var sizeBtn = document.getElementsByClassName("sizeBtn");
    for (var i = 0; i < sizeBtn.length; i++) {
        if (sizeBtn[i] !== clickbtn) {
            sizeBtn[i].classList.remove("selected");
        }
    }

    // 나머지 버튼에서 selected 클래스 제거
    var colorBtn = document.getElementsByClassName("colorBtn");
    for (var i = 0; i < colorBtn.length; i++) {
        if (colorBtn[i] !== clickbtn) {
            colorBtn[i].classList.remove("selected");
        }
    }

    var color = clickbtn.value

    // selected 클래스가 없으면 sizebtn disabled
    // 아니면 size 불러오기
    if (!clickbtn.classList.contains("selected")) {
        for (var i = 0; i < sizeBtn.length; i++) {
            sizeBtn[i].classList.add("dis");
            sizeBtn[i].disabled = true;
        }
    } else {
        $.ajax({
            type: 'POST',
            data: {
                goodsIdx: goodsIdx,
                color: color
            },
            url: "/goods/selectSizes",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
                // 각 버튼에 대해 jsonArr에 속하는지 여부를 확인하고 처리
                for (var i = 0; i < sizeBtn.length; i++) {
                    var isValueInJson = json.includes(sizeBtn[i].value);
                    if(isValueInJson){
                        sizeBtn[i].disabled = !isValueInJson;
                        sizeBtn[i].classList.remove("dis");
                    } else{
                        sizeBtn[i].classList.add("dis");
                        sizeBtn[i].disabled = true;
                    }


                }
            },
            error: function () {
                console.log("error");
            },
        });
    }
}


function toggleSize(clickbtn) {
    goodsIdx = $(clickbtn).attr('name');
    var so = "selectOption" + goodsIdx;
    var selectIdx = document.getElementsByClassName(so)[0].getElementsByTagName("li").length;
    var liIdx = selectIdx++

    clickbtn.classList.toggle("selected");

    // 나머지 버튼에서 'selected' 클래스 제거
    var sizeBtn = document.getElementsByClassName("sizeBtn");
    for (var i = 0; i < sizeBtn.length; i++) {
        if (sizeBtn[i] !== clickbtn) {
            sizeBtn[i].classList.remove("selected");
        }
    }

    var selectedColorBtn = document.querySelector('.colorBtn.selected');
    var color = selectedColorBtn.value
    var size = clickbtn.value


    // 담겨있는 요소 비교
    var colors = document.getElementsByClassName("colors")
    var sizes = document.getElementsByClassName("sizes")
    var liLength = document.getElementsByClassName(so)[0].getElementsByTagName("li").length;

    for (var i = 0; i < liLength; i++) {
        var colorVal = colors[i].value;
        var sizeVal = sizes[i].value;
        if (colorVal + sizeVal == color + size) {
            alert("이미담겼티비")
            return;
        }
    }

    // 안 담겼으면 장바구니 리스트 추가
    var selectOption_div = document.getElementsByClassName(so)[0];
    var selectOption_li = document.createElement("li")
    selectOption_li.id = "selectOptionLi_" + liIdx;

    var selectGoodsname_p = document.createElement("p");
    selectGoodsname_p.innerText = document.getElementById("goodsName").innerText;

    var color_input = document.createElement("input");
    color_input.type = "text"
    color_input.disabled = "true"
    color_input.id = "color" + liIdx;
    color_input.className = "colors"
    color_input.value = color

    var Slash_span = document.createElement("span");
    Slash_span.innerText = " / "

    var size_input = document.createElement("input");
    size_input.type = "text"
    size_input.disabled = "true"
    size_input.id = "size" + liIdx;
    size_input.className = "sizes"
    size_input.value = size

    var selectCnt_input = document.createElement("input")
    selectCnt_input.id = "stock" + liIdx;
    selectCnt_input.className = "stocks"
    selectCnt_input.type = "number";
    selectCnt_input.value = 1;
    selectCnt_input.onchange = function (){
        changeCnt(liIdx)
    }

    var remove_btn = document.createElement("button")
    remove_btn.className = "x"
    remove_btn.type = "button"
    remove_btn.innerHTML = "X"
    remove_btn.onclick = function () {
        removeSelect(liIdx);
    };

    selectOption_li.appendChild(selectGoodsname_p);
    selectOption_li.appendChild(color_input);
    selectOption_li.appendChild(Slash_span);
    selectOption_li.appendChild(size_input);
    selectOption_li.appendChild(selectCnt_input);
    selectOption_li.appendChild(remove_btn);
    selectOption_div.appendChild(selectOption_li);

    //옵션박스 토글
    var optionBox = "selectOption" + goodsIdx; // optionBox 변수를 선언하고 값을 할당합니다.
    var currentDisplayStyle = $('.' + optionBox).css('display'); // 현재 display 속성 값을 가져옵니다.
    $('.' + optionBox).css('display', 'block'); // display 속성을 토글합니다.
    $('.' + optionBox+ ' li').css('padding', '10px 10px');
    var currentImgSrc = $('.optionOpen img').attr('src');
    var newImgSrc = currentDisplayStyle === 'block' ? '/img/up.png' : '/img/down.png';
    $('.optionOpen img').attr('src', newImgSrc);
}

function removeSelect(selectId) {
    var selectOption_li = document.getElementById("selectOptionLi_" + selectId);

    selectOption_li.remove();
}

//옵션박스 토글
function openOption(optionBox) {
    var goodsIdx = $(optionBox).attr('name');
    var optionBoxClass = "selectOption" + goodsIdx;
    var currentDisplayStyle = $('.' + optionBoxClass).css('display');


    // display 속성 토글
    $('.' + optionBoxClass).css('display', currentDisplayStyle === 'block' ? 'none' : 'block');

    // padding 속성 토글
    $('.' + optionBoxClass + ' li').css('padding', currentDisplayStyle === 'block' ? '0' : '10px 10px');

    // 이미지 토글
    var currentImgSrc = $('.optionOpen img').attr('src');
    var newImgSrc = currentDisplayStyle === 'block' ? '/img/up.png' : '/img/down.png';
    $('.optionOpen img').attr('src', newImgSrc);
}

// 수량
function changeCnt(liIdx){
    var stock = $('#stock'+liIdx);

    if(stock.val()<1){
        alert("최소 한개 이상 선택해주세요")
        stock.val(1)
        return;
    }
}