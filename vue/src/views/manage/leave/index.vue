<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form :inline="true" ref="form" label-width="70px" :model="listQuery.entity">
            <el-form-item label="学生姓名" v-has-permi="['student:list']">
              <el-select size="mini"
                         v-model="listQuery.entity.studentId"
                         filterable
                         clearable
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
            <el-form-item label="是否返校">
              <el-select size="mini" clearable v-model="listQuery.entity.isBack">
                <el-option
                    label="已返校"
                    :value="true">
                </el-option>
                <el-option
                    label="未返校"
                    :value="false">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="mini" type="primary" @click="select">查询</el-button>
              <el-button size="mini" type="danger" @click="reset">重置</el-button>
              <el-button size="mini" type="primary" v-has-permi="['leave:save']" @click="showDialog(undefined)">新增</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="pageInfo.list" v-loading="loading" height="405">
            <el-table-column label="学生姓名" width="150">
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
            <el-table-column prop="time" label="签离时间" width="250"></el-table-column>
            <el-table-column label="原因" width="200" prop="reason" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="target" label="目的地"></el-table-column>
            <el-table-column prop="backDate" label="返校时间"></el-table-column>
            <el-table-column label="是否返校">
              <template slot-scope="scope">
                <span style="color: #67C23A" v-if="scope.row.isBack">是</span>
                <span style="color: #909399" v-else>否
                  <span v-if="new Date() > new Date(scope.row.backDate + ' 23:59:59')" style="color: #F56C6C">(已超时)</span>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="250">
              <template slot-scope="scope">
                <el-button type="primary" size="mini" v-has-permi="['leave:query']" @click="showDialog(scope.row.id)">详情</el-button>
                <el-button type="danger" size="mini" v-has-permi="['leave:delete']" @click="del(scope.row.id)">删除</el-button>
                <el-button type="primary" size="mini"
                           v-has-permi="['leave:update']"
                           @click="update(scope.row.id)"
                           :disabled="scope.row.isBack"
                >确定返校</el-button>
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
              <template v-if="record && record.id !== undefined">
                <el-form-item label="签离时间">
                  <el-date-picker
                      v-model="record.time"
                      type="datetime"
                      placeholder="选择日期时间" readonly>
                  </el-date-picker>
                </el-form-item>
              </template>
              <el-form-item label="返校时间" prop="backDate">
                <el-date-picker
                    v-model="record.backDate"
                    type="date" value-format="yyyy-MM-dd"
                    placeholder="选择日期" :readonly="record && record.id !== undefined">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="目的地" prop="target">
                <el-input  v-model="record.target" placeholder="输入目的地"
                           :readonly="record && record.id !== undefined"></el-input>
              </el-form-item>
              <el-form-item label="原因" prop="reason">
                <el-input type="textarea"
                          :rows="5" v-model="record.reason" placeholder="输入原因"
                          :readonly="record && record.id !== undefined"></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                            <el-button type="danger" @click="handleClose">取 消</el-button>
                            <el-button v-if="!(record && record.id !== undefined)" type="primary" @click="save" v-has-permi="['back:save']">保 存</el-button>
                          </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {list, save, update, del, query} from "@/api/leave";
import {listByName,query as queryStudent} from "@/api/student";
export default {
  name: "index",
  methods:{
    update(id) {
      this.$confirm("是否确定", "警告", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        update(id).then((res) => {
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
    save() {
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          save(this.record).then(res => {
            if (res.statusCode === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              this.listData()
              this.handleClose()
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
    async showDialog(id) {
      this.dialogTitle = '添加'
      if (id) {
        const {data} = await query(id)
        this.dialogTitle = '详情'
        if (data) {
          this.record = data
          this.dialogVisible = true
        }
      } else {
        this.dialogVisible = true
      }

    },
    handleClose() {
      this.dialogVisible = false
      this.students = []
      this.record = {
        id: undefined,
        time:undefined,
        reason: undefined,
        isBack:undefined,
        studentId: undefined,
        target: undefined,
        backDate: undefined
      }
      this.$refs['saveOrUpdateForm'].resetFields()
    },
    async getStudent(id) {
      const {data} = await queryStudent(id)
      this.student = data
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
    async listData() {
      this.loading = true
      const {data} = await list(this.listQuery)
      this.pageInfo = data
      this.loading = false
    },
    handleSizeChange(val) {
      this.listQuery.rows = val
      this.listData()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.listData()
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
      selectLoading:true,
      students: [],
      student: {},
      record: {
        id: undefined,
        time:undefined,
        reason: undefined,
        isBack:undefined,
        studentId: undefined,
        target: undefined,
        backDate: undefined
      },
      dialogVisible: false,
      dialogTitle: '',
      rules:{
        studentId : [
          {required: true, message: '姓名不能为空', trigger: 'change'}
        ],
        reason: [
          {required: true, message: '原因不能为空', trigger: 'blur'}
        ],
        target: [
          {required: true, message: '目的不能为空', trigger: 'blur'}
        ],
        backDate: [
          {required: true, message: '返校日期不能为空', trigger: 'blur'}
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
