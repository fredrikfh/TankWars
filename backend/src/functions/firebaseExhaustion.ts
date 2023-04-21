import { log } from './console';
import * as https from 'https';

const schedule = require('node-schedule');

/**
 * This function notifies the slack channel when firebase is exhausted
 * The exhaustion is reset at midnight every day
 */

let exhausted = false;

// Schedule a task to run at midnight every day
schedule.scheduleJob('0 0 * * *', () => {
  if (exhausted) {
    exhausted = false;
    notifySlack('Firebase is ready again');
    log('Exhaustion reset');
  }
});

export function setExhausted() {
  exhausted = true;
  log('Firebase is exhausted', 'danger');
  notifySlack('Firebase is exhausted for the day');
}

export function isExhausted() {
  return exhausted;
}

function notifySlack(message: string) {
  const slackWebhook =
    'https://hooks.slack.com/services/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';
  const payload = JSON.stringify({
    username: 'NTNU-VM',
    icon_emoji: ':warning:',
    text: message,
  });

  const options = new URL(slackWebhook);

  const requestOptions = {
    hostname: options.hostname,
    path: options.pathname + options.search,
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Content-Length': Buffer.byteLength(payload),
    },
  };

  const req = https.request(requestOptions, (res) => {
    log('Slack notification sent');
  });

  req.on('error', (error) => {
    log('Slack notification failed');
  });

  req.write(payload);
  req.end();
}
