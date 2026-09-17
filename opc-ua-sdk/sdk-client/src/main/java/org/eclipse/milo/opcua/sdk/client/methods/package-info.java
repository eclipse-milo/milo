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
 * Calls Methods on a server and reports their outcomes.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.client.methods.UaMethod} is a Method resolved from an
 * ObjectNode. Its {@code call} forms return the outputs of a Good call and throw {@link
 * org.eclipse.milo.opcua.sdk.client.methods.UaMethodException} for any other operation status. Its
 * {@code callResult} forms return a {@link
 * org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult} instead, which carries the operation
 * status, the per-argument statuses and diagnostics, the response header those diagnostics index,
 * and the outputs of a Good or Uncertain call. A result can be mapped to a typed view; a failed
 * conversion is kept apart from the operation status, and {@code requireGood} collapses either kind
 * of failure back to an exception.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions} carries per-call request
 * settings, the ReturnDiagnostics mask and the timeout hint, through {@code OpcUaClient.callAsync}
 * to the request header.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.client.methods;

import org.jspecify.annotations.NullMarked;
