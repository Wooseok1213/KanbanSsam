// 보드 추가 팝업 열기
function openAddBoardPopup() {
    $('#board-add-container').addClass('active');
}

// 보드 추가 팝업 닫기
function closeAddBoardPopup() {
    $('#name').val(null);
    $('#introduction').val(null);
    $('#board-add-container').removeClass('active');
}

// 보드 추가 API 요청
function addBoard() {
    const name = $('#name').val();
    const introduction = $('#introduction').val();

    const data = {
        'name': name,
        'introduction': introduction
    };

    console.log(data)

    const accessToken = getAccessToken();

    $.ajax({
        type: 'POST',
        url: `/boards`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: JSON.stringify(data),
        success: function (response) {
            alert('보드 추가 성공.');
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 보드 수정 팝업 열기
function openUpdateBoardPopup(boardId, name, introduction) {
    $('#board-update-container').addClass('active');
    $('#update-boardId').val(boardId);
    $('#update-name').val(name);
    $('#update-introduction').val(introduction);
}

// 보드 수정 팝업 닫기
function closeUpdateBoardPopup() {
    $('#update-boardId').val(null);
    $('#update-name').val(null);
    $('#update-introduction').val(null);
    $('#board-update-container').removeClass('active');
}

// 보드 수정 API 요청
function updateBoard() {
    const boardId = $('#update-boardId').val();
    const name = $('#update-name').val();
    const introduction = $('#update-introduction').val();

    const data = {
        'name': name,
        'introduction': introduction
    };

    const accessToken = getAccessToken();

    $.ajax({
        type: 'PUT',
        url: `/boards/${boardId}`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: JSON.stringify(data),
        success: function (response) {
            alert('보드 수정 성공.');
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 보드 삭제 alert
function deleteBoard(boardId) {
    const accessToken = getAccessToken();
    if (confirm("삭제하는 경우 작성한 모든 데이터가 삭제됩니다.\n정말 삭제하시겠습니까?") === true) {
        // $.ajax({
        //     type: 'DELETE',
        //     url: `/boards/${boardId}`,
        //     beforeSend : function(xhr){
        //         xhr.setRequestHeader("Authorization", accessToken);
        //     },
        //     success: function (response) {
        //         alert('보드 삭제 성공.');
        //         window.location.reload();
        //     },error: err => {
        //         alert(err.responseJSON.message);
        //     }
        // })
    }
}