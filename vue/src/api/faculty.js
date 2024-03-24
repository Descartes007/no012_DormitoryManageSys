import request from '@/utils/request'

export function list() {
    return request({
        url: '/faculty/list',
        method: 'get'
    })
}

export function listAll() {
    return request({
        url: '/faculty/listAll',
        method: 'get'
    })
}

export function query(id) {
    return request({
        url:'/faculty/query?id=' + id,
        method: 'get'
    })
}

export function saveOrUpdate(data) {
    return request({
        url: '/faculty/saveOrUpdate',
        method: 'post',
        data: data
    })
}

export function del(id) {
    return request({
        url: '/faculty/delete?id=' + id,
        method: 'get'
    })
}
