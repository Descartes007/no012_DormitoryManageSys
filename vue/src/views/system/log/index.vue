<template>
  <div class="router-container">
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form :inline="true" ref="form" label-width="70px" :model="listQuery.entity">
            <el-form-item label="类名">
              <el-input clearable v-model="listQuery.entity.clas" size="small"
                        placeholder="类名"></el-input>
            </el-form-item>
            <el-form-item label="方法名">
              <el-input clearable v-model="listQuery.entity.method" size="small"
                        placeholder="方法名"></el-input>
            </el-form-item>
            <el-form-item label="ip">
              <el-input clearable v-model="listQuery.entity.ip" size="small"
                        placeholder="ip"></el-input>
            </el-form-item>
            <el-form-item label="操作用户" v-has-permi="['system:user:list']">
              <el-select v-model="listQuery.entity.userId" filterable
                         size="small" placeholder="操作用户" @visible-change="getUserList">
                <el-option v-for="user in userList"
                           :key="user.id"
                           :label="user.realName"
                           :value="user.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="url">
              <el-input clearable v-model="listQuery.entity.url" size="small"
                        placeholder="url"></el-input>
            </el-form-item>
            <el-form-item label="操作时间">
              <el-date-picker
                  v-model="listQuery.entity.operateTimeStart"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  type="datetime" size="small"
                  placeholder="选择开始时间">
              </el-date-picker>
              -
              <el-date-picker
                  v-model="listQuery.entity.operateTimeEnd"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  type="datetime" size="small"
                  placeholder="选择结束时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="small" @click="select">查询</el-button>
              <el-button type="danger" size="small" @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="pageInfo.list" v-loading="loading" height="348">
            <el-table-column label="操作用户" width="200">
              <template slot-scope="scope">
                <el-popover
                    placement="top-start"
                    width="500"
                    trigger="hover">
                  <el-form ref="form" inline :model="scope.row.systemUser" label-width="50px">
                    <el-form-item label="名称 : ">
                      {{ scope.row.systemUser.realName }}
                    </el-form-item>
                    <el-form-item label="电话 : ">
                      {{ scope.row.systemUser.cellphone }}
                    </el-form-item>
                    <el-form-item label="邮箱 : ">
                      {{ scope.row.systemUser.email }}
                    </el-form-item>
                  </el-form>
                  <span slot="reference">
                    {{ scope.row.systemUser.realName }}
                  </span>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column label="url" prop="url" width="340"></el-table-column>
            <el-table-column label="ip" prop="ip" width="200"></el-table-column>
            <el-table-column label="操作时间" prop="operateTime" width="250"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button v-has-permi="['system:log:query']"
                           size="mini" type="primary" @click="showDialog(scope.row.id)">查看
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
          <el-dialog
              title="详情"
              :visible.sync="dialogVisible"
              width="50%"
              :before-close="handleClose">
            <el-form ref="form" :model="log" label-width="100px">
              <el-row :gutter="20">
                <el-col :span="span">
                  <el-form-item label="操作用户 :">
                    <el-popover
                        placement="top-start"
                        width="500"
                        trigger="hover">
                      <el-form ref="form" inline :model="log.systemUser" label-width="50px">
                        <el-form-item label="名称 : ">
                          {{ log.systemUser.realName }}
                        </el-form-item>
                        <el-form-item label="电话 : ">
                          {{ log.systemUser.cellphone }}
                        </el-form-item>
                        <el-form-item label="邮箱 : ">
                          {{ log.systemUser.email }}
                        </el-form-item>
                      </el-form>
                      <span slot="reference">
                        {{ log.systemUser.realName }}
                      </span>
                    </el-popover>
                  </el-form-item>
                </el-col>
                <el-col :span="span">
                  <el-form-item label="类名 :">
                    {{ log.clas }}
                  </el-form-item>
                </el-col>
                <el-col :span="span">
                  <el-form-item label="方法名 :">
                    {{ log.method }}
                  </el-form-item>
                </el-col>
                <el-col :span="span">
                  <el-form-item label="ip :">
                    {{ log.ip }}
                  </el-form-item>
                </el-col>
                <el-col :span="span">
                  <el-form-item label="url :">
                    {{ log.url }}
                  </el-form-item>
                </el-col>
                <el-col :span="span">
                  <el-form-item label="操作时间 :">
                    {{ log.operateTime }}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="描述 :">
                {{ log.description }}
              </el-form-item>
              <el-form-item label="参数 :">
                <json-viewer
                    :value="log.param"
                    boxed copyable
                    :expand-depth="2"
                    :expanded="false">
                </json-viewer>
              </el-form-item>
              <el-form-item label="返回值">
                <json-viewer
                    :value="log.result"
                    boxed copyable
                    :expand-depth="2"
                    :expanded="false">
                </json-viewer>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button type="primary" size="small" @click="dialogVisible = false">关 闭</el-button>
            </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {list, query} from "@/api/system/SystemLog";
import {listAll} from "@/api/system/systemUser";
import JsonViewer from 'vue-json-viewer'

export default {
  name: "index",
  components: {JsonViewer},
  data() {
    return {
      span: 12,
      pageInfo: {},
      listQuery: {
        entity: {},
        page: 1,
        rows: 10
      },
      loading: true,
      userList: [],
      dialogVisible: false,
      log: {
        id: undefined,
        userId: undefined,
        systemUser: {},
        clas: undefined,
        method: undefined,
        ip: undefined,
        operateTime: undefined,
        url: undefined,
        description: undefined,
        param: undefined
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.log = {
        id: undefined,
        userId: undefined,
        systemUser: {},
        clas: undefined,
        method: undefined,
        ip: undefined,
        operateTime: undefined,
        url: undefined,
        description: undefined,
        param: undefined
      }
    },
    async showDialog(id) {
      const {data} = await query(id)
      this.log = data
      this.log.param = JSON.parse(this.log.param)
      this.log.result = JSON.parse(this.log.result || "{}")
      this.dialogVisible = true
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
    },
    reset() {
      this.listQuery.entity = {}
      this.select()
    },
    select() {
      this.listQuery.page = 1
      this.listData()
    },
    async getUserList() {
      const {data} = await listAll()
      this.userList = data
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
