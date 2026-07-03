/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;

/**
 * The message security observed on one received UADP NetworkMessage: the mode indicated by the
 * SecurityFlags, the SecurityTokenId, and the processing {@link SecurityOutcome}.
 *
 * <p>Surfaced as {@link DecodedNetworkMessage#security()}; {@code null} there means the wire
 * message carried security mode None (no SecurityHeader, or a SecurityHeader with no SecurityFlags
 * set) — that null is the "received None" input to the receive-side mode acceptance gate.
 *
 * @param mode the mode seen on the wire: {@link MessageSecurityMode#Sign} or {@link
 *     MessageSecurityMode#SignAndEncrypt}.
 * @param securityTokenId the SecurityTokenId from the SecurityHeader.
 * @param outcome the security processing outcome; anything but {@link SecurityOutcome#VERIFIED} is
 *     a header-only skip.
 */
public record ReceivedSecurity(
    MessageSecurityMode mode, UInteger securityTokenId, SecurityOutcome outcome) {}
