fetch('/hash/hashtags')
    .then(response => response.json())
    .then(hashtags => {
        var tags = document.getElementById('tags');
        window.tagify = new Tagify(tags, {
            whitelist: hashtags,
            dropdown: {
                enabled: 0 // 드롭다운을 항상 활성화
            }
        });
    });
