import request from '@/utils/request'

export function listTree() {
    return request({
        url: '/system/function/list',
        method: 'get'
    })
}

export function del(id) {
    return request({
        url: '/system/function/delete?id=' + id,
        method: 'get'
    })
}

export function saveOrUpdate(data) {
    return request({
        url: '/system/function/saveOrUpdate',
        method: 'post',
        data: data
    })
}

export function query(id) {
    return request({
        url: '/system/function/query?id=' + id,
        method: 'get'
    })
}
