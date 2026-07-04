/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.junit.jupiter.api.Test;

/**
 * The Part 14 §7.2.4.3 receive-mode matrix, decided per (group, NetworkMessage) by {@link
 * ReaderDispatcher#receiveModeAccepts}: drop received modes below the configured mode (SHALL),
 * process received modes above it (MAY — the keys come from the same token window), drop secured
 * messages to mode-None groups (their keys never resolve), and treat group-level {@code Invalid}
 * (or no config at all) like None.
 */
class ReceiveModeGateTest {

  private static MessageSecurityConfig config(MessageSecurityMode mode) {
    return MessageSecurityConfig.builder()
        .mode(mode)
        .securityGroup(new SecurityGroupRef("SG"))
        .build();
  }

  @Test
  void noneConfiguredAcceptsOnlyReceivedNone() {
    MessageSecurityConfig none = config(MessageSecurityMode.None);

    assertTrue(ReaderDispatcher.receiveModeAccepts(none, MessageSecurityMode.None));
    assertFalse(ReaderDispatcher.receiveModeAccepts(none, MessageSecurityMode.Sign));
    assertFalse(ReaderDispatcher.receiveModeAccepts(none, MessageSecurityMode.SignAndEncrypt));
  }

  @Test
  void absentConfigBehavesLikeNone() {
    assertTrue(ReaderDispatcher.receiveModeAccepts(null, MessageSecurityMode.None));
    assertFalse(ReaderDispatcher.receiveModeAccepts(null, MessageSecurityMode.Sign));
    assertFalse(ReaderDispatcher.receiveModeAccepts(null, MessageSecurityMode.SignAndEncrypt));
  }

  @Test
  void invalidConfiguredBehavesLikeNone() {
    MessageSecurityConfig invalid = config(MessageSecurityMode.Invalid);

    assertTrue(ReaderDispatcher.receiveModeAccepts(invalid, MessageSecurityMode.None));
    assertFalse(ReaderDispatcher.receiveModeAccepts(invalid, MessageSecurityMode.Sign));
    assertFalse(ReaderDispatcher.receiveModeAccepts(invalid, MessageSecurityMode.SignAndEncrypt));
  }

  @Test
  void signConfiguredDropsNoneAndAcceptsSignOrHigher() {
    MessageSecurityConfig sign = config(MessageSecurityMode.Sign);

    // received None while secured-configured: SHALL drop
    assertFalse(ReaderDispatcher.receiveModeAccepts(sign, MessageSecurityMode.None));
    assertTrue(ReaderDispatcher.receiveModeAccepts(sign, MessageSecurityMode.Sign));
    // received above configured: MAY process — same key window decrypts it
    assertTrue(ReaderDispatcher.receiveModeAccepts(sign, MessageSecurityMode.SignAndEncrypt));
  }

  @Test
  void signAndEncryptConfiguredAcceptsOnlySignAndEncrypt() {
    MessageSecurityConfig signAndEncrypt = config(MessageSecurityMode.SignAndEncrypt);

    assertFalse(ReaderDispatcher.receiveModeAccepts(signAndEncrypt, MessageSecurityMode.None));
    // received Sign while configured SignAndEncrypt: SHALL drop, even though the payload may
    // have decoded (the keys exist) — the gate is what enforces it
    assertFalse(ReaderDispatcher.receiveModeAccepts(signAndEncrypt, MessageSecurityMode.Sign));
    assertTrue(
        ReaderDispatcher.receiveModeAccepts(signAndEncrypt, MessageSecurityMode.SignAndEncrypt));
  }
}
