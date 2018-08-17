#!/bin/sh

echo "CATALINA_HOME = " $CATALINA_HOME

mkdir -p /run/conf

if [ -s /run/secrets/*.context.xml ];
  then
    # Symlink context.xml.
    ln -sf /run/secrets/*.context.xml $CATALINA_HOME/conf/Catalina/localhost/ROOT.xml
    echo "FOUND context.xml."
  else
    echo "context.xml NOT FOUND, proceeding with default config"
fi

if [ -s /run/secrets/*.shiro.ini ];
  then
    # Symlink shiro.ini.
    ln -sf /run/secrets/*.shiro.ini /run/conf/shiro.ini
    echo "FOUND shiro.ini."
  else
    echo "shiro.ini NOT FOUND, proceeding with default config"
fi

if [ -s /run/secrets/*.setenv.sh ];
  then
    # Symlink setenv.sh.
    ln -sf /run/secrets/*.setenv.sh $CATALINA_HOME/bin/setenv.sh
    echo "FOUND setenv.sh."
  else
    echo "setenv.sh NOT FOUND, proceeding with default config"
fi

if [ -s /run/secrets/*.isis.properties ] && [ -s /run/secrets/*.logging.properties ];
  then
    # Symlink isis.properties.
    ln -sf /run/secrets/*.isis.properties /run/conf/isis.properties
    ln -sf /run/secrets/*.logging.properties /run/conf/logging.properties
    echo "FOUND isis.properties and logging.properties."
  else
    echo "isis.properties and/or logging.properties NOT FOUND, proceeding with default config"
fi

# Running Catalina
echo "Starting Catalina:"
${SERVER_HOME}/bin/catalina.sh run
