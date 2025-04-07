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

import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.mockito.Mockito;

public class DynamicEncodingContext extends AbstractEncodingContext {

  private static final String TEST_NAMESPACE = "https://github.com/eclipse/milo/DataTypeTest";

  public DynamicEncodingContext() {
    super();

    Mockito.when(dataTypeTree.getDataType(XV_DATA_TYPE.getNodeId())).thenReturn(XV_DATA_TYPE);
    Mockito.when(dataTypeTree.getDataType(APPLICATION_TYPE_DATA_TYPE.getNodeId()))
        .thenReturn(APPLICATION_TYPE_DATA_TYPE);

    dataTypeManager.registerType(
        XV_DATA_TYPE.getNodeId(),
        DynamicCodecFactory.create(Namespaces.OPC_UA, XV_DATA_TYPE, dataTypeTree),
        XV_DATA_TYPE.getBinaryEncodingId(),
        XV_DATA_TYPE.getXmlEncodingId(),
        XV_DATA_TYPE.getJsonEncodingId());

    dataTypeManager.registerType(
        APPLICATION_TYPE_DATA_TYPE.getNodeId(),
        DynamicCodecFactory.create(Namespaces.OPC_UA, APPLICATION_TYPE_DATA_TYPE, dataTypeTree),
        APPLICATION_TYPE_DATA_TYPE.getBinaryEncodingId(),
        APPLICATION_TYPE_DATA_TYPE.getXmlEncodingId(),
        APPLICATION_TYPE_DATA_TYPE.getJsonEncodingId());

    dataTypeManager.registerType(
        abstractTestType.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, abstractTestType, dataTypeTree),
        abstractTestType.getBinaryEncodingId(),
        abstractTestType.getXmlEncodingId(),
        abstractTestType.getJsonEncodingId());

    dataTypeManager.registerType(
        concreteTestType.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, concreteTestType, dataTypeTree),
        concreteTestType.getBinaryEncodingId(),
        concreteTestType.getXmlEncodingId(),
        concreteTestType.getJsonEncodingId());

    dataTypeManager.registerType(
        concreteTestTypeEx.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, concreteTestTypeEx, dataTypeTree),
        concreteTestTypeEx.getBinaryEncodingId(),
        concreteTestTypeEx.getXmlEncodingId(),
        concreteTestTypeEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinScalarFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithBuiltinScalarFields, dataTypeTree),
        structWithBuiltinScalarFields.getBinaryEncodingId(),
        structWithBuiltinScalarFields.getXmlEncodingId(),
        structWithBuiltinScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinScalarFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithBuiltinScalarFields, dataTypeTree),
        structWithBuiltinScalarFields.getBinaryEncodingId(),
        structWithBuiltinScalarFields.getXmlEncodingId(),
        structWithBuiltinScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinScalarFieldsEx.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithBuiltinScalarFieldsEx, dataTypeTree),
        structWithBuiltinScalarFieldsEx.getBinaryEncodingId(),
        structWithBuiltinScalarFieldsEx.getXmlEncodingId(),
        structWithBuiltinScalarFieldsEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinArrayFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithBuiltinArrayFields, dataTypeTree),
        structWithBuiltinArrayFields.getBinaryEncodingId(),
        structWithBuiltinArrayFields.getXmlEncodingId(),
        structWithBuiltinArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinArrayFieldsEx.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithBuiltinArrayFieldsEx, dataTypeTree),
        structWithBuiltinArrayFieldsEx.getBinaryEncodingId(),
        structWithBuiltinArrayFieldsEx.getXmlEncodingId(),
        structWithBuiltinArrayFieldsEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithAbstractScalarFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithAbstractScalarFields, dataTypeTree),
        structWithAbstractScalarFields.getBinaryEncodingId(),
        structWithAbstractScalarFields.getXmlEncodingId(),
        structWithAbstractScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithAbstractArrayFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithAbstractArrayFields, dataTypeTree),
        structWithAbstractArrayFields.getBinaryEncodingId(),
        structWithAbstractArrayFields.getXmlEncodingId(),
        structWithAbstractArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithAbstractMatrixFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithAbstractMatrixFields, dataTypeTree),
        structWithAbstractMatrixFields.getBinaryEncodingId(),
        structWithAbstractMatrixFields.getXmlEncodingId(),
        structWithAbstractMatrixFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithOptionalScalarFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithOptionalScalarFields, dataTypeTree),
        structWithOptionalScalarFields.getBinaryEncodingId(),
        structWithOptionalScalarFields.getXmlEncodingId(),
        structWithOptionalScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithOptionalArrayFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithOptionalArrayFields, dataTypeTree),
        structWithOptionalArrayFields.getBinaryEncodingId(),
        structWithOptionalArrayFields.getXmlEncodingId(),
        structWithOptionalArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinMatrixFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithBuiltinMatrixFields, dataTypeTree),
        structWithBuiltinMatrixFields.getBinaryEncodingId(),
        structWithBuiltinMatrixFields.getXmlEncodingId(),
        structWithBuiltinMatrixFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithBuiltinMatrixFieldsEx.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithBuiltinMatrixFieldsEx, dataTypeTree),
        structWithBuiltinMatrixFieldsEx.getBinaryEncodingId(),
        structWithBuiltinMatrixFieldsEx.getXmlEncodingId(),
        structWithBuiltinMatrixFieldsEx.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithStructureScalarFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithStructureScalarFields, dataTypeTree),
        structWithStructureScalarFields.getBinaryEncodingId(),
        structWithStructureScalarFields.getXmlEncodingId(),
        structWithStructureScalarFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithStructureArrayFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithStructureArrayFields, dataTypeTree),
        structWithStructureArrayFields.getBinaryEncodingId(),
        structWithStructureArrayFields.getXmlEncodingId(),
        structWithStructureArrayFields.getJsonEncodingId());

    dataTypeManager.registerType(
        structWithStructureMatrixFields.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, structWithStructureMatrixFields, dataTypeTree),
        structWithStructureMatrixFields.getBinaryEncodingId(),
        structWithStructureMatrixFields.getXmlEncodingId(),
        structWithStructureMatrixFields.getJsonEncodingId());

    dataTypeManager.registerType(
        unionOfScalar.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, unionOfScalar, dataTypeTree),
        unionOfScalar.getBinaryEncodingId(),
        unionOfScalar.getXmlEncodingId(),
        unionOfScalar.getJsonEncodingId());

    dataTypeManager.registerType(
        unionOfArray.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, unionOfArray, dataTypeTree),
        unionOfArray.getBinaryEncodingId(),
        unionOfArray.getXmlEncodingId(),
        unionOfArray.getJsonEncodingId());

    dataTypeManager.registerType(
        unionOfMatrix.getNodeId(),
        DynamicCodecFactory.create(TEST_NAMESPACE, unionOfMatrix, dataTypeTree),
        unionOfMatrix.getBinaryEncodingId(),
        unionOfMatrix.getXmlEncodingId(),
        unionOfMatrix.getJsonEncodingId());
  }
}
