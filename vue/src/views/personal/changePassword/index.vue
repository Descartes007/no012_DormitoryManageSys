<template>
  <div>
    <div class="container">
      <div class="inner_container">
        <div class="user-form">
          <el-form ref="form" :model="formData" :rules="rules" label-width="100px">
            <el-form-item label="原密码:" prop="oldPassword">
              <el-input v-model="formData.oldPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item label="修改密码:" prop="current1">
              <el-input v-model="formData.current1" type="password"></el-input>
            </el-form-item>
            <el-form-item label="再次输入:" prop="current2">
              <el-input v-model="formData.current2" type="password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="send('form')">修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {changePassword} from "../../../api/user";

export default {
  name: "index",
  data() {
    let validator1 = (rule, value, callback) => {
      if (value === this.formData.oldPassword) {
        callback(new Error())
      } else {
        callback()
      }
    }
    let validator2 = (rule, value, callback) => {
      if (value !== this.formData.current1) {
        callback(new Error())
      } else {
        callback()
      }
    }
    return {
      formData: {
        oldPassword: '',
        current1: '',
        current2: ''
      },
      rules: {
        oldPassword: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        current1: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'},
          {validator: validator1, message: "修改后的密码不能于原密码相同", trigger: 'blur'}
        ],
        current2: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {validator: validator2, message: "两次密码需要相同", trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    send(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          changePassword(this.formData).then(res => {
            if (res.statusCode === 200) {
              this.$message({
                message: res.msg,
                type: 'success'
              });
              setTimeout(() =>{
                location.reload()
              }, 1000)
              this.formData = {
                oldPassword: '',
                current1: '',
                current2: ''
              }
            }
          })
        } else {
          this.$message({
            message: '请正确填写参数',
            type: 'error'
          });
          return false;
        }
      });
    }
  }
}
</script>

<style scoped>
.user-form {
  width: 400px;
  margin: 0 auto;
}
</style>
