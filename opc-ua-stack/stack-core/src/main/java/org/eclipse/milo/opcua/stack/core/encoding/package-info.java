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
 * Connects data type codecs to wire encoders and decoders through an {@link
 * org.eclipse.milo.opcua.stack.core.encoding.EncodingContext}.
 *
 * <p>The context supplies codec and encoding registries, resource limits, and the namespace and
 * server tables used to resolve identifiers. Codecs read and write fields through {@link
 * org.eclipse.milo.opcua.stack.core.encoding.UaEncoder} and {@link
 * org.eclipse.milo.opcua.stack.core.encoding.UaDecoder}; concrete encoders own the wire format.
 *
 * <p>Codecs use {@code encodeEncodingMask}/{@code decodeEncodingMask} for optional structures and
 * {@code encodeSwitchField}/{@code decodeSwitchField} for unions. Decoders receive optional-field
 * or union-member names in definition order. Binary and XML use numeric headers; other encodings
 * may derive presence and selection from member names. Codecs own which members are present and
 * must encode present null/default values. Ordinary UInt32 fields always use the primitive methods,
 * even when named {@code EncodingMask} or {@code SwitchField}.
 *
 * <p>XML element namespaces are separate from NodeId namespaces. Applications and model libraries
 * supply authoritative model-to-schema mappings using {@code EncodingContext.withXmlNamespaceUris}.
 * The returned view owns an immutable snapshot of the mappings and delegates the other services to
 * the original context. Pass that view throughout the encoding operation, including ExtensionObject
 * encoding, so internally created encoders see the same metadata. Missing mappings retain the model
 * URI for compatibility; they do not establish schema validity. Conflicting mappings must be
 * resolved by the caller before constructing a view. No model loading or schema discovery occurs
 * during encoding.
 */
package org.eclipse.milo.opcua.stack.core.encoding;
