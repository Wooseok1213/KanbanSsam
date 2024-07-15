$(document).ready(function () {

    // 로그인한 사용자 권한 확인
    const token = Cookies.get('Authorization').replace('Bearer ', '');
    const base64Payload = token.split('.')[1];
    const base64 = base64Payload.replace(/-/g, '+').replace(/_/g, '/');

    const decodedJWT = JSON.parse(
        decodeURIComponent(
            window
                .atob(base64)
                .split('')
                .map(function (c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                })
                .join('')
        )
    );

    if (decodedJWT.auth !== "MANAGER") {
        $('.column-btn-container').empty();
        $('.board-btn-container').empty();
    }

})

function getAccessToken() {
    let access = Cookies.get('Authorization');

    if(access === undefined) {
        return '';
    }

    return access;
}

function getRefreshToken() {
    let refresh = Cookies.get('RefreshToken');
    if (refresh === undefined) {
        return '';
    }
    return refresh;
}

function validationUserRole() {
    // 로그인한 사용자 권한 확인
    const token = Cookies.get('Authorization').replace('Bearer ', '');
    const base64Payload = token.split('.')[1];
    const base64 = base64Payload.replace(/-/g, '+').replace(/_/g, '/');

    const decodedJWT = JSON.parse(
        decodeURIComponent(
            window
                .atob(base64)
                .split('')
                .map(function (c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                })
                .join('')
        )
    );

    if (decodedJWT.auth !== "MANAGER") {
        $('.column-btn-container').empty();
        $('.board-btn-container').empty();
    }
}

function moveColumnAuthority() {
    // 로그인한 사용자 권한 확인
    const token = Cookies.get('Authorization').replace('Bearer ', '');
    const base64Payload = token.split('.')[1];
    const base64 = base64Payload.replace(/-/g, '+').replace(/_/g, '/');

    const decodedJWT = JSON.parse(
        decodeURIComponent(
            window
                .atob(base64)
                .split('')
                .map(function (c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                })
                .join('')
        )
    );

    return decodedJWT.auth === "MANAGER";


}