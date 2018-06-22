# /export/shell/deploy.sh

cd /export/work/gitstudy/formateverythingweb/
git pull
mvn clean -U install

cd /export/work/gitstudy/formateverythingweb/format-service/target

pathname=/export/packages/`date '+%Y%m%d.%H%M%S'`/
mkdir $pathname

cp -r format-service-1.0.0-SNAPSHOT-package/* $pathname

chmod 777 $pathname/bin/start-analysis.sh
$pathname/bin/start-analysis.sh