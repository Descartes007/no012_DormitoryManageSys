<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form :inline="true" ref="form" label-width="70px" :model="listQuery.entity">
            <el-form-item label="姓名">
              <el-input clearable v-model="listQuery.entity.name" size="small"
                        placeholder="姓名"></el-input>
            </el-form-item>
            <el-form-item label="学号">
              <el-input clearable v-model="listQuery.entity.number" size="small"
                        placeholder="学号"></el-input>
            </el-form-item>
            <el-form-item label="班级" v-has-permi="['faculty:list']">
              <el-select filterable clearable v-model="listQuery.entity.facultyId" size="small"
                         placeholder="班级" @visible-change="getFacultyList">
                <el-option v-for="item in facultyList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="寝室" v-has-permi="['room:list']">
              <el-select filterable clearable v-model="listQuery.entity.roomId" size="small"
                         placeholder="寝室" @visible-change="getRoomList">
                <el-option v-for="item in roomList" :key="item.id" :label="item.number" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="在寝">
              <el-select filterable clearable v-model="listQuery.entity.isLeave" size="small">
                <el-option label="在寝" :value="false"></el-option>
                <el-option label="不在寝" :value="true"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="mini" type="primary" @click="select">查询</el-button>
              <el-button size="mini" type="danger" @click="reset">重置</el-button>
              <el-button size="mini" type="primary" v-has-permi="['student:save']" @click="showDialog(undefined)">新增</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="pageInfo.list" v-loading="loading" height="345">
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
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button type="primary" size="mini" v-has-permi="['student:query']" @click="showDialog(scope.row.id)">详情</el-button>
                <el-button type="danger" size="mini" v-has-permi="['student:delete']" @click="applicationDialogOpen(scope.row.id, scope.row.name)">申请退宿</el-button>
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
            <el-form ref="saveOrUpdateForm" :rules="rules" :model="student" label-width="80px">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="student.name" placeholder="输入姓名"></el-input>
              </el-form-item>
              <el-form-item label="学号" prop="number">
                <el-input v-model="student.number" placeholder="输入学号"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="student.phone" placeholder="输入电话"></el-input>
              </el-form-item>
              <el-form-item label="寝室" prop="roomId">
                <el-select filterable v-model="student.roomId" style="width: 100%"
                           placeholder="选择寝室">
                  <el-option v-for="item in roomList" :key="item.id" :label="item.number" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="班级" prop="facultyId">
                <tree-select
                    v-model="student.facultyId"
                    :options="facultyTree"
                    :normalizer="normalizer"
                    :show-count="true"
                    placeholder="选择班级">
                </tree-select>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                            <el-button type="danger" @click="handleClose">取 消</el-button>
                            <el-button type="primary" @click="saveOrUpdate"
                                       v-has-permi="['student:update','student:save']">保 存</el-button>
                          </span>
          </el-dialog>
          <el-dialog title="退宿申请" :visible.sync="applicationDialogVisible" width="45%" @closed="applicationDialogClose">
            <el-form ref="application" :model="application" label-width="80px">
              <el-form-item label="学生姓名">
                <el-input readonly v-model="application.studentName"></el-input>
              </el-form-item>
              <el-form-item label="申请理由" prop="reason">
                <el-input v-model="application.reason" type="textarea"
                          placeholder="请输入申请理由" rows="10"></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                            <el-button type="danger" @click="applicationDialogClose">取 消</el-button>
                            <el-button type="primary" @click="del">保 存</el-button>
                          </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {list, query, del, saveOrUpdate} from '@/api/student'
import {list as listFaculty, listAll} from "@/api/faculty";
import {listAll as listRooms} from "@/api/room";
import TreeSelect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "index",
  components:{TreeSelect},
  data() {
    return {
      listQuery: {
        entity: {},
        rows: 10,
        page: 1
      },
      student: {
        id: undefined,
        name: undefined,
        roomId: undefined,
        facultyId: undefined,
        number: undefined,
        registrationDate: undefined,
        phone: undefined,
        photo: undefined,
        isLeave: undefined
      },
      pageInfo: {},
      loading: true,
      roomList: [],
      facultyList: [],
      facultyTree: [],
      dialogVisible: false,
      applicationDialogVisible: false,
      dialogTitle: '新增学生',
      rules: {
        name: [
          {required: true, message: '姓名不能为空', trigger: 'blur'}
        ],
        number: [
          {required: true, message: '学号不能为空', trigger: 'blur'}
        ],
        roomId: [
          {required: true, message: '寝室不能为空', trigger: 'blur'}
        ]
      },
      application: {
        studentId: undefined,
        studentName: undefined,
        reason: undefined
      }
    }
  },
  methods: {
    applicationDialogOpen(id, name) {
      this.application.studentId = id
      this.application.studentName = name
      this.applicationDialogVisible = true
    },
    applicationDialogClose() {
      this.application = {
        studentId: undefined,
        studentName: undefined,
        reason: undefined
      }
      this.applicationDialogVisible = false
    },
    del() {
      this.$confirm("是否确定", "警告", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        del(this.application).then((res) => {
          if (res.statusCode === 200) {
            this.$message({
              type: 'success',
              message: res.msg
            });
            this.applicationDialogClose()
            this.listData()
          }
        })
      })
    },
    async showDialog(id) {
      this.dialogTitle = "添加学生"
      let facultyTree = await listFaculty()
      this.facultyTree = facultyTree.data
      await this.getRoomList(1)
      if (id) {
        const {data} = await query(id)
        if (data) {
          this.dialogTitle = "修改学生"
          this.student = data
          this.dialogVisible = true
        }
      } else {
        this.dialogVisible = true
      }
    },
    async getRoomList(v) {
      if (v) {
        const {data} = await listRooms()
        this.roomList = data
      } else {
        this.roomList = []
      }
    },
    async getFacultyList(v) {
      if (v) {
        const {data} = await listAll()
        this.facultyList = data
      } else {
        this.facultyList = []
      }
    },
    handleClose() {
      this.student = {
        id: undefined,
        name: undefined,
        roomId: undefined,
        facultyId: undefined,
        number: undefined,
        registrationDate: undefined,
        phone: undefined,
        photo: undefined
      }
      this.roomList = []
      this.facultyTree = []
      this.dialogVisible = false
      this.$refs['saveOrUpdateForm'].resetFields()
    },
    saveOrUpdate() {
      const _this = this
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.student).then(res => {
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
    reset() {
      this.listQuery.entity = {}
      this.select()
    },
    select() {
      this.listQuery.page = 1
      this.listData()
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
