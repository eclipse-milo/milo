/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;

/**
 * Adds declaring model namespaces to an existing codec's XML fields without changing its field or
 * optional-mask logic.
 *
 * <p>Use this adapter when inherited fields belong to a different model than the encoded structure.
 * Keys are immediate field names, including {@code EncodingMask} when its declaration is inherited.
 * Values are model URIs, resolved through {@link EncodingContext#getXmlNamespaceUris()}. Unlisted
 * fields keep the encoder's enclosing namespace. Nested structure contents, builtin components, and
 * array items use their own namespaces. Supply an entry for every field that can be emitted in a
 * different declaring namespace; this adapter does not discover declarations or validate schemas.
 *
 * <p>Register or pass the adapter wherever the original codec was used. For example, to adapt an
 * existing generated codec without regenerating it:
 *
 * <pre>{@code
 * DataTypeCodec xmlCodec = new XmlDataTypeCodec(existingCodec,
 *     Map.of("EncodingMask", resultModelUri, "ResultId", resultModelUri));
 * context.getDataTypeManager().registerType(
 *     typeId, xmlCodec, binaryEncodingId, xmlEncodingId, jsonEncodingId);
 * }</pre>
 *
 * <p>Only {@link OpcUaXmlEncoder} uses the metadata. Other encoders and all decoders are delegated
 * unchanged. Calling the original codec bypasses the adapter. An inner adapter supplies its own
 * complete mapping; it does not merge the enclosing adapter's mapping. This adapter is immutable;
 * concurrent use also requires the delegate to support it.
 */
public final class XmlDataTypeCodec implements DataTypeCodec {

  private final DataTypeCodec delegate;
  private final Map<String, String> fieldNamespaces;

  /**
   * Wraps a codec with a snapshot of its declaring model namespaces.
   *
   * @param delegate the codec that owns the field layout and optional-mask handling.
   * @param fieldNamespaces the immediate field names mapped to declaring model URIs; the URIs need
   *     not be registered in the namespace table.
   * @throws NullPointerException if the delegate, map, a key, or a value is null.
   * @throws IllegalArgumentException if a field name or model URI is blank.
   */
  public XmlDataTypeCodec(DataTypeCodec delegate, Map<String, String> fieldNamespaces) {
    this.delegate = requireNonNull(delegate);
    this.fieldNamespaces = Map.copyOf(fieldNamespaces);
    this.fieldNamespaces.forEach(
        (field, namespace) -> {
          if (field.isBlank() || namespace.isBlank()) {
            throw new IllegalArgumentException(
                "field names and model namespace URIs must not be blank");
          }
        });
  }

  @Override
  public Class<?> getType() {
    return delegate.getType();
  }

  @Override
  public UaStructuredType decode(EncodingContext context, UaDecoder decoder) {
    return delegate.decode(context, decoder);
  }

  @Override
  public void encode(EncodingContext context, UaEncoder encoder, UaStructuredType value) {
    if (encoder instanceof OpcUaXmlEncoder xmlEncoder) {
      xmlEncoder.withFieldNamespaces(
          fieldNamespaces, () -> delegate.encode(context, encoder, value));
    } else {
      delegate.encode(context, encoder, value);
    }
  }
}
