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
 * Protocol identities, status constants, namespace tables and checked UA failures shared by the
 * stack and SDKs.
 *
 * <p>Generated Method descriptors depend on this stack layer without selecting a client or server
 * SDK. {@link org.eclipse.milo.opcua.stack.core.UaArgumentConversionException} preserves a failed
 * argument's position so server dispatch can report input diagnostics and client calls can retain
 * output conversion failures separately from the remote operation status.
 */
package org.eclipse.milo.opcua.stack.core;
