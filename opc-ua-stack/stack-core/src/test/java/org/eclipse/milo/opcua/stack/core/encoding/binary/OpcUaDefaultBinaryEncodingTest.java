/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;

class OpcUaDefaultBinaryEncodingTest {

  @Test
  void serializationRoundTrip() throws Exception {
    EncodingContext context = new DefaultEncodingContext();
    OpcUaDefaultBinaryEncoding encoding = OpcUaDefaultBinaryEncoding.getInstance();

    var value = new XVType(1.0, 2.0f);
    NodeId encodingId = XVType.XML_ENCODING_ID.toNodeIdOrThrow(context.getNamespaceTable());

    ExtensionObject encoded = encoding.encode(context, value);

    UaStructuredType decoded = encoding.decode(context, encoded);

    assertEquals(value, decoded);
  }
}
