<template>
  <div class="home">
    <el-row :gutter="70">
      <el-col :span="8">
        <el-card class="grid-content" style="height: 154px">
          <div class="avatar">
            <el-avatar :size="70" :src="icon" v-if="icon !== undefined"></el-avatar>
            <el-avatar :size="70" v-else>{{name}}</el-avatar>
          </div>
          <div class="welcome-content">
            <div>欢迎您， {{name}}</div>
            <div class="little">我管理的宿舍楼：{{statistics.building}}</div>
            <div class="little">宿舍总数： {{statistics.roomNum}}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="grid-content" style="height: 154px">

        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="grid-content" style="height: 154px">
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="30">
      <el-col :span="12">
        <el-card class="grid-content" style="height: 180px">
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="grid-content" style="height: 180px">
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card class="grid-content" style="height: 200px">
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import store from "../store";
import {get} from "@/api/system/home";

export default {
  name: 'Home',
  data() {
    return {
      statistics: {
        building: undefined,
        roomNum: undefined,
        totalStudent: undefined,
        livingStudent: undefined,
        leaveStudent: undefined,
        unprocessedRepair: undefined,
      }
    }
  },
  computed: {
    activeMenu() {
      const route = this.$route
      const {path} = route
      return path
    },
    name() {
      return store.state.userName
    },
    icon() {
      return store.state.userIcon
    }
  },
  methods: {
    async get() {
      const {data} = await get()
      this.statistics = data
    }
  },
  created() {
    this.get()
  }
}
</script>
<style scoped>
.avatar {
  margin: 20px 20px;
  float: left;
}
.home {
  padding: 20px;
}
.welcome-content {
  /*float: left;*/
  margin: 30px 0;
}
.grid-content {
  margin: 10px 0;
  position: relative;
}
.fixed:after {
  content: '';
  clear: both;
  display: block;
  visibility: hidden;
  height: 0;
  width: 0;
}
.little {
  font-size: 13px;
  color: #909399;
}
.mid {
  text-align: center;
}
</style>
