#!/usr/bin/env bash
# /export/shell/deploy.sh
# 尝试获取最新代码,并编译

. /etc/profile
. /export/shell/ltool/llog.sh
. /export/shell/ltool/lpid.sh


sourcepath=/export/work/gitstudy/formateverythingweb

log "编译项目 开始"

log "编译项目 获取最新代码开始"
cd $sourcepath
git pull
log "编译项目 获取最新代码结束"

log "编译项目 编译最新代码开始"
mvn clean -U install
log "编译项目 编译最新代码结束"

chmod 777 $sourcepath/cicd/deploy-web.sh

./$sourcepath/cicd/deploy-web.sh

log "编译项目 结束"
