/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.sdk.core.types.json;

import com.google.gson.JsonObject;
import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class JsonStruct implements UaStructuredType {

  private final DataType dataType;
  private final JsonObject jsonObject;

  public JsonStruct(DataType dataType, JsonObject jsonObject) {
    this.dataType = dataType;
    this.jsonObject = jsonObject;
  }

  public DataType getDataType() {
    return dataType;
  }

  public JsonObject getJsonObject() {
    return jsonObject;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return dataType.getNodeId().expanded();
  }

  @Override
  public String getTypeName() {
    return dataType.getBrowseName().name();
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    NodeId binaryEncodingId = dataType.getBinaryEncodingId();
    return binaryEncodingId != null ? binaryEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    NodeId xmlEncodingId = dataType.getXmlEncodingId();
    return xmlEncodingId != null ? xmlEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    NodeId jsonEncodingId = dataType.getJsonEncodingId();
    return jsonEncodingId != null ? jsonEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    JsonStruct that = (JsonStruct) object;
    return Objects.equals(getDataType(), that.getDataType())
        && Objects.equals(getJsonObject(), that.getJsonObject());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDataType(), getJsonObject());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", JsonStruct.class.getSimpleName() + "[", "]")
        .add("dataType=" + dataType)
        .add("jsonObject=" + jsonObject)
        .toString();
  }
}
