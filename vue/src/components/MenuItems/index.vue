<template>
  <el-submenu v-if="menu.menuType === 'M'" :index="menu.id.toString()">
    <template slot="title">
      <i class="iconfont" :class="menu.icon?menu.icon : 'iconfeedback'"></i>
      <span>{{ menu.name }}</span>
    </template>
    <menu-item v-for="item in menu.children" :key="item.id" :menu="item"></menu-item>
  </el-submenu>
  <el-menu-item :index="menu.path[0] === '/'?menu.path:'/'+menu.path" v-else @click="to(menu.path)">
    <i class="iconfont" :class="menu.icon?menu.icon : 'iconfeedback'"></i>
    <span slot="title">{{ menu.name }}</span>
  </el-menu-item>
</template>

<script>
export default {
  name: "MenuItem",
  props: ['menu'],
  methods: {
    to(url) {
      if (url[0] !== '/') url = '/' + url
      this.$router.push(url).catch(err => {
        err
      })
    }
  }
}
</script>

<style scoped>

</style>
