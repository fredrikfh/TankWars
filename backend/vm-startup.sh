#!/bin/bash

# Install PM2 globally if not already installed
if ! command -v pm2 &> /dev/null; then
  sudo npm install pm2 -g
fi

# Start the Express.js app
cd /home/git/tdt4240-tank-wars/backend && sudo git pull && sudo npx yarn && sudo npx yarn start:prod


# Wait for the app to start and check the status
sleep 10
BACKEND_STATUS=$(sudo pm2 show backend --no-color | grep "status" | awk '{print $4}')

if [ "$BACKEND_STATUS" == "online" ]; then
  echo "Backend started successfully"
  exit 0
else
  echo "Backend failed to start"
  exit 1
fi