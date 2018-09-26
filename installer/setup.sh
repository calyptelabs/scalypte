#!/bin/sh

DIRNAME=`dirname "$0"`
PROGNAME=`basename "$0"`
GREP="grep"

MAX_FD="maximum"

cygwin=false;
darwin=false;
linux=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;

    Darwin*)
        darwin=true
        ;;

    Linux)
        linux=true
        ;;
esac

if $cygwin ; then
    [ -n "$APP_HOME" ] &&
        APP_HOME=`cygpath --unix "$APP_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
    [ -n "$JAVAC_JAR" ] &&
        JAVAC_JAR=`cygpath --unix "$JAVAC_JAR"`
fi

RESOLVED_APP_HOME=`cd "$DIRNAME"; pwd`
if [ "x$APP_HOME" = "x" ]; then
    APP_HOME=$RESOLVED_APP_HOME
else
 SANITIZED_APP_HOME=`cd "$APP_HOME"; pwd`
 if [ "$RESOLVED_APP_HOME" != "$SANITIZED_APP_HOME" ]; then
   echo "WARNING APP_HOME may be pointing to a different installation - unpredictable results may occur."
   echo ""
 fi
fi
export APP_HOME

if [ "x$JAVA" = "x" ]; then
    if [ "x$JAVA_HOME" != "x" ]; then
        JAVA="$JAVA_HOME/bin/java"
    else
        JAVA="java"

       
       "$JAVA" -version > /dev/null 2>&1 && HAS_JAVA="yes"
       if [ "x$HAS_JAVA" = "x" ]; then
           echo "The JRE not installed!"
           echo "Go to https://www.java.com/en/download/manual.jsp"
           exit 1
       fi
    fi
fi

if [ "x$APP_BASE_DIR" = "x" ]; then
   APP_BASE_DIR="$APP_HOME"
fi

if [ "x$APP_LOG_DIR" = "x" ]; then
   APP_LOG_DIR="$APP_BASE_DIR/log"
fi


if $cygwin; then
    APP_HOME=`cygpath --path --windows "$APP_HOME"`
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
    APP_BASE_DIR=`cygpath --path --windows "$APP_BASE_DIR"`
    APP_LOG_DIR=`cygpath --path --windows "$APP_LOG_DIR"`
fi

eval \"$JAVA\" \
\"-Dapp.log.file=$APP_LOG_DIR/boot.log\" \
\"-Dapp.logging.configuration=file:$APP_CONFIG_DIR/logging.properties\" \
-jar \"$APP_HOME/setup.jar\" \
-Dapp.home.dir=\"$APP_HOME\" \
"$@"
APP_STATUS=$?
exit $APP_STATUS
