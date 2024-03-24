import request from '@/utils/request'

export function del(id) {
    return request({
        url: '/depart/delete/' + id,
        method: 'get'
    })
}

export function listFlow(id) {
    return request({
        url: '/depart/listFlow/' + id,
        method: 'get'
    })
}

export function listMy(data) {
    return request({
        url: '/depart/listMy',
        method: 'post',
        data: data
    })
}

export function listMyFlow(data) {
    return request({
        url: '/depart/listMyFlow',
        method: 'post',
        data: data
    })
}

export function query(id) {
    return request({
        url: '/depart/query/' + id,
        method: 'get'
    })
}

export function update(data) {
    return request({
        url: '/depart/update',
        method: 'post',
        data:data
    })
}
