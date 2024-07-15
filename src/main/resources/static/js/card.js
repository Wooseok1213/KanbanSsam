// 카드 추가 팝업 열기
function openAddCardPopup(columnId) {
    $('#card-add-container').addClass('active');
    $('#columnId').val(columnId);
}

// 카드 추가 팝업 닫기
function closeAddCardPopup() {
    $('#title').val(null);
    $('#responsiblePerson').val(null);
    $('#content').val(null);
    $('#deadline').val(null);
    $('#card-add-container').removeClass('active');
}

// 카드 추가 API 요청
function addCard() {
    const columnId = $('#columnId').val();
    const title = $('#title').val();
    const responsiblePerson = $('#responsiblePerson').val();
    const content = $('#content').val();
    const deadline = $('#deadline').val();

    const data = {
        'title': title,
        'responsiblePerson': responsiblePerson,
        'content': content,
        'deadline': deadline
    };

    console.log(data)

    const accessToken = getAccessToken();

    $.ajax({
        type: 'POST',
        url: `/columns/${columnId}/cards`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: JSON.stringify(data),
        success: function (response) {
            alert('카드 추가 성공.');
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 카드 삭제 alert
function deleteCard(cardId) {
    const accessToken = getAccessToken();
    if (confirm("삭제하는 경우 작성한 모든 데이터가 삭제됩니다.\n정말 삭제하시겠습니까?") === true) {
        $.ajax({
            type: 'DELETE',
            url: `/cards/${cardId}`,
            beforeSend : function(xhr){
                xhr.setRequestHeader("Authorization", accessToken);
            },
            success: function (response) {
                alert('카드 삭제 성공.');
                window.location.reload();
            },error: err => {
                alert(err.responseJSON.message);
            }
        })
    }
}

// 카드 수정 팝업 열기
function openUpdateCardPopup(cardId, title, responsiblePerson, content, deadline) {
    $('#card-update-container').addClass('active');
    $('#update-cardId').val(cardId);
    $('#update-title').val(title);
    $('#update-responsiblePerson').val(responsiblePerson);
    $('#update-content').val(content);
    $('#update-deadline').val(deadline);
    console.log(cardId)
    console.log(title)
    console.log(responsiblePerson)
    console.log(content)
    console.log(deadline)
}

// 카드 수정 팝업 닫기
function closeUpdateCardPopup() {
    $('#update-title').val(null);
    $('#update-responsiblePerson').val(null);
    $('#update-content').val(null);
    $('#update-deadline').val(null);
    $('#card-update-container').removeClass('active');
}

// 카드 수정 API 요청
function updateCard() {
    const cardId = $('#update-cardId').val();
    const title = $('#update-title').val();
    const responsiblePerson = $('#update-responsiblePerson').val();
    const content = $('#update-content').val();
    const deadline = $('#update-deadline').val();

    const data = {
        'title': title,
        'responsiblePerson': responsiblePerson,
        'content': content,
        'deadline': deadline
    };

    const accessToken = getAccessToken();

    $.ajax({
        type: 'PUT',
        url: `/cards/${cardId}`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: JSON.stringify(data),
        success: function (response) {
            alert('카드 수정 성공.');
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 보드 별 전체 카드 목록 조회 API 요청
function getCardListByBoard() {
    const boardId = document.querySelector(".all-container").id.slice(4);
    $.ajax({
        type: 'GET',
        url: `/boards/${boardId}/cards`,
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                    let card = response[i];
                    let tempHtml = addAllCardItem(card);
                    $('.all-container').append(tempHtml);
                }
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 보드별 전체 카드 목록 HTML 생성
function addAllCardItem(card) {
    return `<div id="all-card-${card.cardId}" class="card">
            <div class="text">
                <span>${card.title}</span>
            </div>
            <div class="text">
                <span>담당자 : </span><span>${card.responsiblePerson}</span>
            </div>
            <button onclick="location.href='/cards/${card.cardId}'"
                    type="button">상세</button>
            <button onclick="deleteCard('${card.cardId}')"
                    type="button">삭제</button>
            <button onclick="openUpdateCardPopup(
                '${card.cardId}',
                '${card.title}',
                '${card.responsiblePerson}',
                '${card.content}',
                '${card.deadline}')"
                    type="button">수정</button>
        </div>`;
}

// 사용자 별 카드 목록 조회 API 요청
function getCardListByUser() {
    const boardId = document.querySelector(".user-container").id.slice(5);

    $.ajax({
        type: 'GET',
        url: `/boards/${boardId}/cards/byUser`,
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let card = response[i];
                let tempHtml = addUserCardItem(card);
                $('.all-container').append(tempHtml);
            }
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 사용자별 카드 목록 HTML 생성
function addUserCardItem(schedule, type) {
    return `<div th:each="cardListByColumn : ${cardListByColumn}"
             th:id="'user-'+${cardListByColumn.columnId}"
             class="user">
             <div >

            <h1 th:text="${cardListByColumn.getColumnName()}"></h1>

            <div class="column-btn-container">
                <button th:onclick="openUpdateColumnPopup(
            [[${cardListByColumn.columnId}]],
            [[${cardListByColumn.getColumnName()}]])">Update Column</button>

                <button th:onclick="deleteColumn([[${cardListByColumn.columnId}]])">Delete Column</button>
            </div>

            <div th:id="${cardListByColumn.columnId}"
                 class="card-container">
                <div th:each="cardList : ${cardListByColumn.getCardList()}"
                     th:id="'card-'+${cardList.getCardId()}"
                     class="card" draggable="true">
                    <div class="text">
                        <span th:text="${cardList.getTitle()}"></span>
                    </div>
                    <div class="text">
                        <span>담당자 : </span><span th:text="${cardList.getResponsiblePerson()}"></span>
                    </div>
                    <button th:onclick="|location.href='@{'/cards/'+${cardList.getCardId()}}'|"
                            type="button">상세</button>
                    <button th:onclick="deleteCard([[${cardList.getCardId()}]])"
                            type="button">삭제</button>
                    <button th:onclick="openUpdateCardPopup(
                [[${cardList.getCardId()}]],
                [[${cardList.getTitle()}]],
                [[${cardList.getResponsiblePerson()}]],
                [[${cardList.getContent()}]],
                [[${cardList.getDeadline}]])"
                            type="button">수정</button>
                </div>
            </div>
            <button th:onclick="openAddCardPopup([[${cardListByColumn.getColumnId()}]])">Add Card</button>
        </div>`;
}


$(document).ready(function () {

    $('.nav div.nav-colum').on('click', function () {
        $('div.nav-colum').addClass('active');
        $('div.nav-user').removeClass('active');
        $('div.nav-all').removeClass('active');

        $('.column-container').show();
        $('.user-container').hide();
        $('.all-container').hide();
    })
    $('.nav div.nav-user').on('click', function () {
        $('div.nav-user').addClass('active');
        $('div.nav-colum').removeClass('active');
        $('div.nav-all').removeClass('active');

        getCardListByUser()

        $('.user-container').show();
        $('.column-container').hide();
        $('.all-container').hide();
    })
    $('.nav div.nav-all').on('click', function () {
        $('div.nav-colum').removeClass('active');
        $('div.nav-user').removeClass('active');
        $('div.nav-all').addClass('active');

        $('div.all-container').empty();
        getCardListByBoard()

        $('.column-container').hide();
        $('.user-container').hide();
        $('.all-container').show();
    })

    $('.column-container').show();
    $('.user-container').hide();
    $('.all-container').hide();


    // 순서 이동 (drog & drop)
    const cards = document.querySelectorAll(".card");

    const cardContainer = document.querySelectorAll(".card-container");
    cardContainer.forEach((cardContainer) => {
        new Sortable(cardContainer, {
            group: "card",
            animation: 150,
            ghostClass: "blue-background-class"
        });
    });

    cards.forEach(cards => {
        cards.addEventListener("dragstart", () => {
            cards.classList.add("dragging");
        });

        cards.addEventListener("dragend", () => {
            cards.classList.remove("dragging");
            const columnId = cards.parentNode.id;

            const cardDivList = Array.from(cards.parentNode.querySelectorAll(".card"));
            const cardIdList = cardDivList.map(e => e.id.slice(5));

            updateCardOrders(columnId, cardIdList);
        });
    });
})

// 카드 순서 이동 API 요청
function updateCardOrders(columnId, cardIdList) {
    const data = {cardIdList};

    $.ajax({
        type: 'PUT',
        url: `/columns/${columnId}/cards/orders`,
        contentType: 'application/json',
        data: JSON.stringify(cardIdList),
        success: function (response) {
            console.log("success")
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}