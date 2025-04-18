/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd.generic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.core.dtd.AbstractBsdCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.opcfoundation.opcua.binaryschema.StructuredType;

public class StructCodec extends AbstractBsdCodec<Struct, Struct.Member> {

  public StructCodec(
      String namespaceUri, NodeId dataTypeId, NodeId encodingId, StructuredType structuredType) {

    super(namespaceUri, dataTypeId, encodingId, structuredType);
  }

  @Override
  protected Struct createStructure(String name, LinkedHashMap<String, Struct.Member> members) {
    return new Struct(name, members);
  }

  @Override
  protected Map<String, Struct.Member> getMembers(Struct value) {
    return value.getMembers();
  }

  @Override
  protected Struct.Member opcUaToMemberTypeScalar(String name, Object value, String typeName) {
    return new Struct.Member(name, value);
  }

  @Override
  protected Struct.Member opcUaToMemberTypeArray(String name, Object values, String typeName) {
    return new Struct.Member(name, values);
  }

  @Override
  protected Object memberTypeToOpcUaScalar(Struct.Member member, String typeName) {
    return member.getValue();
  }

  @Override
  protected Object memberTypeToOpcUaArray(Struct.Member member, String typeName) {
    Object value = member.getValue();

    if (value == null) {
      return null;
    } else if (value instanceof List<?> list) {
      return list.toArray();
    } else if (value.getClass().isArray()) {
      List<Object> values = new ArrayList<>();

      Object flattened = ArrayUtil.flatten(value);
      int length = Array.getLength(flattened);
      for (int i = 0; i < length; i++) {
        values.add(Array.get(flattened, i));
      }

      return values.toArray();
    } else {
      return value;
    }
  }
}
