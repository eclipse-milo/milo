/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

enum State {
  Inactive,
  CreatingWait,
  Creating,
  Activating,
  Transferring,
  Initializing,
  Active,
  Closing,
  ReactivatingWait,
  Reactivating
}
