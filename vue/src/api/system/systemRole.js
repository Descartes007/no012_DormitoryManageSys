import request from '@/utils/request'

export function listInSelect() {
    return request({
        url: '/system/role/listInSelect',
        method: 'get'
    })
}

export function query(id) {
    return request({
        url: '/system/role/query?id=' + id,
        method: 'get'
    })
}

export function list(data) {
    return request({
        url: '/system/role/list',
        method: 'post',
        data: data
    })
}

export function del(id) {
    return request({
        url: '/system/role/delete?id=' + id,
        method: 'get'
    })
}

export function saveOrUpdate(data) {
    return request({
        url: '/system/role/saveOrUpdate',
        method: 'post',
        data: data
    })
}
