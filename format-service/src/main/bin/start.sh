#!/bin/sh

###################################
#JDK所在路径
JAVA=java

# (-n 字符串)字符串的长度不为0则为真
# (-z 字符串)字符串的长度不为零则为真
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME 没有配置，将退出"
    exit 0
fi

JAVA=$JAVA_HOME/bin/java

#执行程序启动所使用的系统用户，考虑到安全，推荐不使用root帐号
RUNNING_USER=didi

#Java程序所在的目录（当前start.sh文件的上一级目录）
APP_HOME=$(cd `dirname $0`/..; pwd)

#需要启动的Java主程序（main方法类）
APP_MAINCLASS=com.lzc.home.article.stock.StockFilter

#拼凑完整的classpath参数，包括指定lib目录下所有的jar,jdk6以后可以不用for循环了，支持配置lib目录
CLASSPATH=$APP_HOME/.:$APP_HOME/lib/*
#for i in "$APP_HOME"/lib/*.jar; do
#   CLASSPATH="$CLASSPATH":"$i"
#done

#java虚拟机启动参数
JAVA_OPTS="-Xms512m -Xmx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m $JAVA_OPTS"

#初始化psid变量（全局）
psid=0

checkpid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`

   if [ -n "$javaps" ]; then
      psid=`echo $javaps | awk '{print $1}'`
   else
      psid=0
   fi
}

checkpid

if [ $psid -ne 0 ]; then
    echo :App is already running, current pid=$psid, it will killed by shell
    kill -9 $pid
fi
JAVA_CMD="nohup $JAVA \
          $JAVA_OPTS \
          -classpath $CLASSPATH \
          $APP_MAINCLASS >/dev/null 2>&1 &"

echo $JAVA_CMD
$JAVA_CMD

checkpid

if [ $psid -ne 0 ]; then
    echo "[OK]"
else
    echo "[Failed]"
fi


