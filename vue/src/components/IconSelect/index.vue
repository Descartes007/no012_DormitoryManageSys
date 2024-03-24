<template>
  <div class="icon-body">
    <el-input v-model="name" style="position: relative;"
              clearable placeholder="请输入图标名称"
              @clear="filterIcons" @input.native="filterIcons">
      <i slot="suffix" class="el-icon-search el-input__icon"/>
    </el-input>
    <div class="icon-list">
      <div v-for="(item, index) in iconList" :key="index" @click="selectedIcon(item.font_class)">
        <i class="iconfont" :class="'icon' + item.font_class" style="font-size: 30px"></i>
        <span>{{ item.name }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {glyphs} from '@/assets/icons/iconfont.json'

export default {
  name: 'IconSelect',
  data() {
    return {
      name: '',
      iconList: glyphs
    }
  },
  methods: {
    filterIcons() {
      this.iconList = glyphs
      if (this.name) {
        this.iconList = this.iconList.filter(item => item.font_class.includes(this.name))
      }
    },
    selectedIcon(name) {
      this.$emit('selected', 'icon' + name)
      document.body.click()
    }
  },
  created() {

  }
}
</script>

<style scoped>
.icon-body {
  padding: 10px;
}

.icon-list {
  height: 200px;
  overflow-y: scroll;
}

.icon-list div {
  height: 30px;
  line-height: 30px;
  margin-bottom: -5px;
  cursor: pointer;
  width: 33%;
  float: left;
}

.icon-list span {
  display: inline-block;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>
