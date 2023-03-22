#!/bin/bash

# Kill the service running on port 80, if any
sudo kill $(sudo lsof -t -i:80)

# Start the Express.js app
cd /home/git/tdt4240-tank-wars/backend && sudo git pull && sudo npx yarn && sudo npx yarn start >/dev/null 2>&1 &

# Wait for the app to start and check the status
BACKEND_PID=$!
sleep 10
if ps -p $BACKEND_PID > /dev/null; then 
  echo "Backend started successfully"
  exit 0
else 
  echo "Backend failed to start"
  exit 1
fi

