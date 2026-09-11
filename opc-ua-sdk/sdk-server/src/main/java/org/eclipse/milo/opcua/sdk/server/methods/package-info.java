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
 * <p>{@link org.eclipse.milo.opcua.sdk.server.methods.MethodBindings} owns explicit application
 * registrations for Methods shared by several Objects. One dispatcher per Method selects by the
 * invocation ObjectId and invokes application code outside its lifecycle lock. Each {@link
 * org.eclipse.milo.opcua.sdk.server.methods.MethodBinding} token owns only its registration;
 * closing a replaced token cannot remove the replacement. The last registration restores the
 * captured fallback only while the registry still owns the Method handler. External raw replacement
 * takes precedence, and another registry cannot chain its dispatcher around an active one.
 *
 * <p>Applications close tokens or remove ObjectId registrations before deleting owner nodes, and
 * close the registry on namespace shutdown. Cleanup does not delete nodes, cancel callbacks or wait
 * for selected invocations. A callback selected before cleanup can complete afterward. Actual Call
 * dispatch continues to enforce Method ownership, ConditionManager precedence and session access.
 */
package org.eclipse.milo.opcua.sdk.server.methods;
