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
 * values. Decoding skips non-element nodes at value boundaries without modifying the input DOM or
 * whitespace inside String and XmlElement payloads. Named fields omitted from an active structure,
 * including trailing fields and all fields of an empty structure, use the field decoder defaults.
 * Reads without an available element outside a structure remain decoding errors.
 *
 * <p>XML element namespaces are separate from NodeId namespaces. Supply authoritative
 * model-to-schema mappings through {@code EncodingContext.withXmlNamespaceUris} and pass that view
 * to the encoder or Default XML ExtensionObject encoding. Internally created encoders use the same
 * context. Missing mappings preserve the model URI for compatibility. Structure fields belong to
 * their declaring schema; nested structure contents and typed array items use their type's schema.
 * Codecs that delegate inherited fields to a base codec can enter it through {@code encodeStruct}
 * with a null field name to apply its namespace without adding a wrapper. Flat codecs can instead
 * be registered through {@link org.eclipse.milo.opcua.stack.core.encoding.xml.XmlDataTypeCodec}.
 * The adapter supplies immediate field names and their declaring model URIs, including an inherited
 * {@code EncodingMask}. It activates metadata only while its delegate encodes; the XML encoder
 * resolves the schema namespaces and keeps nested contents in their own scopes. The codec still
 * owns field order, optional bits, and the number of masks written. No generated code changes or
 * model discovery are required, but all relevant registrations must use the adapter. The decoder
 * matches structure fields by local name and does not validate schemas, so a round trip alone
 * cannot establish XML namespace conformance.
 *
 * <p>Nullable strings, byte strings, XML elements, and arrays retain the distinction between an
 * empty value and a value marked null by the XML Schema instance {@code nil} attribute. This
 * attribute is interpreted only on UA value containers, never inside opaque XML payloads. Named
 * array fields contain their members directly, without an additional {@code List} wrapper. Each
 * member uses its value decoder's null/default rules independently of the array container. Array
 * reads restore the cursor to the following field even when a member or length check fails.
 *
 * <p>Variants identify their contained builtin type from the XML element name. Matrix decoding
 * validates dimensions, element types, and element counts for both Variants and directly decoded
 * matrices. Java null and {@link org.eclipse.milo.opcua.stack.core.types.builtin.Matrix#ofNull()}
 * both encode as a null matrix without requiring type metadata. Structured values are delegated to
 * the codecs registered in the context.
 *
 * <p>ExtensionObject decoding preserves the encoding identified by the Body payload. A ByteString
 * element in the OPC UA Types.xsd namespace carries binary bytes; other payload elements remain XML
 * structures. The encoding id is retained for subsequent decoding with the context's codecs.
 *
 * <p>Structures inside Variants are carried as ExtensionObjects. Null elements of typed structure
 * arrays and Matrices use an {@code xsi:nil} ExtensionObject element; they are not passed to a
 * structure codec. Decoding retains these positions as null-valued ExtensionObjects. Matrices
 * passed to the Variant encoder need explicit data type metadata when their first element cannot
 * identify the structure type.
 */
package org.eclipse.milo.opcua.stack.core.encoding.xml;
