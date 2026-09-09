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
 * Converts OPC UA values between JSON and the stack's Java value types.
 *
 * <p>Encoders and decoders use an encoding context to resolve namespaces and structured-type
 * codecs. The encoder supports compact and verbose output; the decoder reads each value from its
 * current input position. Callers supply the context and manage the encoder's output lifecycle.
 *
 * <p>Variants carry structures as ExtensionObjects. Non-null structure bodies use their registered
 * encoding. Null elements of typed structure arrays and Matrices are JSON {@code null} in both
 * modes, as required for array elements by OPC UA Part 6, 5.4.5. Decoding returns ExtensionObject
 * arrays with those null positions preserved; callers decode non-null bodies using the context.
 */
package org.eclipse.milo.opcua.stack.core.encoding.json;
