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
 * Message security extension SPI: {@code SecurityKeyProvider} supplies {@code SecurityKeySet} key
 * material for security groups, shaped for eventual Security Key Service (SKS) {@code
 * GetSecurityKeys} integration. Not consumed by the v1 runtime, which supports {@code
 * MessageSecurityMode.None} only.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.security;

import org.jspecify.annotations.NullMarked;
