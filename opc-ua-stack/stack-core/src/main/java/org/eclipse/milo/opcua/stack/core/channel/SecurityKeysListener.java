/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

/**
 * A listener that is notified when symmetric security keys are derived during an OpenSecureChannel
 * handshake.
 *
 * <p>Implementations must be thread-safe — multiple channels may derive keys concurrently.
 *
 * @see SecurityKeyset
 * @see WiresharkKeyLogWriter
 */
public interface SecurityKeysListener {

  /**
   * Called after symmetric keys have been derived and installed on a SecureChannel.
   *
   * @param keyset the derived keyset for the new security token.
   */
  void onSecurityKeysCreated(SecurityKeyset keyset);
}
