let page = 0;
const size = 3;
let urlParams = new URLSearchParams(window.location.search);
// let hashtag = urlParams.get('hashtag');
let hashtag = window.location.pathname.split('/').pop();
let swiper;

$(document).ready(function () {
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

    loadMore();
    page++;
});

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
        loadMore();
        page++;
    }
});

function loadMore() {
    $.ajax({
        url: `/board/hashtag_ajax/${hashtag}?page=${page}&size=${size}`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            $.each(data.content, function (i, board) {
                // 게시물 HTML 생성
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
                    <li>${new Date(board.modifiedDate).toISOString().slice(0, 19).replace('T', ' ')}</li>`;
                if (board.hashtags && board.hashtags.length > 0) {
                    let hashtags = board.hashtags.map(hashtag => `<a href="/board/hashtag/${encodeURIComponent(hashtag)}">${hashtag}</a>`);
                    boardHtml += `<li>${hashtags.join(' ')}</li>`;
                }
                $('#boardList').append(boardHtml);
            });
            initSwiper();
        },
        error: function (xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
}
function initSwiper() {
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

initSwiper();