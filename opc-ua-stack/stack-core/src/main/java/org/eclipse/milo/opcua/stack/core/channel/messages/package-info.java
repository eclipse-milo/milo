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
 * UA-TCP message values and codecs used at the transport handshake boundary.
 *
 * <p>This package covers the simple UACP messages that are exchanged before or outside normal
 * SecureChannel chunk processing: {@link
 * org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage HelloMessage}, {@link
 * org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage ReverseHelloMessage},
 * {@link org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage AcknowledgeMessage},
 * and {@link org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage ErrorMessage}. Each
 * value class owns the body layout for one message, while {@link
 * org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder TcpMessageEncoder}, {@link
 * org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder TcpMessageDecoder}, and
 * {@link org.eclipse.milo.opcua.stack.core.channel.messages.MessageType MessageType} own the common
 * UA-TCP header representation.
 *
 * <p>Secure messages such as {@code OPN}, {@code CLO}, and {@code MSG} are identified by {@link
 * org.eclipse.milo.opcua.stack.core.channel.messages.MessageType MessageType}, but their payloads
 * are handled by the channel chunking and security pipeline rather than these simple-message
 * codecs.
 */
package org.eclipse.milo.opcua.stack.core.channel.messages;
