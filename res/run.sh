#!/bin/bash
touch mac.pcap
chmod o=rw mac.pcap
sudo tshark -c 30 -i wlan0 -w mac.pcap 
sudo  tshark -r mac.pcap -T fields -e eth.src -E header=y -E quote=n -E occurrence=f