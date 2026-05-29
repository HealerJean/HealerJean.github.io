1. 输入 `arch -x86_64 zsh` ：如果成功，输入 `arch` 会显示 `i386`。

   **问题1：**

   ```shell
   └─[$] arch -x86_64 zsh                                               [13:36:44]
   
   arch: posix_spawnp: zsh: Bad CPU type in executable
   ```

   解决：`softwareupdate --install-rosetta --agree-to-license`

   ```sh
   └─[$] softwareupdate --install-rosetta --agree-to-license            [13:40:10]
   
   By using the agreetolicense option, you are agreeing that you have run this tool with the license only option and have read and agreed to the terms.
   If you do not agree, press CTRL-C and cancel this process immediately.
   2026-04-17 13:40:43.203 softwareupdate[57357:836207] Package Authoring Error: 047-48628: Package reference com.apple.pkg.RosettaUpdateAuto is missing installKBytes attribute
   Install of Rosetta 2 finished successfully
   ```

2. 加载 `npm`：输入 

   ```sh
   export NVM_DIR="$HOME/.nvm"
   [ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
   ```

3. 安装 `node` ：成功

   ```sh
   nvm install 14.21.3 --arch=x64
   ```

- 后续使用：`nvm use 14.21.3`



其他：

- `install`：`npm install --registry=http://registry.xxxx.com`

- 设置默认镜像：`npm config set registry http://registry.xxxx.com`  

  ```sh
  npm config get registry
  ```

  

- 设置 `node` 默认版本：

  ```sh
  nvm alias default 14.21.3
  ```

  


`