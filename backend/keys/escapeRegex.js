function jsonToMaskableGitLabCiString(jsonObj) {
    // Convert JSON object to a string
    const jsonString = JSON.stringify(jsonObj);
  
    // Encode JSON string to Base64
    const base64EncodedJson = Buffer.from(jsonString).toString('base64');
  
    // Create a maskable GitLab CI string
    const maskableGitLabCiString = `base64:${base64EncodedJson}`;
  
    return maskableGitLabCiString;
  }

function maskableGitLabCiStringToJson(maskableGitLabCiString) {
    // Verify the maskable GitLab CI string starts with 'base64:'

    if (!maskableGitLabCiString.startsWith('base64:')) {
      throw new Error('Invalid maskable GitLab CI string format.');
    }
  
    // Extract the Base64-encoded JSON string
    const base64EncodedJson = maskableGitLabCiString.substring(7);
  
    // Decode the Base64-encoded JSON string
    const jsonString = Buffer.from(base64EncodedJson, 'base64').toString('utf8');
  
    // Parse the JSON string into a JSON object
    const jsonObj = JSON.parse(jsonString);
  
    return jsonObj;
  }

module.exports = {
    jsonToMaskableGitLabCiString,
    maskableGitLabCiStringToJson,
    };