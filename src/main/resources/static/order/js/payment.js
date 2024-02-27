// 구매자 정보
const user_email = $('#memberEmail').text();
const username = $('#memberName').text();

// 결제창 함수 넣어주기
const buyButton = document.getElementById('payment')


buyButton.setAttribute('onclick', `kakaoPay('${user_email}', '${username}')`)

var IMP = window.IMP;

var today = new Date();
var hours = today.getHours(); // 시
var minutes = today.getMinutes();  // 분
var seconds = today.getSeconds();  // 초
var milliseconds = today.getMilliseconds();
var makeMerchantUid = `${hours}` + `${minutes}` + `${seconds}` + `${milliseconds}`;

function kakaoPay(useremail, username) {

    const finalPrice = $('#finalPrice').text();
    const phone = $('#memberphone').text();
    const postcode = $('#memberPostcode').text();
    const addr = $('#memberPostaddress').text() + $('#memberdetailaddress').text();
    const productName = $('#product_0').text();


    if (confirm("구매 하시겠습니까?")) { // 구매 클릭시 한번 더 확인하기
        var uid = ''
        IMP.init("imp73402603"); // 가맹점 식별코드
        IMP.request_pay({
            pg: 'kakaopay.TC0ONETIME', // PG사 코드표에서 선택
            pay_method: 'card', // 결제 방식
            merchant_uid: "IMP" + makeMerchantUid, // 결제 고유 번호
            name: productName, // 제품명
            amount: finalPrice, // 가격
            //구매자 정보 ↓
            buyer_email: `${useremail}`,
            buyer_name: `${username}`,
            buyer_tel: '010-1234-5678',
            buyer_addr: addr,
            buyer_postcode: postcode
        }, async function (rsp) { // callback
            if (rsp.success) { //결제 성공시
                console.log(rsp);
                $.ajax({
                    type: 'POST',
                    data: {
                        imp_uid: rsp.imp_uid
                        //기타 필요한 데이터가 있으면 추가 전달
                    },
                    contentType: 'application/json',
                    url: "/verifyIamport/" + rsp.imp_uid,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(header, token);
                    },
                    success: function (json) {
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
                            orderNum: rsp.merchant_uid,
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

                    },
                    error: function () {
                        console.log("error");
                    },
                });
            } else if (rsp.success == false) { // 결제 실패시
                alert(rsp.error_msg)
            }
        });
    } else { // 구매 확인 알림창 취소 클릭시 돌아가기
        return false;
    }
}
