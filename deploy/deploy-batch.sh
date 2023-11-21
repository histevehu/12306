#!/bin/bash

# 服务发布脚本，当服务打包jar包并上传到服务器后，执行此脚本即可发布服务
echo "publish batch----------"

# 根据jar包名找到原有的进程并杀死
process_id=`ps -ef | grep batch.jar | grep -v grep |awk '{print $2}'`
if [ $process_id ] ; then
sudo kill -9 $process_id
fi

source /etc/profile
nohup java -jar -Dspring.profiles.active=prod ~/batch.jar > /dev/null 2>&1 &

echo "end publish"
