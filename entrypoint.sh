#!/bin/bash

echo "10.2.39.7 admin.saaslatampd.stratio.com" >> /etc/hosts
echo "10.2.32.68 rocket-comfandi.saaslatampd.stratio.com" >> /etc/hosts

java -jar /usr/apps/app.jar
