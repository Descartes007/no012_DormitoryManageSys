import request from '@/utils/request'

export function list(data) {
    return request({
        url: '/backlate/list',
        method: 'post',
        data: data
    })
}

export function saveOrUpdate(data) {
    return request({
        url: '/backlate/saveOrUpdate',
        method: 'post',
        data: data
    })
}
export function query(id) {
    return request ({
        url:'/backlate/query/' + id,
        method: 'get'
    })
}

export function del(id) {
    return request ({
        url: '/backlate/delete/' + id,
        method: 'get'
    })
}
