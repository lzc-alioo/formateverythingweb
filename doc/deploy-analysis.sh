# /export/shell/deploy.sh

. /etc/profile

cd /export/work/gitstudy/formateverythingweb/
git pull
mvn clean -U install

cd /export/work/gitstudy/formateverythingweb/format-service/target

pathname=/export/packages/stock-analysis/`date '+%Y%m%d.%H%M%S'`/
mkdir -p $pathname

cp -r format-service-1.0.0-SNAPSHOT-package/* $pathname

find /export/packages/stock-analysis/ -mtime +3|xargs -exec rm -rf {} \;

chmod 777 $pathname/bin/*.sh
$pathname/bin/start-analysis.sh