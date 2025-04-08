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
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
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

public interface UaEncoder {

  EncodingContext getEncodingContext();

  void encodeBoolean(String field, Boolean value) throws UaSerializationException;

  void encodeSByte(String field, Byte value) throws UaSerializationException;

  void encodeInt16(String field, Short value) throws UaSerializationException;

  void encodeInt32(String field, Integer value) throws UaSerializationException;

  void encodeInt64(String field, Long value) throws UaSerializationException;

  void encodeByte(String field, UByte value) throws UaSerializationException;

  void encodeUInt16(String field, UShort value) throws UaSerializationException;

  void encodeUInt32(String field, UInteger value) throws UaSerializationException;

  void encodeUInt64(String field, ULong value) throws UaSerializationException;

  void encodeFloat(String field, Float value) throws UaSerializationException;

  void encodeDouble(String field, Double value) throws UaSerializationException;

  void encodeString(String field, String value) throws UaSerializationException;

  void encodeDateTime(String field, DateTime value) throws UaSerializationException;

  void encodeGuid(String field, UUID value) throws UaSerializationException;

  void encodeByteString(String field, ByteString value) throws UaSerializationException;

  void encodeXmlElement(String field, XmlElement value) throws UaSerializationException;

  void encodeNodeId(String field, NodeId value) throws UaSerializationException;

  void encodeExpandedNodeId(String field, ExpandedNodeId value) throws UaSerializationException;

  void encodeStatusCode(String field, StatusCode value) throws UaSerializationException;

  void encodeQualifiedName(String field, QualifiedName value) throws UaSerializationException;

  void encodeLocalizedText(String field, LocalizedText value) throws UaSerializationException;

  void encodeExtensionObject(String field, ExtensionObject value) throws UaSerializationException;

  void encodeDataValue(String field, DataValue value) throws UaSerializationException;

  void encodeVariant(String field, Variant value) throws UaSerializationException;

  void encodeDiagnosticInfo(String field, DiagnosticInfo value) throws UaSerializationException;

  void encodeMessage(String field, UaMessageType message) throws UaSerializationException;

  void encodeEnum(String field, UaEnumeratedType value) throws UaSerializationException;

  void encodeStruct(String field, UaStructuredType value, ExpandedNodeId dataTypeId)
      throws UaSerializationException;

  void encodeStruct(String field, UaStructuredType value, NodeId dataTypeId)
      throws UaSerializationException;

  void encodeStruct(String field, UaStructuredType value, DataTypeCodec codec)
      throws UaSerializationException;

  void encodeBooleanArray(String field, Boolean[] value) throws UaSerializationException;

  void encodeSByteArray(String field, Byte[] value) throws UaSerializationException;

  void encodeInt16Array(String field, Short[] value) throws UaSerializationException;

  void encodeInt32Array(String field, Integer[] value) throws UaSerializationException;

  void encodeInt64Array(String field, Long[] value) throws UaSerializationException;

  void encodeByteArray(String field, UByte[] value) throws UaSerializationException;

  void encodeUInt16Array(String field, UShort[] value) throws UaSerializationException;

  void encodeUInt32Array(String field, UInteger[] value) throws UaSerializationException;

  void encodeUInt64Array(String field, ULong[] value) throws UaSerializationException;

  void encodeFloatArray(String field, Float[] value) throws UaSerializationException;

  void encodeDoubleArray(String field, Double[] value) throws UaSerializationException;

  void encodeStringArray(String field, String[] value) throws UaSerializationException;

  void encodeDateTimeArray(String field, DateTime[] value) throws UaSerializationException;

  void encodeGuidArray(String field, UUID[] value) throws UaSerializationException;

  void encodeByteStringArray(String field, ByteString[] value) throws UaSerializationException;

  void encodeXmlElementArray(String field, XmlElement[] value) throws UaSerializationException;

  void encodeNodeIdArray(String field, NodeId[] value) throws UaSerializationException;

  void encodeExpandedNodeIdArray(String field, ExpandedNodeId[] value)
      throws UaSerializationException;

  void encodeStatusCodeArray(String field, StatusCode[] value) throws UaSerializationException;

  void encodeQualifiedNameArray(String field, QualifiedName[] value)
      throws UaSerializationException;

  void encodeLocalizedTextArray(String field, LocalizedText[] value)
      throws UaSerializationException;

  void encodeExtensionObjectArray(String field, ExtensionObject[] value)
      throws UaSerializationException;

  void encodeDataValueArray(String field, DataValue[] value) throws UaSerializationException;

  void encodeVariantArray(String field, Variant[] value) throws UaSerializationException;

  void encodeDiagnosticInfoArray(String field, DiagnosticInfo[] value)
      throws UaSerializationException;

  void encodeEnumArray(String field, UaEnumeratedType[] value) throws UaSerializationException;

  void encodeStructArray(String field, UaStructuredType[] value, NodeId dataTypeId)
      throws UaSerializationException;

  void encodeStructArray(String field, UaStructuredType[] value, ExpandedNodeId dataTypeId)
      throws UaSerializationException;

  void encodeMatrix(String field, Matrix value) throws UaSerializationException;

  void encodeEnumMatrix(String field, Matrix value) throws UaSerializationException;

  void encodeStructMatrix(String field, Matrix value, NodeId dataTypeId)
      throws UaSerializationException;

  void encodeStructMatrix(String field, Matrix value, ExpandedNodeId dataTypeId)
      throws UaSerializationException;
}
