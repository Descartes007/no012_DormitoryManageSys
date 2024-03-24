import store from '@/store'

export default {
    inserted(el, binding) {
        let all_permission = store.state.permission
        let {value} = binding
        for (let v of value) {
            for (let p of all_permission) {
                if (p === v) {
                    return
                }
            }
        }
        el.parentNode && el.parentNode.removeChild(el)
    },
}
