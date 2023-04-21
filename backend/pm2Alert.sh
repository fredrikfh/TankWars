#!/bin/bash

# Replace this variable with your webhook URL
webhook_url="https://hooks.slack.com/services/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"

# You can customize the message and other fields as needed
payload="{
  \"username\": \"PM2 Alert\",
  \"icon_emoji\": \":warning:\",
  \"text\": \"PM2 process stopped or crashed. Please check the server.\"
}"

curl -X POST -H 'Content-type: application/json' --data "$payload" "$webhook_url"
