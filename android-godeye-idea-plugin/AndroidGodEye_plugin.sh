#! /bin/sh

port_running=`adb logcat -d | grep 'AndroidGodEye monitor is running at port' | tail -1 | cut -d '<' -f2|cut -d '>' -f1`

if  [[ ! -n "$port_running" ]] ;then
    echo "Can not find which port AndroidGodEye monitor is running at."
    port_running=5390
else
    echo "AndroidGodEye monitor is running at ${port_running}"
fi

read -p "Input monitor port, press 'Enter' for default ${port_running}: " MONITOR_PORT

if  [[ ! -n "$MONITOR_PORT" ]] ;then
    MONITOR_PORT=${port_running}
fi

echo "Use port $MONITOR_PORT"

adb forward tcp:${MONITOR_PORT} tcp:${MONITOR_PORT} && open "http://localhost:$MONITOR_PORT/index.html" && read -p "Press any key to continue..."