$(document).ready(function () {
    calculateOrderAmount();
})
$(".checkItem").on("change", function () {
    calculateOrderAmount();
});

function selectRemove() {
    var selectedItems = document.querySelectorAll('input[name="orderItem"]:checked');
    var cartItems = Array.from(selectedItems).map(item => item.value);

    if (cartItems.length == 0) {
        alert("상품을 선택해주십시오")
        return false;
    } else {
        $.ajax({
            type: 'POST',
            data: JSON.stringify(cartItems),
            contentType: 'application/json',
            url: "/order/deleteCarts",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
                alert(json)
                history.go(0);
            },
            error: function () {
                console.log("error");
            },
        });
    }

}

function calculateOrderAmount() {
    // 체크된 상품의 가격 합산
    var totalAmount = 0;
    var checkItem = $(".checkItem")
    var itemPrice = $(".itemPrices")
    for (var i = 0; i < checkItem.length; i++) {
        if (checkItem[i].checked) {
            totalAmount += parseInt(itemPrice[i].innerHTML);
        }
        var delivery = 3000
        if (totalAmount >= 50000) {
            delivery = 0
        }
        var finalPrice = parseInt(totalAmount) + parseInt(delivery);

        // 결과를 화면에 업데이트
        $("#totalAmount").text(totalAmount);
        $("#delivery").text(delivery);
        $("#finalPrice").text(finalPrice);
    }
}

function validateForm() {
    var selectedItems = document.querySelectorAll('input[name="orderItem"]:checked');
    var orderItemsValue = Array.from(selectedItems).map(item => item.value);

    if (orderItemsValue.length == 0) {
        alert("상품을 선택해주십시오")
        return false;
    }
}

function deleteCart(idx) {
    $.ajax({
        type: 'POST',
        data: {
            "cartItem": idx
        },
        url: "/order/deleteCart",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            alert(json)
            history.go(0);
        },
        error: function () {
            console.log("error");
        },
    });
}

function changCnt(idx, input) {
    var cnt = input.value

    if (cnt < 1) {
        alert("최소 한개 이상 선택해주세요")
        input.value = 1
        return
    }
    $.ajax({
        type: 'POST',
        data: {
            "ciIdx": idx,
            "cnt": cnt
        },
        url: "/order/changeCnt",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            document.getElementById()
        },
        error: function () {
            console.log("error");
        },
    });
}



function allCheck(allCheckId) {
    var checkItem = $(".checkItem")
    for (var i = 0; i < checkItem.length; i++) {
        checkItem[i].checked = allCheckId.checked;
    }
}

function updateAllCheck() {
    var allCheckId = document.getElementById("allCheckId")
    var checkItem = $(".checkItem")
    for (var i = 0; i < checkItem.length; i++) {
        if (!checkItem[i].checked) {
            allCheckId.checked = false;
            return
        }
        allCheckId.checked = true;
    }
}