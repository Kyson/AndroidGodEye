@echo off

setlocal enabledelayedexpansion

FOR /F "delims=" %%a in ('adb logcat -d ^| findstr /c:"AndroidGodEye monitor is running at port"') do (set port_running_logcat=%%a)

echo %port_running_logcat%

FOR /F "tokens=1,2 delims=[]" %%a in ("%port_running_logcat%") do set port_running=%%b

echo %port_running%

if "%port_running%"=="" set port_running=5390

SET /P MONITOR_PORT="Input monitor port, press 'Enter' for default %port_running%: "

if "%MONITOR_PORT%"=="" set MONITOR_PORT=port_running

echo Use port %MONITOR_PORT%

adb forward tcp:%MONITOR_PORT% tcp:%MONITOR_PORT% && cmd /c start http://localhost:%MONITOR_PORT%/index.html

pause