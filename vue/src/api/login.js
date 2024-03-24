import request from '@/utils/request'

export function getInfo() {
    return request({
        url: '/permission/info',
        method: 'get'
    })
}

export function countUnread() {
    return request({
        url: '/permission/countUnread',
        method: 'get'
    })
}

export function logout() {
    return request({
        url: 'login/logout',
        method: 'get'
    })
}
