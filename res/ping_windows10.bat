@echo off
for /L %%a in (1,1,254) do @start /b ping 192.168.0.%%a -w 100 -n 2 >nul