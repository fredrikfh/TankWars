import NodeCache from 'node-cache';
import admin from './firebaseAdmin';
import { log } from './console';

/**
 * This is a cache and firebaseHandler for the firestore database
 */

const queryCache = new NodeCache({ stdTTL: 60 }); // 60 seconds TTL

export async function getUsers(): Promise<any> {
  const usersRef = admin.firestore().collection('users');
  const cacheKey = 'userRef';
  return retrieveFromCache(usersRef, queryCache, cacheKey);
}

export async function getTopUsers(): Promise<any> {
  const usersRef = admin.firestore().collection('users');
  const querySnapshot = await usersRef.orderBy('highscore', 'desc').limit(10);
  const cacheKey = 'topUsers';
  return retrieveFromCache(querySnapshot, queryCache, cacheKey);
}

/**
 * A function that retrieves data from the cache if it exists, otherwise it retrieves it from firestore
 *
 * @param ref Firestore reference
 * @param cache The cache to retrieve data from
 * @param cacheKey The key to use for the cache
 * @returns cached or firestore result (null if error)
 */
async function retrieveFromCache(
  ref: any,
  cache: NodeCache,
  cacheKey: string
): Promise<any | null> {
  const cachedValue = cache.get(cacheKey);

  if (cachedValue) {
    log('Cache-hit: retrieved from cache');
    return cachedValue;
  } else {
    log('Cache-miss: retrieving from firestore...');
    return await sendFirestoreRequest(ref, cache, cacheKey);
  }
}

/**
 * Sends a request to firestore and returns the result
 *
 * @param ref
 * @param cache
 * @param cacheKey
 * @returns firestore result
 */
async function sendFirestoreRequest(
  ref: any,
  cache: NodeCache,
  cacheKey: string
): Promise<any | null> {
  try {
    log('firebase: sending request to firestore...');
    const snapshot = await ref.get();
    cache.set(cacheKey, snapshot);
    log("Cache-set: set cache for key '" + cacheKey + "'");
    return snapshot;
  } catch (err: any) {
    if (err?.code === 8) {
      log('error: firestore max-limit reached', 'danger');
    } else {
      log('error: firestore error code ' + err?.code, 'danger');
    }
    return null;
  }
}
