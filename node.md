```js
const path = require("path");
const { NodeSSH } = require("node-ssh");
const ssh = new NodeSSH();

const configs = {
  host: " ",
  username: " ",
  password: " ",
  port: "",
  // 线上文件夹地址
  serverPath: " ",
  // 本地文件夹地址
  distPath: path.resolve('D:/'),
};
//上传到服务器
(function () {
  console.log("开始上传, ssh连接中...");
  ssh
    .connect({
      //configs存放的是连接远程机器的信息
      host: configs.host,
      username: configs.username,
      password: configs.password,
      port: configs.port, //ssh连接默认在22端口
    })
    .then(async ()=> {
      console.log("ssh连接成功:", configs.host);
      console.log("上传中...");
      await ssh.execCommand(`rm -rf ${configs.serverPath}`)
      //上传网站的发布包至configs中配置的远程服务器的指定地址
      ssh
        .putDirectory(configs.distPath, configs.serverPath)
        .then(function () {
          console.log("上传成功");
          process.exit(0);
        })
        .catch((err) => {
          console.log("上传出错:", err);
          process.exit(0);
        });
    })
    .catch((err) => {
      console.log("ssh连接失败:", err);
      process.exit(0);
    });
})()
```
