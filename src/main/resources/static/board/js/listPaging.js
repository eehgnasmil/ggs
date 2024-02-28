    let page = 0;
    const size = 8;
    let urlParams = new URLSearchParams(window.location.search);
    let category = urlParams.get('category') || 'all';
    let bsort = urlParams.get('bsort') || 'new';
	var swiper;    
$(document).ready(function () {
    // 이 코드는 문서가 로드되고 나서 실행되므로 Swiper 초기화는 여기에서 이루어져야 합니다.
    var swiper = new Swiper(".mySwiper2", {
        pagination: {
            el: ".swiper-pagination",
            type: "progressbar",
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });

    $('#sortList a').each(function () {
        let href = $(this).attr('href');
        href = href.replace('category=all', 'category=' + category);
        $(this).attr('href', href);
    });

    loadMore();
});
    $('#loadMoreButton').on('click', loadMore);
    function loadMore() {		
        $.ajax({
            url: `/board/board_list_ajax?category=${category}&bsort=${bsort}&page=${page}&size=${size}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                $.each(data.content, function (i, board) {
                console.log(board); // 데이터 확인
                    let boardHtml = `
                    <li style="width: 25%; padding:0 0 30px 0; display:inline-block; text-align: center; ">
                    <ul>
                    	<div  style="position:relative; height: 0; padding-bottom:120%; overflow:hidden;" class="mySwiper2 swiper">
	                    	<div class="swiper-wrapper">
		                        ${board.imageUrls.map(
									imageUrl =>
									`<li class="swiper-slide" style="padding-bottom:130%;"><img src="${imageUrl}" alt="Board image"></li>`
									).join('')}
							</div>
								<div class="i-hover">
		                            <div class="swiper-button-next"></div>
		                            <div class="swiper-button-prev"></div>
		                            <div class="swiper-pagination"></div>
		                        </div>								
						</div>	
                        <li><a href="/board/board_detail/${board.idx}">${board.idx}</a></li>
                        <li>${board.title}</li>
                        <li>${board.category}</li>
                        <li>${board.nickname}</li>
                        <li>${board.viewcount}</li>
                        <li>${board.likesCount}</li>
                        <li>${new Date(board.modifiedDate).toISOString().slice(0, 19).replace('T', ' ')}</li>`                       
                        if (board.hashtags && board.hashtags.length > 0) { // 해시태그가 있을 경우에만 추가합니다.
                            boardHtml += `<li>${board.hashtags.join(', ')}</li>`;
                    	}
                    boardHtml += `</ul></li>`;

                    $('#boardList').append(boardHtml);
                });


	            // 새로운 데이터가 추가될 때마다 Swiper를 다시 초기화합니다.
	            initSwiper();
                page++;
            },
            error: function (xhr, status, error) {
                console.error(xhr.responseText); // 오류 메시지 출력
            }
        });
    }
// Swiper 초기화 함수
function initSwiper() {
    // 이미 Swiper가 초기화되었는지 확인 후 초기화
    if (swiper) {
        swiper.destroy();
    }

    swiper = new Swiper(".mySwiper2", {
        pagination: {
            el: ".swiper-pagination",
            type: "progressbar",
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });
}

// 페이지 로드 시 초기 Swiper 초기화
initSwiper();
