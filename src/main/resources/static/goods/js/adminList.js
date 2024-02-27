function validateForm(){
    var selectedItems  = document.querySelectorAll('input[name="goods"]:checked');
    var goodsItems = Array.from(selectedItems).map(item => item.value);

    if(goodsItems.length == 0){
        alert("상품을 선택해주십시오")
        return false;
    } else {
        $.ajax({
            type: 'POST',
            data: JSON.stringify(goodsItems),
            contentType: 'application/json',
            url: "/admin/deleteGoods",
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
function allCheck(allCheckId){
    var checkItem = $(".checkItem")
    for(var i=0; i<checkItem.length;i++){
        checkItem[i].checked = allCheckId.checked;
    }
}

function updateAllCheck(){
    var allCheckId = document.getElementById("allCheckId")
    var checkItem = $(".checkItem")
    for(var i=0; i<checkItem.length;i++){
        if(!checkItem[i].checked){
            allCheckId.checked = false;
            return
        }
        allCheckId.checked = true;
    }
}