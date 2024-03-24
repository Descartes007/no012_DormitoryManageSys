<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form :inline="true" ref="form" :model="listQuery.entity">
            <el-form-item label="角色名称">
              <el-input clearable v-model="listQuery.entity.name" size="small"
                        placeholder="角色名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="select">查询</el-button>
              <el-button type="danger" size="small" @click="reset">重置</el-button>
              <el-button v-has-permi="['system:role:save']" @click="showDialog(undefined)"
                         size="small" type="primary">新增
              </el-button>
            </el-form-item>
          </el-form>
          <el-table :data="pageInfo.list" v-loading="loading" height="410">
            <el-table-column label="序号">
              <template slot-scope="scope">{{ (listQuery.page - 1) * (listQuery.rows) + scope.$index + 1 }}</template>
            </el-table-column>
            <el-table-column label="名称" prop="name"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button v-has-permi="['system:role:query']"
                           size="mini" type="primary" @click="showDialog(scope.row.id)">详情
                </el-button>
                <template>
                  <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                    <el-button style="margin-left: 8px" v-has-permi="['system:role:delete']"
                               slot="reference"
                               size="mini" type="danger">删除
                    </el-button>
                  </el-popconfirm>
                </template>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="page">
          <el-pagination
              background
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
          <el-dialog
              :title="dialogTitle"
              :visible.sync="dialogVisible"
              width="45%"
              @closed="handleClose"
          >
            <el-form ref="saveOrUpdateForm" :rules="rules" :model="role" label-width="80px">
              <el-form-item label="名称" prop="name">
                <el-input v-model="role.name" placeholder="输入名称"></el-input>
              </el-form-item>
              <el-form-item label="菜单" prop="functionIds">
                <my-tree :tree="listTree" :checked="role.functionIds"></my-tree>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                            <el-button type="danger" @click="handleClose">取 消</el-button>
                            <el-button type="primary" @click="saveOrUpdate"
                                       v-has-permi="['system:role:update','system:role:save']">保 存</el-button>
                          </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {del, list, query, saveOrUpdate} from "@/api/system/systemRole";
import {listTree} from "../../../api/system/SystemFunction";
import MyTree from '@/components/MyTree'

export default {
  name: "index",
  components: {
    MyTree
  },
  data() {
    return {
      loading: true,
      pageInfo: {},
      listQuery: {
        entity: {},
        page: 1,
        rows: 10
      },
      role: {
        id: undefined,
        name: '',
        functionIds: []
      },
      listTree: [],
      dialogTitle: '',
      dialogVisible: false,
      rules: {
        name: {required: true, message: '名称不能为空', trigger: 'blur'}
      }
    }
  },
  methods: {
    saveOrUpdate() {
      const _this = this
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.role).then(res => {
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
    select() {
      this.listQuery.page = 1
      this.listData()
    },
    reset() {
      this.listQuery.entity = {}
      this.select()
    },
    handleClose() {
      this.role = {
        id: undefined,
        name: '',
        functionIds: []
      }
      this.listTree = []
      this.$refs['saveOrUpdateForm'].resetFields()
      this.dialogVisible = false
    },
    async showDialog(id) {
      this.dialogTitle = "添加角色"
      const {data} = await listTree();
      this.listTree = data
      if (id) {
        this.dialogTitle = "修改角色"
        const {data} = await query(id)
        if (data) {
          this.dialogVisible = true
          this.role = data
        }
      } else {
        this.dialogVisible = true
      }
    },
    async listData() {
      this.loading = true
      const {data} = await list(this.listQuery)
      this.pageInfo = data
      this.loading = false
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.listData()
    },
    handleSizeChange(val) {
      this.listQuery.rows = val
      this.listData()
    }
  },
  created() {
    this.listData()
  }
}
</script>

<style scoped>
.table {
  padding: 30px;
}

</style>
