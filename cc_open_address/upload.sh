#!/bin/bash - 
source core.sh
unset -f upload_jar
unset -f upload_tasks
unset -f upload_scripts
function upload_jar(){
    for ip in ${!keys[@]}; do
        ssh_key=${keys[$ip]}
        user=${users[$ip]}
        scp -i $ssh_key remote.sh cc_open_address.jar $user@$ip:~ &
    done
}
function upload_tasks(){
    if [[ $1 != "" ]]; then
        load=$1
    else
        load=1000
    fi
    total=`wc fileList | tr "\t" " " | sed "s/  */ /" | cut -d' ' -f2`
    begin=`cat task_begin_number`
    for ip in ${!keys[@]}; do
        end=$(($load+$begin-1))
        echo "begin with $begin, end with $end"
        ssh_key=${keys[$ip]}
        user=${users[$ip]}
        get_tasks tasks $begin $end
        begin=$(($begin+$load))
        echo $tasks > temp_file
        scp -i $ssh_key temp_file $user@$ip:~/tasks &
    done
    echo $(($end+1)) > task_begin_number
}
function upload_scripts(){
    for ip in ${!keys[@]}; do
        ssh_key=${keys[$ip]}
        user=${users[$ip]}
        scp -i $ssh_key remote.sh $user@$ip:~/ &
    done
}
