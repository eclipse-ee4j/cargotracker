#!/bin/sh

versionArray=$(kubectl get pods -n $1 -o json | jq -r [.items[].spec.containers[].image])
podVersionStatus="allUpdated"

for version in $(echo $versionArray | jq -r '.[]')
do
    if [ $version != $2 ] 
    then
        podVersionStatus="notAllUpdated"
    fi
done

echo $podVersionStatus