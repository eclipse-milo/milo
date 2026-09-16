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
 * <p>Structure codecs control optional field presence by invoking encoders only for present fields.
 * Null array fields are omitted in COMPACT and emitted as named JSON {@code null} in VERBOSE. Empty
 * arrays remain {@code []} in both modes. Outside a structure, null arrays are emitted as JSON
 * {@code null}, preserving positions in enclosing arrays. Array decoders accept both omitted fields
 * and explicit JSON {@code null} as null arrays, while preserving empty arrays. ExtensionObject
 * arrays preserve null elements as JSON {@code null} in both modes. The scalar VERBOSE
 * ExtensionObject default of {@code {}} applies outside these array elements.
 *
 * <p>COMPACT is the default mode for both directions. For VERBOSE, configure the encoder and
 * decoder with {@code OpcUaJsonEncoder.Encoding.VERBOSE}. Structure codecs must use the semantic
 * mask and selector operations: VERBOSE omits those headers and reconstructs them from field names
 * supplied by the codec, while COMPACT retains numeric headers. Enum fields use numeric values in
 * COMPACT and strings containing the numeric value in VERBOSE. Legacy codecs that read and write
 * headers as ordinary UInt32 fields must migrate before using VERBOSE. RawData type inference is
 * not supported; these modes do not infer missing runtime type identifiers.
 *
 * <p>For structures, {@link org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaJsonDecoder}
 * buffers the current object and lets its codec select members by name. Each nested structure has
 * its own lookup scope, and the outer reader is restored when decoding succeeds or fails. Missing
 * members retain their type's default value; unread members are errors. Reset discards the previous
 * input and lookup state.
 *
 * <p>ExtensionObjects use the OPC UA 1.05 Compact/Verbose mapping. Native structure fields share an
 * object with {@code UaTypeId}; Binary and XML bodies use {@code UaEncoding} and a base64 {@code
 * UaBody}. XML bytes are UTF-8. The type manager translates between the envelope's data type id and
 * the Binary/XML wrapper's encoding id without decoding the body. Unresolved Binary/XML identities
 * are errors; unknown native types retain their JSON body for later decoding. Messages use this
 * same envelope. Legacy nested native envelopes are not unwrapped, and legacy header aliases are
 * not recognized. A native field named {@code UaBody} remains a structure field and is never
 * unwrapped.
 *
 * <p>Buffered structures, ExtensionObject envelopes, and Variant bodies preserve numeric tokens and
 * reject duplicate names before a JSON tree could discard them. Built-in value decoders retain
 * their own unknown-field policies. Input character and buffered-container depth limits come from
 * the encoding context; size or depth violations report {@code Bad_EncodingLimitsExceeded}, while
 * malformed values report {@code Bad_DecodingError}.
 *
 * <p>Variants carry structures as ExtensionObjects. Non-null structure bodies use their registered
 * encoding. Null elements of typed structure arrays and Matrices are JSON {@code null} in both
 * modes, as required for array elements by OPC UA Part 6, 5.4.5. Decoding returns ExtensionObject
 * arrays with those null positions preserved; callers decode non-null bodies using the context.
 */
package org.eclipse.milo.opcua.stack.core.encoding.json;
