import request from '@/utils/request'

export function sendToUser(data) {
    return request({
        url: '/notice/sendToUser/' + data.to,
        method: 'post',
        data: {
            msg:data.msg
        }
    })
}

export function sendToBuilding(data) {
    return request({
        url: '/notice/sendToBuilding/' + data.to,
        method: 'post',
        data: {
            msg:data.msg
        }
    })
}

export function deleteReceive(id) {
    return request({
        url: '/notice/deleteReceive/' + id,
        method: 'get'
    })
}

export function deleteSend(id) {
    return request({
        url: '/notice/deleteSend/' + id,
        method: 'get'
    })
}

export function query(id) {
    return request({
        url: '/notice/query/' + id,
        method: 'get'
    })
}

export function querydetail(id) {
    return request({
        url: '/notice/querydetail/' + id,
        method: 'get'
    })
}

export function list(data) {
    return request({
        url: '/notice/list',
        method: 'post',
        data: data
    })
}

export function listSend(data) {
    return request({
        url: '/notice/listSend',
        method: 'post',
        data: data
    })
}
