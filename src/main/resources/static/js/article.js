// 삭제 기능, HTML에서 id를 delete-btn으로 설정한 엘리먼트를 찾음
const deleteButton = document.getElementById('delete-btn');

// 수정 기능, id가 modify-btn인 엘리먼트 조회
const modifyButton = document.getElementById('modify-btn');

if (deleteButton) {
    // 클릭 이벤트가 발생시 fetch() 메서드를 통해 /api/articles/DELETE 요청을 보내는 역할
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`,{
            method: 'DELETE'
        })
            // fetch()가 잘 완료되면 연이어 실행되는 메서드
        .then(() => {
            alert('삭제가 완료되었습니다.');
            // 실행 시 사용자의 웹 브라우저 화면을 현재 주소를 기반해 옮겨주는 역할을 함
            location.replace('/articles');
        });
    });
}

if (modifyButton) {
    // 클릭 이벤트가 감지되면 수정 API 요청
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            });
    });
}