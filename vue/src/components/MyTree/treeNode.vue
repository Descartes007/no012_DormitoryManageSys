<template>
  <div>
    <div class="el-tree-node__content" :style="{'padding-left':padding + 'px'}"
         @click="childrenDisplay = !childrenDisplay">
            <span v-if="item.children && item.children.length>0 && !childrenDisplay"
                  class="el-tree-node__expand-icon el-icon-caret-right"></span>
      <span v-else-if="item.children && item.children.length>0 && childrenDisplay"
            class="expanded el-tree-node__expand-icon el-icon-caret-right"></span>
      <span v-else class="placeholder"></span>
      <el-checkbox @change="dateChanged" v-model="check" :label="item.id">
        {{ '' }}
      </el-checkbox>
      <span class="el-checkbox__label">{{ item.name }}</span>
    </div>
    <el-collapse-transition>
      <div v-show="childrenDisplay">
        <tree-node @on-data-change="onDateChange" v-for="item1 in item.children" :checked="checked"
                   :item="item1" :key="item1.id" :padding="padding + 18"
                   class="el-tree-node__children"></tree-node>
      </div>
    </el-collapse-transition>
  </div>
</template>

<script>
export default {
  name: "treeNode",
  props: {
    item: Object,
    padding: {
      default: 0
    },
    checked: Array
  },
  watch: {
    checked(val) {
      this.check = val.includes(this.item.id)
    }
  },
  data() {
    return {
      check: false,
      childrenDisplay: false
    }
  },
  methods: {
    onDateChange(checked, id) {
      this.$emit('on-data-change', checked, id)
    },
    dateChanged() {
      this.$emit('on-data-change', this.check, this.item.id)
    }
  },
  created() {
    this.check = this.checked.includes(this.item.id)
  }
}
</script>

<style scoped>
.el-checkbox__label {
  padding: 0;
}

.placeholder {
  height: 24px;
  width: 24px;
}
</style>
