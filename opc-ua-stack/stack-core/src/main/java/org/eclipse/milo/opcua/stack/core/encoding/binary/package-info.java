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
 * Encodes and decodes OPC UA Binary values in caller-supplied Netty buffers.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder} and {@link
 * org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder} read and write built-in
 * values and delegate structure fields to codecs registered in the {@link
 * org.eclipse.milo.opcua.stack.core.encoding.EncodingContext}. Callers set the buffer before use
 * and remain responsible for releasing it.
 *
 * <p>Variants carry structures as ExtensionObjects, including elements of typed structure arrays
 * and Matrices. Non-null structures use their registered binary encoding; null structure elements
 * use a null ExtensionObject with no encoded body, as defined by OPC 10000-6, 5.2.2.15. Decoding a
 * Variant returns those ExtensionObjects; callers use the encoding context to decode non-null
 * bodies into structures. {@link
 * org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding} supplies this
 * structure-to-body conversion and resolves codecs by their encoding NodeIds.
 */
package org.eclipse.milo.opcua.stack.core.encoding.binary;
