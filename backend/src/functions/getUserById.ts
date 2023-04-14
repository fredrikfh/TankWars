// import { QueryDocumentSnapshot } from 'firebase-admin/firestore';
import { DocumentData } from 'firebase-admin/firestore';
import { User } from '../../types/User';
import { log } from './console';
import admin from './firebaseAdmin';
import { getUsers, sendFirestoreRequest } from './firebaseCache';

export async function getUserById(id: string): Promise<User | null> {
  const useCache = true; // set to false if you want to query firebase for each request
  const usersRef = admin.firestore().collection('users');
  const searchParam = id;
  let userSnapshot; // result from firebase search
  let user: User; // result from cache

  if (searchParam === undefined || searchParam === '') {
    log('no id provided');
    return null;
  }

  // If the cache is enabled, try to get the user from the cache
  if (useCache) {
    const cachedUsers = await getUsers();
    if (cachedUsers) {
      user = cachedUsers.find(
        (u: User) => u.username === searchParam || u.id === searchParam
      );
      if (user) {
        log('User found in cache: ' + user.id);
        return user;
      }
    }
    log("User wasn't found in cache");
  }

  // no hit in cache, search firebase

  let ref = usersRef.where('username', '==', searchParam);
  // const userByUsernameSnapshot = await usersRef.where('username', '==', searchParam).get();

  // Map the response to a more readable format
  // const responseMapper = (firestoreResponse: QueryDocumentSnapshot[]) => {
  //   return firestoreResponse.map((doc) => {
  //     return { id: doc.id, ...doc.data() };
  //   });
  // };

  let result = await sendFirestoreRequest(ref);
  if (result) {
    // return result;
    const myData = (result as { id: string; data: DocumentData }[])[0];
    // map the contents of myData into a User object
    const user: User = {
      id: myData.id,
      username: myData.data.username,
      wins: myData.data.wins,
      losses: myData.data.losses,
      highscore: myData.data.highscore,
      games: myData.data.games,
    };

    log('User found by username: ' + myData.data.username);
    return user;
  } else {
    let usernameRef = usersRef.doc(searchParam);
    result = null;
    result = await sendFirestoreRequest(usernameRef);
    if (result) {
      const myData = result as { id: string; data: DocumentData };
      if (myData.data != null) {
        // map the contents of myData into a User object
        const user: User = {
          id: myData.id,
          username: myData.data.username,
          wins: myData.data.wins,
          losses: myData.data.losses,
          highscore: myData.data.highscore,
          games: myData.data.games,
        };

        log('User found by id: ' + user.id);
        return user;
      }
    }
  }

  log('User not found: ' + searchParam);
  return null;
}
