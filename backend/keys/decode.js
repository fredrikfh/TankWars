const fs = require('fs');
const { maskableGitLabCiStringToJson } = require('./escapeRegex');
const jsonObj = maskableGitLabCiStringToJson(process.env.FB_PRIV_KEY_JSON_ENCODED);
fs.writeFileSync('keys/fb-key.json', JSON.stringify(jsonObj, null, 2));