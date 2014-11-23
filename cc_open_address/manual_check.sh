#!/bin/bash - 
source core.sh
for ip in  ${!keys[@]}; do
    ssh_key=${keys[$ip]}
    user=${users[$ip]}
    ssh $user@$ip -i $ssh_key 
done
