<template>
  <div>
    <div class="container">
      <div class="inner_container">
        <div class="user-form">
          <div class="avatar" title="点击更换头像">
            <el-upload
                class="avatar-uploader"
                :action="action"
                :show-file-list="false"
                :headers="header"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
              <img v-if="user.icon" :src="user.icon" class="avatar" alt="">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
          <br>
          <el-form ref="form" :rules="rules" :model="user" label-width="100px">
            <el-form-item label="用户名称:" prop="realName">
              <el-input v-model="user.realName"></el-input>
            </el-form-item>
            <el-form-item label="电话:" prop="cellphone">
              <el-input v-model="user.cellphone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱:" prop="email">
              <el-input v-model="user.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="update('form')">修改</el-button>
              <el-button type="info" @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {getUserInfo, update, changeIcon} from "@/api/user";
import {getToken} from "@/utils/auth";
import store from "@/store";

export default {
  name: "index",
  data() {
    let validator1 = (rule, value, callback) => {
      if (value.length === 0) {
        callback()
        return
      }
      let reg = /^1[3456789]\d{9}$/
      if (!reg.test(value)) {
        callback(new Error())
      } else {
        callback()
      }
    }
    let validator2 = (rule, value, callback) => {
      if (value.length === 0) {
        callback()
        return
      }
      let reg = /^[0-9a-zA-Z_.-]+[@][0-9a-zA-Z_.-]+([.][a-zA-Z]+){1,2}$/;
      if (!reg.test(value)) {
        callback(new Error())
      } else {
        callback()
      }
    }
    return {
      header: {
        "_ut": getToken()
      },
      action: process.env.VUE_APP_BASE_API + "/permission/upload",
      user: {
        id: undefined,
        realName: '',
        icon: undefined,
        cellphone: '',
        email: ''
      },
      rules: {
        realName: [
          {required: true, message: '名称不能为空', trigger: 'change'},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'change'}
        ],
        cellphone: [
          {validator: validator1, message: "请输入正确的手机号", trigger: 'change'}
        ],
        email: [
          {validator: validator2, message: "请输入正确的邮箱", trigger: 'change'}
        ]
      }
    }
  },
  methods: {
    async handleAvatarSuccess(res) {
      await this.changeIcon(res.data)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type.indexOf('image/') !== -1;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('只能上传图片');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    async changeIcon(icon) {
      changeIcon(icon).then(res => {
        if (res.statusCode === 200) {
          this.$message({
            message: res.msg,
            type: 'success'
          });
          this.reset()
        }
      })
    },
    update(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          update(this.user).then(res => {
            store.commit('SET_USER_NAME', res.data.realName)
            this.$message({
              message: res.msg,
              type: 'success'
            });
          })
        } else {
          this.$message({
            message: '请正确填写参数',
            type: 'error'
          });
          return false;
        }
      });
    },
    async reset() {
      const {data} = await getUserInfo()
      if (data.icon) {
        data.icon = process.env.VUE_APP_IMG_PREFIX + data.icon
      }
      this.user = data
      store.commit('SET_USER_ICON', this.user.icon)
    }
  },
  created() {
    this.reset()
  }
}
</script>

<style scoped>
.user-form {
  width: 400px;
  margin: 0 auto;
}

.avatar, .avatar-uploader {
  width: 80px;
  height: 80px;
  margin: 0 auto;
}

.avatar-uploader i {
  line-height: 80px;
  text-align: center;
  margin: 0 32px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}
</style>
