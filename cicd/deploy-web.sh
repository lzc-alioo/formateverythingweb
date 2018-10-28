# /export/shell/deploy.sh

. /etc/profile

sourcepath=/export/work/gitstudy/formateverythingweb
sourcejar=$sourcepath/formateverythingweb-web/target/formateverythingweb-web-1.0.0-SNAPSHOT.jar
sourcebin=$sourcepath/cicd/start-web.sh

cd $sourcepath

git pull
mvn clean -U install

targetpath=/export/packages/formateverythingweb-web/`date '+%Y%m%d.%H%M%S'`
targetjar=formateverythingweb-web-1.0.0-SNAPSHOT.jar
mkdir -p $targetpath


cp -r $sourcejar $targetpath
cp -r $sourcebin  $targetpath

if [ ! -f "$targetpath/$targetjar" ]; then
    echo "最新版本已经复制到运行目录中:$pathname$filename"
else
    echo "最新版本未找到，放弃执行:$pathname$filename"
    exit 0
fi

#删除掉历史版本
find /export/packages/formateverythingweb-web// -mtime +3|xargs -exec rm -rf {} \;


chmod 777 $targetpath/*.sh

$targetpath/start-web.sh
