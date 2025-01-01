/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

public interface DataTypeEncoding {

  /** QualifiedName of the OPC UA Binary encoding. */
  QualifiedName BINARY_ENCODING_NAME = new QualifiedName(0, "Default Binary");

  /** QualifiedName of the OPC UA XML encoding. */
  QualifiedName XML_ENCODING_NAME = new QualifiedName(0, "Default XML");

  /** QualifiedName of the OPC UA JSON encoding. */
  QualifiedName JSON_ENCODING_NAME = new QualifiedName(0, "Default JSON");

  QualifiedName getEncodingName();

  Object encode(EncodingContext context, Object decodedBody, NodeId encodingId);

  Object decode(EncodingContext context, Object encodedBody, NodeId encodingId);

  default Object encode(EncodingContext context, Object decodedBody, ExpandedNodeId xEncodingId) {
    NodeId encodingId =
        xEncodingId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "namespace not registered: " + xEncodingId.getNamespaceUri()));

    return encode(context, decodedBody, encodingId);
  }

  default Object decode(EncodingContext context, Object encodedBody, ExpandedNodeId xEncodingId) {
    NodeId encodingId =
        xEncodingId
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_DecodingError,
                        "namespace not registered: " + xEncodingId.getNamespaceUri()));

    return decode(context, encodedBody, encodingId);
  }
}
