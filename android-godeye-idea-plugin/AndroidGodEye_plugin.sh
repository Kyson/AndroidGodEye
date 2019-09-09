#! /bin/sh

read -p "Input monitor port, press 'Enter' for default [5390]:" MONITOR_PORT

if  [ ! -n "$MONITOR_PORT" ] ;then
    MONITOR_PORT=5390
fi

echo "Monitor port is $MONITOR_PORT"

adb forward tcp:$MONITOR_PORT tcp:$MONITOR_PORT && open "http://localhost:$MONITOR_PORT/index.html" && read -p "Press any key to continue..."