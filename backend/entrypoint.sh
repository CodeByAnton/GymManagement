#!/bin/bash

function is_db_ready() {
    nc -z ${DB_HOST} ${DB_PORT} > /dev/null 2>&1
    return $?
}


echo "Waiting for the database to be ready..."
counter=0
while ! is_db_ready && [ $counter -lt ${WAIT_DB_SEC} ]; do
    sleep 1
    counter=$((counter+1))
done

if [ $counter -eq ${WAIT_DB_SEC} ] 
then
    echo "Unable to connect to database."
    exit 1
fi

java -jar backend.jar