import NodeCache from 'node-cache';
// import admin from './firebaseAdmin';
import admin from './firebaseAdmin';
import { log } from './console';
import {
  CollectionReference,
  DocumentReference,
  DocumentSnapshot,
  Query,
  QueryDocumentSnapshot,
  QuerySnapshot,
} from 'firebase-admin/firestore';
import { User } from '../../types/User';
import { setExhausted } from './firebaseExhaustion';

/**
 * This is a cache and firebaseHandler for the firestore database
 * All functions that retrieve data from firestore should be implemented here
 */

// cache for all queries
const queryCache = new NodeCache({ stdTTL: 60 }); // 60 seconds TTL

/**
 * This function returns user-id and user-data.
 *
 * @returns a list of users
 */
export async function getUsers(): Promise<any> {
  const usersRef = admin.firestore().collection('users');
  const cacheKey = 'users';
  const responseMapper = (firestoreResponse: QueryDocumentSnapshot[]) => {
    return firestoreResponse.map((doc) => {
      return { id: doc.id, ...doc.data };
    });
  };
  return retrieveFromCache(usersRef, queryCache, cacheKey, responseMapper);
}

/**
 * This function returns user-id of all users.
 *
 * @returns a list of user ids
 */
export async function getUsersIds(): Promise<any> {
  const usersRef = admin.firestore().collection('users');
  const cacheKey = 'userIds';
  const responseMapper = (firestoreResponse: QueryDocumentSnapshot[]) => {
    return firestoreResponse.map((doc) => {
      return doc.id;
    });
  };
  return retrieveFromCache(usersRef, queryCache, cacheKey, responseMapper);
}

/**
 * This function returns the top 10 users (highscore).
 *
 * @returns a list of users
 */
export async function getTopUsers(): Promise<User[] | null> {
  const usersRef: CollectionReference = admin.firestore().collection('users');
  const querySnapshot: Query = usersRef.orderBy('highscore', 'desc').limit(10);
  const cacheKey = 'topUsers';
  const responseMapper = (firestoreResponse: QueryDocumentSnapshot[]) => {
    return firestoreResponse.map((doc) => {
      return { ...doc.data } as User;
    });
  };
  return retrieveFromCache(querySnapshot, queryCache, cacheKey, responseMapper);
}

/**
 * A function that retrieves data from the cache if it exists, otherwise it retrieves it from firestore
 *
 * @param ref Firestore reference
 * @param cache The cache to retrieve data from
 * @param cacheKey The key to use for the cache
 * @param responseMapper A function that maps the firestore response to the desired format
 *
 * @returns cached or firestore result (null if error)
 */
async function retrieveFromCache<T>(
  ref: CollectionReference<T> | Query<T> | DocumentReference<T>,
  cache: NodeCache,
  cacheKey: string,
  responseMapper?: Function
): Promise<any | null> {
  const cachedValue = cache.get(cacheKey);

  if (cachedValue) {
    log('Cache-hit: retrieved from cache');
    return cachedValue;
  } else {
    log('Cache-miss: retrieving from firestore...');
    const firestoreResponse = await sendFirestoreRequest<T>(ref);

    if (!firestoreResponse) return null;

    const response = responseMapper
      ? responseMapper(firestoreResponse)
      : firestoreResponse;
    cache.set(cacheKey, response);
    log("Cache-set: set cache for key '" + cacheKey + "'");
    return response;
  }
}

/**
 * Sends a request to firestore and returns the result
 *
 * @param ref a firestore collection reference
 * @returns firestore result
 */
export async function sendFirestoreRequest<T>(
  ref: CollectionReference<T> | Query<T> | DocumentReference<T>
): Promise<Array<{ id: string; data: T }> | { id: string; data: T | null } | null> {
  try {
    log('firebase: sending request to firestore...', 'warning');
    let data = null;

    if (ref instanceof CollectionReference || ref instanceof Query) {
      const snapshot = (await (
        ref as CollectionReference<T> | Query<T>
      ).get()) as QuerySnapshot<T>;

      if (snapshot.empty) {
        // log('No matching documents.');
        return null;
      }

      data = snapshot.docs.map((doc) => {
        return { id: doc.id, data: doc.data() };
      });
    } else if (ref instanceof DocumentReference) {
      const snapshot = (await (ref as DocumentReference<T>).get()) as DocumentSnapshot<T>;
      // log('document snapshot: ' + JSON.stringify(snapshot));

      if (snapshot.exists) {
        data = { id: snapshot.id, data: snapshot.data() || null };
      } else {
        data = { id: snapshot.id, data: null };
      }
    } else {
      throw new Error('Unsupported reference type');
    }

    return data;
  } catch (err: any) {
    if (err?.code === 8) {
      log('error: firestore max-limit reached', 'danger');
      setExhausted();
    } else {
      log('error: firestore error code ' + err?.code, 'danger');
    }
    return null;
  }
}
