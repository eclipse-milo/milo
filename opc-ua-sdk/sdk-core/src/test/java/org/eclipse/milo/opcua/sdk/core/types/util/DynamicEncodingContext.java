/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types.util;

import org.eclipse.milo.opcua.sdk.core.types.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.mockito.Mockito;

public class DynamicEncodingContext extends AbstractEncodingContext {

  public static final DataType XV_DATA_TYPE =
      new AbstractDataType(
          NodeIds.XVType,
          new QualifiedName(0, "XVType"),
          XVType.definition(new NamespaceTable()),
          false) {

        @Override
        public NodeId getBinaryEncodingId() {
          return NodeIds.XVType_Encoding_DefaultBinary;
        }

        @Override
        public NodeId getXmlEncodingId() {
          return NodeIds.XVType_Encoding_DefaultXml;
        }

        @Override
        public NodeId getJsonEncodingId() {
          return NodeIds.XVType_Encoding_DefaultJson;
        }
      };

  public DynamicEncodingContext() {
    super();

    DataTypeCodec xvDataTypeCodec = DynamicCodecFactory.create(XV_DATA_TYPE, dataTypeTree);

    dataTypeManager.registerType(
        XV_DATA_TYPE.getNodeId(),
        xvDataTypeCodec,
        XV_DATA_TYPE.getBinaryEncodingId(),
        XV_DATA_TYPE.getXmlEncodingId(),
        XV_DATA_TYPE.getJsonEncodingId());

    Mockito.when(dataTypeTree.getDataType(XV_DATA_TYPE.getNodeId())).thenReturn(XV_DATA_TYPE);

    dataTypeManager.registerType(
        abstractTestType.getNodeId(),
        DynamicCodecFactory.create(abstractTestType, dataTypeTree),
        abstractTestType.getBinaryEncodingId(),
        abstractTestType.getXmlEncodingId(),
        abstractTestType.getJsonEncodingId());

    dataTypeManager.registerType(
        concreteTestType.getNodeId(),
        DynamicCodecFactory.create(concreteTestType, dataTypeTree),
        concreteTestType.getBinaryEncodingId(),
        concreteTestType.getXmlEncodingId(),
        concreteTestType.getJsonEncodingId());

    dataTypeManager.registerType(
        concreteTestTypeEx.getNodeId(),
        DynamicCodecFactory.create(concreteTestTypeEx, dataTypeTree),
        concreteTestTypeEx.getBinaryEncodingId(),
        concreteTestTypeEx.getXmlEncodingId(),
        concreteTestTypeEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinScalarFields.getNodeId(),
        DynamicCodecFactory.create(structWithBuiltinScalarFields, dataTypeTree),
        structWithBuiltinScalarFields.getBinaryEncodingId(),
        structWithBuiltinScalarFields.getXmlEncodingId(),
        structWithBuiltinScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinScalarFields.getNodeId(),
        DynamicCodecFactory.create(structWithBuiltinScalarFields, dataTypeTree),
        structWithBuiltinScalarFields.getBinaryEncodingId(),
        structWithBuiltinScalarFields.getXmlEncodingId(),
        structWithBuiltinScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinScalarFieldsEx.getNodeId(),
        DynamicCodecFactory.create(structWithBuiltinScalarFieldsEx, dataTypeTree),
        structWithBuiltinScalarFieldsEx.getBinaryEncodingId(),
        structWithBuiltinScalarFieldsEx.getXmlEncodingId(),
        structWithBuiltinScalarFieldsEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinArrayFields.getNodeId(),
        DynamicCodecFactory.create(structWithBuiltinArrayFields, dataTypeTree),
        structWithBuiltinArrayFields.getBinaryEncodingId(),
        structWithBuiltinArrayFields.getXmlEncodingId(),
        structWithBuiltinArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinArrayFieldsEx.getNodeId(),
        DynamicCodecFactory.create(structWithBuiltinArrayFieldsEx, dataTypeTree),
        structWithBuiltinArrayFieldsEx.getBinaryEncodingId(),
        structWithBuiltinArrayFieldsEx.getXmlEncodingId(),
        structWithBuiltinArrayFieldsEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithAbstractScalarFields.getNodeId(),
        DynamicCodecFactory.create(structWithAbstractScalarFields, dataTypeTree),
        structWithAbstractScalarFields.getBinaryEncodingId(),
        structWithAbstractScalarFields.getXmlEncodingId(),
        structWithAbstractScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithAbstractArrayFields.getNodeId(),
        DynamicCodecFactory.create(structWithAbstractArrayFields, dataTypeTree),
        structWithAbstractArrayFields.getBinaryEncodingId(),
        structWithAbstractArrayFields.getXmlEncodingId(),
        structWithAbstractArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithAbstractMatrixFields.getNodeId(),
        DynamicCodecFactory.create(structWithAbstractMatrixFields, dataTypeTree),
        structWithAbstractMatrixFields.getBinaryEncodingId(),
        structWithAbstractMatrixFields.getXmlEncodingId(),
        structWithAbstractMatrixFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithOptionalScalarFields.getNodeId(),
        DynamicCodecFactory.create(structWithOptionalScalarFields, dataTypeTree),
        structWithOptionalScalarFields.getBinaryEncodingId(),
        structWithOptionalScalarFields.getXmlEncodingId(),
        structWithOptionalScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithOptionalArrayFields.getNodeId(),
        DynamicCodecFactory.create(structWithOptionalArrayFields, dataTypeTree),
        structWithOptionalArrayFields.getBinaryEncodingId(),
        structWithOptionalArrayFields.getXmlEncodingId(),
        structWithOptionalArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinMatrixFields.getNodeId(),
        DynamicCodecFactory.create(structWithBuiltinMatrixFields, dataTypeTree),
        structWithBuiltinMatrixFields.getBinaryEncodingId(),
        structWithBuiltinMatrixFields.getXmlEncodingId(),
        structWithBuiltinMatrixFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinMatrixFieldsEx.getNodeId(),
        DynamicCodecFactory.create(structWithBuiltinMatrixFieldsEx, dataTypeTree),
        structWithBuiltinMatrixFieldsEx.getBinaryEncodingId(),
        structWithBuiltinMatrixFieldsEx.getXmlEncodingId(),
        structWithBuiltinMatrixFieldsEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithStructureScalarFields.getNodeId(),
        DynamicCodecFactory.create(structWithStructureScalarFields, dataTypeTree),
        structWithStructureScalarFields.getBinaryEncodingId(),
        structWithStructureScalarFields.getXmlEncodingId(),
        structWithStructureScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithStructureArrayFields.getNodeId(),
        DynamicCodecFactory.create(structWithStructureArrayFields, dataTypeTree),
        structWithStructureArrayFields.getBinaryEncodingId(),
        structWithStructureArrayFields.getXmlEncodingId(),
        structWithStructureArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithStructureMatrixFields.getNodeId(),
        DynamicCodecFactory.create(structWithStructureMatrixFields, dataTypeTree),
        structWithStructureMatrixFields.getBinaryEncodingId(),
        structWithStructureMatrixFields.getXmlEncodingId(),
        structWithStructureMatrixFields.getJsonEncodingId());

    dataTypeManager.registerType(
        unionOfScalar.getNodeId(),
        DynamicCodecFactory.create(unionOfScalar, dataTypeTree),
        unionOfScalar.getBinaryEncodingId(),
        unionOfScalar.getXmlEncodingId(),
        unionOfScalar.getJsonEncodingId());

    dataTypeManager.registerType(
        unionOfArray.getNodeId(),
        DynamicCodecFactory.create(unionOfArray, dataTypeTree),
        unionOfArray.getBinaryEncodingId(),
        unionOfArray.getXmlEncodingId(),
        unionOfArray.getJsonEncodingId());

    dataTypeManager.registerType(
        unionOfMatrix.getNodeId(),
        DynamicCodecFactory.create(unionOfMatrix, dataTypeTree),
        unionOfMatrix.getBinaryEncodingId(),
        unionOfMatrix.getXmlEncodingId(),
        unionOfMatrix.getJsonEncodingId());
  }
}
