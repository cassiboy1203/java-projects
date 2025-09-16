#!/usr/bin/env sh
java -Xms4096M -Xmx8192M -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar purpur-1.21.7-2477.jar nogui
