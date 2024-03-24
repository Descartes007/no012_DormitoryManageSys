import request from '@/utils/request'

export function list(data) {
    return request({
        url: '/system/log/list',
        method: 'post',
        data: data
    })
}

export function query(id) {
    return request({
        url: '/system/log/query?id=' + id,
        method: 'get'
    })
}
