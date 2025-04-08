/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

public interface UaDecoder {

  EncodingContext getEncodingContext();

  Boolean decodeBoolean(String field) throws UaSerializationException;

  Byte decodeSByte(String field) throws UaSerializationException;

  Short decodeInt16(String field) throws UaSerializationException;

  Integer decodeInt32(String field) throws UaSerializationException;

  Long decodeInt64(String field) throws UaSerializationException;

  UByte decodeByte(String field) throws UaSerializationException;

  UShort decodeUInt16(String field) throws UaSerializationException;

  UInteger decodeUInt32(String field) throws UaSerializationException;

  ULong decodeUInt64(String field) throws UaSerializationException;

  Float decodeFloat(String field) throws UaSerializationException;

  Double decodeDouble(String field) throws UaSerializationException;

  String decodeString(String field) throws UaSerializationException;

  DateTime decodeDateTime(String field) throws UaSerializationException;

  UUID decodeGuid(String field) throws UaSerializationException;

  ByteString decodeByteString(String field) throws UaSerializationException;

  XmlElement decodeXmlElement(String field) throws UaSerializationException;

  NodeId decodeNodeId(String field) throws UaSerializationException;

  ExpandedNodeId decodeExpandedNodeId(String field) throws UaSerializationException;

  StatusCode decodeStatusCode(String field) throws UaSerializationException;

  QualifiedName decodeQualifiedName(String field) throws UaSerializationException;

  LocalizedText decodeLocalizedText(String field) throws UaSerializationException;

  ExtensionObject decodeExtensionObject(String field) throws UaSerializationException;

  DataValue decodeDataValue(String field) throws UaSerializationException;

  Variant decodeVariant(String field) throws UaSerializationException;

  DiagnosticInfo decodeDiagnosticInfo(String field) throws UaSerializationException;

  UaMessageType decodeMessage(String field) throws UaSerializationException;

  Integer decodeEnum(String field);

  UaStructuredType decodeStruct(String field, NodeId dataTypeId) throws UaSerializationException;

  UaStructuredType decodeStruct(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException;

  UaStructuredType decodeStruct(String field, DataTypeCodec codec) throws UaSerializationException;

  Boolean @Nullable [] decodeBooleanArray(String field) throws UaSerializationException;

  Byte @Nullable [] decodeSByteArray(String field) throws UaSerializationException;

  Short @Nullable [] decodeInt16Array(String field) throws UaSerializationException;

  Integer @Nullable [] decodeInt32Array(String field) throws UaSerializationException;

  Long @Nullable [] decodeInt64Array(String field) throws UaSerializationException;

  UByte @Nullable [] decodeByteArray(String field) throws UaSerializationException;

  UShort @Nullable [] decodeUInt16Array(String field) throws UaSerializationException;

  UInteger @Nullable [] decodeUInt32Array(String field) throws UaSerializationException;

  ULong @Nullable [] decodeUInt64Array(String field) throws UaSerializationException;

  Float @Nullable [] decodeFloatArray(String field) throws UaSerializationException;

  Double @Nullable [] decodeDoubleArray(String field) throws UaSerializationException;

  String @Nullable [] decodeStringArray(String field) throws UaSerializationException;

  DateTime @Nullable [] decodeDateTimeArray(String field) throws UaSerializationException;

  UUID @Nullable [] decodeGuidArray(String field) throws UaSerializationException;

  ByteString @Nullable [] decodeByteStringArray(String field) throws UaSerializationException;

  XmlElement @Nullable [] decodeXmlElementArray(String field) throws UaSerializationException;

  NodeId @Nullable [] decodeNodeIdArray(String field) throws UaSerializationException;

  ExpandedNodeId @Nullable [] decodeExpandedNodeIdArray(String field)
      throws UaSerializationException;

  StatusCode @Nullable [] decodeStatusCodeArray(String field) throws UaSerializationException;

  QualifiedName @Nullable [] decodeQualifiedNameArray(String field) throws UaSerializationException;

  LocalizedText @Nullable [] decodeLocalizedTextArray(String field) throws UaSerializationException;

  ExtensionObject @Nullable [] decodeExtensionObjectArray(String field)
      throws UaSerializationException;

  DataValue @Nullable [] decodeDataValueArray(String field) throws UaSerializationException;

  Variant @Nullable [] decodeVariantArray(String field) throws UaSerializationException;

  DiagnosticInfo @Nullable [] decodeDiagnosticInfoArray(String field)
      throws UaSerializationException;

  Integer @Nullable [] decodeEnumArray(String field) throws UaSerializationException;

  UaStructuredType @Nullable [] decodeStructArray(String field, NodeId dataTypeId)
      throws UaSerializationException;

  UaStructuredType @Nullable [] decodeStructArray(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException;

  Matrix decodeMatrix(String field, OpcUaDataType dataType) throws UaSerializationException;

  Matrix decodeEnumMatrix(String field) throws UaSerializationException;

  Matrix decodeStructMatrix(String field, NodeId dataTypeId) throws UaSerializationException;

  Matrix decodeStructMatrix(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException;
}
