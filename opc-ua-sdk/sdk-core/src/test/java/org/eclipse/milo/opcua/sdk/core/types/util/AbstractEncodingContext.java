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

import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingManager;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingManager;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.test.types.AbstractTestType;
import org.eclipse.milo.opcua.test.types.ConcreteTestType;
import org.eclipse.milo.opcua.test.types.ConcreteTestTypeEx;
import org.eclipse.milo.opcua.test.types.StructWithAbstractArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithAbstractMatrixFields;
import org.eclipse.milo.opcua.test.types.StructWithAbstractScalarFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinArrayFieldsEx;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinMatrixFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinMatrixFieldsEx;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinScalarFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinScalarFieldsEx;
import org.eclipse.milo.opcua.test.types.StructWithOptionalArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithOptionalScalarFields;
import org.eclipse.milo.opcua.test.types.StructWithStructureArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithStructureMatrixFields;
import org.eclipse.milo.opcua.test.types.StructWithStructureScalarFields;
import org.eclipse.milo.opcua.test.types.TestEnumType;
import org.eclipse.milo.opcua.test.types.UnionOfArray;
import org.eclipse.milo.opcua.test.types.UnionOfMatrix;
import org.eclipse.milo.opcua.test.types.UnionOfScalar;
import org.mockito.Mockito;

public abstract class AbstractEncodingContext implements EncodingContext {

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

  public static final DataType APPLICATION_TYPE_DATA_TYPE =
      new AbstractDataType(
          NodeIds.ApplicationType,
          new QualifiedName(0, "ApplicationType"),
          ApplicationType.definition(),
          false) {};

  public final DataType abstractTestType;
  public final DataType concreteTestType;
  public final DataType concreteTestTypeEx;
  public final DataType structWithBuiltinScalarFields;
  public final DataType structWithBuiltinScalarFieldsEx;
  public final DataType structWithBuiltinArrayFields;
  public final DataType structWithBuiltinArrayFieldsEx;
  public final DataType structWithBuiltinMatrixFields;
  public final DataType structWithBuiltinMatrixFieldsEx;
  public final DataType structWithAbstractScalarFields;
  public final DataType structWithAbstractArrayFields;
  public final DataType structWithAbstractMatrixFields;
  public final DataType structWithOptionalScalarFields;
  public final DataType structWithOptionalArrayFields;
  public final DataType structWithStructureScalarFields;
  public final DataType structWithStructureArrayFields;
  public final DataType structWithStructureMatrixFields;

  public final DataType unionOfScalar;
  public final DataType unionOfArray;
  public final DataType unionOfMatrix;

  public final DataType testEnumType;

  public DataTypeTree dataTypeTree = Mockito.mock(DataTypeTree.class);
  public DataTypeManager dataTypeManager = new DefaultDataTypeManager();

  public NamespaceTable namespaceTable = new NamespaceTable();

  public AbstractEncodingContext() {
    namespaceTable.add("https://github.com/eclipse/milo/DataTypeTest");

    Mockito.when(dataTypeTree.getDataType(XV_DATA_TYPE.getNodeId())).thenReturn(XV_DATA_TYPE);

    Mockito.when(dataTypeTree.getDataType(APPLICATION_TYPE_DATA_TYPE.getNodeId()))
        .thenReturn(APPLICATION_TYPE_DATA_TYPE);

    dataTypeManager.registerType(
        XV_DATA_TYPE.getNodeId(),
        new XVType.Codec(),
        XV_DATA_TYPE.getBinaryEncodingId(),
        XV_DATA_TYPE.getXmlEncodingId(),
        XV_DATA_TYPE.getJsonEncodingId());

    abstractTestType = setupAbstractTestType();
    concreteTestType = setupConcreteTestType();
    concreteTestTypeEx = setupConcreteTestTypeEx();
    structWithBuiltinScalarFields = setupStructWithBuiltinScalarFields();
    structWithBuiltinScalarFieldsEx = setupStructWithBuiltinScalarFieldsEx();
    structWithBuiltinArrayFields = setupStructWithBuiltinArrayFields();
    structWithBuiltinArrayFieldsEx = setupStructWithBuiltinArrayFieldsEx();
    structWithBuiltinMatrixFields = setupStructWithBuiltinMatrixFields();
    structWithBuiltinMatrixFieldsEx = setupStructWithBuiltinMatrixFieldsEx();
    structWithAbstractScalarFields = setupStructWithAbstractScalarFields();
    structWithAbstractArrayFields = setupStructWithAbstractArrayFields();
    structWithAbstractMatrixFields = setupStructWithAbstractMatrixFields();
    structWithOptionalScalarFields = setupStructWithOptionalScalarFields();
    structWithOptionalArrayFields = setupStructWithOptionalArrayFields();
    structWithStructureScalarFields = setupStructWithStructureScalarFields();
    structWithStructureArrayFields = setupStructWithStructureArrayFields();
    structWithStructureMatrixFields = setupStructWithStructureMatrixFields();
    unionOfScalar = setupUnionOfScalar();
    unionOfArray = setupUnionOfArray();
    unionOfMatrix = setupUnionOfMatrix();
    testEnumType = setupTestEnumType();
  }

  @Override
  public DataTypeManager getDataTypeManager() {
    return dataTypeManager;
  }

  @Override
  public EncodingManager getEncodingManager() {
    return DefaultEncodingManager.createAndInitialize();
  }

  @Override
  public EncodingLimits getEncodingLimits() {
    return EncodingLimits.DEFAULT;
  }

  @Override
  public NamespaceTable getNamespaceTable() {
    return namespaceTable;
  }

  @Override
  public ServerTable getServerTable() {
    return new ServerTable();
  }

  protected DataType setupAbstractTestType() {
    NodeId typeId = AbstractTestType.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "AbstractTestType"),
            AbstractTestType.definition(getNamespaceTable()),
            true) {

          @Override
          public NodeId getBinaryEncodingId() {
            return NodeId.NULL_VALUE;
          }

          @Override
          public NodeId getXmlEncodingId() {
            return NodeId.NULL_VALUE;
          }

          @Override
          public NodeId getJsonEncodingId() {
            return NodeId.NULL_VALUE;
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupConcreteTestType() {
    NodeId typeId = ConcreteTestType.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "ConcreteTestType"),
            ConcreteTestType.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return ConcreteTestType.BINARY_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return ConcreteTestType.XML_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return ConcreteTestType.JSON_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupConcreteTestTypeEx() {
    NodeId typeId = ConcreteTestTypeEx.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "ConcreteTestTypeEx"),
            ConcreteTestTypeEx.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return ConcreteTestTypeEx.BINARY_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return ConcreteTestTypeEx.XML_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return ConcreteTestTypeEx.JSON_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupStructWithBuiltinScalarFields() {
    NodeId typeId =
        StructWithBuiltinScalarFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithScalarFields"),
            StructWithBuiltinScalarFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithBuiltinScalarFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithBuiltinScalarFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithBuiltinScalarFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithBuiltinScalarFieldsEx() {
    NodeId typeId =
        StructWithBuiltinScalarFieldsEx.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithScalarFieldsEx"),
            StructWithBuiltinScalarFieldsEx.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithBuiltinScalarFieldsEx.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithBuiltinScalarFieldsEx.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithBuiltinScalarFieldsEx.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.getBuiltinType(NodeIds.Duration)).thenReturn(OpcUaDataType.Double);

    Mockito.when(dataTypeTree.isEnumType(NodeIds.ApplicationType)).thenReturn(true);
    Mockito.when(dataTypeTree.getDataType(NodeIds.ApplicationType))
        .thenReturn(APPLICATION_TYPE_DATA_TYPE);

    Mockito.when(
            dataTypeTree.isEnumType(
                TestEnumType.TypeInfo.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow()))
        .thenReturn(true);
    Mockito.when(
            dataTypeTree.getDataType(
                TestEnumType.TypeInfo.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow()))
        .thenReturn(testEnumType);

    Mockito.when(dataTypeTree.isStructType(NodeIds.XVType)).thenReturn(true);

    Mockito.when(dataTypeTree.getBuiltinType(NodeIds.AccessLevelType))
        .thenReturn(OpcUaDataType.Byte);
    Mockito.when(dataTypeTree.getBuiltinType(NodeIds.AccessRestrictionType))
        .thenReturn(OpcUaDataType.UInt16);
    Mockito.when(dataTypeTree.getBuiltinType(NodeIds.AccessLevelExType))
        .thenReturn(OpcUaDataType.UInt32);
    Mockito.when(dataTypeTree.getBuiltinType(NodeIds.BitFieldMaskDataType))
        .thenReturn(OpcUaDataType.UInt64);

    return dataType;
  }

  protected DataType setupStructWithBuiltinArrayFields() {
    NodeId typeId =
        StructWithBuiltinArrayFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithArrayFields"),
            StructWithBuiltinArrayFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithBuiltinArrayFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithBuiltinArrayFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithBuiltinArrayFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithBuiltinArrayFieldsEx() {
    NodeId typeId =
        StructWithBuiltinArrayFieldsEx.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithArrayFieldsEx"),
            StructWithBuiltinArrayFieldsEx.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithBuiltinArrayFieldsEx.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithBuiltinArrayFieldsEx.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithBuiltinArrayFieldsEx.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithAbstractScalarFields() {
    NodeId typeId =
        StructWithAbstractScalarFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithAbstractScalarFields"),
            StructWithAbstractScalarFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithAbstractScalarFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithAbstractScalarFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithAbstractScalarFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.getBuiltinType(NodeIds.Number)).thenReturn(OpcUaDataType.Variant);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupStructWithAbstractArrayFields() {
    NodeId typeId =
        StructWithAbstractArrayFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithAbstractArrayFields"),
            StructWithAbstractArrayFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithAbstractArrayFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithAbstractArrayFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithAbstractArrayFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.getBuiltinType(NodeIds.Number)).thenReturn(OpcUaDataType.Variant);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupStructWithAbstractMatrixFields() {
    NodeId typeId =
        StructWithAbstractMatrixFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithAbstractMatrixFields"),
            StructWithAbstractMatrixFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithAbstractMatrixFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithAbstractMatrixFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithAbstractMatrixFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithOptionalScalarFields() {
    NodeId typeId =
        StructWithOptionalScalarFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithOptionalScalarFields"),
            StructWithOptionalScalarFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithOptionalScalarFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithOptionalScalarFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithOptionalScalarFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithOptionalArrayFields() {
    NodeId typeId =
        StructWithOptionalArrayFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithOptionalArrayFields"),
            StructWithOptionalArrayFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithOptionalArrayFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithOptionalArrayFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithOptionalArrayFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithBuiltinMatrixFields() {
    NodeId typeId =
        StructWithBuiltinMatrixFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithMatrixFields"),
            StructWithBuiltinMatrixFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithBuiltinMatrixFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithBuiltinMatrixFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithBuiltinMatrixFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithBuiltinMatrixFieldsEx() {
    NodeId typeId =
        StructWithBuiltinMatrixFieldsEx.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithMatrixFieldsEx"),
            StructWithBuiltinMatrixFieldsEx.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithBuiltinMatrixFieldsEx.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithBuiltinMatrixFieldsEx.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithBuiltinMatrixFieldsEx.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithStructureScalarFields() {
    NodeId typeId =
        StructWithStructureScalarFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithStructureScalarFields"),
            StructWithStructureScalarFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithStructureScalarFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithStructureScalarFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithStructureScalarFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithStructureArrayFields() {
    NodeId typeId =
        StructWithStructureArrayFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithStructureArrayFields"),
            StructWithStructureArrayFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithStructureArrayFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithStructureArrayFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithStructureArrayFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupStructWithStructureMatrixFields() {
    NodeId typeId =
        StructWithStructureMatrixFields.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "StructWithStructureMatrixFields"),
            StructWithStructureMatrixFields.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return StructWithStructureMatrixFields.BINARY_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return StructWithStructureMatrixFields.XML_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return StructWithStructureMatrixFields.JSON_ENCODING_ID
                .toNodeId(namespaceTable)
                .orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);

    return dataType;
  }

  protected DataType setupUnionOfScalar() {
    NodeId typeId = UnionOfScalar.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "UnionOfScalar"),
            UnionOfScalar.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return UnionOfScalar.BINARY_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return UnionOfScalar.XML_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return UnionOfScalar.JSON_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupUnionOfArray() {
    NodeId typeId = UnionOfArray.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "UnionOfArray"),
            UnionOfArray.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return UnionOfArray.BINARY_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return UnionOfArray.XML_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return UnionOfArray.JSON_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupUnionOfMatrix() {
    NodeId typeId = UnionOfMatrix.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId,
            new QualifiedName(1, "UnionOfMatrix"),
            UnionOfMatrix.definition(getNamespaceTable()),
            false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return UnionOfMatrix.BINARY_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getXmlEncodingId() {
            return UnionOfMatrix.XML_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }

          @Override
          public NodeId getJsonEncodingId() {
            return UnionOfMatrix.JSON_ENCODING_ID.toNodeId(namespaceTable).orElseThrow();
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.isStructType(typeId)).thenReturn(true);

    return dataType;
  }

  protected DataType setupTestEnumType() {
    NodeId typeId = TestEnumType.TypeInfo.TYPE_ID.toNodeId(getNamespaceTable()).orElseThrow();

    var dataType =
        new AbstractDataType(
            typeId, new QualifiedName(1, "TestEnumType"), TestEnumType.definition(), false) {

          @Override
          public NodeId getBinaryEncodingId() {
            return null;
          }

          @Override
          public NodeId getXmlEncodingId() {
            return null;
          }

          @Override
          public NodeId getJsonEncodingId() {
            return null;
          }
        };

    Mockito.when(dataTypeTree.getDataType(typeId)).thenReturn(dataType);
    Mockito.when(dataTypeTree.isEnumType(typeId)).thenReturn(true);

    return dataType;
  }
}
