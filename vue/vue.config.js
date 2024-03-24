module.exports = {
  publicPath: "./",
  //打包输出文件夹
  outputDir:"dms",
  chainWebpack: config => {
    //关闭语法检查
    config.module.rules.delete('eslint');
    config.plugin('html').tap((args) => {
      args[0].title = 'myproject';
      return args;
    });
  },
  //错误输出为编译警告
  lintOnSave: process.env.NODE_ENV === 'development',
  devServer: {
    open: true,
    port: 8087,
    https: false
  }
}
