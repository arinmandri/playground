const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

export function getAccessToken() {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
}

export function getRefreshToken() {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
}

export function setTokens(access: string, refresh: string) {
    if (access != null)
        localStorage.setItem(ACCESS_TOKEN_KEY, access);
    if (refresh != null)
        localStorage.setItem(REFRESH_TOKEN_KEY, refresh);
    console.log('토큰 저장', access, refresh);
}

export function clearTokens() {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    console.log('토큰 삭제');
}
