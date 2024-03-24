<template>
  <div>
    <el-tabs v-model="activeName" tab-position="left" @tab-click="doTapChange">
      <el-tab-pane label="通知" name="1">
        <el-tabs v-model="msgActiveName" @tab-click="doMsgTapChange">
          <el-tab-pane label="我收到的" name="receive">
            <div style="width: 80%;margin: 0 auto">
              <el-table :data="pageInfo.list" height="470">
                <el-table-column
                    label="序号" width="80"
                >
                  <template slot-scope="scope">{{ (listQuery.page - 1) * (listQuery.rows) + scope.$index + 1 }}
                  </template>
                </el-table-column>
                <el-table-column label="内容" width="400">
                  <template slot-scope="scope">{{scope.row.msg.substr(0,15)}}{{scope.row.msg.length > 15 ? "..." : ""}}</template>
                </el-table-column>
                <el-table-column prop="user.realName" label="发送者" width="200"></el-table-column>
                <el-table-column label="时间" prop="time" width="200"></el-table-column>
                <el-table-column label="操作" width="200">
                  <template slot-scope="scope">
                    <template>
                      <el-badge is-dot class="item" v-if="!scope.row.isRead">
                      <el-button size="mini" type="primary" @click="msgQuery(scope.row.id, 1)">查看
                      </el-button>
                      </el-badge>
                      <el-button v-else size="mini" type="primary" @click="msgQuery(scope.row.id, 1)">查看
                      </el-button>
                      <el-popconfirm title="确定删除吗？" @confirm="msgDel(scope.row.id, 1)">
                        <el-button style="margin-left: 8px"
                                   slot="reference"
                                   size="mini" type="danger">删除
                        </el-button>
                      </el-popconfirm>
                    </template>
                  </template>
                </el-table-column>
              </el-table>
              <div class="page" style="margin-top: 10px">
                <el-pagination
                    background
                    :hide-on-single-page="false"
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
            </div>
          </el-tab-pane>
          <el-tab-pane label="我发送的" name="send">
            <div style="width: 60%;margin: 0 auto">
              <el-table :data="pageInfo.list" height="470">
                <el-table-column
                    label="序号" width="80"
                >
                  <template slot-scope="scope">{{ (listQuery.page - 1) * (listQuery.rows) + scope.$index + 1 }}
                  </template>
                </el-table-column>
                <el-table-column label="内容" width="320">
                  <template slot-scope="scope">{{scope.row.msg.substr(0,15)}}{{scope.row.msg.length > 15 ? "..." : ""}}</template>
                </el-table-column>
                <el-table-column label="时间" prop="time" width="200"></el-table-column>
                <el-table-column label="操作" width="200">
                  <template slot-scope="scope">
                    <template>
                      <el-button size="mini" type="primary" @click="msgQuery(scope.row.id, 2)">查看
                      </el-button>
                      <el-popconfirm title="确定删除吗？" @confirm="msgDel(scope.row.id, 2)">
                        <el-button style="margin-left: 8px"
                                   slot="reference"
                                   size="mini" type="danger">删除
                        </el-button>
                      </el-popconfirm>
                    </template>
                  </template>
                </el-table-column>
              </el-table>
              <div class="page" style="margin-top: 10px">
                <el-pagination
                    background
                    :hide-on-single-page="false"
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
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-tab-pane>
      <el-tab-pane label="退宿审核" name="2">
        <el-tabs v-model="departActiveName" @tab-click="doDepartTabChange">
          <el-tab-pane label="我审核的" name="receive">
            <div style="width: 90%;margin: 0 auto">
              <el-table :data="pageInfo.list" height="470">
                <el-table-column label="学生姓名">
                  <template slot-scope="scope">
                    <el-popover
                        placement="bottom"
                        width="200"
                        trigger="click" @show="getStudent(scope.row.application.student.id)" @hide="hidePopover">
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
                      <span slot="reference" style="color: #409EFF;cursor: pointer">{{scope.row.application.student.name}}</span>
                    </el-popover>
                  </template>
                </el-table-column>
                <el-table-column label="申请人" prop="application.applyUser.realName"></el-table-column>
                <el-table-column label="申请理由" :show-overflow-tooltip="true" prop="application.reason"></el-table-column>
                <el-table-column label="申请时间" prop="application.time"></el-table-column>
                <el-table-column label="是否通过">
                  <template slot-scope="scope">
                    <span v-if="scope.row.isAgree===undefined">未审核</span>
                    <span style="color: #67C23A" v-else-if="scope.row.isAgree">通过</span>
                    <span style="color: #F56C6C" v-else>不通过</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作时间">
                  <template slot-scope="scope">
                    <span v-if="scope.row.isAgree===undefined">未审核</span>
                    <span style="color: #67C23A" v-else-if="scope.row.isAgree">{{scope.row.operateTime}}</span>
                    <span style="color: #F56C6C" v-else>{{scope.row.operateTime}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="审核理由" :show-overflow-tooltip="true">
                  <template slot-scope="scope">
                    <span v-if="scope.row.isAgree===undefined">未审核</span>
                    <span style="color: #67C23A" v-else-if="scope.row.isAgree">{{scope.row.reason}}</span>
                    <span style="color: #F56C6C" v-else>{{scope.row.reason}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button type="primary" size="mini" :disabled="scope.row.isAgree !== undefined" @click="showVerifyDialog(scope.row.id)">审核</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我发起的" name="send">
            <div style="width: 80%;margin: 0 auto">
              <el-table :data="pageInfo.list" height="470">
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
                <el-table-column label="理由" prop="reason"></el-table-column>
                <el-table-column label="申请时间" prop="time"></el-table-column>
                <el-table-column label="状态">
                  <template slot-scope="scope">
                    {{scope.row.isFinished ? '已结束，' + (scope.row.isPass? "通过":"不通过"):'正在审核'}}
                  </template>
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="showDepartVerifyDialog(scope.row.id)">查看审核记录</el-button>
                    <el-button size="mini" type="danger" :disabled="scope.row.isFinished" @click="msgDel(scope.row.id, 3)">撤销申请</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="page" style="margin-top: 10px">
                <el-pagination
                    background
                    :hide-on-single-page="false"
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
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-tab-pane>
      <el-tab-pane label="发送通知" name="4" v-has-permi="['home:send:msg']">
        <div style="width: 80%;margin: 20px auto">
          <el-form :model="messageToSend" ref="sendMsg" :rules="messageToSendRules" label-width="100px">
            <el-form-item label="接收宿舍楼" prop="to">
              <tree-select
                  @open="listBuildingList"
                  v-model="messageToSend.to"
                  :options="buildingList"
                  :normalizer="normalizer"
                  :show-count="true"
                  placeholder="选择宿舍楼">
              </tree-select>
            </el-form-item>
            <el-form-item label="通知内容" prop="msg">
              <el-input type="textarea" v-model="messageToSend.msg" rows="10" placeholder="输入通知内容"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="sendMsg">发 送</el-button>
              <el-button type="danger" @click="cancel">清 空</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="msg-dialog">
      <el-dialog :visible.sync="msgDialogVisible" @closed="msgDialogClose" append-to-body title="详情" width="40%">
        <el-form label-width="80px">
          <el-form-item label="发送者" v-if="msgDetail.user">
            <el-input readonly v-model="msgDetail.user.realName"></el-input>
          </el-form-item>
          <el-form-item label="时间">
            <el-input readonly v-model="msgDetail.time"></el-input>
          </el-form-item>
          <el-form-item label="内容">
            <el-input type="textarea" readonly v-model="msgDetail.msg" rows="10"></el-input>
          </el-form-item>
          <el-form-item label="接收者" v-if="msgDetail.receiveUsers">
            <div style="width:50%;float: left" v-for="item of msgDetail.receiveUsers">
              <span>{{item.user.realName}} </span>
              <span v-if="item.isRead" style="color: #67C23A"> 已读</span>
              <span v-else style="color: #909399"> 未读</span>
            </div>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
    <div class="depart-dialog">
      <el-dialog :visible.sync="departVerifyDialogVisible" @closed="departVerifyDialogClose"
                 append-to-body title="审核记录" width="40%">
        <el-timeline>
          <el-timeline-item
              v-for="(activity, index) in departVerifyList"
              :key="index"
              :color="activity.isAgree === undefined?'':activity.isAgree?'#0bbd87':'#f56c6c'"
              :timestamp="activity.operateTime">
            {{activity.operateUser.realName + "：" +
          (activity.isAgree===undefined ? "等待审核" : ((activity.isAgree?"通过":"不通过") +  "，原因：" + (activity.reason||"无")))}}
          </el-timeline-item>
        </el-timeline>
      </el-dialog>
      <el-dialog :visible.sync="verifyDialogVisible" append-to-body title="审核" @close="closeVerifyDialog" width="45%">
        <el-form label-width="80px" :rules="verifyRules" :model="departApplicationUser" ref="verifyForm">
          <el-form-item label="是否通过" prop="isAgree">
            <el-radio v-model="departApplicationUser.isAgree" :label="true">通过</el-radio>
            <el-radio v-model="departApplicationUser.isAgree" :label="false">不通过</el-radio>
          </el-form-item>
          <el-form-item label="原因">
            <el-input type="textarea" rows="10" v-model="departApplicationUser.reason"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitVerify">提 交</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import {list, deleteReceive, deleteSend, query,listSend, sendToBuilding, querydetail} from "@/api/notice";
import {del, listFlow, listMy, listMyFlow, query as queryDepart, update} from "@/api/depart";
import {list as listBuilding} from '@/api/building'
import {query as queryStudent} from '@/api/student'
import TreeSelect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {countUnread} from "@/api/login";
import store from "@/store";

export default {
  name: "index",
  components: {TreeSelect},
  props: ['change'],
  data() {
    return {
      messageToSendRules:{
        msg: [
          {required: true, message: '请填写内容', trigger: 'blur'}
        ]
      },
      verifyRules: {
        isAgree: [
          {required: true, message:"选择是否同意",trigger: "blur"}
        ]
      },
      messageToSend: {
        msg:undefined,
        to: undefined
      },
      buildingList:[],
      activeName: '1',
      msgActiveName: 'receive',
      departActiveName: 'receive',
      msg:{
        to: undefined,
        msg: undefined
      },
      msgDetail: {
        id: undefined,
        msg: undefined,
        user: {}
      },
      msgDialogVisible: false,
      departVerifyDialogVisible: false,
      verifyDialogVisible: false,
      departApplicationUser: {
        id: undefined,
        isAgree: undefined,
        reason: undefined
      },
      pageInfo: {},
      listQuery: {
        page: 1,
        rows: 10,
        entity: {}
      },
      student: {},
      departVerifyList: []
    }
  },
  methods: {
    async doTapChange() {
      let tab = this.activeName
      await this.refreshCount()
      if (tab === '1') {
        await this.doMsgTapChange()
      } else if (tab === '2') {
        await this.doDepartTabChange()
      } else if (tab === '3') {

      } else if (tab === '4') {
        this.messageToSend = {
          msg:undefined,
          to: undefined
        }
      }
    },
    async doDepartTabChange() {
      if (this.departActiveName === 'receive') {
        const {data} = await listMyFlow(this.listQuery)
        this.pageInfo = data
      } else {
        const {data} = await listMy(this.listQuery)
        this.pageInfo = data
      }
    },
    submitVerify() {
      this.$refs['verifyForm'].validate((valid) => {
        if (valid) {
          update(this.departApplicationUser).then(res => {
            if (res.statusCode === 200) {
              this.$message({
                message: res.msg,
                type: 'success'
              });
              this.doTapChange()
              this.closeVerifyDialog()
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
    showVerifyDialog(id) {
      this.departApplicationUser.id = id
      this.verifyDialogVisible = true
    },
    closeVerifyDialog() {
      this.departApplicationUser = {
        id: undefined,
        isAgree: undefined,
        reason: undefined
      }
      this.verifyDialogVisible = false
    },
    async showDepartVerifyDialog(id) {
      const {data} = await listFlow(id)
      this.departVerifyList = data
      if (data) {
        this.departVerifyDialogVisible = true
      }
    },
    departVerifyDialogClose() {
      this.departVerifyList = []
      this.departVerifyDialogVisible = false
    },
    sendMsg() {
      if (this.messageToSend.to === undefined) {
        this.$message({
          message: '请选择宿舍楼',
          type: 'error'
        });
        return
      }
      this.$refs['sendMsg'].validate((valid) => {
        if (valid) {
          sendToBuilding(this.messageToSend).then(res => {
            if (res.statusCode === 200) {
              this.$message({
                message: res.msg,
                type: 'success'
              });
            }
          })
          this.cancel()
        } else {
          this.$message({
            message: '请正确填写参数',
            type: 'error'
          });
          return false;
        }
      })
    },
    cancel() {
      this.messageToSend = {
        to: undefined,
        msg: undefined
      }
    },
    async listBuildingList() {
      const {data} = await listBuilding()
      this.buildingList = data
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.doTapChange(this.activeName)
    },
    handleSizeChange(val) {
      this.listQuery.rows = val
      this.doTapChange(this.activeName)
    },
    async msgQuery(id,type) {
      if (type === 1) {
        const {data} = await query(id)
        this.msgDetail = data
      } else if (type === 2) {
        const {data} = await querydetail(id)
        this.msgDetail = data
      }
      await this.doTapChange(this.activeName)
      this.msgDialogVisible = true
    },
    async refreshCount() {
      const count = await countUnread()
      if (count) {
        store.commit('SET_COUNT_UNREAD', count.data)
      }
    },
    msgDialogClose() {
      this.msgDetail = {
        id: undefined,
        msg: undefined,
        user: undefined
      }
      this.msgDialogVisible = false
    },
    async msgDel(id, type) {
      if (type === 1) {
        deleteReceive(id).then(res => {
          if (res.statusCode === 200) {
            this.$message({
              message: res.msg,
              type:'success'
            })
            this.doTapChange(this.activeName)
          }
        })
      } else if (type === 2){
        deleteSend(id).then(res => {
          if (res.statusCode === 200) {
            this.$message({
              message: res.msg,
              type:'success'
            })
            this.doTapChange(this.activeName)
          }
        })
      } else if (type === 3) {
        del(id).then(res => {
          if (res.statusCode === 200) {
            this.$message({
              message: res.msg,
              type:'success'
            })
            this.doTapChange(this.activeName)
          }
        })
      } else if (type === 4) {

      }
    },
    async doMsgTapChange() {
      if (this.msgActiveName === 'receive') {
        const {data} = await list(this.listQuery)
        this.pageInfo = data
      } else {
        const {data} = await listSend(this.listQuery)
        this.pageInfo = data
      }
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
    async getStudent(id) {
      const {data} = await queryStudent(id)
      this.student = data
    },
    hidePopover() {
      this.student = {}
    }
  },
  mounted() {
    this.doTapChange(this.activeName)
  },
  watch: {
    change(val) {
      if (val === 1) {
        this.doTapChange(this.activeName)
      }
    }
  }
}
</script>

<style scoped>

</style>
