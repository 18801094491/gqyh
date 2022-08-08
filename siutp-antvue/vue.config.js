const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// vue.config.js
module.exports = {
  //基本路径
  publicPath: './',
  //npm run build构建时的输出目录名称
  outputDir: 'dist',
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  //打包app时放开该配置
  configureWebpack: config => {
    console.log('田野修改版本，正在打包!!!')
    //生产环境取消 console.log
    if (process.env.NODE_ENV === 'production') {
      // 为生产环境修改配置...
      //配置最小优化器
      config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
    }
  },
  chainWebpack: (config) => {
    //配置 resolve 别名
    config.resolve.alias
      .set('@$', resolve('src'))
      .set('@api', resolve('src/api'))
      .set('@assets', resolve('src/assets'))
      .set('@comp', resolve('src/components'))
      .set('@views', resolve('src/views'))
      .set('@layout', resolve('src/layout'))
  },
  css: {
    loaderOptions: {
      // 全局注入通用样式
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */
          //改变主题样式
          'primary-color': '#448cda',
          'link-color': '#448cda',
          'border-radius-base': '4px',
        },
        javascriptEnabled: true,
      }
    }
  },
  // 它支持webPack-dev-server的所有选项 配置开发环境服务器
  devServer: {
    //接口
    port: 3000,
    // 配置多个代理 
    proxy: {
      '/': {
        target: '',
        ws: false,
        changeOrigin: true
      },

    }

  },

  lintOnSave: false
}