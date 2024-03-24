<template>
  <div>
    <tree-node v-for="item in tree" @on-data-change="onDateChange"
               :key="item.id" :item="item" :checked="checked"></tree-node>
  </div>
</template>

<script>
import TreeNode from './treeNode'

export default {
  name: "MyTree",
  components: {
    TreeNode
  },
  methods: {
    onDateChange(checked, id) {
      if (checked) {
        this.checked.push(id)
      } else {
        this.checked.splice(this.checked.findIndex(item => item === id), 1)
      }
    }
  },
  props: {
    tree: {
      type: Array,
      checked: Array
    },
    padding: {
      type: Number,
      default: 0
    },
    checked: Array
  }
}
</script>

<style scoped>

</style>
