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
 * Returned synchronous handlers for standard Method declarations.
 *
 * <p>A generated model interface exposes bind methods accepting these callbacks and a {@link
 * org.eclipse.milo.opcua.sdk.server.methods.MethodBindings} registry. A callback returns no value,
 * one value, or a common named output contract. Detailed callbacks also return operation status and
 * argument diagnostics. Generated Bad outcomes carry no wire outputs; an application StatusCode
 * output remains separate from the operation status.
 *
 * <p>The supplied registry dispatches by invocation ObjectId. Callers own the returned {@link
 * org.eclipse.milo.opcua.sdk.server.methods.MethodBinding} token and close it to unregister their
 * binding. Closing a selecting view does not close that independently owned token. Handlers execute
 * synchronously; their return values do not introduce asynchronous server dispatch.
 *
 * <p>These interfaces are generated from the standard NodeSet. Existing manager-owned server
 * Methods can retain their own AbstractMethodInvocationHandler installation and lifetime.
 */
package org.eclipse.milo.opcua.sdk.server.model.methods;
