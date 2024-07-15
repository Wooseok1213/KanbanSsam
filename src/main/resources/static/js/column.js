// 컬럼 추가 팝업 열기
function openAddColumnPopup() {
    $('#column-add-container').addClass('active');
}

// 컬럼 추가 팝업 닫기
function closeAddColumnPopup() {
    $('#column-add-container').removeClass('active');
    $('#name').val(null);
}

// 컬럼 추가 API 요청
function addColumn() {
    const name = $('#name').val();
    const boardId = $('.column-container').attr('id');

    const data = {
        'name': name
    };

    const accessToken = getAccessToken();

    $.ajax({
        type: 'POST',
        url: `/boards/${boardId}/columns`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: JSON.stringify(data),
        success: function (response) {
            console.log(response.title)
            alert('컬럼 추가 성공.');
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 컬럼 수정 팝업 열기
function openUpdateColumnPopup(columnId, name) {
    $('#column-update-container').addClass('active');
    $('#update-name').val(name);
    $('#update-column-id').val(columnId);
}

// 컬럼 수정 팝업 닫기
function closeUpdateColumnPopup() {
    $('#column-update-container').removeClass('active');
    $('#name').val(null);
}

// 컬럼 수정 API 요청
function updateColumn() {
    const name = $('#update-name').val();
    const columnId = $('#update-column-id').val();

    const data = {
        'name': name
    };

    const accessToken = getAccessToken();

    $.ajax({
        type: 'PUT',
        url: `/columns/${columnId}`,
        contentType: 'application/json',
        beforeSend : function(xhr){
            xhr.setRequestHeader("Authorization", accessToken);
        },
        data: JSON.stringify(data),
        success: function (response) {
            console.log(response.title)
            alert('컬럼 수정 성공.');
            window.location.reload();
        },error: err => {
            alert(err.responseJSON.message);
        }
    })
}

// 컬럼 삭제 alert
function deleteColumn(columnId) {
    const accessToken = getAccessToken();
    if (confirm("삭제하는 경우 작성한 모든 데이터가 삭제됩니다.\n정말 삭제하시겠습니까?") === true) {
        $.ajax({
            type: 'DELETE',
            url: `/columns/${columnId}`,
            beforeSend : function(xhr){
                xhr.setRequestHeader("Authorization", accessToken);
            },
            success: function (response) {
                alert('컬럼 삭제 성공.');
                window.location.reload();
            },error: err => {
                alert(err.responseJSON.message);
            }
        })
    }
}

$(document).ready(function () {
    if (moveColumnAuthority()) {
        // 순서 이동 (drog & drop)
        const columns = document.querySelectorAll(".column");

        const columnContainer = document.querySelectorAll(".column-container");
        columnContainer.forEach((columnContainer) => {
            new Sortable(columnContainer, {
                group: "column",
                animation: 150,
                ghostClass: "blue-background-class"
            });
        });

        columns.forEach(columns => {
            columns.addEventListener("dragstart", () => {
                columns.classList.add("dragging");
            });

            columns.addEventListener("dragend", () => {
                columns.classList.remove("dragging");

                const boardId = columns.parentNode.id;

                const columnDivList = Array.from(columns.parentNode.querySelectorAll(".column"));
                const columnIdList = columnDivList.map(e => Number(e.id.slice(7)));

                console.log(boardId);
                console.log(columnIdList)

                updateColumnOrders(boardId, columnIdList);
            });
        });
    }

})

// 컬럼 순서 이동 API 요청
function updateColumnOrders(boardId, columnIdList) {
    const data = columnIdList;
    const accessToken = getAccessToken();

    console.log("Sending data:", JSON.stringify(data)); // 로그 추가

     $.ajax({
         type: 'PUT',
         url: `/boards/${boardId}/columns/orders`,
         contentType: 'application/json',
         beforeSend : function(xhr){
             xhr.setRequestHeader("Authorization", accessToken);
         },
         data: JSON.stringify(data),
         success: function (response) {
             console.log("success")
         },error: err => {
             alert("error");
         }
     })
}