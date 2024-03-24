<template>
  <div id="app">
    <div class="pull-left" :class="isCollapse?'nav-collapse':'nav'">
      <el-menu :default-active="activeMenu" class="nav" :collapse="isCollapse" unique-opened>
        <img :class="isCollapse?'logo-collapse':'logo'" src="./assets/logo.png" alt="" @click="to('/')">
        <el-menu-item index="/index"
                      @click="to('/index')">
          <i class="iconfont iconhomepage"></i>
          <span slot="title">首页</span>
        </el-menu-item>
        <menu-item :menu="item" :key="item.id" v-for="item in menu"/>
      </el-menu>
    </div>
    <div :class="isCollapse?'main-collapse':'main'">
      <div :class="isCollapse?'top-bar-collapse':'top-bar'">
        <el-header>
          <div class="left">
                        <span @click="isCollapse = !isCollapse">
                            <i class="el-icon-s-fold" v-if="!isCollapse"></i>
                            <i class="el-icon-s-unfold" v-else></i>
                        </span>
          </div>
          <div class="right">
            <div class="mail">
              <el-badge :value="count" :max="99" class="item">
                <i class="el-icon-message" @click="showMailDialog"></i>
              </el-badge>
            </div>
            <div class="icon">
              <el-avatar
                  :src="icon" v-if="icon"></el-avatar>
              <el-avatar v-else> {{ name }}</el-avatar>
            </div>
            <el-dropdown @command="handleCommand">
                              <span class="el-dropdown-link name">
                                {{ name }}<i class="el-icon-arrow-down el-icon--right"></i>
                              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="setting">
                  <span style="display:block;">个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item command="change">
                  <span style="display:block;">修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item command="logout">
                  <span style="display:block;">注销</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>
      </div>
      <div class="router">
        <div class="breadcrumb">
          <breadcrumb/>
        </div>
        <transition name="fade-transform" mode="out-in">
          <router-view/>
        </transition>
      </div>
    </div>

    <div class="dialog">
      <el-dialog :visible.sync="dialogVisible" @closed="handleClose" title="消息" :fullscreen="true">
        <message :change="change"></message>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import store from './store'
import Message from './components/Message'
import MenuItem from './components/MenuItems'
import {logout} from "./api/login";
import {removeToken} from "./utils/auth";
import Breadcrumb from '@/components/Breadcrumb'

export default {
  data() {
    return {
      change: 0,
      isCollapse: false,
      dialogVisible: false
    }
  },
  components: {
    MenuItem, Breadcrumb,Message
  },
  methods: {
    showMailDialog() {
      this.change = 1
      this.dialogVisible = true
    },
    handleClose() {
      this.change = 0
      this.dialogVisible = false
    },
    to(url) {
      this.$router.push(url).catch(err => {
        err
      })
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.logout()
      } else if (command === 'setting') {
        this.to('/personal/info')
      } else if (command === 'change') {
        this.to('/personal/change_password')
      }
    },
    async logout() {
      await logout()
      await removeToken()
      window.location.href = process.env.VUE_APP_SSO
    }
  },
  computed: {
    activeMenu() {
      const route = this.$route
      const {path} = route
      return path
    },
    count() {
      return store.state.countUnread
    },
    menu() {
      return store.state.menu
    },
    name() {
      return store.state.userName
    },
    icon() {
      return store.state.userIcon
    }
  }
}
</script>
<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.28s;
}

.fade-enter,
.fade-leave-active {
  opacity: 0;
}

/* fade-transform */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all .5s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

html {
  height: 100%;
  overflow-x: hidden;
}

body {
  height: 100%;
  background-color: #F0F7F9;
}

.main {
  height: 100%;
  margin-left: 200px;
}

.main-collapse {
  height: 100%;
  margin-left: 64px;
}

#app {
  height: 100%;
}

* {
  margin: 0;
}

.nav {
  width: 200px;
  height: 100%;
}

.nav-collapse {
  width: 64px;
  height: 100%;
}

.logo {
  width: 60px;
  height: 60px;
  margin: 20px 70px;
  cursor: pointer;
}

.logo-collapse {
  width: 34px;
  height: 34px;
  margin: 20px 15px;
  cursor: pointer;
}

.top-bar {
  background-color: white;
  position: fixed;
  left: 201px;
  right: 0;
  z-index: 100;
}

.top-bar-collapse {
  background-color: white;
  position: fixed;
  left: 65px;
  right: 0;
  z-index: 100;
}

.left {
  float: left;
  line-height: 60px;
}

.left span i {
  font-size: 25px;
  color: #656565;
  cursor: pointer;
}

.right {
  float: right;
  position: relative;
}

.icon, .name {
  float: left;
}

.name {
  line-height: 60px;
  color: #2399f1;
  cursor: pointer;
}

.icon {
  margin: 10px;
}

.breadcrumb {
  background-color: white;
  padding-left: 20px;
}

.container {
  padding: 20px;
}

.inner_container {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pull-left {
  position: fixed;
  left: 0;
  top: 0;
}

.router {
  padding-top: 60px;
}
.mail {
  float: left;
  height: 60px;
  font-size: 22px;
  cursor: pointer;
  position: absolute;
  left: -30px;
  top: 18px;
}
.item:hover {
  color: #409EFF;
}
.item:active {
  color: #000000;
}
</style>
