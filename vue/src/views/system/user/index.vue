<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form size="mini">
            <el-form-item>
              <el-button v-has-permi="['system:user:save']" @click="showDialog(undefined)" size="mini" type="primary">
                新增
              </el-button>
            </el-form-item>
          </el-form>
          <el-form :inline="true" ref="form" :model="listQuery.entity">
            <el-form-item label="名称">
              <el-input clearable v-model="listQuery.entity.realName" size="small"
                        placeholder="用户名称"></el-input>
            </el-form-item>
            <el-form-item label="角色" v-has-permi="['system:role:list']">
              <el-select :loading="roleListLoading" size="small" clearable filterable multiple
                         v-model="listQuery.entity.userRoleId"
                         placeholder="用户角色" @visible-change="listRoleList">
                <el-option v-for="role in roleList" :key="role.id" :label="role.name"
                           :value="role.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="领导">
              <el-select :loading="roleListLoading" filterable clearable v-model="listQuery.entity.leaderId" size="small"
                        placeholder="领导" @visible-change="listUserList">
                <el-option v-for="user in userList"
                           :key="user.id" :value="user.id"
                           :label="user.realName"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="所管宿舍楼">
              <el-select :loading="roleListLoading" filterable clearable v-model="listQuery.entity.buildingId" size="small"
                         @visible-change="listBuildingList"
                        placeholder="所管宿舍楼">
                <el-option v-for="building in buildingList"
                           :key="building.id" :value="building.id"
                           :label="building.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="select">查询</el-button>
              <el-button type="danger" size="small" @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="pageInfo.list" v-loading="loading" height="400">
            <el-table-column label="头像" width="100">
              <template slot-scope="scope">
                <el-avatar :src="scope.row.icon" size="medium" v-if="scope.row.icon"></el-avatar>
                <el-avatar v-else size="medium">{{ scope.row.realName }}</el-avatar>
              </template>
            </el-table-column>
            <el-table-column label="在线" width="70">
              <template slot-scope="scope">
                <span style="color: #67C23A" v-if="scope.row.isOnLine">在线</span>
                <span style="color: #909399" v-else>离线</span>
              </template>
            </el-table-column>
            <el-table-column prop="realName" label="名称" width="150"></el-table-column>
            <el-table-column prop="leader.realName" label="领导" width="100"></el-table-column>
            <el-table-column prop="building.name" label="所管宿舍楼" width="150"></el-table-column>
            <el-table-column prop="cellphone" label="电话" width="130"></el-table-column>
            <el-table-column label="角色" width="170">
              <template slot-scope="scope">{{ role(scope.row.userRole) }}</template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button v-has-permi="['system:user:query']"
                           size="mini" type="primary" @click="showDialog(scope.row.id)">详情
                </el-button>
                <template>
                  <el-popconfirm title="确定重置吗？" @confirm="resetPassword(scope.row.id)">
                    <el-button style="margin-left: 8px"
                               slot="reference"
                               v-has-permi="['system:user:update']"
                               size="mini" type="warning">重置密码
                    </el-button>
                  </el-popconfirm>
                </template>
                <template>
                  <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                    <el-button style="margin:0 8px" v-has-permi="['system:user:delete']"
                               slot="reference"
                               size="mini" type="danger">删除
                    </el-button>
                  </el-popconfirm>
                </template>
                <el-button v-has-permi="['home:send:msg']"
                           size="mini" type="primary" @click="msgOpen(scope.row.id)">发送消息
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="page">
          <el-pagination
              background
              :hide-on-single-page="true"
              :page-size="listQuery.rows"
              :page-count="pageInfo.pages"
              layout="->, prev, pager, next, jumper, total, sizes"
              :total="pageInfo.total"
              :current-page.sync="pageInfo.pageNum"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
          >
          </el-pagination>
        </div>
        <div class="dialog">
          <el-dialog title="发送消息"
                     @closed="handleClose"
                     :visible.sync="msgDialogVisible" :before-close="confirmClose"
                     width="45%"
          >
            <el-form ref="msgFrom" :model="msg">
              <el-form-item>
                <el-input
                    type="textarea"
                    rows="10"
                    placeholder="请输入内容"
                    v-model="msg.msg">
                </el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button type="danger" @click="msgClose">取 消</el-button>
              <el-button type="primary" @click="sendMsg">发 送</el-button>
            </span>
          </el-dialog>
          <el-dialog
              :title="dialogTitle"
              :visible.sync="dialogVisible"
              width="45%"
              @closed="handleClose"
          >
            <el-form ref="saveOrUpdateForm" :rules="rules" :model="user" label-width="80px">
              <el-form-item label="登录名称" prop="loginName">
                <el-input v-model="user.loginName" placeholder="输入登录名" :readonly="user.id !== undefined"></el-input>
              </el-form-item>
              <el-form-item label="名字" prop="realName">
                <el-input v-model="user.realName" placeholder="输入名字"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="cellphone">
                <el-input v-model="user.cellphone" placeholder="输入电话"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="user.email" placeholder="输入邮箱"></el-input>
              </el-form-item>
              <el-form-item label="角色">
                <el-select style="width: 100%" clearable multiple v-model="user.userRoleId"
                           placeholder="选择角色">
                  <el-option v-for="role in roleList" :key="role.id" :label="role.name"
                             :value="role.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="领导">
                <el-select style="width: 100%" clearable filterable v-model="user.leaderId"
                           placeholder="领导">
                  <el-option v-for="user in userList" :key="user.id" :label="user.realName"
                             :value="user.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="宿舍楼" prop="buildingId">
                <tree-select
                    v-model="user.buildingId"
                    :options="buildingList"
                    :normalizer="normalizer"
                    :show-count="true"
                    placeholder="选择宿舍楼">
                </tree-select>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button type="danger" @click="handleClose">取 消</el-button>
              <el-button type="primary" @click="saveOrUpdate"
                         v-has-permi="['system:user:update','system:user:save']">保 存</el-button>
            </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {del, list, query, resetPassword, saveOrUpdate, listAll} from "@/api/system/systemUser";
import {listInSelect} from "@/api/system/systemRole";
import {sendToUser} from "@/api/notice";
import {list as listBuilding, listAll as listAllBuilding} from '@/api/building'
import TreeSelect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "index",
  components: {TreeSelect},
  data() {
    let validator1 = (rule, value, callback) => {
      if (!value || value.length === 0) {
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
      if (!value || value.length === 0) {
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
      loading: true,
      pageInfo: {},
      msg: {
        msg: undefined,
        to: undefined
      },
      user: {
        id: undefined,
        userRoleId: [],
        email: undefined,
        cellphone: undefined,
        realName: undefined,
        loginName: undefined,
        leaderId: undefined,
        buildingId: undefined
      },
      listQuery: {
        entity: {},
        page: 1,
        rows: 10
      },
      roleList: undefined,
      userList: undefined,
      buildingList: undefined,
      roleListLoading: true,
      dialogVisible: false,
      dialogTitle: '添加用户',
      msgDialogVisible: false,
      rules: {
        loginName: [
          {required: true, message: '登录名不能为空', trigger: 'blur'},
          {min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur'}
        ],
        realName: [
          {required: true, message: '名字不能为空', trigger: 'blur'},
          {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        cellphone: [
          {validator: validator1, message: "请输入正确的手机号", trigger: 'blur'}
        ],
        email: [
          {validator: validator2, message: "请输入正确的邮箱", trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    sendMsg() {
      sendToUser(this.msg).then(res => {
        if (res.statusCode === 200) {
          this.$message({
            message: '发送成功',
            type: 'success'
          });
          this.msgClose()
        }
      })

    },
    confirmClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    msgOpen(id) {
      this.msg.to = id
      this.msgDialogVisible = true
    },
    msgClose() {
      this.msg =  {
        msg: undefined,
            to: undefined
      }
      this.msgDialogVisible = false
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children
      };
    },
    resetPassword(id) {
      resetPassword(id).then(res => {
        if (res.statusCode === 200) {
          this.$message({
            message: res.msg,
            type: 'success'
          });
        }
      })
    },
    async saveOrUpdate() {
      const _this = this
      if (this.user.buildingId === undefined) {
        this.$message({
          message: '请选择宿舍楼',
          type: 'error'
        });
        return
      }
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.user).then(res => {
            if (res.statusCode === 200) {
              this.$message({
                message: res.msg,
                type: 'success'
              });
              _this.handleClose()
              _this.listData()
            }
          })
        } else {
          this.$message({
            message: '请正确填写参数',
            type: 'error'
          });
          return false;
        }
      })
    },
    async showDialog(row) {
      this.dialogTitle = "添加用户"
      const rl = await listInSelect()
      this.roleList = rl.data
      const ul = await listAll()
      this.userList = ul.data
      const bl = await listBuilding()
      this.buildingList = bl.data
      if (row) {
        this.dialogTitle = "修改用户"
        const {data} = await query(row)
        if (data) {
          this.dialogVisible = true
          this.user = data
        }
      } else {
        this.dialogVisible = true
      }
    },
    handleClose() {
      this.user = {
        id: undefined,
        userRoleId: [],
        email: undefined,
        cellphone: undefined,
        realName: undefined,
        loginName: undefined
      }
      this.roleList = []
      this.buildingList = []
      this.userList = []
      this.$refs['saveOrUpdateForm'].resetFields()
      this.dialogVisible = false
    },
    del(id) {
      del(id).then(res => {
        if (res.statusCode === 200) {
          this.$message({
            message: res.msg,
            type: 'success'
          });
          this.listData()
        }
      })
    },
    reset() {
      this.listQuery.entity = {}
      this.select()
    },
    select() {
      this.listQuery.page = 1
      this.listData()
    },
    role(roles) {
      let res = ""
      for (let l of roles) {
        res += l.name + " "
      }
      return res
    },
    async listData() {
      this.loading = true
      const {data} = await list(this.listQuery)
      for (let u of data.list) {
        if (u.icon) {
          u.icon = process.env.VUE_APP_IMG_PREFIX + u.icon
        }
      }
      this.pageInfo = data
      window.scrollTo(0, 0);
      this.loading = false
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.listData()
    },
    handleSizeChange(val) {
      this.listQuery.rows = val
      this.listData()
    },
    async listRoleList(visible) {
      if (visible) {
        const {data} = await listInSelect();
        this.roleList = data
        this.roleListLoading = false
      } else {
        this.roleList = []
        this.roleListLoading = true
      }
    },
    async listUserList(visible) {
      if (visible) {
        const {data} = await listAll();
        this.userList = data
        this.roleListLoading = false
      } else {
        this.roleList = []
        this.roleListLoading = true
      }
    },
    async listBuildingList(visible) {
      if (visible) {
        const {data} = await listAllBuilding();
        this.buildingList = data
        this.roleListLoading = false
      } else {
        this.buildingList = []
        this.roleListLoading = true
      }
    }
  },
  created() {
    this.listData()
  }
}
</script>

<style scoped>
.table {
  padding: 10px;
}

.router-container {
  height: 100%;
}
</style>
