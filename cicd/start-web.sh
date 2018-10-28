# /export/shell/deploy.sh

. /etc/profile
. /export/shell/ltool/*.sh

targetpath=/export/packages/formateverythingweb-web/`date '+%Y%m%d.%H%M%S'`
targetjar=formateverythingweb-web-1.0.0-SNAPSHOT.jar


JAVA=$JAVA_HOME/bin/java

#执行程序启动所使用的系统用户，考虑到安全，推荐不使用root帐号
RUNNING_USER=admin

#Java程序所在的目录（当前start.sh文件的上一级目录）
BASEDIR=$(cd `dirname $0`/; pwd)

#需要启动的Java主程序（main方法类）
#APP_MAINCLASS=com.lzc.home.article.launcher.ServiceLauncher

#拼凑完整的classpath参数，包括指定lib目录下所有的jar,jdk6以后可以不用for循环了，支持配置lib目录
CLASSPATH=$BASEDIR/.:$BASEDIR/lib/*


#java虚拟机启动参数
JAVA_OPTS=" -Dspring.profiles.active=dev -Dspring.datasource.password=Lzcmylinux1# -Xms64m -Xmx512m -Xmn64m -Djava.awt.headless=true $JAVA_OPTS"

#初始化psid变量（全局）
psid=0

checkpid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $targetjar`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}

checkpid

if [ $psid -ne 0 ]; then
    echo :App is already running, current pid=$psid, it will killed by shell
    kill -9 $psid
fi

nohup $JAVA \
    -Dbasedir="$BASEDIR" \
    -Dfile.encoding="UTF-8" \
    $JAVA_OPTS \
    -jar  \
    $targetjar >/dev/null 2>nohup.log &

checkpid

if [ $psid -ne 0 ]; then
    echo "[OK]"
else
    echo "[Failed]"
fi


