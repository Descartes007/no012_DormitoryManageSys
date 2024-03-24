import request from '@/utils/request'

export function listAll() {
    return request({
        url: '/room/listAll',
        method: 'get'
    })
}

export function list(data) {
    return request({
        url: '/room/list',
        method: 'post',
        data: data
    })
}

export function del(id) {
    return request({
        url: '/room/delete/' + id,
        method: 'get'
    })
}

export function query(id) {
    return request({
        url: "/room/query/" + id,
        method: 'get'
    })
}

export function saveOrUpdate(data) {
    return request({
        url: '/room/saveOrUpdate',
        method: 'post',
        data: data
    })
}
