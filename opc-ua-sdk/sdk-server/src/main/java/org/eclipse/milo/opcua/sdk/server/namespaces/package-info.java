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
 * Server-owned namespaces that load the standard model and expose server state and diagnostics.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace} loads generated nodes and
 * configures their runtime behavior. Its standard subscription and Condition refresh Method
 * adapters declare fixed argument metadata, validate inputs through {@link
 * org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler}, and use the calling
 * session when consulting the subscription or Condition manager. Normal Call dispatch remains
 * responsible for access and Method ownership checks.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.namespaces.ServerNamespace} owns server-specific
 * fragments such as diagnostics. Fragments shut down before their owning namespace unregisters, so
 * their nodes and managers remain available during cleanup.
 */
package org.eclipse.milo.opcua.sdk.server.namespaces;
