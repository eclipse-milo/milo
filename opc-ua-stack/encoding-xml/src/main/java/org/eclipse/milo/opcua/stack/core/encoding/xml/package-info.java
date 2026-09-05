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
 * Converts OPC UA values between XML and the stack's Java value types.
 *
 * <p>Encoders and decoders use an encoding context for namespaces, limits, and structured-type
 * codecs. A decoder owns its XML cursor and should be used for one input at a time. Element
 * boundaries determine the value layout; indentation and comments between elements do not carry
 * values.
 *
 * <p>Variants identify their contained builtin type from the XML element name. Matrix decoding
 * validates dimensions, element types, and element counts for both Variants and directly decoded
 * matrices. Structured values are delegated to the codecs registered in the context.
 */
package org.eclipse.milo.opcua.stack.core.encoding.xml;
