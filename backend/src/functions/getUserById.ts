import { User } from '../../types/User';
import { log } from './console';
import admin from './firebaseAdmin';

export async function getUserById(id: string): Promise<User | null> {
  const usersRef = admin.firestore().collection('users');
  const searchParam = id;

  // Search for user by id
  log('firebase: searching for user by id: ' + searchParam);
  let userSnapshot = await usersRef.doc(searchParam).get();

  // If the user is not found by id, search for user by username
  if (!userSnapshot.exists) {
    log('firebase: searching for user by username: ' + searchParam);
    const userByUsernameSnapshot = await usersRef
      .where('username', '==', searchParam)
      .get();
    if (!userByUsernameSnapshot.empty) {
      userSnapshot = userByUsernameSnapshot.docs[0];
    }
  }

  if (userSnapshot) {
    log('found user: ' + userSnapshot.id);
    return userSnapshot.data() as User;
  } else {
    log('User not found');
    return null;
  }
}
