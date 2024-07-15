$(document).ready(function () {

    const accessToken = getAccessToken();

    // 로그인한 사용자의 보드 목록 조회
    $.ajax({
        type: 'GET',
        url: `/guests`,
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        success: function (response) {
            const userName = response.userName;
            const boardList = response.boardList;

            $('#user-name').text(userName);

            for (let i = 0; i < boardList.length; i++) {
                let board = boardList[i];
                let tempHtml = addAllBoardItem(board);
                $('.board-list').append(tempHtml);
            }
            validationUserRole();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })

})

// 로그인한 사용자의 보드 HTML 생성
function addAllBoardItem(board) {
    return `<div id="board-${board.boardId}" class="board">

        <div class="board-btn-container">
            <button onclick="openUpdateBoardPopup('${board.boardId}', '${board.name}', '${board.introduction}')">
            보드 수정</button>

            <button onclick="deleteBoard('${board.boardId}')">보드 삭제</button>
            <button onclick="openInviteBoardPopup('${board.boardId}')">보드 초대</button>
        </div>

        <div class="text">
            <span>${board.name}</span>
        </div>
        <div class="text">
            <span>${board.introduction}</span>
        </div>
        
        <button onclick="location.href='/boards/${board.boardId}/columns/cardList'"
                    type="button">상세</button>
    </div>`;
}

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
    $('#update-board-id').val(boardId);
    $('#update-name').val(name);
    $('#update-introduction').val(introduction);
}

// 보드 수정 팝업 닫기
function closeUpdateBoardPopup() {
    $('#update-board-id').val(null);
    $('#update-name').val(null);
    $('#update-introduction').val(null);
    $('#board-update-container').removeClass('active');
}

// 보드 수정 API 요청
function updateBoard() {
    const boardId = $('#update-board-id').val();
    const name = $('#update-name').val();
    const introduction = $('#update-introduction').val();

    const data = {
        'name': name,
        'introduction': introduction
    };

    console.log(boardId)
    console.log(data)

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
        $.ajax({
            type: 'DELETE',
            url: `/boards/${boardId}`,
            beforeSend : function(xhr){
                xhr.setRequestHeader("Authorization", accessToken);
            },
            success: function (response) {
                alert('보드 삭제 성공.');
                window.location.reload();
            },error: err => {
                alert(err.responseJSON.message);
            }
        })
    }
}

// 보드 초대 팝업 열기
function openInviteBoardPopup(boardId) {
    $('#board-invite-container').addClass('active');
    $('#invite-board-id').val(boardId);
}

// 보드 초대 팝업 닫기
function closeInviteBoardPopup() {
    $('#invite-email').val(null);
    $('#board-invite-container').removeClass('active');
}

// 보드 초대 회원 조회
function findUser() {
    const email = $('#invite-email').val();

    const data = {
        'email': email,
    };

    console.log(data);

    const accessToken = getAccessToken();

    $.ajax({
        type: 'GET',
        url: `/users`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: data,
        success: function (response) {
            addInviteList(response.email)
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 보드 초대 리스트 등록
function addInviteList(email) {

    const guestList = Array.from(document.querySelectorAll(".guest"))
        .map(e => e.innerText);

    for (let i = 0; i < guestList.length; i++) {
        if (guestList[i] === email) {
            alert("중복된 회원입니다.")
            return
        }
    }

    let tempHtml = `<span class="guest">${email}</span><br/>`;
    $('.invite-list').append(tempHtml);
}

// 보드 초대 API
function inviteBoard() {

    const boardId = $('#invite-board-id').val();

    const emailList = Array.from(document.querySelectorAll(".guest"))
        .map(e => e.innerText);

    console.log(emailList);

    const accessToken = getAccessToken();

    $.ajax({
        type: 'POST',
        url: `/boards/${boardId}/invite`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: JSON.stringify(emailList),
        success: function (response) {
            alert("보드 초대 성공");
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}