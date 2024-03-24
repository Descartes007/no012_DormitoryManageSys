import request from '@/utils/request'

export function query(id) {
    return request({
        url: '/system/user/query?id=' + id,
        method: 'get'
    })
}
export function listAll() {
    return request({
        url: '/system/user/listAll',
        method: 'get'
    })
}
export function list(data) {
    return request({
        url: '/system/user/list',
        method: 'post',
        data: data
    })
}

export function del(id) {
    return request({
        url: '/system/user/delete?id=' + id,
        method: 'get'
    })
}

export function saveOrUpdate(data) {
    return request({
        url: '/system/user/saveOrUpdate',
        method: 'post',
        data: data
    })
}

export function resetPassword(id) {
    return request({
        url: '/system/user/resetPassword?id=' + id,
        method: 'get'
    })
}


