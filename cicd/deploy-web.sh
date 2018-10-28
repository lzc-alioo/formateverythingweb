#!/usr/bin/env bash
# /export/shell/deploy.sh

. /etc/profile
. /export/shell/ltool/llog.sh

sourcepath=/export/work/gitstudy/formateverythingweb
sourcejar=$sourcepath/formateverythingweb-web/target/formateverythingweb-web-1.0.0-SNAPSHOT.jar
sourcebin=$sourcepath/cicd/start-web.sh

log "构建项目 开始"

log "构建项目 获取最新代码开始"
cd $sourcepath
git pull
log "构建项目 获取最新代码结束"

log "构建项目 编译最新代码开始"
mvn clean -U install
log "构建项目 编译最新代码结束"

targetpath=/export/packages/formateverythingweb-web/`date '+%Y%m%d.%H%M%S'`
targetjar=formateverythingweb-web-1.0.0-SNAPSHOT.jar
mkdir -p $targetpath

log "构建项目 复制可执行程序开始"
cp -r $sourcejar $targetpath
cp -r $sourcebin  $targetpath

if [ -f "$targetpath/$targetjar" ]; then
    echo "最新版本已经复制到运行目录中:$pathname$filename"
else
    echo "最新版本未找到，放弃执行:$pathname$filename"
    exit 0
fi
log "构建项目 复制可执行程序结束"

log "构建项目 删除掉历史版本开始"
find /export/packages/formateverythingweb-web// -mtime +3|xargs -exec rm -rf {} \;
log "构建项目 删除掉历史版本结束"


chmod 777 $targetpath/*.sh

$targetpath/start-web.sh


log "构建项目 结束"
