@echo off
set curr_dir=%cd%
chdir C:\Program Files\Wireshark
tshark -c 800 -i WiFi -w "C:\Users\wwwli\Documents\GitHub\OccupancyMonitoring\res\mac.pcap" not ether host 9c:e0:63:14:c6:42
tshark -r "C:\Users\wwwli\Documents\GitHub\OccupancyMonitoring\res\mac.pcap" -T fields -e eth.src -E header=y -E quote=n -E occurrence=f 
