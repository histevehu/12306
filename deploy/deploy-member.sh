#!/bin/bash
echo "publish member----------"

process_id=`ps -ef | grep member.jar | grep -v grep |awk '{print $2}'`
if [ $process_id ] ; then
sudo kill -9 $process_id
fi

source /etc/profile
nohup java -jar -Dspring.profiles.active=prod ~/member.jar > /dev/null 2>&1 &

echo "end publish"
