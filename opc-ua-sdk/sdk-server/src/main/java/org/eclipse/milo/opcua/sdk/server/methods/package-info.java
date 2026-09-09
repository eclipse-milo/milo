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
 * representation. A null Matrix is delivered as a null value for any declaration.
 *
 * <p>Validation uses a copy of the request's argument array, so substitutions do not modify the
 * request. Implementations add value constraints through {@code validateInputArgumentValues} and
 * report individual failures with {@link
 * org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException}. The invocation context
 * preserves the calling session, object and Method node.
 */
package org.eclipse.milo.opcua.sdk.server.methods;
