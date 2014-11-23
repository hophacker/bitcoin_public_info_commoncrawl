#!/bin/bash - 
remote_files=" cc_open_address.jar  data  err  finished  output  result*tar.gz  tasks "
function check_cc_open_address() {
    p=`pgrep -f "cc_open_address.jar"`
    if [[ -n "$p" ]]; then
        echo "cc_open_address is running"
        return 1
    else
        echo "cc_open_address is dead"
        return 0
    fi
}
function runJob(){
    java -jar cc_open_address.jar `cat tasks` 2>err &
}
function runJobEndlessly(){
    while [ 1 -eq 1 ]; do
        check_cc_open_address
        if [ $? -eq 0 ]; then
            ./getUnfinished.rb
            java -jar cc_open_address.jar `head -n 1000 unfinished` 2>err &
        fi
        sleep 60
    done
}
function killJob(){
    pgrep -f "cc_open_address.jar" | xargs -I {} kill -9 {}
}
function prepareData(){
    killJob
    tarFile=$1
    killJob
    tar caf $tarFile data/ finished  output err
    if [[ $? == 0 ]]; then
        rm data/* -rf
    fi
}
function updateSystem(){
    #echo -e "1\n" | sudo -S 
    sudo apt-get update
    sudo apt-get install openjdk-7-jre-headless -y
}
function clearFiles(){
    rm $remote_files -rf
}
