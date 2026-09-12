/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerMembers;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerPropertyValues;
import com.digitalpetri.opcua.uanodeset.runtime.values.NumericValues;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class PublishedEventsTypeNode extends PublishedDataSetTypeNode
    implements PublishedEventsType {
  public PublishedEventsTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  public PublishedEventsTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public Optional<VariableNode> getPropertyNode(QualifiedName browseName) {
    return findNode(
            browseName,
            n -> n instanceof VariableNode,
            r ->
                r.isForward()
                    && (r.getReferenceTypeId().equals(NodeIds.HasProperty)
                        || getNodeContext()
                            .getServer()
                            .getReferenceTypeTree()
                            .isSubtypeOf(r.getReferenceTypeId(), NodeIds.HasProperty)))
        .map(n -> (VariableNode) n);
  }

  @Override
  public PropertyTypeNode getPubSubEventNotifierNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EventNotifier",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:EventNotifier (declaration i=14586, owner i=14572)"));
  }

  @Override
  public @Nullable NodeId getPubSubEventNotifier() {
    var node = getPubSubEventNotifierNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EventNotifier (declaration i=14586, owner i=14572)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setPubSubEventNotifier(@Nullable NodeId value) {
    var node = getPubSubEventNotifierNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EventNotifier (declaration i=14586, owner i=14572)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getSelectedFieldsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SelectedFields",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SelectedFields (declaration i=14587, owner i=14572)"));
  }

  @Override
  public @Nullable SimpleAttributeOperand @Nullable [] getSelectedFields() {
    var node = getSelectedFieldsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SelectedFields (declaration i=14587, owner i=14572)"
              + " on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        SimpleAttributeOperand[].class,
        SimpleAttributeOperand.class,
        "http://opcfoundation.org/UA/:SelectedFields (declaration i=14587, owner i=14572)");
  }

  @Override
  public void setSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value) {
    var node = getSelectedFieldsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SelectedFields (declaration i=14587, owner i=14572)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getFilterNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Filter",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Filter (declaration i=14588, owner i=14572)"));
  }

  @Override
  public @Nullable ContentFilter getFilter() {
    var node = getFilterNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Filter (declaration i=14588, owner i=14572)"
              + " on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        ContentFilter.class,
        ContentFilter.class,
        "http://opcfoundation.org/UA/:Filter (declaration i=14588, owner i=14572)");
  }

  @Override
  public void setFilter(@Nullable ContentFilter value) {
    var node = getFilterNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Filter (declaration i=14588, owner i=14572)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable UaMethodNode getModifyFieldSelectionMethodNode() {
    return ServerMembers.lookup(
        this,
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModifyFieldSelection",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            true,
            "http://opcfoundation.org/UA/:ModifyFieldSelection (declaration i=15052, owner"
                + " i=14572)"));
  }

  @Override
  public MethodBinding bindModifyFieldSelection(
      MethodBindings bindings, PublishedEventsType.ModifyFieldSelectionHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getModifyFieldSelectionMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "ConfigurationVersion",
                  ExpandedNodeId.parse("i=14593")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "FieldNameAliases",
                  ExpandedNodeId.parse("i=12")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, "")),
              new Argument(
                  "PromotedFields",
                  ExpandedNodeId.parse("i=1")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, "")),
              new Argument(
                  "SelectedFields",
                  ExpandedNodeId.parse("i=601")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {
              new Argument(
                  "NewConfigurationVersion",
                  ExpandedNodeId.parse("i=14593")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 4;
          }

          @Override
          protected CallMethodResult invokeResult(
              org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler
                      .InvocationContext
                  context,
              Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable ConfigurationVersionDataType callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable ConfigurationVersionDataType convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (ConfigurationVersionDataType) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable ConfigurationVersionDataType projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (ConfigurationVersionDataType) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            @Nullable String @Nullable [] callbackInput1 = null;
            if (inputValues.length > 1) {
              try {
                @Nullable String @Nullable [] convertedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      convertedInput1 = null;
                    } else {
                      convertedInput1 = new String[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedInput1.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedInput1[valueIndex] = (String) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable String @Nullable [] projectedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      projectedInput1 = null;
                    } else {
                      projectedInput1 = new String[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < projectedInput1.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        projectedInput1[valueIndex] = (String) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput1 = projectedInput1;
              } catch (UaException failure) {
                inputResults[1] = failure.getStatusCode();
              }
            }
            @Nullable Boolean @Nullable [] callbackInput2 = null;
            if (inputValues.length > 2) {
              try {
                @Nullable Boolean @Nullable [] convertedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      convertedInput2 = null;
                    } else {
                      convertedInput2 = new Boolean[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedInput2.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedInput2[valueIndex] = (Boolean) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable Boolean @Nullable [] projectedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      projectedInput2 = null;
                    } else {
                      projectedInput2 = new Boolean[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < projectedInput2.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        projectedInput2[valueIndex] = (Boolean) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput2 = projectedInput2;
              } catch (UaException failure) {
                inputResults[2] = failure.getStatusCode();
              }
            }
            @Nullable SimpleAttributeOperand @Nullable [] callbackInput3 = null;
            if (inputValues.length > 3) {
              try {
                @Nullable SimpleAttributeOperand @Nullable [] convertedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      convertedInput3 = null;
                    } else {
                      convertedInput3 = new SimpleAttributeOperand[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedInput3.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedInput3[valueIndex] = (SimpleAttributeOperand) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable SimpleAttributeOperand @Nullable [] projectedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      projectedInput3 = null;
                    } else {
                      projectedInput3 = new SimpleAttributeOperand[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < projectedInput3.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        projectedInput3[valueIndex] = (SimpleAttributeOperand) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput3 = projectedInput3;
              } catch (UaException failure) {
                inputResults[3] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              @Nullable ConfigurationVersionDataType outputs =
                  handler.invoke(
                      context, callbackInput0, callbackInput1, callbackInput2, callbackInput3);
              Variant outputValue0;
              {
                @Nullable ConfigurationVersionDataType convertedValue;
                {
                  Object methodValue = outputs;
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=14593")
                            .toNodeId(namespaceTable)
                            .orElseThrow(
                                () ->
                                    new UaException(
                                        StatusCodes.Bad_NodeIdInvalid,
                                        "Method argument DataType namespace is unavailable"));
                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                        && dataTypeTree.getDataType(argumentDataTypeId) == null) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "Method argument NewConfigurationVersion (effective property i=15517,"
                              + " DataType i=14593) is unavailable in the effective type tree;"
                              + " resolved DataType: "
                              + argumentDataTypeId);
                    }
                    methodValue =
                        NumericValues.normalize(methodValue, dataTypeTree, argumentDataTypeId);
                    if (methodValue != null) {
                      Object shapeElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      int valueRank =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getValueRank()
                              : ArrayUtil.getValueRank(methodValue);
                      boolean emptyArray =
                          methodValue.getClass().isArray()
                              && ArrayUtil.getValueRank(methodValue) == 1
                              && Array.getLength(methodValue) == 0;
                      if (!(valueRank == -1)) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch, "Method argument ValueRank mismatch");
                      }
                      if (methodValue instanceof Matrix) {
                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                        if (dimensions.length < 2
                            || !shapeElements.getClass().isArray()
                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Malformed Method Matrix representation");
                        }
                        long elementCount = 1;
                        for (int dimension : dimensions) {
                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch, "Malformed Method Matrix dimensions");
                          }
                          elementCount *= dimension;
                        }
                        if (elementCount != Array.getLength(shapeElements)) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix dimensions do not match its elements");
                        }
                        if (!(((Matrix) methodValue)
                            .getDataType()
                            .equals(Variant.of(shapeElements).getDataType()))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix DataType does not match its elements");
                        }
                      }
                      Variant.of(shapeElements);
                    }
                    if (methodValue != null) {
                      Object typedElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      if (NodeIds.Structure.equals(argumentDataTypeId)
                          || dataTypeTree.isStructType(argumentDataTypeId)) {
                        var declaredType = dataTypeTree.getType(argumentDataTypeId);
                        if (typedElements.getClass().isArray()) {
                          var structureCodec =
                              context
                                  .getServer()
                                  .getStaticEncodingContext()
                                  .getDataTypeManager()
                                  .getCodec(argumentDataTypeId);
                          Class<?> structureClass =
                              structureCodec == null
                                  ? UaStructuredType.class
                                  : structureCodec.getType();
                          Object decodedStructures =
                              Array.newInstance(structureClass, Array.getLength(typedElements));
                          for (int structureIndex = 0;
                              structureIndex < Array.getLength(typedElements);
                              structureIndex++) {
                            Object structure = Array.get(typedElements, structureIndex);
                            if (structure instanceof ExtensionObject) {
                              structure =
                                  ((ExtensionObject) structure).isNull()
                                      ? null
                                      : ((ExtensionObject) structure)
                                          .decode(context.getServer().getStaticEncodingContext());
                            }
                            if (structure != null) {
                              if (!(structure instanceof UaStructuredType)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method argument requires a Structure value");
                              }
                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                  || declaredType != null && declaredType.isAbstract()) {
                                if (!dataTypeTree.isSubtypeOf(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE),
                                    argumentDataTypeId)) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure is not a subtype of the effective"
                                          + " DataType");
                                }
                              } else {
                                if (!argumentDataTypeId.equals(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE))) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure does not match the effective DataType");
                                }
                              }
                            }
                            Array.set(decodedStructures, structureIndex, structure);
                          }
                          methodValue =
                              methodValue instanceof Matrix
                                  ? new Matrix(
                                      decodedStructures,
                                      ((Matrix) methodValue).getDimensions().clone())
                                  : decodedStructures;
                        } else {
                          if (typedElements instanceof ExtensionObject) {
                            typedElements =
                                ((ExtensionObject) typedElements).isNull()
                                    ? null
                                    : ((ExtensionObject) typedElements)
                                        .decode(context.getServer().getStaticEncodingContext());
                          }
                          if (typedElements != null) {
                            if (!(typedElements instanceof UaStructuredType)) {
                              throw new UaException(
                                  StatusCodes.Bad_TypeMismatch,
                                  "Method argument requires a Structure value");
                            }
                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                || declaredType != null && declaredType.isAbstract()) {
                              if (!dataTypeTree.isSubtypeOf(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE),
                                  argumentDataTypeId)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure is not a subtype of the effective DataType");
                              }
                            } else {
                              if (!argumentDataTypeId.equals(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE))) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure does not match the effective DataType");
                              }
                            }
                          }
                          methodValue = typedElements;
                        }
                      } else {
                        Variant.of(typedElements);
                        NodeId assignableDataTypeId =
                            dataTypeTree.getBackingClass(argumentDataTypeId) == Number.class
                                    && dataTypeTree.isSubtypeOf(argumentDataTypeId, NodeIds.Integer)
                                ? NodeIds.Integer
                                : argumentDataTypeId;
                        if (dataTypeTree.getBackingClass(argumentDataTypeId) != Variant.class
                            && !dataTypeTree.isAssignable(
                                assignableDataTypeId, ArrayUtil.getBoxedType(typedElements))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch, "Method argument DataType mismatch");
                        }
                      }
                    }
                    convertedValue = (ConfigurationVersionDataType) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                try {
                  Object wireValue = convertedValue;
                  Object wireElements =
                      wireValue instanceof Matrix ? ((Matrix) wireValue).getElements() : wireValue;
                  NumericValues.requireEncodable(wireValue);
                  wireValue =
                      ExtensionObject.encodeValue(
                          context.getServer().getStaticEncodingContext(), wireValue);
                  outputValue0 = Variant.of(wireValue);
                } catch (UaSerializationException encodingFailure) {
                  throw new UaException(
                      encodingFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                          ? StatusCodes.Bad_OutOfRange
                          : StatusCodes.Bad_TypeMismatch,
                      encodingFailure);
                } catch (ClassCastException | IllegalArgumentException encodingFailure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, encodingFailure);
                }
              }
              return new CallMethodResult(
                  StatusCode.GOOD,
                  new StatusCode[0],
                  new DiagnosticInfo[0],
                  new Variant[] {outputValue0});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }

  @Override
  public MethodBinding bindModifyFieldSelectionDetailed(
      MethodBindings bindings, PublishedEventsType.ModifyFieldSelectionDetailedHandler handler)
      throws UaException {
    Objects.requireNonNull(bindings, "bindings");
    Objects.requireNonNull(handler, "handler");
    UaMethodNode methodNode = getModifyFieldSelectionMethodNode();
    if (methodNode == null) {
      throw new UaException(StatusCodes.Bad_NotFound, "Cannot bind an absent Method");
    }
    return bindings.bind(
        this,
        methodNode,
        new org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler(methodNode) {
          @Override
          public Argument[] getInputArguments() {
            return new Argument[] {
              new Argument(
                  "ConfigurationVersion",
                  ExpandedNodeId.parse("i=14593")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, "")),
              new Argument(
                  "FieldNameAliases",
                  ExpandedNodeId.parse("i=12")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, "")),
              new Argument(
                  "PromotedFields",
                  ExpandedNodeId.parse("i=1")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, "")),
              new Argument(
                  "SelectedFields",
                  ExpandedNodeId.parse("i=601")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText(null, ""))
            };
          }

          @Override
          public Argument[] getOutputArguments() {
            return new Argument[] {
              new Argument(
                  "NewConfigurationVersion",
                  ExpandedNodeId.parse("i=14593")
                      .toNodeId(getNode().getNodeContext().getNamespaceTable())
                      .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown)),
                  -1,
                  null,
                  new LocalizedText(null, ""))
            };
          }

          @Override
          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 4;
          }

          @Override
          protected CallMethodResult invokeResult(
              org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler
                      .InvocationContext
                  context,
              Variant[] inputValues)
              throws UaException {
            StatusCode[] inputResults = new StatusCode[inputValues.length];
            Arrays.fill(inputResults, StatusCode.GOOD);
            @Nullable ConfigurationVersionDataType callbackInput0 = null;
            if (inputValues.length > 0) {
              try {
                @Nullable ConfigurationVersionDataType convertedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    convertedInput0 = (ConfigurationVersionDataType) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable ConfigurationVersionDataType projectedInput0;
                {
                  Object methodValue = inputValues[0].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    projectedInput0 = (ConfigurationVersionDataType) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput0 = projectedInput0;
              } catch (UaException failure) {
                inputResults[0] = failure.getStatusCode();
              }
            }
            @Nullable String @Nullable [] callbackInput1 = null;
            if (inputValues.length > 1) {
              try {
                @Nullable String @Nullable [] convertedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      convertedInput1 = null;
                    } else {
                      convertedInput1 = new String[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedInput1.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedInput1[valueIndex] = (String) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable String @Nullable [] projectedInput1;
                {
                  Object methodValue = inputValues[1].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      projectedInput1 = null;
                    } else {
                      projectedInput1 = new String[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < projectedInput1.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        projectedInput1[valueIndex] = (String) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput1 = projectedInput1;
              } catch (UaException failure) {
                inputResults[1] = failure.getStatusCode();
              }
            }
            @Nullable Boolean @Nullable [] callbackInput2 = null;
            if (inputValues.length > 2) {
              try {
                @Nullable Boolean @Nullable [] convertedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      convertedInput2 = null;
                    } else {
                      convertedInput2 = new Boolean[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedInput2.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedInput2[valueIndex] = (Boolean) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable Boolean @Nullable [] projectedInput2;
                {
                  Object methodValue = inputValues[2].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      projectedInput2 = null;
                    } else {
                      projectedInput2 = new Boolean[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < projectedInput2.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        projectedInput2[valueIndex] = (Boolean) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput2 = projectedInput2;
              } catch (UaException failure) {
                inputResults[2] = failure.getStatusCode();
              }
            }
            @Nullable SimpleAttributeOperand @Nullable [] callbackInput3 = null;
            if (inputValues.length > 3) {
              try {
                @Nullable SimpleAttributeOperand @Nullable [] convertedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      convertedInput3 = null;
                    } else {
                      convertedInput3 = new SimpleAttributeOperand[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < convertedInput3.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        convertedInput3[valueIndex] = (SimpleAttributeOperand) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                @Nullable SimpleAttributeOperand @Nullable [] projectedInput3;
                {
                  Object methodValue = inputValues[3].getValue();
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    if (methodValue == null) {
                      projectedInput3 = null;
                    } else {
                      projectedInput3 = new SimpleAttributeOperand[Array.getLength(methodValue)];
                      for (int valueIndex = 0; valueIndex < projectedInput3.length; valueIndex++) {
                        Object valueElement = Array.get(methodValue, valueIndex);
                        projectedInput3[valueIndex] = (SimpleAttributeOperand) valueElement;
                      }
                    }
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                callbackInput3 = projectedInput3;
              } catch (UaException failure) {
                inputResults[3] = failure.getStatusCode();
              }
            }
            if (Arrays.stream(inputResults).anyMatch(StatusCode::isBad)) {
              throw new InvalidArgumentException(inputResults);
            }
            try {
              var result =
                  handler.invoke(
                      context, callbackInput0, callbackInput1, callbackInput2, callbackInput3);
              if (result == null) {
                throw new UaException(
                    StatusCodes.Bad_InternalError, "A detailed Method handler returned null");
              }
              if (!result.hasOutputs()) {
                return new CallMethodResult(
                    result.status(),
                    result.inputResults(),
                    result.inputDiagnostics(),
                    new Variant[0]);
              }
              var outputs = result.outputs();
              Variant outputValue0;
              {
                @Nullable ConfigurationVersionDataType convertedValue;
                {
                  Object methodValue = outputs;
                  try {
                    if (methodValue instanceof Matrix && ((Matrix) methodValue).isNull()) {
                      methodValue = null;
                    }
                    NamespaceTable namespaceTable = context.getServer().getNamespaceTable();
                    DataTypeTree dataTypeTree = context.getServer().getDataTypeTree();
                    NodeId argumentDataTypeId =
                        ExpandedNodeId.parse("i=14593")
                            .toNodeId(namespaceTable)
                            .orElseThrow(
                                () ->
                                    new UaException(
                                        StatusCodes.Bad_NodeIdInvalid,
                                        "Method argument DataType namespace is unavailable"));
                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                        && dataTypeTree.getDataType(argumentDataTypeId) == null) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "Method argument NewConfigurationVersion (effective property i=15517,"
                              + " DataType i=14593) is unavailable in the effective type tree;"
                              + " resolved DataType: "
                              + argumentDataTypeId);
                    }
                    methodValue =
                        NumericValues.normalize(methodValue, dataTypeTree, argumentDataTypeId);
                    if (methodValue != null) {
                      Object shapeElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      int valueRank =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getValueRank()
                              : ArrayUtil.getValueRank(methodValue);
                      boolean emptyArray =
                          methodValue.getClass().isArray()
                              && ArrayUtil.getValueRank(methodValue) == 1
                              && Array.getLength(methodValue) == 0;
                      if (!(valueRank == -1)) {
                        throw new UaException(
                            StatusCodes.Bad_TypeMismatch, "Method argument ValueRank mismatch");
                      }
                      if (methodValue instanceof Matrix) {
                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                        if (dimensions.length < 2
                            || !shapeElements.getClass().isArray()
                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Malformed Method Matrix representation");
                        }
                        long elementCount = 1;
                        for (int dimension : dimensions) {
                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                            throw new UaException(
                                StatusCodes.Bad_TypeMismatch, "Malformed Method Matrix dimensions");
                          }
                          elementCount *= dimension;
                        }
                        if (elementCount != Array.getLength(shapeElements)) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix dimensions do not match its elements");
                        }
                        if (!(((Matrix) methodValue)
                            .getDataType()
                            .equals(Variant.of(shapeElements).getDataType()))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "Method Matrix DataType does not match its elements");
                        }
                      }
                      Variant.of(shapeElements);
                    }
                    if (methodValue != null) {
                      Object typedElements =
                          methodValue instanceof Matrix
                              ? ((Matrix) methodValue).getElements()
                              : methodValue;
                      if (NodeIds.Structure.equals(argumentDataTypeId)
                          || dataTypeTree.isStructType(argumentDataTypeId)) {
                        var declaredType = dataTypeTree.getType(argumentDataTypeId);
                        if (typedElements.getClass().isArray()) {
                          var structureCodec =
                              context
                                  .getServer()
                                  .getStaticEncodingContext()
                                  .getDataTypeManager()
                                  .getCodec(argumentDataTypeId);
                          Class<?> structureClass =
                              structureCodec == null
                                  ? UaStructuredType.class
                                  : structureCodec.getType();
                          Object decodedStructures =
                              Array.newInstance(structureClass, Array.getLength(typedElements));
                          for (int structureIndex = 0;
                              structureIndex < Array.getLength(typedElements);
                              structureIndex++) {
                            Object structure = Array.get(typedElements, structureIndex);
                            if (structure instanceof ExtensionObject) {
                              structure =
                                  ((ExtensionObject) structure).isNull()
                                      ? null
                                      : ((ExtensionObject) structure)
                                          .decode(context.getServer().getStaticEncodingContext());
                            }
                            if (structure != null) {
                              if (!(structure instanceof UaStructuredType)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method argument requires a Structure value");
                              }
                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                  || declaredType != null && declaredType.isAbstract()) {
                                if (!dataTypeTree.isSubtypeOf(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE),
                                    argumentDataTypeId)) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure is not a subtype of the effective"
                                          + " DataType");
                                }
                              } else {
                                if (!argumentDataTypeId.equals(
                                    ((UaStructuredType) structure)
                                        .getTypeId()
                                        .toNodeId(namespaceTable)
                                        .orElse(NodeId.NULL_VALUE))) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "Method Structure does not match the effective DataType");
                                }
                              }
                            }
                            Array.set(decodedStructures, structureIndex, structure);
                          }
                          methodValue =
                              methodValue instanceof Matrix
                                  ? new Matrix(
                                      decodedStructures,
                                      ((Matrix) methodValue).getDimensions().clone())
                                  : decodedStructures;
                        } else {
                          if (typedElements instanceof ExtensionObject) {
                            typedElements =
                                ((ExtensionObject) typedElements).isNull()
                                    ? null
                                    : ((ExtensionObject) typedElements)
                                        .decode(context.getServer().getStaticEncodingContext());
                          }
                          if (typedElements != null) {
                            if (!(typedElements instanceof UaStructuredType)) {
                              throw new UaException(
                                  StatusCodes.Bad_TypeMismatch,
                                  "Method argument requires a Structure value");
                            }
                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                || declaredType != null && declaredType.isAbstract()) {
                              if (!dataTypeTree.isSubtypeOf(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE),
                                  argumentDataTypeId)) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure is not a subtype of the effective DataType");
                              }
                            } else {
                              if (!argumentDataTypeId.equals(
                                  ((UaStructuredType) typedElements)
                                      .getTypeId()
                                      .toNodeId(namespaceTable)
                                      .orElse(NodeId.NULL_VALUE))) {
                                throw new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "Method Structure does not match the effective DataType");
                              }
                            }
                          }
                          methodValue = typedElements;
                        }
                      } else {
                        Variant.of(typedElements);
                        NodeId assignableDataTypeId =
                            dataTypeTree.getBackingClass(argumentDataTypeId) == Number.class
                                    && dataTypeTree.isSubtypeOf(argumentDataTypeId, NodeIds.Integer)
                                ? NodeIds.Integer
                                : argumentDataTypeId;
                        if (dataTypeTree.getBackingClass(argumentDataTypeId) != Variant.class
                            && !dataTypeTree.isAssignable(
                                assignableDataTypeId, ArrayUtil.getBoxedType(typedElements))) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch, "Method argument DataType mismatch");
                        }
                      }
                    }
                    convertedValue = (ConfigurationVersionDataType) methodValue;
                  } catch (UaSerializationException conversionFailure) {
                    throw new UaException(
                        conversionFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                            ? StatusCodes.Bad_OutOfRange
                            : StatusCodes.Bad_TypeMismatch,
                        conversionFailure);
                  } catch (ClassCastException | IllegalArgumentException conversionFailure) {
                    throw new UaException(StatusCodes.Bad_TypeMismatch, conversionFailure);
                  }
                }
                try {
                  Object wireValue = convertedValue;
                  Object wireElements =
                      wireValue instanceof Matrix ? ((Matrix) wireValue).getElements() : wireValue;
                  NumericValues.requireEncodable(wireValue);
                  wireValue =
                      ExtensionObject.encodeValue(
                          context.getServer().getStaticEncodingContext(), wireValue);
                  outputValue0 = Variant.of(wireValue);
                } catch (UaSerializationException encodingFailure) {
                  throw new UaException(
                      encodingFailure.getStatusCode().getValue() == StatusCodes.Bad_OutOfRange
                          ? StatusCodes.Bad_OutOfRange
                          : StatusCodes.Bad_TypeMismatch,
                      encodingFailure);
                } catch (ClassCastException | IllegalArgumentException encodingFailure) {
                  throw new UaException(StatusCodes.Bad_TypeMismatch, encodingFailure);
                }
              }
              return new CallMethodResult(
                  result.status(),
                  new StatusCode[0],
                  new DiagnosticInfo[0],
                  new Variant[] {outputValue0});
            } catch (UaRuntimeException failure) {
              throw new UaException(failure.getStatusCode().getValue(), failure);
            } catch (RuntimeException callbackFailure) {
              throw new UaException(StatusCodes.Bad_InternalError, callbackFailure);
            }
          }
        });
  }
}
