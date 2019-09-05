#!/bin/bash
adb forward tcp:5390 tcp:5390 && open "http://localhost:5390/index.html" && read -p "Press any key to continue..."
