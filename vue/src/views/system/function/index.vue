<template>
  <div>
    <div class="container">
      <div class="inner_container">
        <div class="table">
          <el-form>
            <el-form-item>
              <el-button v-has-permi="['system:function:save']"
                         size="small" type="primary" @click="showDialog(undefined)">新增
              </el-button>
            </el-form-item>

          </el-form>
          <el-table v-loading="loading"
                    :data="functionList"
                    :tree-props="{children: 'children'}"
                    row-key="id">
            <el-table-column label="菜单名称" prop="name"></el-table-column>
            <el-table-column label="图标" width="80">
              <template slot-scope="scope">
                <i class="iconfont" :class="scope.row.icon"></i>
              </template>
            </el-table-column>
            <el-table-column label="排序序号" prop="orderNum" width="80"></el-table-column>
            <el-table-column label="权限标识" prop="permission"></el-table-column>
            <el-table-column label="组件路径" prop="component"></el-table-column>
            <el-table-column label="路由地址" prop="path"></el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button v-has-permi="['system:function:query']"
                           size="mini" type="primary" @click="showDialog(scope.row.id)">详情
                </el-button>
                <template>
                  <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
                    <el-button style="margin-left: 8px" v-has-permi="['system:function:delete']"
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
              width="40%"
              @closed="handleClose"
          >
            <el-form ref="saveOrUpdateForm" :rules="rules" :model="form" label-width="80px">
              <el-form-item label="上级菜单">
                <tree-select
                    v-model="form.parentId"
                    :options="functionList"
                    :normalizer="normalizer"
                    :show-count="true"
                    placeholder="选择上级菜单"></tree-select>
              </el-form-item>
              <el-form-item label="菜单类型">
                <el-radio-group v-model="form.menuType">
                  <el-radio label="M">目录</el-radio>
                  <el-radio label="C">菜单</el-radio>
                  <el-radio label="F">按钮</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="菜单名称" prop="name">
                    <el-input v-model="form.name" placeholder="输入菜单名称"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="菜单图标" v-if="form.menuType !== 'F'">
                    <el-popover
                        placement="bottom-start"
                        width="460"
                        trigger="click"
                    >
                      <icon-select ref="iconSelect" @selected="select"/>
                      <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                        <i
                            v-if="form.icon"
                            slot="prefix"
                            :class="form.icon"
                            class="iconfont"
                            style="line-height: 40px"
                        />
                        <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                      </el-input>
                    </el-popover>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="菜单排序">
                    <el-input-number v-model="form.orderNum" :min="0" :step="1"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="组件路径" prop="component" v-if="form.menuType === 'C'">
                    <el-input v-model="form.component" placeholder="输入组件路径"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="路由地址" prop="path" v-if="form.menuType === 'C'">
                    <el-input v-model="form.path" placeholder="输入路由地址"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="权限标识" v-if="form.menuType !== 'M'">
                    <el-input v-model="form.permission" placeholder="输入权限标识"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
            <span slot="footer" class="dialog-footer">
                            <el-button type="danger" @click="handleClose">取 消</el-button>
                            <el-button type="primary" v-has-permi="['system:function:update']" @click="saveOrUpdate">保 存</el-button>
                          </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {del, listTree, query, saveOrUpdate} from "@/api/system/SystemFunction";
import TreeSelect from '@riophae/vue-treeselect'
import IconSelect from '@/components/IconSelect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "index",
  components: {
    TreeSelect, IconSelect
  },
  data() {
    return {
      loading: true,
      functionList: [],
      form: {
        id: undefined,
        parentId: undefined,
        name: '',
        orderNum: undefined,
        path: '',
        component: '',
        menuType: 'M',
        permission: '',
        icon: ''
      },
      listTree: [],
      dialogTitle: '',
      dialogVisible: false,
      rules: {
        name: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
        ],
        component: [
          {required: true, message: '组件不能为空', trigger: 'blur'}
        ],
        path: [
          {required: true, message: '路径不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    async listData() {
      this.loading = true
      const {data} = await listTree();
      this.functionList = data
      this.loading = false
    },
    async showDialog(id) {
      this.dialogTitle = '添加菜单'
      if (id) {
        const {data} = await query(id)
        this.dialogTitle = '修改菜单'
        if (data) {
          this.dialogVisible = true
          this.form = data
        }
      } else {
        this.dialogVisible = true
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
    saveOrUpdate() {
      const _this = this
      this.$refs['saveOrUpdateForm'].validate((valid) => {
        if (valid) {
          saveOrUpdate(this.form).then(res => {
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
    select(name) {
      this.form.icon = name
    },
    handleClose() {
      this.form = {
        id: undefined,
        parentId: undefined,
        name: '',
        orderNum: undefined,
        path: '',
        component: '',
        menuType: 'M',
        permission: '',
        icon: ''
      }
      this.$refs['saveOrUpdateForm'].resetFields()
      this.dialogVisible = false
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
