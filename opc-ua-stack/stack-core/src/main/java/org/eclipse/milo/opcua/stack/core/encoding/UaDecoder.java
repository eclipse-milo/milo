/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import java.util.UUID;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
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

  Object decodeStruct(String field, NodeId dataTypeId) throws UaSerializationException;

  Object decodeStruct(String field, ExpandedNodeId dataTypeId) throws UaSerializationException;

  Object decodeStruct(String field, DataTypeCodec codec) throws UaSerializationException;

  Boolean[] decodeBooleanArray(String field) throws UaSerializationException;

  Byte[] decodeSByteArray(String field) throws UaSerializationException;

  Short[] decodeInt16Array(String field) throws UaSerializationException;

  Integer[] decodeInt32Array(String field) throws UaSerializationException;

  Long[] decodeInt64Array(String field) throws UaSerializationException;

  UByte[] decodeByteArray(String field) throws UaSerializationException;

  UShort[] decodeUInt16Array(String field) throws UaSerializationException;

  UInteger[] decodeUInt32Array(String field) throws UaSerializationException;

  ULong[] decodeUInt64Array(String field) throws UaSerializationException;

  Float[] decodeFloatArray(String field) throws UaSerializationException;

  Double[] decodeDoubleArray(String field) throws UaSerializationException;

  String[] decodeStringArray(String field) throws UaSerializationException;

  DateTime[] decodeDateTimeArray(String field) throws UaSerializationException;

  UUID[] decodeGuidArray(String field) throws UaSerializationException;

  ByteString[] decodeByteStringArray(String field) throws UaSerializationException;

  XmlElement[] decodeXmlElementArray(String field) throws UaSerializationException;

  NodeId[] decodeNodeIdArray(String field) throws UaSerializationException;

  ExpandedNodeId[] decodeExpandedNodeIdArray(String field) throws UaSerializationException;

  StatusCode[] decodeStatusCodeArray(String field) throws UaSerializationException;

  QualifiedName[] decodeQualifiedNameArray(String field) throws UaSerializationException;

  LocalizedText[] decodeLocalizedTextArray(String field) throws UaSerializationException;

  ExtensionObject[] decodeExtensionObjectArray(String field) throws UaSerializationException;

  DataValue[] decodeDataValueArray(String field) throws UaSerializationException;

  Variant[] decodeVariantArray(String field) throws UaSerializationException;

  DiagnosticInfo[] decodeDiagnosticInfoArray(String field) throws UaSerializationException;

  Integer[] decodeEnumArray(String field) throws UaSerializationException;

  Object[] decodeStructArray(String field, NodeId dataTypeId) throws UaSerializationException;

  Object[] decodeStructArray(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException;

  <T> T[] decodeArray(String field, Function<String, T> decoder, Class<T> clazz)
      throws UaSerializationException;

  Matrix decodeMatrix(String field, BuiltinDataType builtinDataType)
      throws UaSerializationException;

  Matrix decodeEnumMatrix(String field) throws UaSerializationException;

  Matrix decodeStructMatrix(String field, NodeId dataTypeId) throws UaSerializationException;

  Matrix decodeStructMatrix(String field, ExpandedNodeId dataTypeId)
      throws UaSerializationException;
}
