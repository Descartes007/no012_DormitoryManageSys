import request from '@/utils/request'

export function listAll() {
    return request({
        url: '/building/listAll',
        method: 'get'
    })
}
export function list() {
    return request({
        url: '/building/list',
        method: 'get'
    })
}

export function query(id) {
    return request({
        url: '/building/query?id=' + id,
        method: 'get'
    })
}

export function del(id) {
    return request({
        url: '/building/delete?id=' + id,
        method: 'get'
    })
}

export function saveOrUpdate(data) {
    return request({
        url: '/building/saveOrUpdate',
        method: 'post',
        data: data
    })
}
