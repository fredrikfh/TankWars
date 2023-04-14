import path from 'path';

// establish connection to firebase
// const admin = require('firebase-admin');
import * as admin from 'firebase-admin';
const serviceAccount = path.join(__dirname, '../../..', 'keys', 'fb-key.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

export default admin;
