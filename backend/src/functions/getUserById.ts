import { User } from '../../types/User';
import { log } from './console';
import admin from './firebaseAdmin';

export async function getUserById(id: string): Promise<User | null> {
  const usersRef = admin.firestore().collection('users');
  const snapshot = await usersRef.get();
  const searchParam = id;

  const user =
    snapshot.docs.find((doc: { id: string }) => doc.id === searchParam) ||
    (
      await Promise.all(
        snapshot.docs.map(async (doc: { id: string }) => {
          const userSnapshot = await usersRef.doc(doc.id).get();
          return userSnapshot.data().username === searchParam ? userSnapshot : null;
        })
      )
    ).find(Boolean);

  if (user) {
    return user.data() as User;
  } else {
    log('User not found');
    return null;
  }
}
