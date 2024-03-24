export function resolveRouter(list, res) {
    for (let i of list) {
        if (i.menuType === 'C') {
            let route = {
                path: i.path[0] === '/' ? i.path : "/" + i.path,
                name: i.name,
                meta: {
                    title: i.name,
                    breadcrumb: true
                },
                component: loadView(i.component)
            }
            res.push(route)
        }
        if (i.children) {
            resolveRouter(i.children, res)
        }
    }
}

export const loadView = (view) => { // 路由懒加载
    return (resolve) => require([`./${view}`], resolve)
}
