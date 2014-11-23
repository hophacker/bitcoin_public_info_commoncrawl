function check_process() {
    p=$(ps -A | grep $1 )
    if [[ -n "$p" ]]; then
        return 1
    else
        return 0
    fi
}

check_process $1
echo $?
