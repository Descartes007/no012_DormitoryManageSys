<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form size="mini">
            <el-form-item>
              <el-button v-has-permi="['manage:building:save']" @click="showDialog(undefined)" size="mini" type="primary">
                新增
              </el-button>
            </el-form-item>
          </el-form>

          <el-table :expand-row-keys="defaultExpand" :data="list" v-loading="loading" :tree-props="{children: 'children'}" row-key="id">
            <el-table-column label="名称" prop="name"></el-table-column>
            <el-table-column label="宿舍数量" prop="roomNum"></el-table-column>
            <el-table-column label="人数" prop="studentNum"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button v-has-permi="['manage:building:query']"
                           size="mini" type="primary" @click="showDialog(scope.row.id)">详情
                </el-button>
                <template>
                  <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                    <el-button style="margin-left: 8px" v-has-permi="['manage:building:delete']"
                               slot="reference"
                               size="mini" type="danger">删除
                    </el-button>
                  </el-popconfirm>
                </template>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="dialog">
          <el-dialog
              :title="dialogTitle"
              :visible.sync="dialogVisible"
              width="45%"
              @closed="handleClose"
          >
            <el-form ref="saveOrUpdateForm" :rules="rules" :model="building" label-width="80px">
              <el-form-item label="上级">
                <tree-select
                    v-model="building.parentId"
                    :options="list"
                    :normalizer="normalizer"
                    :show-count="true"
                    placeholder="选择上级">
                </tree-select>
              </el-form-item>
              <el-form-item label="名称" prop="name">
                <el-input v-model="building.name" placeholder="输入名称"></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button type="danger" @click="handleClose">取 消</el-button>
              <el-button type="primary" v-has-permi="['manage:building:update']" @click="saveOrUpdate">保 存</el-button>
            </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {list, query, del, saveOrUpdate} from "@/api/building";
import TreeSelect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "index",
  components:{TreeSelect},
  data() {
    return {
      list:[],
      building:{
        id: undefined,
        name: undefined
      },
      loading: undefined,
      rules: {
        name: {required: true, message: '名称不能为空', trigger: 'blur'}
      },
      defaultExpand: [],
      dialogVisible:false,
      dialogTitle: '添加'
    }
  },
  methods: {
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
    saveOrUpdate() {
      const _this = this
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.building).then(res => {
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
    async showDialog(id) {
      this.dialogTitle = '添加楼'
      if (id) {
        this.dialogTitle = '修改楼'
        const {data} = await query(id)
        if (data) {
          this.building = data
          this.dialogVisible = true
        }
      } else {
        this.dialogVisible = true
      }
    },
    handleClose() {
      this.building = {
        id: undefined,
        name: undefined
      }
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
    async listData() {
      this.loading = true
      const {data} = await list()
      this.list = data
      this.loading = false
      for (let i of this.list) {
        this.defaultExpand.push(i.id.toString())
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
