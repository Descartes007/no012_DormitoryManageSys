import Cookies from 'js-cookie'

const TokenKey = '_ut'

export function getToken() {
    return Cookies.get(TokenKey)
}

export function setToken(token) {
    return Cookies.set(TokenKey, token, {path: "/", expires: 1})
}

export function removeToken() {
    return Cookies.remove(TokenKey)
}
