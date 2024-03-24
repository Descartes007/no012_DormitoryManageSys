import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const store = new Vuex.Store({
    state: {
        permission: [],
        menu: [],
        hasRole: false,
        userName: undefined,
        userIcon: undefined,
        socket: undefined,
        countUnread: 0,
        reLoginConfirm: false
    },
    mutations: {
        SET_RE_LOGIN_COMFIRM(state) {
            state.reLoginConfirm = true
        },
        SET_COUNT_UNREAD(state, count) {
            state.countUnread = count
        },
        SET_PERMISSION(state, permission) {
            state.permission = permission
        },
        DELETE_PERMISSION(state) {
            state.permission = []
        },
        SET_HAS_ROLE(state, bool) {
            state.hasRole = bool
        },
        SET_MENU(state, menu) {
            state.menu = menu
        },
        SET_USER_NAME(state, userName) {
            state.userName = userName
        },
        SET_USER_ICON(state, userIcon) {
            state.userIcon = userIcon
        },
        SET_SOCKET(state, socket) {
            state.socket = socket
        }
    }
})

export default store
