import {getToken, removeToken} from '@/utils/auth'
import axios from 'axios'
import {Message, MessageBox} from 'element-ui'
import store from "@/store";

const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    timeout: 10000
})
// request拦截器
service.interceptors.request.use(
    async config => {
        let token = getToken()
        if (token) {
            config.headers['_ut'] = token
        }
        return config
    },
    error => {
        console.log(error)
        return Promise.reject(error)
    }
)
//response 拦截器
service.interceptors.response.use(
    response => {
        const res = response.data
        if (res.statusCode === 200) {
            return res
        } else {
            if (res.statusCode === 407) {
                if (!store.state.reLoginConfirm) {
                    MessageBox.confirm('登录过期,请重新登录', '确认退出', {
                        confirmButtonText: '重新登录',
                        type: 'warning'
                    }).then(() => {
                        removeToken()
                        location.reload()
                    })
                    store.commit("SET_RE_LOGIN_COMFIRM")
                } else {
                    Message({
                        message: res.msg,
                        type: 'error'
                    })
                }
            } else {
                Message({
                    message: res.msg || 'Error',
                    type: 'error',
                    duration: 4 * 1000
                })
            }
            return Promise.resolve(new Error(res.msg || 'Error'))
        }
    },
    error => {
        Message({
            message: error.message || "Error",
            type: 'error',
            duration: 4 * 1000
        })
        return Promise.resolve(error)
    }
)

export default service
