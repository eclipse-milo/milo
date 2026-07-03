/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Security Key Service (SKS) pull client: a {@code SecurityKeyProvider} that fetches PubSub key
 * material by calling the well-known {@code GetSecurityKeys} method on an SKS.
 *
 * <p>{@code SksSecurityKeyProvider} implements the Part 14 SecurityKeyServices resolution model
 * (Table 40): each configured entry is an SKS identity record — not a connectable endpoint — that
 * is resolved via the GetEndpoints service at the entry's discovery URLs, keyed by the SKS
 * ApplicationUri. The provider connects with an encrypted session, calls {@code GetSecurityKeys}
 * (ns=0;i=15215) on the PublishSubscribe Object (ns=0;i=14443), and surfaces the outputs as a
 * {@code SecurityKeySet}.
 *
 * <p>The only public type is {@code SksSecurityKeyProvider} (and its builder); everything else in
 * this package is an implementation detail.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.sks;

import org.jspecify.annotations.NullMarked;
