import request from '@/utils/request'

export function save(data) {
    return request({
        url: '/leave/save',
        method: 'post',
        data: data
    })
}

export function list(data) {
    return request({
        url: '/leave/list',
        method: 'post',
        data: data
    })
}

export function update(id) {
    return request({
        url: '/leave/update/' + id,
        method: 'get'
    })
}

export function del(id) {
    return request({
        url: '/leave/delete/' + id,
        method: 'get'
    })
}

export function query(id) {
    return request({
        url: '/leave/query/' + id,
        method: 'get'
    })
}

