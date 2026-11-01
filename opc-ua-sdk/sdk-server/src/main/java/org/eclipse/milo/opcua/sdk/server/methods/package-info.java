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
 * Handles Method calls dispatched by the server's address spaces.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler} is the call
 * boundary. {@link org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler}
 * checks argument counts, ranks, dimensions and data types before calling application value
 * validation and the implementation callback. Type or shape mismatches produce operation and
 * per-argument statuses without entering the callback. The Method service applies the configured
 * access controller before address-space dispatch resolves the Method. Direct callers are
 * responsible for access checks.
 *
 * <p>BaseDataType and other Variant-backed declarations accept supported Variant payload types
 * while retaining their declared shape constraints. Narrower declarations keep their own type
 * checks. Structure declarations validate decoded type identities using the server's data type tree
 * and encoding context. Their scalar and array inputs are delivered as decoded structures; Matrix
 * inputs retain their original representation. Variant-backed inputs also retain their payload
 * representation. A null Matrix is delivered as a null value for any declaration. An empty value
 * for a declaration of ValueRank 2 or greater is delivered as an empty Matrix of the declared rank,
 * since the wire form of an empty value carries no dimensions.
 *
 * <p>Validation uses a copy of the request's argument array, so substitutions do not modify the
 * request. Implementations add value constraints through {@code validateInputArgumentValues} and
 * report individual failures with {@link
 * org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException}. The invocation context
 * preserves the calling session, object and Method node.
 *
 * <p>A handler chooses one of two callbacks. The output callback, {@code invoke}, receives every
 * declared input and reports Good with its output array. The result callback, {@code invokeResult},
 * returns a {@link org.eclipse.milo.opcua.sdk.server.methods.MethodResult}, so it can report
 * Uncertain with outputs or Bad without them. A handler overriding the result callback can also
 * override {@code getRequiredInputArgumentCount} to let callers omit trailing inputs; omitted
 * inputs are absent from the supplied array, while a supplied null keeps its position. A null
 * result, or an invalid-argument rejection with the wrong number of results, is logged and reported
 * as Bad_InternalError.
 *
 * <h2>Diagnostics</h2>
 *
 * <p>Handlers explain failures with resolved {@link org.eclipse.milo.opcua.stack.core.Diagnostic}s
 * and never handle string-table indexes. The diagnostic of a {@code UaException} thrown from a
 * callback, or attached to a {@code MethodResult}, explains the operation; the per-argument
 * diagnostics of an {@code InvalidArgumentException} explain its input arguments. {@link
 * org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler} reports them to the
 * Call service through the {@link org.eclipse.milo.opcua.sdk.server.CallOperationDiagnostics} of
 * the {@code CallContext} it is invoked with. The Call service returns them, subject to the
 * request's ReturnDiagnostics mask and the server's diagnostics policy, encoded against the
 * response's string table. A handler invoked directly, outside a Call request, has nowhere to
 * report to, and its diagnostics are dropped.
 *
 * <p>A {@link org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler} that builds wire
 * results itself can report resolved diagnostics through the same context. Argument diagnostics it
 * puts directly in its {@code CallMethodResult} cannot refer to the response's string table, so
 * only their index-free fields, additional info and inner status codes, are kept; a requested
 * symbolic id or localized text index drops that operation's argument diagnostics.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.methods.MethodBindings} is an application-owned
 * lifetime for ObjectId-specific handlers on Methods shared by several Objects. Bind validates the
 * ownership relationship and typed argument metadata, then installs the handler on the Method node
 * for that ObjectId. Each {@link org.eclipse.milo.opcua.sdk.server.methods.MethodBinding} token
 * owns only its registration; closing a replaced token cannot remove the replacement. The Method's
 * default handler is never touched, so raw handler replacement and ObjectId registrations do not
 * interfere.
 *
 * <p>Applications close tokens or remove ObjectId registrations before deleting owner nodes, and
 * close the registry on namespace shutdown. Cleanup does not delete nodes, cancel callbacks or wait
 * for selected invocations. A callback selected before cleanup can complete afterward. Actual Call
 * dispatch continues to enforce Method ownership, ConditionManager precedence and session access.
 */
package org.eclipse.milo.opcua.sdk.server.methods;
