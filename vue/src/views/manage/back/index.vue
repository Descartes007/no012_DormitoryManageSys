<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form :inline="true" ref="form" label-width="70px" :model="listQuery.entity">
            <el-form-item label="学生姓名" v-has-permi="['student:list']">
              <el-select size="mini"
                  v-model="listQuery.entity.studentId"
                  filterable clearable
                  remote
                  placeholder="请输入关键词"
                  :remote-method="getStudentsFromRemote"
                  :loading="selectLoading">
                <el-option
                    v-for="item in students"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="mini" type="primary" @click="select">查询</el-button>
              <el-button size="mini" type="danger" @click="reset">重置</el-button>
              <el-button size="mini" type="primary" v-has-permi="['back:save']" @click="showDialog(undefined)">新增</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="pageInfo.list" v-loading="loading" height="405">
            <el-table-column label="学生姓名">
              <template slot-scope="scope">
                <el-popover
                    placement="bottom"
                    width="200"
                    trigger="click" @show="getStudent(scope.row.student.id)" @hide="hidePopover">
                  <el-form label-width="40px">
                    <el-form-item label="姓名">
                      <el-input v-model="student.name" readonly size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="学号">
                      <el-input v-model="student.number" readonly size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="电话">
                      <el-input v-model="student.phone" readonly size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="班级" v-if="student.faculty">
                      <el-input v-model="student.faculty.name" readonly size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="寝室" v-if="student.room">
                      <el-input v-model="student.room.number" readonly size="mini"></el-input>
                    </el-form-item>
                  </el-form>
                  <span slot="reference" style="color: #409EFF;cursor: pointer">{{scope.row.student.name}}</span>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column label="原因" prop="reason" width="450" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="时间" width="200">
              <template slot-scope="scope">
                <span v-if="scope.row.backDate !== undefined">{{scope.row.backDate}}</span>
                <span v-else style="color: red">未归</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="300">
              <template slot-scope="scope">
                <el-button type="primary" size="mini" v-has-permi="['back:query']" @click="showDialog(scope.row.id)">详情</el-button>
                <el-button type="danger" size="mini" v-has-permi="['back:delete']" @click="del(scope.row.id)">删除</el-button>
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
          <el-dialog :title="dialogTitle"
                     :visible.sync="dialogVisible"
                     width="45%"
                     @closed="handleClose">
            <el-form :model="record" ref="saveOrUpdateForm" label-width="80px" :rules="rules">
              <template v-if="record && record.id !== undefined">
                <el-form-item label="学生姓名">
                  <el-input v-model="record.student.name" readonly></el-input>
                </el-form-item>
              </template>
              <template v-else>
                <el-form-item label="学生姓名" prop="studentId">
                  <el-select v-model="record.studentId" remote filterable placeholder="输入关键字"
                             :remote-method="getStudentsFromRemote" :loading="selectLoading">
                    <el-option v-for="item in students" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </template>
              <el-form-item label="回寝时间">
                <el-date-picker value-format="yyyy-MM-dd HH:mm:ss"
                    v-model="record.backDate"
                    type="datetime"
                    placeholder="选择日期时间">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="原因" prop="reason">
                <el-input v-model="record.reason" type="textarea"
                          placeholder="输入原因" rows="10"></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                            <el-button type="danger" @click="handleClose">取 消</el-button>
                            <el-button type="primary" @click="saveOrUpdate" v-has-permi="['back:update','back:save']">保 存</el-button>
                          </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {list, query, del, saveOrUpdate} from '@/api/back'
import {listByName,query as queryStudent} from "@/api/student";

export default {
  name: "index",
  methods: {
    saveOrUpdate() {
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.record).then(res => {
            if (res.statusCode === 200) {
              this.$message({
                message: res.msg,
                type: 'success'
              });
              this.handleClose()
              this.listData()
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
    handleClose() {
      this.record = {
        id: undefined,
        studentId: undefined,
        reason: undefined,
        backDate: undefined
      }
      this.dialogVisible = false
      this.$refs['saveOrUpdateForm'].resetFields()
    },
    async showDialog(id) {
      this.dialogTitle = '添加'
      if (id) {
        this.dialogTitle = '修改'
        const {data} = await query(id)
        if (data) {
          this.record = data
          this.dialogVisible = true
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
    del(id) {
      this.$confirm("是否确定", "警告", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(id).then((res) => {
          if (res.statusCode === 200) {
            this.$message({
              type: 'success',
              message: res.msg
            });
            this.listData()
          }
        })
      })
    },
    async getStudentsFromRemote(query) {
      if (query !== '') {
        this.selectLoading = true
        const {data} = await listByName(query)
        this.students = data.map(item => {
          return {id: item.id, name:item.room.number + " " + item.name + ":" + item.number}
        })
        this.selectLoading = false
      } else {
        this.students = []
      }
    },
    reset() {
      this.students = []
      this.listQuery.entity = {}
      this.select()
    },
    select() {
      this.listQuery.page = 1
      this.listData()
    },
    handleSizeChange(val) {
      this.listQuery.rows = val
      this.listData()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.listData()
    },
    async getStudent(id) {
      const {data} = await queryStudent(id)
      this.student = data
    },
    hidePopover() {
      this.student = {}
    }
  },
  data() {
    return {
      listQuery:{
        page: 1,
        rows: 10,
        entity: {}
      },
      pageInfo: {},
      loading: true,
      students: [],
      student:{},
      record: {
        id: undefined,
        studentId: undefined,
        reason: undefined,
        backDate: undefined
      },
      selectLoading: true,
      dialogVisible: false,
      dialogTitle: '',
      rules :{
        studentId : [
          {required: true, message: '姓名不能为空', trigger: 'blur'}
        ],
        reason: [
          {required: true, message: '原因不能为空', trigger: 'blur'}
        ]
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
  padding: 30px;
}
</style>
