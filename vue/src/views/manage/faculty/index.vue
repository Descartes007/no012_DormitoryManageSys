<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <splitpanes style="height: 550px">
          <pane min-size="20" size="30">
            <el-button v-has-permi="['faculty:save']" type="primary" size="small" style="margin-bottom: 5px" @click="showDialog">添加</el-button>
            <div style="width: 80%;">
              <el-tree @node-click="handleNodeClick"
                       :expand-on-click-node="false" :default-expanded-keys="expandKeys"
                       :data="list"
                       node-key="id" :render-content="renderContent"
                       :props="defaultProps">
              </el-tree>
            </div>
          </pane>
          <pane min-size="30">
            <div style="width: 70%;margin: 0 auto">
              <el-form v-if="selectNode" ref="form" :model="selectNode" :rules="rules" label-width="80px">
                <el-form-item label="上级菜单">
                  <tree-select
                      v-model="selectNode.parentId"
                      :options="list"
                      :normalizer="normalizer"
                      :show-count="true"
                      placeholder="选择上级菜单">
                  </tree-select>
                </el-form-item>
                <el-form-item label="学院名称" prop="name">
                  <el-input clearable v-model="selectNode.name"
                            placeholder="学院名称"></el-input>
                </el-form-item>
                <el-form-item label="排序序号" prop="orderNum">
                  <el-input-number v-model="selectNode.orderNum"
                            placeholder="排序序号"></el-input-number>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="small" v-has-permi="['faculty:update']" @click="saveOrUpdate(0)">修改</el-button>
                  <el-button type="warning" size="small" @click="handleNodeClick(selectNode)">重置</el-button>
                  <el-button type="danger" size="small" v-has-permi="['faculty:delete']" @click="del(selectNode.id)">删除</el-button>
                  <el-button type="primary" size="small" v-has-permi="['student:list']" @click="showDialog1(selectNode.id)">查看学生</el-button>
                </el-form-item>
              </el-form>
            </div>
          </pane>
        </splitpanes>
        <div>
          <el-dialog
              title="添加学院"
              :visible.sync="dialogVisible"
              width="40%"
              @closed="handleClose"
          >
            <el-form ref="form" :model="faculty" :rules="rules" label-width="80px">
              <el-form-item label="上级学院">
                <tree-select
                    v-model="faculty.parentId"
                    :options="list"
                    :normalizer="normalizer"
                    :show-count="true"
                    placeholder="选择上级学院">
                </tree-select>
              </el-form-item>
              <el-form-item label="学院名称" prop="name">
                <el-input clearable v-model="faculty.name"
                          placeholder="学院名称"></el-input>
              </el-form-item>
              <el-form-item label="排序序号" prop="orderNum">
                <el-input-number v-model="faculty.orderNum"
                                 placeholder="排序序号"></el-input-number>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" v-has-permi="['faculty:save']" @click="saveOrUpdate(1)">添加</el-button>
              </el-form-item>
            </el-form>
          </el-dialog>
          <el-dialog title="学生"
                     :visible.sync="dialogVisible1"
                     width="65%"
                     @closed="handleClose1">
            <el-table :data="pageInfo.list" height="430">
              <el-table-column label="姓名" prop="name" width="100"></el-table-column>
              <el-table-column label="学号" prop="number" width="160"></el-table-column>
              <el-table-column label="班级" prop="faculty.name" width="160"></el-table-column>
              <el-table-column label="宿舍" prop="room.number" width="120"></el-table-column>
              <el-table-column label="电话" prop="phone" width="150"></el-table-column>
              <el-table-column label="注册日期" prop="registrationDate" width="120"></el-table-column>
              <el-table-column label="是否在寝" width="100">
                <template slot-scope="scope">
                  <span style="color: #67C23A" v-if="!scope.row.isLeave">是</span>
                  <span style="color: #909399" v-else>否</span>
                </template>
              </el-table-column>
            </el-table>
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
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Pane, Splitpanes} from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
import {list, query, saveOrUpdate, del} from "@/api/faculty";
import {list as listStudent} from '@/api/student'
import TreeSelect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "index",
  components: { Splitpanes, Pane, TreeSelect },
  data() {
    return {
      dialogVisible1: false,
      pageInfo: [],
      listQuery: {
        entity:{},
        rows:10,
        page:1
      },
      list:[],
      selectNode: undefined,
      faculty:{},
      expandKeys: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      dialogVisible: false,
      rules:{
        name: {require: true,message: '名称不能为空', trigger: 'blur'}
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.listQuery.rows = val
      this.listStudent()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.listStudent()
    },
    async listStudent(id) {
      this.listQuery.entity.facultyId = id
      const {data} = await listStudent(this.listQuery)
      this.pageInfo = data
    },
    showDialog1(id) {
      this.listStudent(id)
      this.dialogVisible1 = true
    },
    handleClose1() {
      this.dialogVisible1 = false
      this.studentList = []
      this.listQuery = {
        entity:{},
        rows:10,
        page:1
      }
    },
    async listData() {
      const {data} = await list()
      this.list = data
      for (let i of this.list) {
        this.expandKeys.push(i.id)
      }
    },
    async handleNodeClick(node) {
      const {data} = await query(node.id)
      this.selectNode = data
    },
    showDialog() {
      this.dialogVisible = true
    },
    handleClose() {
      this.faculty = {}
      this.selectNode = undefined
      this.$refs['form'].resetFields()
      this.dialogVisible = false
    },
    del(id) {
      this.$confirm("是否确定删除改节点以及子节点", "警告", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(id).then((res) => {
          if (res.statusCode === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.selectNode = undefined
            this.listData()
          }
        })
      })
    },
    saveOrUpdate(f) {
      const _this = this
      let data = this.selectNode
      if (f) {
        data = this.faculty
      }
      this.$refs['form'].validate((valid) => {
        if (valid) {
          saveOrUpdate(data).then(res => {
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
    renderContent(h, { node, data }) {
      return (
          <span class="custom-tree-node">
            <span>{node.label}</span>
            <span style="font-size:12px;color:#C5C5C5">
              {data.studentNum}
            </span>
          </span>);
    }
  },
  created() {
    this.listData()
  }
}
</script>

<style>
.splitpanes__splitter {
  width: 6px;
  background: #F5F5F5;
  position: relative;
}
.splitpanes__splitter:before,  .splitpanes__splitter:after{
  background-color: rgba(0,0,0,.15);
  content: '';
  width: 1px;
  height: 30px;
  float: left;
  position: absolute;
  display: block;
  top: 50%;
  left: 50%;
  margin-top: -15px;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.splitpanes__splitter:after {
  margin-left: 1px;
}
.splitpanes__splitter:before {
  margin-left: -2px;
}
.page {
  margin-top: 5px;
}
</style>
