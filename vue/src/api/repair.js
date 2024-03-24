import request from '@/utils/request'

export function list(data) {
    return request({
        url: '/repair/list',
        method: 'post',
        data: data
    })
}

export function query(id) {
    return request({
        url: '/repair/query/' + id,
        method: 'get'
    })
}


export function saveOrUpdate(data) {
    return request({
        url: '/repair/saveOrUpdate',
        method: 'post',
        data: data
    })
}

export function updateStatus(id) {
    return request({
        url: '/repair/updateStatus/' + id,
        method: 'get'
    })
}

export function del(id) {
    return request({
        url:'/repair/delete/' + id,
        method: 'get'
    })
}
