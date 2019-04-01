@echo off
set curr_dir=%cd%
chdir C:\Program Files\Wireshark
tshark -c 1500 -i WiFi -w "C:\Users\wwwli\Documents\GitHub\OccupancyMonitoring\res\mac.pcap" 
tshark -r "C:\Users\wwwli\Documents\GitHub\OccupancyMonitoring\res\mac.pcap" -T fields -e eth.src -E header=y -E quote=n -E occurrence=f 
