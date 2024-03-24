import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const constantRoutes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        redirect: '/index',
        children: [
            {
                path: 'index',
                name: 'index',
                component: Home,
                meta: {title: '首 页'}
            }
        ]
    },
    {
        path: '/404',
        component: () => import('../views/error-page/404'),
        meta: {title: '404'}
    },
    {
        path: '/personal/info',
        component: () => import('../views/personal/info'),
        meta: {title: '个人信息'}
    },
    {
        path: '/personal/change_password',
        component: () => import('../views/personal/changePassword'),
        meta: {title: '修改密码'}
    }
]

const createRouter = () => new VueRouter({
    scrollBehavior: () => ({y: 0}),
    routes: constantRoutes
})
const router = createRouter()

export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
}

export default router
