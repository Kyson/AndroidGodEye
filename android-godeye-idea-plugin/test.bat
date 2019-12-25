setlocal enabledelayedexpansion
FOR /F "delims=" %%a in ('D:\dev\android\sdk\platform-tools\adb logcat -d ^| findstr /c:"AndroidGodEye monitor is running at port"') do (set yy=%%a)

echo %yy%

FOR /F "tokens=1,2 delims=[]" %%a in ("%yy%") do set newPATH=%%b
echo newPATH=%newPATH%
