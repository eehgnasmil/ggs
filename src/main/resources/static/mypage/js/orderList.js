function orderCancle(orderIdx) {
    if (confirm("취소하시겠습니까?")) {
        $.ajax({
            type: 'POST',
            data: {
                orderIdx: orderIdx
            },
            url: "/order/orderCancle",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
                location.reload();
            },
            error: function () {
                alert("잠시 후 다시 시도하세요.")
            },
        });
    } else {return}
}

function orderCheck(orderIdx) {
    if(confirm("구매 확정하시겠습니까?")) {
        $.ajax({
            type: 'POST',
            data: {
                orderIdx: orderIdx
            },
            url: "/order/orderCheck",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (json) {
                location.reload();
            },
            error: function () {
                alert("잠시 후 다시 시도하세요.")
            },
        });
    }
}

function review(orderIdx) {
    $.ajax({
        type: 'POST',
        data: {
            orderIdx: orderIdx
        },
        url: "/order/orderCancle",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (json) {
            alert(json)
        },
        error: function () {
            alert("잠시 후 다시 시도하세요.")
        },
    });
}