var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');


function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}


$(document).ready(function () {
    total()
})

function test() {
    var point = $('#discount').text();
    if (point == "") {
        point = 0
    }

    var delivery = parseInt($('#delivery').text());
    var totalAmount = parseInt($('#totalAmount').text());


    var products = [];
    var productElements = $('.products');

    productElements.each(function () {
        var productValue = parseInt($(this).val()); // 엘리먼트에서 숫자를 가져와서 정수로 변환
        products.push(productValue);
    });
    console.log(products)

    var payment = {
        products: products,
        orderNum: "gkdlgkdkgdkgkdg",
        price: totalAmount,
        point: parseInt(point),
        delivery: delivery
    }
    console.log(payment)

    $.ajax({
        type: 'POST',
        data: JSON.stringify(payment),
        contentType: 'application/json',
        url: "/order/payment",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            alert(json)
            var idx = json
            window.location.href = "/order/orderdetail/" + idx;
        },
        error: function () {
            console.log("error");
        },
    });
}

function total() {
    var itemPrices = $(".itemPrices")

    var totalAmount = 0;

    itemPrices.each(function () {
        totalAmount += parseInt($(this).text());
    });
    $('#totalAmount').html(totalAmount);

    delivery(totalAmount)
}

function delivery(totalAmount) {
    if (totalAmount >= 50000) {
        $('#delivery').text(0);
    } else {
        $('#delivery').text(3000);
    }
    finalAmount()
}

function finalAmount() {
    let totalAmount = $('#totalAmount').text();
    let delivery = $('#delivery').text();
    let discount = $('#discount').text();

    if (discount == "") {
        discount = 0
    }

    let finalAmount = parseInt(totalAmount) + parseInt(delivery) - parseInt(discount)

    $('#finalPrice').text(finalAmount)
}

function usePoint() {
    var usePoint = parseInt($('#usePointInput').val());
    var myPoint = parseInt($('#myPoint').text());
    var totalAmount = parseInt($('#totalAmount').text());
    if (usePoint > myPoint) {
        alert("포인트가 부족합니다.")
        $('#usePointInput').val(0)
    } else if (usePoint > totalAmount) {
        alert("결제금액을 초과했습니다.")
        $('#usePointInput').val(0)
    } else {
        $('#discount').text(usePoint)
        finalAmount()
    }
}
