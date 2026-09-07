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
 * OPC UA values shared by the protocol encoders and the client and server SDKs. A {@link
 * org.eclipse.milo.opcua.stack.core.types.builtin.Variant} carries a scalar, array, or {@link
 * org.eclipse.milo.opcua.stack.core.types.builtin.Matrix}. A {@link
 * org.eclipse.milo.opcua.stack.core.types.builtin.DataValue} adds status and timestamps for reads,
 * writes, and subscription notifications. Matrix preserves the dimensions of a multidimensional
 * value alongside its flattened elements so encoders and SDK operations can use the same value.
 *
 * <p>These containers do not establish exclusive ownership of their contents. Wrapping an array or
 * mutable element does not make it immutable. Callers sharing values with the SDK should keep them
 * unchanged while consumers retain them, including for change detection or use in hash collections.
 * Create an independent value before updating shared contents; copying an array alone does not
 * isolate mutable objects inside it. See the individual types for their copying and accessor
 * contracts.
 */
package org.eclipse.milo.opcua.stack.core.types.builtin;
