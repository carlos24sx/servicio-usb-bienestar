#!/bin/bash

echo "******************************"
echo "***** Running Container *****"
echo "******************************"

cd jenkins/ && docker-compose -f docker-compose-servicio-usb-base.yml up -d