var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

var goodsIdx;

function ischeckBuy(element){
    goodsIdx = $(element).attr('name');
    var so = "selectOption" + goodsIdx;
    var optionLi = document.getElementsByClassName(so)[0].getElementsByTagName("li").length;
    if (optionLi == 0) {
        alert("선택안했티비")
        return;
    }
    var colors = document.getElementsByClassName("colors")
    var sizes = document.getElementsByClassName("sizes")
    var stocks = document.getElementsByClassName("stocks")
    var options = []
    for (var i = 0; i < optionLi; i++) {
        var option = {
            goodsIdx : goodsIdx,
            color: colors[i].value,
            size: sizes[i].value,
            cnt: stocks[i].value
        }
        options.push(option)
    }
    $.ajax({
        type: 'POST',
        data: JSON.stringify(options),
        contentType: 'application/json',
        url: "/order/checkCart",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            if(json==false){
                var result = confirm("장바구니에 동일한 상품이 존재합니다. \n 함께 주문하시겠습니까?");
                goodsBuy(result,options)
            } else{
                result = true
                goodsBuy(result,options)
            }
        },
        error: function () {
            console.log("error");
        },
    });
}
function goodsBuy(result,options){
    alert(result)
    console.log(options)

    $.ajax({
        type: 'POST',
        data: JSON.stringify(options),
        headers : {'result': result},
        contentType: 'application/json',
        url: "/order/cartlist",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            // 서버 응답에서 null 값을 제거
            var filteredJson = json.filter(function (item) {
                return item !== null;
            });

            console.log(filteredJson);

            // 각 값에 대해 숨겨진 input 요소 생성
            filteredJson.forEach(function (item, index) {
                var input = document.createElement("input");
                input.setAttribute("type", "hidden");
                input.setAttribute("name", "orderItem");
                input.setAttribute("value", item);

                document.getElementById("orderItem").appendChild(input);
            });


            // 수정된 form으로 submit
            document.getElementById("hiddenForm").submit();
        },
        error: function () {
            console.log("error");
        },
    });
}


//========================장바구니=============================
// 장바구니 체크
function ischeckCart(element){
    goodsIdx = $(element).attr('name');
    var so = "selectOption" + goodsIdx;
    var optionLi = document.getElementsByClassName(so)[0].getElementsByTagName("li").length;
    if (optionLi == 0) {
        alert("선택안했티비")
        return;
    }
    var colors = document.getElementsByClassName("colors")
    var sizes = document.getElementsByClassName("sizes")
    var stocks = document.getElementsByClassName("stocks")
    var options = []
    for (var i = 0; i < optionLi; i++) {
        var option = {
            goodsIdx : goodsIdx,
            color: colors[i].value,
            size: sizes[i].value,
            cnt: stocks[i].value
        }
        options.push(option)
    }
    $.ajax({
        type: 'POST',
        data: JSON.stringify(options),
        contentType: 'application/json',
        url: "/order/checkCart",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            if(json==false){
                var result = confirm("장바구니에 동일한 상품이 존재합니다. \n 상품을 담으시겠습니까?");
                if (result){
                    goodsCart(options)
                } else{
                    alert("안담았티비")
                    return;
                }
            }else{
                goodsCart(options);
            }
        },
        error: function () {
            console.log("error");
        },
    });
}
// 장바구니 담기
function goodsCart(options) {
    $.ajax({
        type: 'POST',
        data: JSON.stringify(options),
        contentType: 'application/json',
        url: "/order/cartlist",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            alert("담았티비")
        },
        error: function () {
            console.log("error");
        },
    });
}