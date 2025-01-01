/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.util.Unit;

public interface OpcClientTransport {

  /**
   * Get the {@link OpcClientTransportConfig} associated with this transport.
   *
   * @return the {@link OpcClientTransportConfig} associated with this transport.
   */
  OpcClientTransportConfig getConfig();

  /**
   * Connect this transport implementation.
   *
   * @param applicationContext the {@link ClientApplicationContext} associated with this transport.
   * @return a {@link CompletableFuture} that completes successfully when this transport connects,
   *     or completes exceptionally if an error occurred.
   */
  CompletableFuture<Unit> connect(ClientApplicationContext applicationContext);

  /**
   * Disconnect this transport implementation.
   *
   * @return a {@link CompletableFuture} that completes successfully when this transport
   *     disconnects, or completes exceptionally if an error occurred.
   */
  CompletableFuture<Unit> disconnect();

  /**
   * Send a {@link UaRequestMessageType} on this transport implementation.
   *
   * @param requestMessage the {@link UaRequestMessageType} to send.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     UaResponseMessageType} or completes exceptionally if an error occurred.
   */
  CompletableFuture<UaResponseMessageType> sendRequestMessage(UaRequestMessageType requestMessage);
}
