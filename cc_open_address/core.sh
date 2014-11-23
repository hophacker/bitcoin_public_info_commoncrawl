#!/bin/bash - 
unset keys
unset users
declare -A keys
declare -A users
while read line
do
    IFS=' '
    arr=($line)
    ip=${arr[0]}
    ssh_key=${arr[1]}
    user=${arr[2]}
    keys[$ip]="/home/john/.ssh/$ssh_key"
    users[$ip]=$user
done < key_map
function executeSlaves(){
    comm=$1
    for ip in ${!keys[@]}; do
        ssh_key=${keys[$ip]}
        user=${users[$ip]}
        ssh $user@$ip -i $ssh_key  "source remote.sh;$1"  &
    done
}
function get_tasks(){
    begin=$2
    end=$3
    output=`head fileList -n $end | tail -n $(($end-$begin+1)) | tr '\n' ' '`
    eval "$1='$output'"
}
function killJobs(){
    executeSlaves "killJob"
}
function runJobs(){
    executeSlaves "runJob"
}
function clearFiles(){
    executeSlaves "clearFiles"
}
function manualCheck(){
    if [ -z "$1" ]; then
        times=1000
    else
        times=$1
    fi
    count=0
    for ip in  ${!keys[@]}; do
        let count+=1
        if [[ $count > $times ]];then
            break;
        fi
        ssh_key=${keys[$ip]}
        user=${users[$ip]}
        echo $ip
        ssh $user@$ip -i $ssh_key 
    done
}
function updateSystem(){
    executeSlaves "updateSystem"
}
