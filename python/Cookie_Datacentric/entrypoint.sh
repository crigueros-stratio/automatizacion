#!/bin/bash

echo "10.2.39.7 admin.saaslatampd.stratio.com" >> /etc/hosts
echo "10.2.32.68 rocket-comfandi.saaslatampd.stratio.com" >> /etc/hosts

FLASK_APP=main.py flask run --host 0.0.0.0 --port 5000
