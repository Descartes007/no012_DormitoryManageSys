<template>
  <div>
    <el-upload
        :action="action"
        :headers="header"
        list-type="picture-card"
        :on-preview="handlePictureCardPreview"
        :on-success="handleSuccess"
        :file-list="fileList"
        :on-remove="handleRemove">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import {getToken} from "@/utils/auth";
export default {
  name: "imagesUpload",
  props:{
    images: {
      default: () => []
    }
  },
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      action: process.env.VUE_APP_BASE_API + "/permission/upload",
      header: {
        "_ut": getToken()
      },
      imageList: [],
      fileList: []
    };
  },
  methods: {
    handleRemove(file, fileList) {
      this.imageList.splice(this.imageList.indexOf(file.saveName), 1)
      this.$emit("onChange", this.imageList)
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleSuccess(res, file, fileList) {
      if (res.statusCode !== 200) {
        this.$message({
          message: res.msg,
          type: 'error'
        });
        fileList.splice(fileList.indexOf(file), 1)
      } else {
        file.saveName = res.data
        this.imageList.push(res.data)
        this.$emit("onChange", this.imageList)
      }
    },
    init() {
      this.fileList = []
      for (let i of this.images) {
        this.imageList.push(i)
        this.fileList.push({
          saveName: i,
          url: process.env.VUE_APP_IMG_PREFIX + i
        })
      }
    }
  },
  created() {
    this.init()
  }
}
</script>

<style scoped>

</style>
