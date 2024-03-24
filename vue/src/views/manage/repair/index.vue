<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form :inline="true" ref="form" :model="listQuery.entity">
            <el-form-item label="寝室" v-has-permi="['room:list']">
              <el-select filterable clearable v-model="listQuery.entity.roomId" size="small"
                         placeholder="寝室" @visible-change="getRoomList">
                <el-option v-for="item in roomList" :key="item.id" :label="item.number" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="维修状态">
              <el-select clearable v-model="listQuery.entity.status" size="small"
                         placeholder="维修状态">
                <el-option label="完成" :value="true"></el-option>
                <el-option label="未完成" :value="false"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
              <el-date-picker
                  value-format="yyyy-MM-dd"
                  v-model="listQuery.entity.createDate"
                  type="date" size="small"
                  placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button size="mini" type="primary" @click="select">查询</el-button>
              <el-button size="mini" type="danger" @click="reset">重置</el-button>
              <el-button size="mini" type="primary" v-has-permi="['repair:save']" @click="showDialog(undefined)">新增</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="pageInfo.list" v-loading="loading" height="405">
            <el-table-column prop="room.number" label="寝室号" width="150"></el-table-column>
            <el-table-column prop="describe" :show-tooltip-when-overflow="true" label="描述" width="200"></el-table-column>
            <el-table-column label="创建时间" prop="createDate"></el-table-column>
            <el-table-column label="状态">
              <template slot-scope="scope">
                <span style="color: #67C23A" v-if="scope.row.status">已完成</span>
                <span style="color: #909399" v-else>未完成</span>
              </template>
            </el-table-column>
            <el-table-column label="图片">
              <template slot-scope="scope">
                <span v-if="scope.row.picture.length === 0">无</span>
                <span v-else>
                  <el-button type="text" @click="viewPic(scope.row.picture)">查看图片</el-button>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="完成时间" prop="finishDate"></el-table-column>
            <el-table-column label="操作" width="250">
              <template slot-scope="scope">
                <el-button type="primary" size="mini" v-has-permi="['repair:query']" @click="showDialog(scope.row.id)">详情</el-button>
                <el-button type="danger" size="mini" v-has-permi="['repair:delete']" @click="del(scope.row.id)">删除</el-button>
                <el-button type="primary" size="mini"
                           v-has-permi="['repair:update']"
                           @click="updateStatus(scope.row.id)"
                           :disabled="scope.row.status"
                >确定完成</el-button>
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
                     width="45%"
                     :visible.sync="dialogVisible"
                     @closed="handleClose">

            <el-form :model="record" ref="saveOrUpdateForm" label-width="80px" :rules="rules">
              <el-form-item label="寝室号" prop="roomId">
                <el-select filterable clearable v-model="record.roomId" size="small"
                           placeholder="寝室" @visible-change="getRoomList">
                  <el-option v-for="item in roomList" :key="item.id" :label="item.number" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="描述" prop="describe">
                <el-input type="textarea" v-model="record.describe" rows="5" placeholder="输入描述内容"></el-input>
              </el-form-item>
              <el-form-item label="图片">
                <images-upload v-if="!destroy" :images="record.picture" @onChange="onPictureChange"></images-upload>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                            <el-button type="danger" @click="handleClose">取 消</el-button>
                            <el-button type="primary" @click="saveOrUpdate" :disabled="record.status">保 存</el-button>
                          </span>
          </el-dialog>


          <el-dialog title="查看图片" :visible.sync="pictureVisible" width="40%" @closed="handlePictureDialogClose">
            <el-image v-for="p in pictureList" :key="p"
                style="width: 100px; height: 100px; margin-left: 5px"
                :src="p"
                :preview-src-list="pictureList">
            </el-image>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {list, query, saveOrUpdate, del, updateStatus} from "@/api/repair";
import {listAll as listRooms} from "@/api/room";
import imagesUpload from "./components/imagesUpload";
export default {
  name: "index",
  components: {
    imagesUpload
  },
  methods: {
    confirm(hook) {
      this.$confirm("是否确定", "警告", {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(hook)
    },
    updateStatus(id) {
      this.confirm(() => {
        updateStatus(id).then((res) => {
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
    del(id) {
      this.confirm(() => {
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
    saveOrUpdate() {
      const _this = this
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.record).then(res => {
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
    onPictureChange(images) {
      this.record.picture = images
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
      this.destroy = false
      await this.getRoomList(true)
    },
    handleClose() {
      this.dialogVisible = false
      this.destroy = true
      this.roomList = []
      this.record = {
        id: undefined,
        roomId: undefined,
        room: undefined,
        describe: undefined,
        picture: undefined,
        status: undefined,
        createDate: undefined,
        finishDate: undefined
      }
      this.$refs['saveOrUpdateForm'].resetFields()
    },
    async listData() {
      this.loading = true
      const {data} = await list(this.listQuery)
      this.pageInfo = data
      this.loading = false
    },
    async getRoomList(v) {
      if (v) {
        const {data} = await listRooms()
        this.roomList = data
      } else {
        this.roomList = []
      }
    },
    select() {
      this.listQuery.page = 1
      this.listData()
    },
    reset() {
      this.roomList = []
      this.listQuery.entity = {}
      this.select()
    },
    viewPic(pic) {
      this.pictureList = []
      for (let p of pic) {
        this.pictureList.push(process.env.VUE_APP_IMG_PREFIX + p)
      }
      this.pictureVisible = true
    },
    handlePictureDialogClose() {
      this.pictureList = []
      this.pictureVisible = false
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.listData()
    },
    handleSizeChange(val) {
      this.listQuery.rows = val
      this.listData()
    },
  },
  data () {
    return {
      listQuery:{
        page: 1,
        rows: 10,
        entity: {}
      },
      pageInfo: {},
      loading: true,
      dialogVisible: false,
      dialogTitle: '',
      record:{
        id: undefined,
        roomId: undefined,
        room: undefined,
        describe: undefined,
        picture: undefined,
        status: undefined,
        createDate: undefined,
        finishDate: undefined
      },
      roomList: [],
      pictureList: [],
      pictureVisible: false,
      destroy: true, //销毁组件
      rules: {
        roomId: [
          {required: true, message: '寝室不能为空', trigger: 'blur'}
        ],
        describe: [
          {required: true, message: '描述不能为空', trigger: 'blur'}
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
