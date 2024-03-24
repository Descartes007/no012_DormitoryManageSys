import request from '../utils/request'

export function getUserInfo() {
    return request({
        url: '/permission/userinfo',
        method: 'get'
    })
}

export function update(data) {
    return request({
        url: '/system/user/update',
        data: data,
        method: 'post'
    })
}

export function changePassword(data) {
    return request({
        url: '/system/user/change',
        data: data,
        method: 'post'
    })
}

export function changeIcon(icon) {
    return request({
        url: '/system/user/changeIcon/' + icon,
        method: 'get'
    })
}

