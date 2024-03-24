import request from '@/utils/request'

export function get() {
    return request({
        url: '/statistics',
        method: 'get'
    })
}
