PREFIX=registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker
PROJECT=com-hlj-springboot-docker
tag=1
#tag=`date +%m%d%H%M`


docker login --username=北京当趣科技 registry.cn-qingdao.aliyuncs.com --password=DANGQU123569


echo "清理项目..."
mvn clean install
#cd  ${PROJECT}/
echo "开始打包${PROJECT}..."
mvn package

echo "删除之前保留在电脑中的版本..."
docker images | grep registry.cn-qingdao.aliyuncs.com/duodianyouhui/com-hlj-springboot-docker | xargs docker rmi

echo "开始构建..."
docker build -t ${PREFIX}:${tag} .

echo "${PROJECT}构建成功，开始上传至阿里云"
docker push ${PREFIX}:${tag}

echo "镜像${PROJECT}构建并上传至阿里云成功"

