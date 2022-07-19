#!/bin/bash

# this script can be used to replace deprecated javax. package names from a 
# Java EE8 project with the new jakarta. package names in Jakarta 9
# Initial version from rsoika, 2021

echo "replacing:"
echo "	javax.annotation.  -> jakarta.annotation."
echo "	javax.ejb.         -> jakarta.ejb."
echo "	javax.enterprise.  -> jakarta.enterprise."
echo "	javax.faces.       -> jakarta.faces."
echo "	javax.inject.      -> jakarta.inject."
echo "	javax.persistence. -> jakarta.persistence."
echo "	javax.ws.          -> jakarta.ws."
echo "Replacing now..."

###################
## REPLACE LOGIC ##
###################

# replace package names...
find * -name '*.java' | xargs perl -pi -e "s/javax.annotation./jakarta.annotation./g"
find * -name '*.java' | xargs perl -pi -e "s/javax.ejb./jakarta.ejb./g"
find * -name '*.java' | xargs perl -pi -e "s/javax.enterprise./jakarta.enterprise./g"
find * -name '*.java' | xargs perl -pi -e "s/javax.faces./jakarta.faces./g"
find * -name '*.java' | xargs perl -pi -e "s/javax.inject./jakarta.inject./g"
find * -name '*.java' | xargs perl -pi -e "s/javax.persistence./jakarta.persistence./g"
find * -name '*.java' | xargs perl -pi -e "s/javax.ws./jakarta.ws./g"

echo "DONE!"