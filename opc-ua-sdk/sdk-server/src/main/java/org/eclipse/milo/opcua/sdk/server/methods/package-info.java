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
 * returns a complete result, so it can report Uncertain with outputs or Bad without them. A handler
 * overriding the result callback can also override {@code getRequiredInputArgumentCount} to let
 * callers omit trailing inputs; omitted inputs are absent from the supplied array, while a supplied
 * null keeps its position. A result that breaks the CallMethodResult rules is logged and reported
 * as Bad_InternalError.
 *
 * <p>When dispatched through the Call service, the invocation context exposes the request's {@link
 * org.eclipse.milo.opcua.sdk.server.DiagnosticsContext}. A handler interns diagnostic strings there
 * and returns argument diagnostics that index them, one per supplied input. After every
 * address-space group has finished, the service keeps only the fields the ReturnDiagnostics mask
 * requests and builds the response StringTable from the strings those fields reference.
 */
package org.eclipse.milo.opcua.sdk.server.methods;
