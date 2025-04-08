/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
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

  ExtensionObject encode(EncodingContext context, UaStructuredType struct, NodeId encodingId);

  UaStructuredType decode(EncodingContext context, ExtensionObject encoded, NodeId encodingId);
}
