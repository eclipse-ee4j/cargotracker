#!/bin/sh

if curl -s "$1" | grep -q "$2"
then
    echo "Matched"
else
    echo "Unmatched"
fi