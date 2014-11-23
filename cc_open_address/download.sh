#!/bin/bash - 
source core.sh
function getWork(){
    for ip in  ${!keys[@]}; do
        ssh_key=${keys[$ip]}
        user=${users[$ip]}
        file=result${ip}
        tarFile=${file}.tar.gz
        ssh $user@$ip -i $ssh_key "source remote.sh;prepareData $tarFile"
        scp -i $ssh_key $user@$ip:~/$tarFile  result/
    done
}
function checkFiles(){
    if [ -e final -a ! -d final ] 
    then
        echo "result/final should be a folder"
        exit
    fi
    if [ ! -e final ] 
    then
        mkdir final
    fi
    touch final/output
    touch final/finished
    touch final/err
}
function uniqFile(){
    file=$1
    sort $file | uniq > ${file}_temp
    mv ${file}_temp $file -f
}
function syncWork(){
    cd result
    checkFiles
    for ip in  ${!keys[@]}; do
        file=result${ip}
        tarFile=${file}.tar.gz
        echo "dealing with $tarFile"
        sleep 
        tar axvf $tarFile 
        pwd .
        cat output >> final/output
        cat finished >> final/finished
        cat err >> final/err
    done
    rm output finished err
    cd final
    uniqFile output
    uniqFile finished
    cd ../..
}
getWork
syncWork
executeSlaves "runJob"
