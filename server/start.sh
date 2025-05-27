#!/usr/bin/env sh
rm plugins/*.jar
cp -r /data/* .
java -Xms4096M -Xmx8192M -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar purpur-1.21.4-2416.jar nogui
