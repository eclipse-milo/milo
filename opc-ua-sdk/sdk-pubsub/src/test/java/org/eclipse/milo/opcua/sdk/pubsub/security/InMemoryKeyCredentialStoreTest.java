/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.security;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link InMemoryKeyCredentialStore}: lookup/put/remove behavior and the copy semantics
 * of the zeroization posture — the store keeps its own copy of a secret, and every lookup returns a
 * fresh copy the caller owns (and wipes).
 *
 * <p>The wipe of the store's internal copy on remove/replace is not observable from outside (the
 * store never hands out its internal array), so these tests pin the observable half of the
 * contract: isolation of the stored copy from both the put-caller's array and every lookup-returned
 * array.
 */
class InMemoryKeyCredentialStoreTest {

  private static final String RESOURCE_URI = "urn:milo:sks";

  @Test
  void lookupOfUnknownUriReturnsEmpty() {
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();

    assertTrue(store.lookup("urn:unknown").isEmpty());
  }

  @Test
  void putThenLookupReturnsTheCredential() {
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();
    store.put(RESOURCE_URI, "user1", "secret1".toCharArray());

    Optional<KeyCredential> credential = store.lookup(RESOURCE_URI);

    assertTrue(credential.isPresent());
    assertEquals("user1", credential.orElseThrow().credentialId());
    assertArrayEquals("secret1".toCharArray(), credential.orElseThrow().secret());
  }

  @Test
  void putStoresACopyOfTheSecret() {
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();

    char[] secret = "secret1".toCharArray();
    store.put(RESOURCE_URI, "user1", secret);

    // The caller retains ownership of its array and may wipe it immediately.
    Arrays.fill(secret, '\0');

    assertArrayEquals("secret1".toCharArray(), store.lookup(RESOURCE_URI).orElseThrow().secret());
  }

  @Test
  void lookupReturnsAFreshCopyEachCall() {
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();
    store.put(RESOURCE_URI, "user1", "secret1".toCharArray());

    char[] first = store.lookup(RESOURCE_URI).orElseThrow().secret();
    char[] second = store.lookup(RESOURCE_URI).orElseThrow().secret();

    assertNotSame(first, second);

    // The caller wipes its copy after use; the store must be unaffected.
    Arrays.fill(first, '\0');

    assertArrayEquals("secret1".toCharArray(), store.lookup(RESOURCE_URI).orElseThrow().secret());
  }

  @Test
  void putReplacesAnExistingCredential() {
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();
    store.put(RESOURCE_URI, "user1", "secret1".toCharArray());
    store.put(RESOURCE_URI, "user2", "secret2".toCharArray());

    KeyCredential credential = store.lookup(RESOURCE_URI).orElseThrow();

    assertEquals("user2", credential.credentialId());
    assertArrayEquals("secret2".toCharArray(), credential.secret());
  }

  @Test
  void removeDeletesTheCredential() {
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();
    store.put(RESOURCE_URI, "user1", "secret1".toCharArray());

    store.remove(RESOURCE_URI);

    assertTrue(store.lookup(RESOURCE_URI).isEmpty());
  }

  @Test
  void removeOfUnknownUriIsANoOp() {
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();

    store.remove("urn:unknown"); // must not throw

    assertTrue(store.lookup("urn:unknown").isEmpty());
  }

  @Test
  void removeDoesNotWipeCopiesAlreadyHandedOut() {
    // lookup transfers ownership of the returned copy to the caller; a later remove wipes
    // only the store's internal copy.
    InMemoryKeyCredentialStore store = new InMemoryKeyCredentialStore();
    store.put(RESOURCE_URI, "user1", "secret1".toCharArray());

    char[] borrowed = store.lookup(RESOURCE_URI).orElseThrow().secret();
    store.remove(RESOURCE_URI);

    assertArrayEquals("secret1".toCharArray(), borrowed);
  }

  @Test
  void credentialToStringOmitsTheSecret() {
    KeyCredential credential = new KeyCredential("user1", "hunter2".toCharArray());

    String s = credential.toString();

    assertTrue(s.contains("user1"), "unexpected toString: " + s);
    assertFalse(s.contains("hunter2"), "secret leaked into toString: " + s);
  }
}
