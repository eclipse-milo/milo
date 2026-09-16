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
 * Establishes and recovers the Session used by {@link
 * org.eclipse.milo.opcua.sdk.client.OpcUaClient}. The state machine creates and activates Sessions,
 * runs initializers, and monitors connectivity. A reactivation can reuse the same Session object,
 * so object identity alone does not identify an activation.
 *
 * <p>Session activity notifications run in transition order on a serial executor backed by the
 * transport's caller-owned executor. The state machine can continue while an earlier notification
 * waits to run. Consumers such as the publishing manager observe inactive before the subsequent
 * active notification, which lets them suspend and recover their work in that order. Rejected
 * dispatch runs inline to preserve this lifecycle ordering; the client does not shut down the
 * caller's executor.
 *
 * <p>Initializers participate in Session activation. They must complete before operations waiting
 * for an active Session can proceed. Cleanup during initialization must therefore avoid waiting on
 * work that itself needs an active Session.
 *
 * <p>A Session retains its creation endpoint and application certificates. Resolver updates apply
 * to replacement channels and new Sessions. Reactivation keeps the Session's original server
 * certificate and client nonce, but uses the replacement channel certificate and thumbprint for
 * enhanced-policy signature inputs. CreateSession captures the selected endpoint and discovery list
 * together after channel establishment so endpoint comparisons use one resolution result.
 */
package org.eclipse.milo.opcua.sdk.client.session;
