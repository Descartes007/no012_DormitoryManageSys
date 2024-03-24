import request from '@/utils/request'

export function list(data) {
    return request({
        url: '/student/list',
        method: 'post',
        data: data
    })
}
export function listByName(name) {
    return request({
        url: '/student/list?name=' + name,
        method: 'get'
    })
}
export function query(id) {
    return request({
        url: '/student/query?id=' + id,
        method: 'get'
    })
}

export function del(data) {
    return request({
        url: '/student/delete',
        method: 'post',
        data: data
    })
}
export function saveOrUpdate(data) {
    return request({
        url: '/student/saveOrUpdate',
        method: 'post',
        data: data
    })
}
