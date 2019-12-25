@echo off

// TODO KYSON IMPL

SET /P MONITOR_PORT="Input monitor port, press 'Enter' for default [5390]:"
if "%MONITOR_PORT%"=="" set MONITOR_PORT=5390
echo Monitor port is %MONITOR_PORT%
adb forward tcp:%MONITOR_PORT% tcp:%MONITOR_PORT% && cmd /c start http://localhost:%MONITOR_PORT%/index.html
pause