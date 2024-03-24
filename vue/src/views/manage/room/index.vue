<template>
  <div class="container">
    <div class="inner_container">
      <div class="table">
        <el-form :inline="true" ref="selectForm" label-width="70px" :model="listQuery.entity">
          <el-form-item label="宿舍号">
            <el-input v-model="listQuery.entity.number" clearable placeholder="输入宿舍号" size="small"></el-input>
          </el-form-item>
          <el-form-item label="宿舍楼" v-has-permi="['manage:building:list']">
            <el-select @visible-change="visibleChange"
                       v-model="listQuery.entity.buildingId"
                       placeholder="选择宿舍楼" size="small" clearable filterable :loading="selectLoading">
              <el-option
                  v-for="item in buildingList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="入住状态">
            <el-select v-model="listQuery.entity.isFull" clearable size="small">
              <el-option
                  label="未满"
                  :value="false">
              </el-option>
              <el-option
                  label="已满"
                  :value="true">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="mini" type="primary" @click="select">查询</el-button>
            <el-button size="mini" type="danger" @click="reset">重置</el-button>
            <el-button size="mini" type="primary" v-has-permi="['room:save']" @click="showDialog(undefined)">新增</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="pageInfo.list" v-loading="loading" height="410">
          <el-table-column label="序号">
            <template slot-scope="scope">{{ (listQuery.page - 1) * (listQuery.rows) + scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column prop="number" label="寝室号"></el-table-column>
          <el-table-column prop="building.name" label="所属宿舍楼"></el-table-column>
          <el-table-column prop="maxCapacity" label="最大人数"></el-table-column>
          <el-table-column prop="currentNum" label="现有人数"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" v-has-permi="['room:query']" @click="showDialog(scope.row.id)">详情</el-button>
              <template>
                <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                  <el-button style="margin-left: 8px" v-has-permi="['room:delete']"
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
            layout="-> ,prev, pager ,next, jumper, total, sizes"
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
            width="50%"
            @closed="handleClose">
          <el-form ref="saveOrUpdateForm" :rules="rules" :model="room" label-width="80px">
            <el-form-item label="宿舍号" prop="number">
              <el-input v-model="room.number"></el-input>
            </el-form-item>
            <el-form-item label="宿舍楼" prop="buildingId">
              <tree-select
                  v-model="room.buildingId"
                  :options="buildingTree"
                  :normalizer="normalizer"
                  :show-count="true"
                  placeholder="选择宿舍楼">
              </tree-select>
            </el-form-item>
            <el-form-item label="容量" prop="maxCapacity">
              <el-input-number v-model="room.maxCapacity"></el-input-number>
            </el-form-item>
          </el-form>
          <div v-if="studentList.length">
            <el-table :data="studentList">
              <el-table-column label="姓名" prop="name"></el-table-column>
              <el-table-column label="学号" prop="number"></el-table-column>
              <el-table-column label="班级" prop="faculty.name"></el-table-column>
              <el-table-column label="电话" prop="phone"></el-table-column>
              <el-table-column label="注册日期" prop="registrationDate"></el-table-column>
              <el-table-column label="是否在寝" width="100">
                <template slot-scope="scope">
                  <span style="color: #67C23A" v-if="!scope.row.isLeave">是</span>
                  <span style="color: #909399" v-else>否</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <span slot="footer" class="dialog-footer">
              <el-button type="danger" @click="handleClose">取 消</el-button>
              <el-button type="primary" @click="saveOrUpdate"
                         v-has-permi="['room:update','room:save']">保 存</el-button>
            </span>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import {list, query, saveOrUpdate, del} from '@/api/room'
import {list as listBuilding, listAll} from "@/api/building";
import {list as listStudent} from '@/api/student'
import TreeSelect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "index",
  components: {
    TreeSelect
  },
  methods:{
    handleClose() {
      this.room = {}
      this.studentList = []
      this.$refs['saveOrUpdateForm'].resetFields()
      this.dialogVisible = false
    },
    saveOrUpdate() {
      const _this = this
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.room).then(res => {
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
      await this.getBuildingTree()
      if (id) {
        const {data} = await query(id)
        if (data) {
          await this.getStudentList(id)
          this.room = data
          this.dialogVisible = true
        }
      } else {
        this.dialogVisible = true
      }
    },
    async getStudentList(id) {
      const {data} = await listStudent({
        entity: {roomId : id},
        rows: 10,
        page: 1
      })
      this.studentList = data.list
    },
    async getBuildingTree() {
      const {data} = await listBuilding()
      this.buildingTree = data
    },
    async visibleChange(val) {
      if (val) {
        this.selectLoading = true
        const {data} = await listAll()
        this.buildingList = data
        this.selectLoading = false
      } else {
        this.buildingList = []
      }
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
      this.listQuery.page = 1
      this.listQuery.entity = {}
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
  data() {
    const validatePass = (rule, value, callback) => {
      if (value < 1) {
        callback(new Error('容量需要大于1'));
      } else {
        callback();
      }
    };
    return {
      studentList:[],
      listQuery: {
        page: 1,
        rows: 10,
        entity: {}
      },
      pageInfo: {},
      loading: false,
      dialogVisible: false,
      dialogTitle: '新增宿舍',
      room: {
        id: undefined,
        number: undefined,
        buildingId: undefined,
        maxCapacity: undefined,
        isFull: undefined
      },
      buildingTree: [],
      buildingList: [],
      selectLoading: false,
      rules: {
        number: [
          {required: true, message:'宿舍号不能为空', trigger: 'blur'}
        ],
        buildingId: [
          {required: true, message: "请选择宿舍楼", trigger: 'blur'}
        ],
        maxCapacity: [
          {required: true, message: '容量不能为空', trigger: 'blur'},
          {validator:validatePass, message: '容量须大于1', trigger: 'blur'}
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
