#!/bin/bash - 
total_pages=`cat finished | awk '{total+=$2}END{print total}'`
echo "[$total_pages] pages have been scanned"
total_pages_addr=`cat finished | awk '{total+=$3}END{print total}'`
echo "[$total_pages_addr] pages have bitcoin address"
echo "which means [`echo "$total_pages/$total_pages_addr" | bc`] have a bitcoin address"
echo "There are [`wc -l finished | cut -d' ' -f1`] commoncrawl slices have been parsed"
