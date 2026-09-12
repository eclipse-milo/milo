/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientDataTypes;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import com.digitalpetri.opcua.uanodeset.runtime.values.NumericValues;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForReadOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForWriteOutputs;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
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
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class TemporaryFileTransferTypeNode extends BaseObjectTypeNode
    implements TemporaryFileTransferType {
  public TemporaryFileTransferTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
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
        client,
        nodeId,
        nodeClass,
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

  /**
   * Creates an independently owned view context retaining this exact existing node and its client.
   * Cached attributes remain shared even after SDK cache eviction. Close the returned context when
   * its views are no longer needed.
   */
  public static ClientViews createViews(TemporaryFileTransferTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable Double getClientProcessingTimeout() throws UaException {
    PropertyTypeNode node = getClientProcessingTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientProcessingTimeout (declaration i=15745, owner"
              + " i=15744) on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setClientProcessingTimeout(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getClientProcessingTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ClientProcessingTimeout (declaration i=15745, owner"
              + " i=15744) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readClientProcessingTimeout() throws UaException {
    return ClientMembers.await(readClientProcessingTimeoutAsync(), false);
  }

  @Override
  public void writeClientProcessingTimeout(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeClientProcessingTimeoutAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readClientProcessingTimeoutAsync() {
    return getClientProcessingTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientProcessingTimeout (declaration i=15745,"
                            + " owner i=15744) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (Double) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeClientProcessingTimeoutAsync(
      @Nullable Double clientProcessingTimeout) {
    return getClientProcessingTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ClientProcessingTimeout (declaration i=15745,"
                            + " owner i=15744) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(clientProcessingTimeout));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getClientProcessingTimeoutNode() throws UaException {
    return ClientMembers.await(getClientProcessingTimeoutNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientProcessingTimeoutNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ClientProcessingTimeout",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ClientProcessingTimeout (declaration i=15745, owner"
                + " i=15744)"));
  }

  @NullMarked
  @Override
  public UaMethodNode getGenerateFileForReadMethodNode() throws UaException {
    return ClientMembers.await(getGenerateFileForReadMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getGenerateFileForReadMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "GenerateFileForRead",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:GenerateFileForRead (declaration i=15746, owner"
                + " i=15744)"));
  }

  @NullMarked
  @Override
  public TemporaryFileTransferTypeGenerateFileForReadOutputs callGenerateFileForRead(
      @Nullable Object generateOptions) throws UaException {
    return callGenerateFileForReadDetailed(generateOptions).requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>
      callGenerateFileForReadAsync(@Nullable Object generateOptions) {
    CompletableFuture<TemporaryFileTransferTypeGenerateFileForReadOutputs> result =
        new CompletableFuture<>();
    var call = callGenerateFileForReadDetailedAsync(generateOptions);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>
      callGenerateFileForReadDetailed(@Nullable Object generateOptions) throws UaException {
    return callGenerateFileForReadDetailed(MethodCallOptions.NONE, generateOptions);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>
      callGenerateFileForReadDetailed(MethodCallOptions options, @Nullable Object generateOptions)
          throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(
        callGenerateFileForReadDetailedAsync(options, generateOptions), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<
          ? extends MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>>
      callGenerateFileForReadDetailedAsync(@Nullable Object generateOptions) {
    return callGenerateFileForReadDetailedAsync(MethodCallOptions.NONE, generateOptions);
  }

  @NullMarked
  @Override
  public CompletableFuture<
          ? extends MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForReadOutputs>>
      callGenerateFileForReadDetailedAsync(
          MethodCallOptions options, @Nullable Object generateOptions) {
    CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForReadOutputs>>
        result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(generateOptions);
      var lookup = getGenerateFileForReadMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForReadOutputs>>
          pipeline =
              lookup.thenCompose(
                  methodNode -> {
                    if (result.isCancelled()) {
                      return CompletableFuture.failedFuture(new CancellationException());
                    }
                    if (methodNode == null) {
                      return CompletableFuture.failedFuture(
                          new UaException(
                              StatusCodes.Bad_NotFound,
                              "Method node is required for invocation: GenerateFileForRead"));
                    }
                    var inputMetadata =
                        ClientDataTypes.read(
                            this.client,
                            List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=24"))
                                .subList(0, rawInputs.size()),
                            rawInputs.toArray());
                    result.whenComplete(
                        (cancelledValue, cancelledFailure) -> {
                          if (result.isCancelled()) {
                            inputMetadata.cancel(false);
                          }
                        });
                    return inputMetadata
                        .handle(
                            (inputDataTypeTree, inputMetadataFailure) -> {
                              if (inputMetadataFailure != null) {
                                var checkedMetadataFailureinputMetadataFailure =
                                    UaException.extract(inputMetadataFailure);
                                if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                                  throw new CompletionException(
                                      checkedMetadataFailureinputMetadataFailure.orElseThrow());
                                }
                                Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                                while (metadataCauseinputMetadataFailure != null) {
                                  if (metadataCauseinputMetadataFailure
                                      instanceof
                                      UaSerializationException metadataCodecinputMetadataFailure) {
                                    throw new CompletionException(
                                        new UaException(
                                            metadataCodecinputMetadataFailure
                                                        .getStatusCode()
                                                        .getValue()
                                                    == StatusCodes.Bad_OutOfRange
                                                ? StatusCodes.Bad_OutOfRange
                                                : StatusCodes.Bad_TypeMismatch,
                                            metadataCodecinputMetadataFailure));
                                  }
                                  metadataCauseinputMetadataFailure =
                                      metadataCauseinputMetadataFailure.getCause();
                                }
                                throw new CompletionException(
                                    UaException.extract(inputMetadataFailure)
                                        .orElseGet(() -> new UaException(inputMetadataFailure)));
                              }
                              return inputDataTypeTree;
                            })
                        .thenCompose(
                            dataTypeTree -> {
                              if (result.isCancelled()) {
                                return CompletableFuture.failedFuture(new CancellationException());
                              }
                              try {
                                List<Variant> inputArguments = new ArrayList<>();
                                if (rawInputs.size() > 0) {
                                  Variant encoded0;
                                  {
                                    @Nullable Object convertedValue;
                                    {
                                      Object methodValue = rawInputs.get(0);
                                      try {
                                        if (methodValue instanceof Matrix
                                            && ((Matrix) methodValue).isNull()) {
                                          methodValue = null;
                                        }
                                        NamespaceTable namespaceTable =
                                            this.client.getNamespaceTable();
                                        DataTypeTree dataTypeTree_ = dataTypeTree;
                                        NodeId argumentDataTypeId =
                                            ExpandedNodeId.parse("i=24")
                                                .toNodeId(namespaceTable)
                                                .orElseThrow(
                                                    () ->
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "Method argument DataType namespace is"
                                                                + " unavailable"));
                                        if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                            && dataTypeTree_.getDataType(argumentDataTypeId)
                                                == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument GenerateOptions (effective property"
                                                  + " i=15747, DataType i=24) is unavailable in the"
                                                  + " effective type tree; resolved DataType: "
                                                  + argumentDataTypeId);
                                        }
                                        methodValue =
                                            NumericValues.normalize(
                                                methodValue, dataTypeTree_, argumentDataTypeId);
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
                                                StatusCodes.Bad_TypeMismatch,
                                                "Method argument ValueRank mismatch");
                                          }
                                          if (methodValue instanceof Matrix) {
                                            int[] dimensions =
                                                ((Matrix) methodValue).getDimensions();
                                            if (dimensions.length < 2
                                                || !shapeElements.getClass().isArray()
                                                || ArrayUtil.getValueRank(shapeElements) != 1) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Malformed Method Matrix representation");
                                            }
                                            long elementCount = 1;
                                            for (int dimension : dimensions) {
                                              if (dimension < 0
                                                  || elementCount > Integer.MAX_VALUE) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Malformed Method Matrix dimensions");
                                              }
                                              elementCount *= dimension;
                                            }
                                            if (elementCount != Array.getLength(shapeElements)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix dimensions do not match its"
                                                      + " elements");
                                            }
                                            if (!(((Matrix) methodValue)
                                                .getDataType()
                                                .equals(Variant.of(shapeElements).getDataType()))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix DataType does not match its"
                                                      + " elements");
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
                                              || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                            var declaredType =
                                                dataTypeTree_.getType(argumentDataTypeId);
                                            if (typedElements.getClass().isArray()) {
                                              var structureCodec =
                                                  this.client
                                                      .getStaticEncodingContext()
                                                      .getDataTypeManager()
                                                      .getCodec(argumentDataTypeId);
                                              Class<?> structureClass =
                                                  structureCodec == null
                                                      ? UaStructuredType.class
                                                      : structureCodec.getType();
                                              Object decodedStructures =
                                                  Array.newInstance(
                                                      structureClass,
                                                      Array.getLength(typedElements));
                                              for (int structureIndex = 0;
                                                  structureIndex < Array.getLength(typedElements);
                                                  structureIndex++) {
                                                Object structure =
                                                    Array.get(typedElements, structureIndex);
                                                if (structure instanceof ExtensionObject) {
                                                  structure =
                                                      ((ExtensionObject) structure).isNull()
                                                          ? null
                                                          : ((ExtensionObject) structure)
                                                              .decode(
                                                                  this.client
                                                                      .getStaticEncodingContext());
                                                }
                                                if (structure != null) {
                                                  if (!(structure instanceof UaStructuredType)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method argument requires a Structure"
                                                            + " value");
                                                  }
                                                  if (NodeIds.Structure.equals(argumentDataTypeId)
                                                      || declaredType != null
                                                          && declaredType.isAbstract()) {
                                                    if (!dataTypeTree_.isSubtypeOf(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE),
                                                        argumentDataTypeId)) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure is not a subtype of the"
                                                              + " effective DataType");
                                                    }
                                                  } else {
                                                    if (!argumentDataTypeId.equals(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE))) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure does not match the"
                                                              + " effective DataType");
                                                    }
                                                  }
                                                }
                                                Array.set(
                                                    decodedStructures, structureIndex, structure);
                                              }
                                              methodValue =
                                                  methodValue instanceof Matrix
                                                      ? new Matrix(
                                                          decodedStructures,
                                                          ((Matrix) methodValue)
                                                              .getDimensions()
                                                              .clone())
                                                      : decodedStructures;
                                            } else {
                                              if (typedElements instanceof ExtensionObject) {
                                                typedElements =
                                                    ((ExtensionObject) typedElements).isNull()
                                                        ? null
                                                        : ((ExtensionObject) typedElements)
                                                            .decode(
                                                                this.client
                                                                    .getStaticEncodingContext());
                                              }
                                              if (typedElements != null) {
                                                if (!(typedElements instanceof UaStructuredType)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method argument requires a Structure value");
                                                }
                                                if (NodeIds.Structure.equals(argumentDataTypeId)
                                                    || declaredType != null
                                                        && declaredType.isAbstract()) {
                                                  if (!dataTypeTree_.isSubtypeOf(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE),
                                                      argumentDataTypeId)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure is not a subtype of the"
                                                            + " effective DataType");
                                                  }
                                                } else {
                                                  if (!argumentDataTypeId.equals(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE))) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure does not match the"
                                                            + " effective DataType");
                                                  }
                                                }
                                              }
                                              methodValue = typedElements;
                                            }
                                          } else {
                                            Variant.of(typedElements);
                                            NodeId assignableDataTypeId =
                                                dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                            == Number.class
                                                        && dataTypeTree_.isSubtypeOf(
                                                            argumentDataTypeId, NodeIds.Integer)
                                                    ? NodeIds.Integer
                                                    : argumentDataTypeId;
                                            if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                    != Variant.class
                                                && !dataTypeTree_.isAssignable(
                                                    assignableDataTypeId,
                                                    ArrayUtil.getBoxedType(typedElements))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument DataType mismatch");
                                            }
                                          }
                                        }
                                        convertedValue = (Object) methodValue;
                                      } catch (UaSerializationException conversionFailure) {
                                        throw new UaException(
                                            conversionFailure.getStatusCode().getValue()
                                                    == StatusCodes.Bad_OutOfRange
                                                ? StatusCodes.Bad_OutOfRange
                                                : StatusCodes.Bad_TypeMismatch,
                                            conversionFailure);
                                      } catch (ClassCastException
                                          | IllegalArgumentException conversionFailure) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch, conversionFailure);
                                      }
                                    }
                                    try {
                                      Object wireValue = convertedValue;
                                      Object wireElements =
                                          wireValue instanceof Matrix
                                              ? ((Matrix) wireValue).getElements()
                                              : wireValue;
                                      NumericValues.requireEncodable(wireValue);
                                      wireValue =
                                          ExtensionObject.encodeValue(
                                              this.client.getStaticEncodingContext(), wireValue);
                                      encoded0 = Variant.of(wireValue);
                                    } catch (UaSerializationException encodingFailure) {
                                      throw new UaException(
                                          encodingFailure.getStatusCode().getValue()
                                                  == StatusCodes.Bad_OutOfRange
                                              ? StatusCodes.Bad_OutOfRange
                                              : StatusCodes.Bad_TypeMismatch,
                                          encodingFailure);
                                    } catch (ClassCastException
                                        | IllegalArgumentException encodingFailure) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch, encodingFailure);
                                    }
                                  }
                                  inputArguments.add(encoded0);
                                }
                                CallMethodRequest request =
                                    new CallMethodRequest(
                                        getNodeId(),
                                        methodNode.getNodeId(),
                                        inputArguments.toArray(new Variant[0]));
                                return this.client
                                    .getSessionAsync()
                                    .thenCompose(
                                        session -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          RequestHeader originalHeader =
                                              this.client.newRequestHeader(
                                                  session.getAuthenticationToken());
                                          RequestHeader requestHeader =
                                              new RequestHeader(
                                                  originalHeader.getAuthenticationToken(),
                                                  originalHeader.getTimestamp(),
                                                  originalHeader.getRequestHandle(),
                                                  options.returnDiagnostics(),
                                                  originalHeader.getAuditEntryId(),
                                                  originalHeader.getTimeoutHint(),
                                                  originalHeader.getAdditionalHeader());
                                          var call =
                                              this.client.sendRequestAsync(
                                                  new CallRequest(
                                                      requestHeader,
                                                      new CallMethodRequest[] {request}));
                                          result.whenComplete(
                                              (value, failure) -> {
                                                if (result.isCancelled()) {
                                                  call.cancel(false);
                                                }
                                              });
                                          return call.thenCompose(
                                              message -> {
                                                if (result.isCancelled()) {
                                                  throw new CancellationException();
                                                }
                                                try {
                                                  if (!(message instanceof CallResponse response)
                                                      || response.getResponseHeader() == null
                                                      || response
                                                              .getResponseHeader()
                                                              .getServiceResult()
                                                          == null) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_UnexpectedError,
                                                        "Malformed Call response header");
                                                  }
                                                  if (!response
                                                      .getResponseHeader()
                                                      .getServiceResult()
                                                      .isGood()) {
                                                    throw new UaException(
                                                        response
                                                            .getResponseHeader()
                                                            .getServiceResult());
                                                  }
                                                  if (response.getResults() == null
                                                      || response.getResults().length != 1
                                                      || response.getResults()[0] == null
                                                      || response.getResults()[0].getStatusCode()
                                                          == null) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_UnexpectedError,
                                                        "Expected exactly one Call operation"
                                                            + " result");
                                                  }
                                                  CompletableFuture<DataTypeTree> outputMetadata =
                                                      response
                                                              .getResults()[0]
                                                              .getStatusCode()
                                                              .isBad()
                                                          ? CompletableFuture.completedFuture(
                                                              dataTypeTree)
                                                          : ClientDataTypes.read(
                                                              this.client,
                                                              List.<ExpandedNodeId>of(
                                                                  ExpandedNodeId.parse("i=17"),
                                                                  ExpandedNodeId.parse("i=7"),
                                                                  ExpandedNodeId.parse("i=17")),
                                                              (Object)
                                                                  response.getResults()[0]
                                                                      .getOutputArguments());
                                                  result.whenComplete(
                                                      (cancelledValue, cancelledFailure) -> {
                                                        if (result.isCancelled()) {
                                                          outputMetadata.cancel(false);
                                                        }
                                                      });
                                                  return outputMetadata.handle(
                                                      (outputDataTypeTree, metadataFailure) -> {
                                                        if (result.isCancelled()) {
                                                          throw new CancellationException();
                                                        }
                                                        try {
                                                          return MethodCallResult.decode(
                                                              request,
                                                              requestHeader,
                                                              response,
                                                              0,
                                                              outputArguments -> {
                                                                if (metadataFailure != null) {
                                                                  var
                                                                      checkedMetadataFailuremetadataFailure =
                                                                          UaException.extract(
                                                                              metadataFailure);
                                                                  if (checkedMetadataFailuremetadataFailure
                                                                      .isPresent()) {
                                                                    throw checkedMetadataFailuremetadataFailure
                                                                        .orElseThrow();
                                                                  }
                                                                  Throwable
                                                                      metadataCausemetadataFailure =
                                                                          metadataFailure;
                                                                  while (metadataCausemetadataFailure
                                                                      != null) {
                                                                    if (metadataCausemetadataFailure
                                                                        instanceof
                                                                        UaSerializationException
                                                                            metadataCodecmetadataFailure) {
                                                                      throw new UaException(
                                                                          metadataCodecmetadataFailure
                                                                                      .getStatusCode()
                                                                                      .getValue()
                                                                                  == StatusCodes
                                                                                      .Bad_OutOfRange
                                                                              ? StatusCodes
                                                                                  .Bad_OutOfRange
                                                                              : StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                          metadataCodecmetadataFailure);
                                                                    }
                                                                    metadataCausemetadataFailure =
                                                                        metadataCausemetadataFailure
                                                                            .getCause();
                                                                  }
                                                                  throw UaException.extract(
                                                                          metadataFailure)
                                                                      .orElseGet(
                                                                          () ->
                                                                              new UaException(
                                                                                  metadataFailure));
                                                                }
                                                                if ((outputArguments == null
                                                                        ? 0
                                                                        : outputArguments.length)
                                                                    != 3) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Unexpected Method output"
                                                                          + " count");
                                                                }
                                                                if (outputArguments[0] == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Null Method output Variant");
                                                                }
                                                                @Nullable NodeId decoded0;
                                                                {
                                                                  Object methodValue =
                                                                      outputArguments[0].getValue();
                                                                  try {
                                                                    if (methodValue
                                                                            instanceof Matrix
                                                                        && ((Matrix) methodValue)
                                                                            .isNull()) {
                                                                      methodValue = null;
                                                                    }
                                                                    NamespaceTable namespaceTable =
                                                                        this.client
                                                                            .getNamespaceTable();
                                                                    DataTypeTree dataTypeTree_ =
                                                                        outputDataTypeTree;
                                                                    NodeId argumentDataTypeId =
                                                                        ExpandedNodeId.parse("i=17")
                                                                            .toNodeId(
                                                                                namespaceTable)
                                                                            .orElseThrow(
                                                                                () ->
                                                                                    new UaException(
                                                                                        StatusCodes
                                                                                            .Bad_NodeIdInvalid,
                                                                                        "Method"
                                                                                            + " argument"
                                                                                            + " DataType"
                                                                                            + " namespace"
                                                                                            + " is unavailable"));
                                                                    if (!OpcUaDataType.isBuiltin(
                                                                            argumentDataTypeId)
                                                                        && dataTypeTree_
                                                                                .getDataType(
                                                                                    argumentDataTypeId)
                                                                            == null) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " FileNodeId"
                                                                              + " (effective"
                                                                              + " property i=15748,"
                                                                              + " DataType i=17) is"
                                                                              + " unavailable in"
                                                                              + " the effective"
                                                                              + " type tree;"
                                                                              + " resolved"
                                                                              + " DataType: "
                                                                              + argumentDataTypeId);
                                                                    }
                                                                    methodValue =
                                                                        NumericValues.normalize(
                                                                            methodValue,
                                                                            dataTypeTree_,
                                                                            argumentDataTypeId);
                                                                    if (methodValue != null) {
                                                                      Object shapeElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      int valueRank =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getValueRank()
                                                                              : ArrayUtil
                                                                                  .getValueRank(
                                                                                      methodValue);
                                                                      boolean emptyArray =
                                                                          methodValue
                                                                                  .getClass()
                                                                                  .isArray()
                                                                              && ArrayUtil
                                                                                      .getValueRank(
                                                                                          methodValue)
                                                                                  == 1
                                                                              && Array.getLength(
                                                                                      methodValue)
                                                                                  == 0;
                                                                      if (!(valueRank == -1)) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " ValueRank"
                                                                                + " mismatch");
                                                                      }
                                                                      if (methodValue
                                                                          instanceof Matrix) {
                                                                        int[] dimensions =
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions();
                                                                        if (dimensions.length < 2
                                                                            || !shapeElements
                                                                                .getClass()
                                                                                .isArray()
                                                                            || ArrayUtil
                                                                                    .getValueRank(
                                                                                        shapeElements)
                                                                                != 1) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Malformed Method"
                                                                                  + " Matrix"
                                                                                  + " representation");
                                                                        }
                                                                        long elementCount = 1;
                                                                        for (int dimension :
                                                                            dimensions) {
                                                                          if (dimension < 0
                                                                              || elementCount
                                                                                  > Integer
                                                                                      .MAX_VALUE) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Malformed Method"
                                                                                    + " Matrix"
                                                                                    + " dimensions");
                                                                          }
                                                                          elementCount *= dimension;
                                                                        }
                                                                        if (elementCount
                                                                            != Array.getLength(
                                                                                shapeElements)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " dimensions do"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                        if (!(((Matrix) methodValue)
                                                                            .getDataType()
                                                                            .equals(
                                                                                Variant.of(
                                                                                        shapeElements)
                                                                                    .getDataType()))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " DataType does"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                      }
                                                                      Variant.of(shapeElements);
                                                                    }
                                                                    if (methodValue != null) {
                                                                      Object typedElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      if (NodeIds.Structure.equals(
                                                                              argumentDataTypeId)
                                                                          || dataTypeTree_
                                                                              .isStructType(
                                                                                  argumentDataTypeId)) {
                                                                        var declaredType =
                                                                            dataTypeTree_.getType(
                                                                                argumentDataTypeId);
                                                                        if (typedElements
                                                                            .getClass()
                                                                            .isArray()) {
                                                                          var structureCodec =
                                                                              this.client
                                                                                  .getStaticEncodingContext()
                                                                                  .getDataTypeManager()
                                                                                  .getCodec(
                                                                                      argumentDataTypeId);
                                                                          Class<?> structureClass =
                                                                              structureCodec == null
                                                                                  ? UaStructuredType
                                                                                      .class
                                                                                  : structureCodec
                                                                                      .getType();
                                                                          Object decodedStructures =
                                                                              Array.newInstance(
                                                                                  structureClass,
                                                                                  Array.getLength(
                                                                                      typedElements));
                                                                          for (int structureIndex =
                                                                                  0;
                                                                              structureIndex
                                                                                  < Array.getLength(
                                                                                      typedElements);
                                                                              structureIndex++) {
                                                                            Object structure =
                                                                                Array.get(
                                                                                    typedElements,
                                                                                    structureIndex);
                                                                            if (structure
                                                                                instanceof
                                                                                ExtensionObject) {
                                                                              structure =
                                                                                  ((ExtensionObject)
                                                                                              structure)
                                                                                          .isNull()
                                                                                      ? null
                                                                                      : ((ExtensionObject)
                                                                                              structure)
                                                                                          .decode(
                                                                                              this
                                                                                                  .client
                                                                                                  .getStaticEncodingContext());
                                                                            }
                                                                            if (structure != null) {
                                                                              if (!(structure
                                                                                  instanceof
                                                                                  UaStructuredType)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " requires"
                                                                                        + " a Structure"
                                                                                        + " value");
                                                                              }
                                                                              if (NodeIds.Structure
                                                                                      .equals(
                                                                                          argumentDataTypeId)
                                                                                  || declaredType
                                                                                          != null
                                                                                      && declaredType
                                                                                          .isAbstract()) {
                                                                                if (!dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE),
                                                                                        argumentDataTypeId)) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " is not"
                                                                                          + " a subtype"
                                                                                          + " of the"
                                                                                          + " effective"
                                                                                          + " DataType");
                                                                                }
                                                                              } else {
                                                                                if (!argumentDataTypeId
                                                                                    .equals(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE))) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " does"
                                                                                          + " not match"
                                                                                          + " the effective"
                                                                                          + " DataType");
                                                                                }
                                                                              }
                                                                            }
                                                                            Array.set(
                                                                                decodedStructures,
                                                                                structureIndex,
                                                                                structure);
                                                                          }
                                                                          methodValue =
                                                                              methodValue
                                                                                      instanceof
                                                                                      Matrix
                                                                                  ? new Matrix(
                                                                                      decodedStructures,
                                                                                      ((Matrix)
                                                                                              methodValue)
                                                                                          .getDimensions()
                                                                                          .clone())
                                                                                  : decodedStructures;
                                                                        } else {
                                                                          if (typedElements
                                                                              instanceof
                                                                              ExtensionObject) {
                                                                            typedElements =
                                                                                ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .isNull()
                                                                                    ? null
                                                                                    : ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .decode(
                                                                                            this
                                                                                                .client
                                                                                                .getStaticEncodingContext());
                                                                          }
                                                                          if (typedElements
                                                                              != null) {
                                                                            if (!(typedElements
                                                                                instanceof
                                                                                UaStructuredType)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method argument"
                                                                                      + " requires"
                                                                                      + " a Structure"
                                                                                      + " value");
                                                                            }
                                                                            if (NodeIds.Structure
                                                                                    .equals(
                                                                                        argumentDataTypeId)
                                                                                || declaredType
                                                                                        != null
                                                                                    && declaredType
                                                                                        .isAbstract()) {
                                                                              if (!dataTypeTree_
                                                                                  .isSubtypeOf(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE),
                                                                                      argumentDataTypeId)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " is not"
                                                                                        + " a subtype"
                                                                                        + " of the"
                                                                                        + " effective"
                                                                                        + " DataType");
                                                                              }
                                                                            } else {
                                                                              if (!argumentDataTypeId
                                                                                  .equals(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE))) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " does"
                                                                                        + " not match"
                                                                                        + " the effective"
                                                                                        + " DataType");
                                                                              }
                                                                            }
                                                                          }
                                                                          methodValue =
                                                                              typedElements;
                                                                        }
                                                                      } else {
                                                                        Variant.of(typedElements);
                                                                        NodeId
                                                                            assignableDataTypeId =
                                                                                dataTypeTree_
                                                                                                .getBackingClass(
                                                                                                    argumentDataTypeId)
                                                                                            == Number
                                                                                                .class
                                                                                        && dataTypeTree_
                                                                                            .isSubtypeOf(
                                                                                                argumentDataTypeId,
                                                                                                NodeIds
                                                                                                    .Integer)
                                                                                    ? NodeIds
                                                                                        .Integer
                                                                                    : argumentDataTypeId;
                                                                        if (dataTypeTree_
                                                                                    .getBackingClass(
                                                                                        argumentDataTypeId)
                                                                                != Variant.class
                                                                            && !dataTypeTree_
                                                                                .isAssignable(
                                                                                    assignableDataTypeId,
                                                                                    ArrayUtil
                                                                                        .getBoxedType(
                                                                                            typedElements))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " DataType"
                                                                                  + " mismatch");
                                                                        }
                                                                      }
                                                                    }
                                                                    decoded0 = (NodeId) methodValue;
                                                                  } catch (
                                                                      UaSerializationException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        conversionFailure
                                                                                    .getStatusCode()
                                                                                    .getValue()
                                                                                == StatusCodes
                                                                                    .Bad_OutOfRange
                                                                            ? StatusCodes
                                                                                .Bad_OutOfRange
                                                                            : StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  } catch (ClassCastException
                                                                      | IllegalArgumentException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  }
                                                                }
                                                                if (outputArguments[1] == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Null Method output Variant");
                                                                }
                                                                @Nullable UInteger decoded1;
                                                                {
                                                                  Object methodValue =
                                                                      outputArguments[1].getValue();
                                                                  try {
                                                                    if (methodValue
                                                                            instanceof Matrix
                                                                        && ((Matrix) methodValue)
                                                                            .isNull()) {
                                                                      methodValue = null;
                                                                    }
                                                                    NamespaceTable namespaceTable =
                                                                        this.client
                                                                            .getNamespaceTable();
                                                                    DataTypeTree dataTypeTree_ =
                                                                        outputDataTypeTree;
                                                                    NodeId argumentDataTypeId =
                                                                        ExpandedNodeId.parse("i=7")
                                                                            .toNodeId(
                                                                                namespaceTable)
                                                                            .orElseThrow(
                                                                                () ->
                                                                                    new UaException(
                                                                                        StatusCodes
                                                                                            .Bad_NodeIdInvalid,
                                                                                        "Method"
                                                                                            + " argument"
                                                                                            + " DataType"
                                                                                            + " namespace"
                                                                                            + " is unavailable"));
                                                                    if (!OpcUaDataType.isBuiltin(
                                                                            argumentDataTypeId)
                                                                        && dataTypeTree_
                                                                                .getDataType(
                                                                                    argumentDataTypeId)
                                                                            == null) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " FileHandle"
                                                                              + " (effective"
                                                                              + " property i=15748,"
                                                                              + " DataType i=7) is"
                                                                              + " unavailable in"
                                                                              + " the effective"
                                                                              + " type tree;"
                                                                              + " resolved"
                                                                              + " DataType: "
                                                                              + argumentDataTypeId);
                                                                    }
                                                                    methodValue =
                                                                        NumericValues.normalize(
                                                                            methodValue,
                                                                            dataTypeTree_,
                                                                            argumentDataTypeId);
                                                                    if (methodValue != null) {
                                                                      Object shapeElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      int valueRank =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getValueRank()
                                                                              : ArrayUtil
                                                                                  .getValueRank(
                                                                                      methodValue);
                                                                      boolean emptyArray =
                                                                          methodValue
                                                                                  .getClass()
                                                                                  .isArray()
                                                                              && ArrayUtil
                                                                                      .getValueRank(
                                                                                          methodValue)
                                                                                  == 1
                                                                              && Array.getLength(
                                                                                      methodValue)
                                                                                  == 0;
                                                                      if (!(valueRank == -1)) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " ValueRank"
                                                                                + " mismatch");
                                                                      }
                                                                      if (methodValue
                                                                          instanceof Matrix) {
                                                                        int[] dimensions =
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions();
                                                                        if (dimensions.length < 2
                                                                            || !shapeElements
                                                                                .getClass()
                                                                                .isArray()
                                                                            || ArrayUtil
                                                                                    .getValueRank(
                                                                                        shapeElements)
                                                                                != 1) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Malformed Method"
                                                                                  + " Matrix"
                                                                                  + " representation");
                                                                        }
                                                                        long elementCount = 1;
                                                                        for (int dimension :
                                                                            dimensions) {
                                                                          if (dimension < 0
                                                                              || elementCount
                                                                                  > Integer
                                                                                      .MAX_VALUE) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Malformed Method"
                                                                                    + " Matrix"
                                                                                    + " dimensions");
                                                                          }
                                                                          elementCount *= dimension;
                                                                        }
                                                                        if (elementCount
                                                                            != Array.getLength(
                                                                                shapeElements)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " dimensions do"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                        if (!(((Matrix) methodValue)
                                                                            .getDataType()
                                                                            .equals(
                                                                                Variant.of(
                                                                                        shapeElements)
                                                                                    .getDataType()))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " DataType does"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                      }
                                                                      Variant.of(shapeElements);
                                                                    }
                                                                    if (methodValue != null) {
                                                                      Object typedElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      if (NodeIds.Structure.equals(
                                                                              argumentDataTypeId)
                                                                          || dataTypeTree_
                                                                              .isStructType(
                                                                                  argumentDataTypeId)) {
                                                                        var declaredType =
                                                                            dataTypeTree_.getType(
                                                                                argumentDataTypeId);
                                                                        if (typedElements
                                                                            .getClass()
                                                                            .isArray()) {
                                                                          var structureCodec =
                                                                              this.client
                                                                                  .getStaticEncodingContext()
                                                                                  .getDataTypeManager()
                                                                                  .getCodec(
                                                                                      argumentDataTypeId);
                                                                          Class<?> structureClass =
                                                                              structureCodec == null
                                                                                  ? UaStructuredType
                                                                                      .class
                                                                                  : structureCodec
                                                                                      .getType();
                                                                          Object decodedStructures =
                                                                              Array.newInstance(
                                                                                  structureClass,
                                                                                  Array.getLength(
                                                                                      typedElements));
                                                                          for (int structureIndex =
                                                                                  0;
                                                                              structureIndex
                                                                                  < Array.getLength(
                                                                                      typedElements);
                                                                              structureIndex++) {
                                                                            Object structure =
                                                                                Array.get(
                                                                                    typedElements,
                                                                                    structureIndex);
                                                                            if (structure
                                                                                instanceof
                                                                                ExtensionObject) {
                                                                              structure =
                                                                                  ((ExtensionObject)
                                                                                              structure)
                                                                                          .isNull()
                                                                                      ? null
                                                                                      : ((ExtensionObject)
                                                                                              structure)
                                                                                          .decode(
                                                                                              this
                                                                                                  .client
                                                                                                  .getStaticEncodingContext());
                                                                            }
                                                                            if (structure != null) {
                                                                              if (!(structure
                                                                                  instanceof
                                                                                  UaStructuredType)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " requires"
                                                                                        + " a Structure"
                                                                                        + " value");
                                                                              }
                                                                              if (NodeIds.Structure
                                                                                      .equals(
                                                                                          argumentDataTypeId)
                                                                                  || declaredType
                                                                                          != null
                                                                                      && declaredType
                                                                                          .isAbstract()) {
                                                                                if (!dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE),
                                                                                        argumentDataTypeId)) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " is not"
                                                                                          + " a subtype"
                                                                                          + " of the"
                                                                                          + " effective"
                                                                                          + " DataType");
                                                                                }
                                                                              } else {
                                                                                if (!argumentDataTypeId
                                                                                    .equals(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE))) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " does"
                                                                                          + " not match"
                                                                                          + " the effective"
                                                                                          + " DataType");
                                                                                }
                                                                              }
                                                                            }
                                                                            Array.set(
                                                                                decodedStructures,
                                                                                structureIndex,
                                                                                structure);
                                                                          }
                                                                          methodValue =
                                                                              methodValue
                                                                                      instanceof
                                                                                      Matrix
                                                                                  ? new Matrix(
                                                                                      decodedStructures,
                                                                                      ((Matrix)
                                                                                              methodValue)
                                                                                          .getDimensions()
                                                                                          .clone())
                                                                                  : decodedStructures;
                                                                        } else {
                                                                          if (typedElements
                                                                              instanceof
                                                                              ExtensionObject) {
                                                                            typedElements =
                                                                                ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .isNull()
                                                                                    ? null
                                                                                    : ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .decode(
                                                                                            this
                                                                                                .client
                                                                                                .getStaticEncodingContext());
                                                                          }
                                                                          if (typedElements
                                                                              != null) {
                                                                            if (!(typedElements
                                                                                instanceof
                                                                                UaStructuredType)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method argument"
                                                                                      + " requires"
                                                                                      + " a Structure"
                                                                                      + " value");
                                                                            }
                                                                            if (NodeIds.Structure
                                                                                    .equals(
                                                                                        argumentDataTypeId)
                                                                                || declaredType
                                                                                        != null
                                                                                    && declaredType
                                                                                        .isAbstract()) {
                                                                              if (!dataTypeTree_
                                                                                  .isSubtypeOf(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE),
                                                                                      argumentDataTypeId)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " is not"
                                                                                        + " a subtype"
                                                                                        + " of the"
                                                                                        + " effective"
                                                                                        + " DataType");
                                                                              }
                                                                            } else {
                                                                              if (!argumentDataTypeId
                                                                                  .equals(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE))) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " does"
                                                                                        + " not match"
                                                                                        + " the effective"
                                                                                        + " DataType");
                                                                              }
                                                                            }
                                                                          }
                                                                          methodValue =
                                                                              typedElements;
                                                                        }
                                                                      } else {
                                                                        Variant.of(typedElements);
                                                                        NodeId
                                                                            assignableDataTypeId =
                                                                                dataTypeTree_
                                                                                                .getBackingClass(
                                                                                                    argumentDataTypeId)
                                                                                            == Number
                                                                                                .class
                                                                                        && dataTypeTree_
                                                                                            .isSubtypeOf(
                                                                                                argumentDataTypeId,
                                                                                                NodeIds
                                                                                                    .Integer)
                                                                                    ? NodeIds
                                                                                        .Integer
                                                                                    : argumentDataTypeId;
                                                                        if (dataTypeTree_
                                                                                    .getBackingClass(
                                                                                        argumentDataTypeId)
                                                                                != Variant.class
                                                                            && !dataTypeTree_
                                                                                .isAssignable(
                                                                                    assignableDataTypeId,
                                                                                    ArrayUtil
                                                                                        .getBoxedType(
                                                                                            typedElements))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " DataType"
                                                                                  + " mismatch");
                                                                        }
                                                                      }
                                                                    }
                                                                    decoded1 =
                                                                        (UInteger) methodValue;
                                                                  } catch (
                                                                      UaSerializationException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        conversionFailure
                                                                                    .getStatusCode()
                                                                                    .getValue()
                                                                                == StatusCodes
                                                                                    .Bad_OutOfRange
                                                                            ? StatusCodes
                                                                                .Bad_OutOfRange
                                                                            : StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  } catch (ClassCastException
                                                                      | IllegalArgumentException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  }
                                                                }
                                                                if (outputArguments[2] == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Null Method output Variant");
                                                                }
                                                                @Nullable NodeId decoded2;
                                                                {
                                                                  Object methodValue =
                                                                      outputArguments[2].getValue();
                                                                  try {
                                                                    if (methodValue
                                                                            instanceof Matrix
                                                                        && ((Matrix) methodValue)
                                                                            .isNull()) {
                                                                      methodValue = null;
                                                                    }
                                                                    NamespaceTable namespaceTable =
                                                                        this.client
                                                                            .getNamespaceTable();
                                                                    DataTypeTree dataTypeTree_ =
                                                                        outputDataTypeTree;
                                                                    NodeId argumentDataTypeId =
                                                                        ExpandedNodeId.parse("i=17")
                                                                            .toNodeId(
                                                                                namespaceTable)
                                                                            .orElseThrow(
                                                                                () ->
                                                                                    new UaException(
                                                                                        StatusCodes
                                                                                            .Bad_NodeIdInvalid,
                                                                                        "Method"
                                                                                            + " argument"
                                                                                            + " DataType"
                                                                                            + " namespace"
                                                                                            + " is unavailable"));
                                                                    if (!OpcUaDataType.isBuiltin(
                                                                            argumentDataTypeId)
                                                                        && dataTypeTree_
                                                                                .getDataType(
                                                                                    argumentDataTypeId)
                                                                            == null) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " CompletionStateMachine"
                                                                              + " (effective"
                                                                              + " property i=15748,"
                                                                              + " DataType i=17) is"
                                                                              + " unavailable in"
                                                                              + " the effective"
                                                                              + " type tree;"
                                                                              + " resolved"
                                                                              + " DataType: "
                                                                              + argumentDataTypeId);
                                                                    }
                                                                    methodValue =
                                                                        NumericValues.normalize(
                                                                            methodValue,
                                                                            dataTypeTree_,
                                                                            argumentDataTypeId);
                                                                    if (methodValue != null) {
                                                                      Object shapeElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      int valueRank =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getValueRank()
                                                                              : ArrayUtil
                                                                                  .getValueRank(
                                                                                      methodValue);
                                                                      boolean emptyArray =
                                                                          methodValue
                                                                                  .getClass()
                                                                                  .isArray()
                                                                              && ArrayUtil
                                                                                      .getValueRank(
                                                                                          methodValue)
                                                                                  == 1
                                                                              && Array.getLength(
                                                                                      methodValue)
                                                                                  == 0;
                                                                      if (!(valueRank == -1)) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " ValueRank"
                                                                                + " mismatch");
                                                                      }
                                                                      if (methodValue
                                                                          instanceof Matrix) {
                                                                        int[] dimensions =
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions();
                                                                        if (dimensions.length < 2
                                                                            || !shapeElements
                                                                                .getClass()
                                                                                .isArray()
                                                                            || ArrayUtil
                                                                                    .getValueRank(
                                                                                        shapeElements)
                                                                                != 1) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Malformed Method"
                                                                                  + " Matrix"
                                                                                  + " representation");
                                                                        }
                                                                        long elementCount = 1;
                                                                        for (int dimension :
                                                                            dimensions) {
                                                                          if (dimension < 0
                                                                              || elementCount
                                                                                  > Integer
                                                                                      .MAX_VALUE) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Malformed Method"
                                                                                    + " Matrix"
                                                                                    + " dimensions");
                                                                          }
                                                                          elementCount *= dimension;
                                                                        }
                                                                        if (elementCount
                                                                            != Array.getLength(
                                                                                shapeElements)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " dimensions do"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                        if (!(((Matrix) methodValue)
                                                                            .getDataType()
                                                                            .equals(
                                                                                Variant.of(
                                                                                        shapeElements)
                                                                                    .getDataType()))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " DataType does"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                      }
                                                                      Variant.of(shapeElements);
                                                                    }
                                                                    if (methodValue != null) {
                                                                      Object typedElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      if (NodeIds.Structure.equals(
                                                                              argumentDataTypeId)
                                                                          || dataTypeTree_
                                                                              .isStructType(
                                                                                  argumentDataTypeId)) {
                                                                        var declaredType =
                                                                            dataTypeTree_.getType(
                                                                                argumentDataTypeId);
                                                                        if (typedElements
                                                                            .getClass()
                                                                            .isArray()) {
                                                                          var structureCodec =
                                                                              this.client
                                                                                  .getStaticEncodingContext()
                                                                                  .getDataTypeManager()
                                                                                  .getCodec(
                                                                                      argumentDataTypeId);
                                                                          Class<?> structureClass =
                                                                              structureCodec == null
                                                                                  ? UaStructuredType
                                                                                      .class
                                                                                  : structureCodec
                                                                                      .getType();
                                                                          Object decodedStructures =
                                                                              Array.newInstance(
                                                                                  structureClass,
                                                                                  Array.getLength(
                                                                                      typedElements));
                                                                          for (int structureIndex =
                                                                                  0;
                                                                              structureIndex
                                                                                  < Array.getLength(
                                                                                      typedElements);
                                                                              structureIndex++) {
                                                                            Object structure =
                                                                                Array.get(
                                                                                    typedElements,
                                                                                    structureIndex);
                                                                            if (structure
                                                                                instanceof
                                                                                ExtensionObject) {
                                                                              structure =
                                                                                  ((ExtensionObject)
                                                                                              structure)
                                                                                          .isNull()
                                                                                      ? null
                                                                                      : ((ExtensionObject)
                                                                                              structure)
                                                                                          .decode(
                                                                                              this
                                                                                                  .client
                                                                                                  .getStaticEncodingContext());
                                                                            }
                                                                            if (structure != null) {
                                                                              if (!(structure
                                                                                  instanceof
                                                                                  UaStructuredType)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " requires"
                                                                                        + " a Structure"
                                                                                        + " value");
                                                                              }
                                                                              if (NodeIds.Structure
                                                                                      .equals(
                                                                                          argumentDataTypeId)
                                                                                  || declaredType
                                                                                          != null
                                                                                      && declaredType
                                                                                          .isAbstract()) {
                                                                                if (!dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE),
                                                                                        argumentDataTypeId)) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " is not"
                                                                                          + " a subtype"
                                                                                          + " of the"
                                                                                          + " effective"
                                                                                          + " DataType");
                                                                                }
                                                                              } else {
                                                                                if (!argumentDataTypeId
                                                                                    .equals(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE))) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " does"
                                                                                          + " not match"
                                                                                          + " the effective"
                                                                                          + " DataType");
                                                                                }
                                                                              }
                                                                            }
                                                                            Array.set(
                                                                                decodedStructures,
                                                                                structureIndex,
                                                                                structure);
                                                                          }
                                                                          methodValue =
                                                                              methodValue
                                                                                      instanceof
                                                                                      Matrix
                                                                                  ? new Matrix(
                                                                                      decodedStructures,
                                                                                      ((Matrix)
                                                                                              methodValue)
                                                                                          .getDimensions()
                                                                                          .clone())
                                                                                  : decodedStructures;
                                                                        } else {
                                                                          if (typedElements
                                                                              instanceof
                                                                              ExtensionObject) {
                                                                            typedElements =
                                                                                ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .isNull()
                                                                                    ? null
                                                                                    : ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .decode(
                                                                                            this
                                                                                                .client
                                                                                                .getStaticEncodingContext());
                                                                          }
                                                                          if (typedElements
                                                                              != null) {
                                                                            if (!(typedElements
                                                                                instanceof
                                                                                UaStructuredType)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method argument"
                                                                                      + " requires"
                                                                                      + " a Structure"
                                                                                      + " value");
                                                                            }
                                                                            if (NodeIds.Structure
                                                                                    .equals(
                                                                                        argumentDataTypeId)
                                                                                || declaredType
                                                                                        != null
                                                                                    && declaredType
                                                                                        .isAbstract()) {
                                                                              if (!dataTypeTree_
                                                                                  .isSubtypeOf(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE),
                                                                                      argumentDataTypeId)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " is not"
                                                                                        + " a subtype"
                                                                                        + " of the"
                                                                                        + " effective"
                                                                                        + " DataType");
                                                                              }
                                                                            } else {
                                                                              if (!argumentDataTypeId
                                                                                  .equals(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE))) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " does"
                                                                                        + " not match"
                                                                                        + " the effective"
                                                                                        + " DataType");
                                                                              }
                                                                            }
                                                                          }
                                                                          methodValue =
                                                                              typedElements;
                                                                        }
                                                                      } else {
                                                                        Variant.of(typedElements);
                                                                        NodeId
                                                                            assignableDataTypeId =
                                                                                dataTypeTree_
                                                                                                .getBackingClass(
                                                                                                    argumentDataTypeId)
                                                                                            == Number
                                                                                                .class
                                                                                        && dataTypeTree_
                                                                                            .isSubtypeOf(
                                                                                                argumentDataTypeId,
                                                                                                NodeIds
                                                                                                    .Integer)
                                                                                    ? NodeIds
                                                                                        .Integer
                                                                                    : argumentDataTypeId;
                                                                        if (dataTypeTree_
                                                                                    .getBackingClass(
                                                                                        argumentDataTypeId)
                                                                                != Variant.class
                                                                            && !dataTypeTree_
                                                                                .isAssignable(
                                                                                    assignableDataTypeId,
                                                                                    ArrayUtil
                                                                                        .getBoxedType(
                                                                                            typedElements))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " DataType"
                                                                                  + " mismatch");
                                                                        }
                                                                      }
                                                                    }
                                                                    decoded2 = (NodeId) methodValue;
                                                                  } catch (
                                                                      UaSerializationException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        conversionFailure
                                                                                    .getStatusCode()
                                                                                    .getValue()
                                                                                == StatusCodes
                                                                                    .Bad_OutOfRange
                                                                            ? StatusCodes
                                                                                .Bad_OutOfRange
                                                                            : StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  } catch (ClassCastException
                                                                      | IllegalArgumentException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  }
                                                                }
                                                                return TemporaryFileTransferTypeGenerateFileForReadOutputs
                                                                    .of(
                                                                        decoded0, decoded1,
                                                                        decoded2);
                                                              });
                                                        } catch (UaException failure) {
                                                          throw new CompletionException(failure);
                                                        }
                                                      });
                                                } catch (Exception failure) {
                                                  return CompletableFuture.failedFuture(failure);
                                                }
                                              });
                                        });
                              } catch (Exception failure) {
                                return CompletableFuture.failedFuture(failure);
                              }
                            });
                  });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }

  @NullMarked
  @Override
  public UaMethodNode getGenerateFileForWriteMethodNode() throws UaException {
    return ClientMembers.await(getGenerateFileForWriteMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getGenerateFileForWriteMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "GenerateFileForWrite",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:GenerateFileForWrite (declaration i=15749, owner"
                + " i=15744)"));
  }

  @NullMarked
  @Override
  public TemporaryFileTransferTypeGenerateFileForWriteOutputs callGenerateFileForWrite(
      @Nullable Object generateOptions) throws UaException {
    return callGenerateFileForWriteDetailed(generateOptions).requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>
      callGenerateFileForWriteAsync(@Nullable Object generateOptions) {
    CompletableFuture<TemporaryFileTransferTypeGenerateFileForWriteOutputs> result =
        new CompletableFuture<>();
    var call = callGenerateFileForWriteDetailedAsync(generateOptions);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>
      callGenerateFileForWriteDetailed(@Nullable Object generateOptions) throws UaException {
    return callGenerateFileForWriteDetailed(MethodCallOptions.NONE, generateOptions);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>
      callGenerateFileForWriteDetailed(MethodCallOptions options, @Nullable Object generateOptions)
          throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(
        callGenerateFileForWriteDetailedAsync(options, generateOptions), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<
          ? extends
              MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>>
      callGenerateFileForWriteDetailedAsync(@Nullable Object generateOptions) {
    return callGenerateFileForWriteDetailedAsync(MethodCallOptions.NONE, generateOptions);
  }

  @NullMarked
  @Override
  public CompletableFuture<
          ? extends
              MethodCallResult<? extends TemporaryFileTransferTypeGenerateFileForWriteOutputs>>
      callGenerateFileForWriteDetailedAsync(
          MethodCallOptions options, @Nullable Object generateOptions) {
    CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForWriteOutputs>>
        result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(generateOptions);
      var lookup = getGenerateFileForWriteMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForWriteOutputs>>
          pipeline =
              lookup.thenCompose(
                  methodNode -> {
                    if (result.isCancelled()) {
                      return CompletableFuture.failedFuture(new CancellationException());
                    }
                    if (methodNode == null) {
                      return CompletableFuture.failedFuture(
                          new UaException(
                              StatusCodes.Bad_NotFound,
                              "Method node is required for invocation: GenerateFileForWrite"));
                    }
                    var inputMetadata =
                        ClientDataTypes.read(
                            this.client,
                            List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=24"))
                                .subList(0, rawInputs.size()),
                            rawInputs.toArray());
                    result.whenComplete(
                        (cancelledValue, cancelledFailure) -> {
                          if (result.isCancelled()) {
                            inputMetadata.cancel(false);
                          }
                        });
                    return inputMetadata
                        .handle(
                            (inputDataTypeTree, inputMetadataFailure) -> {
                              if (inputMetadataFailure != null) {
                                var checkedMetadataFailureinputMetadataFailure =
                                    UaException.extract(inputMetadataFailure);
                                if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                                  throw new CompletionException(
                                      checkedMetadataFailureinputMetadataFailure.orElseThrow());
                                }
                                Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                                while (metadataCauseinputMetadataFailure != null) {
                                  if (metadataCauseinputMetadataFailure
                                      instanceof
                                      UaSerializationException metadataCodecinputMetadataFailure) {
                                    throw new CompletionException(
                                        new UaException(
                                            metadataCodecinputMetadataFailure
                                                        .getStatusCode()
                                                        .getValue()
                                                    == StatusCodes.Bad_OutOfRange
                                                ? StatusCodes.Bad_OutOfRange
                                                : StatusCodes.Bad_TypeMismatch,
                                            metadataCodecinputMetadataFailure));
                                  }
                                  metadataCauseinputMetadataFailure =
                                      metadataCauseinputMetadataFailure.getCause();
                                }
                                throw new CompletionException(
                                    UaException.extract(inputMetadataFailure)
                                        .orElseGet(() -> new UaException(inputMetadataFailure)));
                              }
                              return inputDataTypeTree;
                            })
                        .thenCompose(
                            dataTypeTree -> {
                              if (result.isCancelled()) {
                                return CompletableFuture.failedFuture(new CancellationException());
                              }
                              try {
                                List<Variant> inputArguments = new ArrayList<>();
                                if (rawInputs.size() > 0) {
                                  Variant encoded0;
                                  {
                                    @Nullable Object convertedValue;
                                    {
                                      Object methodValue = rawInputs.get(0);
                                      try {
                                        if (methodValue instanceof Matrix
                                            && ((Matrix) methodValue).isNull()) {
                                          methodValue = null;
                                        }
                                        NamespaceTable namespaceTable =
                                            this.client.getNamespaceTable();
                                        DataTypeTree dataTypeTree_ = dataTypeTree;
                                        NodeId argumentDataTypeId =
                                            ExpandedNodeId.parse("i=24")
                                                .toNodeId(namespaceTable)
                                                .orElseThrow(
                                                    () ->
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "Method argument DataType namespace is"
                                                                + " unavailable"));
                                        if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                            && dataTypeTree_.getDataType(argumentDataTypeId)
                                                == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument GenerateOptions (effective property"
                                                  + " i=16359, DataType i=24) is unavailable in the"
                                                  + " effective type tree; resolved DataType: "
                                                  + argumentDataTypeId);
                                        }
                                        methodValue =
                                            NumericValues.normalize(
                                                methodValue, dataTypeTree_, argumentDataTypeId);
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
                                                StatusCodes.Bad_TypeMismatch,
                                                "Method argument ValueRank mismatch");
                                          }
                                          if (methodValue instanceof Matrix) {
                                            int[] dimensions =
                                                ((Matrix) methodValue).getDimensions();
                                            if (dimensions.length < 2
                                                || !shapeElements.getClass().isArray()
                                                || ArrayUtil.getValueRank(shapeElements) != 1) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Malformed Method Matrix representation");
                                            }
                                            long elementCount = 1;
                                            for (int dimension : dimensions) {
                                              if (dimension < 0
                                                  || elementCount > Integer.MAX_VALUE) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Malformed Method Matrix dimensions");
                                              }
                                              elementCount *= dimension;
                                            }
                                            if (elementCount != Array.getLength(shapeElements)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix dimensions do not match its"
                                                      + " elements");
                                            }
                                            if (!(((Matrix) methodValue)
                                                .getDataType()
                                                .equals(Variant.of(shapeElements).getDataType()))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method Matrix DataType does not match its"
                                                      + " elements");
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
                                              || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                            var declaredType =
                                                dataTypeTree_.getType(argumentDataTypeId);
                                            if (typedElements.getClass().isArray()) {
                                              var structureCodec =
                                                  this.client
                                                      .getStaticEncodingContext()
                                                      .getDataTypeManager()
                                                      .getCodec(argumentDataTypeId);
                                              Class<?> structureClass =
                                                  structureCodec == null
                                                      ? UaStructuredType.class
                                                      : structureCodec.getType();
                                              Object decodedStructures =
                                                  Array.newInstance(
                                                      structureClass,
                                                      Array.getLength(typedElements));
                                              for (int structureIndex = 0;
                                                  structureIndex < Array.getLength(typedElements);
                                                  structureIndex++) {
                                                Object structure =
                                                    Array.get(typedElements, structureIndex);
                                                if (structure instanceof ExtensionObject) {
                                                  structure =
                                                      ((ExtensionObject) structure).isNull()
                                                          ? null
                                                          : ((ExtensionObject) structure)
                                                              .decode(
                                                                  this.client
                                                                      .getStaticEncodingContext());
                                                }
                                                if (structure != null) {
                                                  if (!(structure instanceof UaStructuredType)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method argument requires a Structure"
                                                            + " value");
                                                  }
                                                  if (NodeIds.Structure.equals(argumentDataTypeId)
                                                      || declaredType != null
                                                          && declaredType.isAbstract()) {
                                                    if (!dataTypeTree_.isSubtypeOf(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE),
                                                        argumentDataTypeId)) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure is not a subtype of the"
                                                              + " effective DataType");
                                                    }
                                                  } else {
                                                    if (!argumentDataTypeId.equals(
                                                        ((UaStructuredType) structure)
                                                            .getTypeId()
                                                            .toNodeId(namespaceTable)
                                                            .orElse(NodeId.NULL_VALUE))) {
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          "Method Structure does not match the"
                                                              + " effective DataType");
                                                    }
                                                  }
                                                }
                                                Array.set(
                                                    decodedStructures, structureIndex, structure);
                                              }
                                              methodValue =
                                                  methodValue instanceof Matrix
                                                      ? new Matrix(
                                                          decodedStructures,
                                                          ((Matrix) methodValue)
                                                              .getDimensions()
                                                              .clone())
                                                      : decodedStructures;
                                            } else {
                                              if (typedElements instanceof ExtensionObject) {
                                                typedElements =
                                                    ((ExtensionObject) typedElements).isNull()
                                                        ? null
                                                        : ((ExtensionObject) typedElements)
                                                            .decode(
                                                                this.client
                                                                    .getStaticEncodingContext());
                                              }
                                              if (typedElements != null) {
                                                if (!(typedElements instanceof UaStructuredType)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method argument requires a Structure value");
                                                }
                                                if (NodeIds.Structure.equals(argumentDataTypeId)
                                                    || declaredType != null
                                                        && declaredType.isAbstract()) {
                                                  if (!dataTypeTree_.isSubtypeOf(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE),
                                                      argumentDataTypeId)) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure is not a subtype of the"
                                                            + " effective DataType");
                                                  }
                                                } else {
                                                  if (!argumentDataTypeId.equals(
                                                      ((UaStructuredType) typedElements)
                                                          .getTypeId()
                                                          .toNodeId(namespaceTable)
                                                          .orElse(NodeId.NULL_VALUE))) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_TypeMismatch,
                                                        "Method Structure does not match the"
                                                            + " effective DataType");
                                                  }
                                                }
                                              }
                                              methodValue = typedElements;
                                            }
                                          } else {
                                            Variant.of(typedElements);
                                            NodeId assignableDataTypeId =
                                                dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                            == Number.class
                                                        && dataTypeTree_.isSubtypeOf(
                                                            argumentDataTypeId, NodeIds.Integer)
                                                    ? NodeIds.Integer
                                                    : argumentDataTypeId;
                                            if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                    != Variant.class
                                                && !dataTypeTree_.isAssignable(
                                                    assignableDataTypeId,
                                                    ArrayUtil.getBoxedType(typedElements))) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument DataType mismatch");
                                            }
                                          }
                                        }
                                        convertedValue = (Object) methodValue;
                                      } catch (UaSerializationException conversionFailure) {
                                        throw new UaException(
                                            conversionFailure.getStatusCode().getValue()
                                                    == StatusCodes.Bad_OutOfRange
                                                ? StatusCodes.Bad_OutOfRange
                                                : StatusCodes.Bad_TypeMismatch,
                                            conversionFailure);
                                      } catch (ClassCastException
                                          | IllegalArgumentException conversionFailure) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch, conversionFailure);
                                      }
                                    }
                                    try {
                                      Object wireValue = convertedValue;
                                      Object wireElements =
                                          wireValue instanceof Matrix
                                              ? ((Matrix) wireValue).getElements()
                                              : wireValue;
                                      NumericValues.requireEncodable(wireValue);
                                      wireValue =
                                          ExtensionObject.encodeValue(
                                              this.client.getStaticEncodingContext(), wireValue);
                                      encoded0 = Variant.of(wireValue);
                                    } catch (UaSerializationException encodingFailure) {
                                      throw new UaException(
                                          encodingFailure.getStatusCode().getValue()
                                                  == StatusCodes.Bad_OutOfRange
                                              ? StatusCodes.Bad_OutOfRange
                                              : StatusCodes.Bad_TypeMismatch,
                                          encodingFailure);
                                    } catch (ClassCastException
                                        | IllegalArgumentException encodingFailure) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch, encodingFailure);
                                    }
                                  }
                                  inputArguments.add(encoded0);
                                }
                                CallMethodRequest request =
                                    new CallMethodRequest(
                                        getNodeId(),
                                        methodNode.getNodeId(),
                                        inputArguments.toArray(new Variant[0]));
                                return this.client
                                    .getSessionAsync()
                                    .thenCompose(
                                        session -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          RequestHeader originalHeader =
                                              this.client.newRequestHeader(
                                                  session.getAuthenticationToken());
                                          RequestHeader requestHeader =
                                              new RequestHeader(
                                                  originalHeader.getAuthenticationToken(),
                                                  originalHeader.getTimestamp(),
                                                  originalHeader.getRequestHandle(),
                                                  options.returnDiagnostics(),
                                                  originalHeader.getAuditEntryId(),
                                                  originalHeader.getTimeoutHint(),
                                                  originalHeader.getAdditionalHeader());
                                          var call =
                                              this.client.sendRequestAsync(
                                                  new CallRequest(
                                                      requestHeader,
                                                      new CallMethodRequest[] {request}));
                                          result.whenComplete(
                                              (value, failure) -> {
                                                if (result.isCancelled()) {
                                                  call.cancel(false);
                                                }
                                              });
                                          return call.thenCompose(
                                              message -> {
                                                if (result.isCancelled()) {
                                                  throw new CancellationException();
                                                }
                                                try {
                                                  if (!(message instanceof CallResponse response)
                                                      || response.getResponseHeader() == null
                                                      || response
                                                              .getResponseHeader()
                                                              .getServiceResult()
                                                          == null) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_UnexpectedError,
                                                        "Malformed Call response header");
                                                  }
                                                  if (!response
                                                      .getResponseHeader()
                                                      .getServiceResult()
                                                      .isGood()) {
                                                    throw new UaException(
                                                        response
                                                            .getResponseHeader()
                                                            .getServiceResult());
                                                  }
                                                  if (response.getResults() == null
                                                      || response.getResults().length != 1
                                                      || response.getResults()[0] == null
                                                      || response.getResults()[0].getStatusCode()
                                                          == null) {
                                                    throw new UaException(
                                                        StatusCodes.Bad_UnexpectedError,
                                                        "Expected exactly one Call operation"
                                                            + " result");
                                                  }
                                                  CompletableFuture<DataTypeTree> outputMetadata =
                                                      response
                                                              .getResults()[0]
                                                              .getStatusCode()
                                                              .isBad()
                                                          ? CompletableFuture.completedFuture(
                                                              dataTypeTree)
                                                          : ClientDataTypes.read(
                                                              this.client,
                                                              List.<ExpandedNodeId>of(
                                                                  ExpandedNodeId.parse("i=17"),
                                                                  ExpandedNodeId.parse("i=7")),
                                                              (Object)
                                                                  response.getResults()[0]
                                                                      .getOutputArguments());
                                                  result.whenComplete(
                                                      (cancelledValue, cancelledFailure) -> {
                                                        if (result.isCancelled()) {
                                                          outputMetadata.cancel(false);
                                                        }
                                                      });
                                                  return outputMetadata.handle(
                                                      (outputDataTypeTree, metadataFailure) -> {
                                                        if (result.isCancelled()) {
                                                          throw new CancellationException();
                                                        }
                                                        try {
                                                          return MethodCallResult.decode(
                                                              request,
                                                              requestHeader,
                                                              response,
                                                              0,
                                                              outputArguments -> {
                                                                if (metadataFailure != null) {
                                                                  var
                                                                      checkedMetadataFailuremetadataFailure =
                                                                          UaException.extract(
                                                                              metadataFailure);
                                                                  if (checkedMetadataFailuremetadataFailure
                                                                      .isPresent()) {
                                                                    throw checkedMetadataFailuremetadataFailure
                                                                        .orElseThrow();
                                                                  }
                                                                  Throwable
                                                                      metadataCausemetadataFailure =
                                                                          metadataFailure;
                                                                  while (metadataCausemetadataFailure
                                                                      != null) {
                                                                    if (metadataCausemetadataFailure
                                                                        instanceof
                                                                        UaSerializationException
                                                                            metadataCodecmetadataFailure) {
                                                                      throw new UaException(
                                                                          metadataCodecmetadataFailure
                                                                                      .getStatusCode()
                                                                                      .getValue()
                                                                                  == StatusCodes
                                                                                      .Bad_OutOfRange
                                                                              ? StatusCodes
                                                                                  .Bad_OutOfRange
                                                                              : StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                          metadataCodecmetadataFailure);
                                                                    }
                                                                    metadataCausemetadataFailure =
                                                                        metadataCausemetadataFailure
                                                                            .getCause();
                                                                  }
                                                                  throw UaException.extract(
                                                                          metadataFailure)
                                                                      .orElseGet(
                                                                          () ->
                                                                              new UaException(
                                                                                  metadataFailure));
                                                                }
                                                                if ((outputArguments == null
                                                                        ? 0
                                                                        : outputArguments.length)
                                                                    != 2) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Unexpected Method output"
                                                                          + " count");
                                                                }
                                                                if (outputArguments[0] == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Null Method output Variant");
                                                                }
                                                                @Nullable NodeId decoded0;
                                                                {
                                                                  Object methodValue =
                                                                      outputArguments[0].getValue();
                                                                  try {
                                                                    if (methodValue
                                                                            instanceof Matrix
                                                                        && ((Matrix) methodValue)
                                                                            .isNull()) {
                                                                      methodValue = null;
                                                                    }
                                                                    NamespaceTable namespaceTable =
                                                                        this.client
                                                                            .getNamespaceTable();
                                                                    DataTypeTree dataTypeTree_ =
                                                                        outputDataTypeTree;
                                                                    NodeId argumentDataTypeId =
                                                                        ExpandedNodeId.parse("i=17")
                                                                            .toNodeId(
                                                                                namespaceTable)
                                                                            .orElseThrow(
                                                                                () ->
                                                                                    new UaException(
                                                                                        StatusCodes
                                                                                            .Bad_NodeIdInvalid,
                                                                                        "Method"
                                                                                            + " argument"
                                                                                            + " DataType"
                                                                                            + " namespace"
                                                                                            + " is unavailable"));
                                                                    if (!OpcUaDataType.isBuiltin(
                                                                            argumentDataTypeId)
                                                                        && dataTypeTree_
                                                                                .getDataType(
                                                                                    argumentDataTypeId)
                                                                            == null) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " FileNodeId"
                                                                              + " (effective"
                                                                              + " property i=15750,"
                                                                              + " DataType i=17) is"
                                                                              + " unavailable in"
                                                                              + " the effective"
                                                                              + " type tree;"
                                                                              + " resolved"
                                                                              + " DataType: "
                                                                              + argumentDataTypeId);
                                                                    }
                                                                    methodValue =
                                                                        NumericValues.normalize(
                                                                            methodValue,
                                                                            dataTypeTree_,
                                                                            argumentDataTypeId);
                                                                    if (methodValue != null) {
                                                                      Object shapeElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      int valueRank =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getValueRank()
                                                                              : ArrayUtil
                                                                                  .getValueRank(
                                                                                      methodValue);
                                                                      boolean emptyArray =
                                                                          methodValue
                                                                                  .getClass()
                                                                                  .isArray()
                                                                              && ArrayUtil
                                                                                      .getValueRank(
                                                                                          methodValue)
                                                                                  == 1
                                                                              && Array.getLength(
                                                                                      methodValue)
                                                                                  == 0;
                                                                      if (!(valueRank == -1)) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " ValueRank"
                                                                                + " mismatch");
                                                                      }
                                                                      if (methodValue
                                                                          instanceof Matrix) {
                                                                        int[] dimensions =
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions();
                                                                        if (dimensions.length < 2
                                                                            || !shapeElements
                                                                                .getClass()
                                                                                .isArray()
                                                                            || ArrayUtil
                                                                                    .getValueRank(
                                                                                        shapeElements)
                                                                                != 1) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Malformed Method"
                                                                                  + " Matrix"
                                                                                  + " representation");
                                                                        }
                                                                        long elementCount = 1;
                                                                        for (int dimension :
                                                                            dimensions) {
                                                                          if (dimension < 0
                                                                              || elementCount
                                                                                  > Integer
                                                                                      .MAX_VALUE) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Malformed Method"
                                                                                    + " Matrix"
                                                                                    + " dimensions");
                                                                          }
                                                                          elementCount *= dimension;
                                                                        }
                                                                        if (elementCount
                                                                            != Array.getLength(
                                                                                shapeElements)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " dimensions do"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                        if (!(((Matrix) methodValue)
                                                                            .getDataType()
                                                                            .equals(
                                                                                Variant.of(
                                                                                        shapeElements)
                                                                                    .getDataType()))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " DataType does"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                      }
                                                                      Variant.of(shapeElements);
                                                                    }
                                                                    if (methodValue != null) {
                                                                      Object typedElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      if (NodeIds.Structure.equals(
                                                                              argumentDataTypeId)
                                                                          || dataTypeTree_
                                                                              .isStructType(
                                                                                  argumentDataTypeId)) {
                                                                        var declaredType =
                                                                            dataTypeTree_.getType(
                                                                                argumentDataTypeId);
                                                                        if (typedElements
                                                                            .getClass()
                                                                            .isArray()) {
                                                                          var structureCodec =
                                                                              this.client
                                                                                  .getStaticEncodingContext()
                                                                                  .getDataTypeManager()
                                                                                  .getCodec(
                                                                                      argumentDataTypeId);
                                                                          Class<?> structureClass =
                                                                              structureCodec == null
                                                                                  ? UaStructuredType
                                                                                      .class
                                                                                  : structureCodec
                                                                                      .getType();
                                                                          Object decodedStructures =
                                                                              Array.newInstance(
                                                                                  structureClass,
                                                                                  Array.getLength(
                                                                                      typedElements));
                                                                          for (int structureIndex =
                                                                                  0;
                                                                              structureIndex
                                                                                  < Array.getLength(
                                                                                      typedElements);
                                                                              structureIndex++) {
                                                                            Object structure =
                                                                                Array.get(
                                                                                    typedElements,
                                                                                    structureIndex);
                                                                            if (structure
                                                                                instanceof
                                                                                ExtensionObject) {
                                                                              structure =
                                                                                  ((ExtensionObject)
                                                                                              structure)
                                                                                          .isNull()
                                                                                      ? null
                                                                                      : ((ExtensionObject)
                                                                                              structure)
                                                                                          .decode(
                                                                                              this
                                                                                                  .client
                                                                                                  .getStaticEncodingContext());
                                                                            }
                                                                            if (structure != null) {
                                                                              if (!(structure
                                                                                  instanceof
                                                                                  UaStructuredType)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " requires"
                                                                                        + " a Structure"
                                                                                        + " value");
                                                                              }
                                                                              if (NodeIds.Structure
                                                                                      .equals(
                                                                                          argumentDataTypeId)
                                                                                  || declaredType
                                                                                          != null
                                                                                      && declaredType
                                                                                          .isAbstract()) {
                                                                                if (!dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE),
                                                                                        argumentDataTypeId)) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " is not"
                                                                                          + " a subtype"
                                                                                          + " of the"
                                                                                          + " effective"
                                                                                          + " DataType");
                                                                                }
                                                                              } else {
                                                                                if (!argumentDataTypeId
                                                                                    .equals(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE))) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " does"
                                                                                          + " not match"
                                                                                          + " the effective"
                                                                                          + " DataType");
                                                                                }
                                                                              }
                                                                            }
                                                                            Array.set(
                                                                                decodedStructures,
                                                                                structureIndex,
                                                                                structure);
                                                                          }
                                                                          methodValue =
                                                                              methodValue
                                                                                      instanceof
                                                                                      Matrix
                                                                                  ? new Matrix(
                                                                                      decodedStructures,
                                                                                      ((Matrix)
                                                                                              methodValue)
                                                                                          .getDimensions()
                                                                                          .clone())
                                                                                  : decodedStructures;
                                                                        } else {
                                                                          if (typedElements
                                                                              instanceof
                                                                              ExtensionObject) {
                                                                            typedElements =
                                                                                ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .isNull()
                                                                                    ? null
                                                                                    : ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .decode(
                                                                                            this
                                                                                                .client
                                                                                                .getStaticEncodingContext());
                                                                          }
                                                                          if (typedElements
                                                                              != null) {
                                                                            if (!(typedElements
                                                                                instanceof
                                                                                UaStructuredType)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method argument"
                                                                                      + " requires"
                                                                                      + " a Structure"
                                                                                      + " value");
                                                                            }
                                                                            if (NodeIds.Structure
                                                                                    .equals(
                                                                                        argumentDataTypeId)
                                                                                || declaredType
                                                                                        != null
                                                                                    && declaredType
                                                                                        .isAbstract()) {
                                                                              if (!dataTypeTree_
                                                                                  .isSubtypeOf(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE),
                                                                                      argumentDataTypeId)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " is not"
                                                                                        + " a subtype"
                                                                                        + " of the"
                                                                                        + " effective"
                                                                                        + " DataType");
                                                                              }
                                                                            } else {
                                                                              if (!argumentDataTypeId
                                                                                  .equals(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE))) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " does"
                                                                                        + " not match"
                                                                                        + " the effective"
                                                                                        + " DataType");
                                                                              }
                                                                            }
                                                                          }
                                                                          methodValue =
                                                                              typedElements;
                                                                        }
                                                                      } else {
                                                                        Variant.of(typedElements);
                                                                        NodeId
                                                                            assignableDataTypeId =
                                                                                dataTypeTree_
                                                                                                .getBackingClass(
                                                                                                    argumentDataTypeId)
                                                                                            == Number
                                                                                                .class
                                                                                        && dataTypeTree_
                                                                                            .isSubtypeOf(
                                                                                                argumentDataTypeId,
                                                                                                NodeIds
                                                                                                    .Integer)
                                                                                    ? NodeIds
                                                                                        .Integer
                                                                                    : argumentDataTypeId;
                                                                        if (dataTypeTree_
                                                                                    .getBackingClass(
                                                                                        argumentDataTypeId)
                                                                                != Variant.class
                                                                            && !dataTypeTree_
                                                                                .isAssignable(
                                                                                    assignableDataTypeId,
                                                                                    ArrayUtil
                                                                                        .getBoxedType(
                                                                                            typedElements))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " DataType"
                                                                                  + " mismatch");
                                                                        }
                                                                      }
                                                                    }
                                                                    decoded0 = (NodeId) methodValue;
                                                                  } catch (
                                                                      UaSerializationException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        conversionFailure
                                                                                    .getStatusCode()
                                                                                    .getValue()
                                                                                == StatusCodes
                                                                                    .Bad_OutOfRange
                                                                            ? StatusCodes
                                                                                .Bad_OutOfRange
                                                                            : StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  } catch (ClassCastException
                                                                      | IllegalArgumentException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  }
                                                                }
                                                                if (outputArguments[1] == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Null Method output Variant");
                                                                }
                                                                @Nullable UInteger decoded1;
                                                                {
                                                                  Object methodValue =
                                                                      outputArguments[1].getValue();
                                                                  try {
                                                                    if (methodValue
                                                                            instanceof Matrix
                                                                        && ((Matrix) methodValue)
                                                                            .isNull()) {
                                                                      methodValue = null;
                                                                    }
                                                                    NamespaceTable namespaceTable =
                                                                        this.client
                                                                            .getNamespaceTable();
                                                                    DataTypeTree dataTypeTree_ =
                                                                        outputDataTypeTree;
                                                                    NodeId argumentDataTypeId =
                                                                        ExpandedNodeId.parse("i=7")
                                                                            .toNodeId(
                                                                                namespaceTable)
                                                                            .orElseThrow(
                                                                                () ->
                                                                                    new UaException(
                                                                                        StatusCodes
                                                                                            .Bad_NodeIdInvalid,
                                                                                        "Method"
                                                                                            + " argument"
                                                                                            + " DataType"
                                                                                            + " namespace"
                                                                                            + " is unavailable"));
                                                                    if (!OpcUaDataType.isBuiltin(
                                                                            argumentDataTypeId)
                                                                        && dataTypeTree_
                                                                                .getDataType(
                                                                                    argumentDataTypeId)
                                                                            == null) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " FileHandle"
                                                                              + " (effective"
                                                                              + " property i=15750,"
                                                                              + " DataType i=7) is"
                                                                              + " unavailable in"
                                                                              + " the effective"
                                                                              + " type tree;"
                                                                              + " resolved"
                                                                              + " DataType: "
                                                                              + argumentDataTypeId);
                                                                    }
                                                                    methodValue =
                                                                        NumericValues.normalize(
                                                                            methodValue,
                                                                            dataTypeTree_,
                                                                            argumentDataTypeId);
                                                                    if (methodValue != null) {
                                                                      Object shapeElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      int valueRank =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getValueRank()
                                                                              : ArrayUtil
                                                                                  .getValueRank(
                                                                                      methodValue);
                                                                      boolean emptyArray =
                                                                          methodValue
                                                                                  .getClass()
                                                                                  .isArray()
                                                                              && ArrayUtil
                                                                                      .getValueRank(
                                                                                          methodValue)
                                                                                  == 1
                                                                              && Array.getLength(
                                                                                      methodValue)
                                                                                  == 0;
                                                                      if (!(valueRank == -1)) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " ValueRank"
                                                                                + " mismatch");
                                                                      }
                                                                      if (methodValue
                                                                          instanceof Matrix) {
                                                                        int[] dimensions =
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions();
                                                                        if (dimensions.length < 2
                                                                            || !shapeElements
                                                                                .getClass()
                                                                                .isArray()
                                                                            || ArrayUtil
                                                                                    .getValueRank(
                                                                                        shapeElements)
                                                                                != 1) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Malformed Method"
                                                                                  + " Matrix"
                                                                                  + " representation");
                                                                        }
                                                                        long elementCount = 1;
                                                                        for (int dimension :
                                                                            dimensions) {
                                                                          if (dimension < 0
                                                                              || elementCount
                                                                                  > Integer
                                                                                      .MAX_VALUE) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Malformed Method"
                                                                                    + " Matrix"
                                                                                    + " dimensions");
                                                                          }
                                                                          elementCount *= dimension;
                                                                        }
                                                                        if (elementCount
                                                                            != Array.getLength(
                                                                                shapeElements)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " dimensions do"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                        if (!(((Matrix) methodValue)
                                                                            .getDataType()
                                                                            .equals(
                                                                                Variant.of(
                                                                                        shapeElements)
                                                                                    .getDataType()))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method Matrix"
                                                                                  + " DataType does"
                                                                                  + " not match its"
                                                                                  + " elements");
                                                                        }
                                                                      }
                                                                      Variant.of(shapeElements);
                                                                    }
                                                                    if (methodValue != null) {
                                                                      Object typedElements =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? ((Matrix)
                                                                                      methodValue)
                                                                                  .getElements()
                                                                              : methodValue;
                                                                      if (NodeIds.Structure.equals(
                                                                              argumentDataTypeId)
                                                                          || dataTypeTree_
                                                                              .isStructType(
                                                                                  argumentDataTypeId)) {
                                                                        var declaredType =
                                                                            dataTypeTree_.getType(
                                                                                argumentDataTypeId);
                                                                        if (typedElements
                                                                            .getClass()
                                                                            .isArray()) {
                                                                          var structureCodec =
                                                                              this.client
                                                                                  .getStaticEncodingContext()
                                                                                  .getDataTypeManager()
                                                                                  .getCodec(
                                                                                      argumentDataTypeId);
                                                                          Class<?> structureClass =
                                                                              structureCodec == null
                                                                                  ? UaStructuredType
                                                                                      .class
                                                                                  : structureCodec
                                                                                      .getType();
                                                                          Object decodedStructures =
                                                                              Array.newInstance(
                                                                                  structureClass,
                                                                                  Array.getLength(
                                                                                      typedElements));
                                                                          for (int structureIndex =
                                                                                  0;
                                                                              structureIndex
                                                                                  < Array.getLength(
                                                                                      typedElements);
                                                                              structureIndex++) {
                                                                            Object structure =
                                                                                Array.get(
                                                                                    typedElements,
                                                                                    structureIndex);
                                                                            if (structure
                                                                                instanceof
                                                                                ExtensionObject) {
                                                                              structure =
                                                                                  ((ExtensionObject)
                                                                                              structure)
                                                                                          .isNull()
                                                                                      ? null
                                                                                      : ((ExtensionObject)
                                                                                              structure)
                                                                                          .decode(
                                                                                              this
                                                                                                  .client
                                                                                                  .getStaticEncodingContext());
                                                                            }
                                                                            if (structure != null) {
                                                                              if (!(structure
                                                                                  instanceof
                                                                                  UaStructuredType)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " requires"
                                                                                        + " a Structure"
                                                                                        + " value");
                                                                              }
                                                                              if (NodeIds.Structure
                                                                                      .equals(
                                                                                          argumentDataTypeId)
                                                                                  || declaredType
                                                                                          != null
                                                                                      && declaredType
                                                                                          .isAbstract()) {
                                                                                if (!dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE),
                                                                                        argumentDataTypeId)) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " is not"
                                                                                          + " a subtype"
                                                                                          + " of the"
                                                                                          + " effective"
                                                                                          + " DataType");
                                                                                }
                                                                              } else {
                                                                                if (!argumentDataTypeId
                                                                                    .equals(
                                                                                        ((UaStructuredType)
                                                                                                structure)
                                                                                            .getTypeId()
                                                                                            .toNodeId(
                                                                                                namespaceTable)
                                                                                            .orElse(
                                                                                                NodeId
                                                                                                    .NULL_VALUE))) {
                                                                                  throw new UaException(
                                                                                      StatusCodes
                                                                                          .Bad_TypeMismatch,
                                                                                      "Method"
                                                                                          + " Structure"
                                                                                          + " does"
                                                                                          + " not match"
                                                                                          + " the effective"
                                                                                          + " DataType");
                                                                                }
                                                                              }
                                                                            }
                                                                            Array.set(
                                                                                decodedStructures,
                                                                                structureIndex,
                                                                                structure);
                                                                          }
                                                                          methodValue =
                                                                              methodValue
                                                                                      instanceof
                                                                                      Matrix
                                                                                  ? new Matrix(
                                                                                      decodedStructures,
                                                                                      ((Matrix)
                                                                                              methodValue)
                                                                                          .getDimensions()
                                                                                          .clone())
                                                                                  : decodedStructures;
                                                                        } else {
                                                                          if (typedElements
                                                                              instanceof
                                                                              ExtensionObject) {
                                                                            typedElements =
                                                                                ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .isNull()
                                                                                    ? null
                                                                                    : ((ExtensionObject)
                                                                                            typedElements)
                                                                                        .decode(
                                                                                            this
                                                                                                .client
                                                                                                .getStaticEncodingContext());
                                                                          }
                                                                          if (typedElements
                                                                              != null) {
                                                                            if (!(typedElements
                                                                                instanceof
                                                                                UaStructuredType)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method argument"
                                                                                      + " requires"
                                                                                      + " a Structure"
                                                                                      + " value");
                                                                            }
                                                                            if (NodeIds.Structure
                                                                                    .equals(
                                                                                        argumentDataTypeId)
                                                                                || declaredType
                                                                                        != null
                                                                                    && declaredType
                                                                                        .isAbstract()) {
                                                                              if (!dataTypeTree_
                                                                                  .isSubtypeOf(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE),
                                                                                      argumentDataTypeId)) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " is not"
                                                                                        + " a subtype"
                                                                                        + " of the"
                                                                                        + " effective"
                                                                                        + " DataType");
                                                                              }
                                                                            } else {
                                                                              if (!argumentDataTypeId
                                                                                  .equals(
                                                                                      ((UaStructuredType)
                                                                                              typedElements)
                                                                                          .getTypeId()
                                                                                          .toNodeId(
                                                                                              namespaceTable)
                                                                                          .orElse(
                                                                                              NodeId
                                                                                                  .NULL_VALUE))) {
                                                                                throw new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_TypeMismatch,
                                                                                    "Method"
                                                                                        + " Structure"
                                                                                        + " does"
                                                                                        + " not match"
                                                                                        + " the effective"
                                                                                        + " DataType");
                                                                              }
                                                                            }
                                                                          }
                                                                          methodValue =
                                                                              typedElements;
                                                                        }
                                                                      } else {
                                                                        Variant.of(typedElements);
                                                                        NodeId
                                                                            assignableDataTypeId =
                                                                                dataTypeTree_
                                                                                                .getBackingClass(
                                                                                                    argumentDataTypeId)
                                                                                            == Number
                                                                                                .class
                                                                                        && dataTypeTree_
                                                                                            .isSubtypeOf(
                                                                                                argumentDataTypeId,
                                                                                                NodeIds
                                                                                                    .Integer)
                                                                                    ? NodeIds
                                                                                        .Integer
                                                                                    : argumentDataTypeId;
                                                                        if (dataTypeTree_
                                                                                    .getBackingClass(
                                                                                        argumentDataTypeId)
                                                                                != Variant.class
                                                                            && !dataTypeTree_
                                                                                .isAssignable(
                                                                                    assignableDataTypeId,
                                                                                    ArrayUtil
                                                                                        .getBoxedType(
                                                                                            typedElements))) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " DataType"
                                                                                  + " mismatch");
                                                                        }
                                                                      }
                                                                    }
                                                                    decoded1 =
                                                                        (UInteger) methodValue;
                                                                  } catch (
                                                                      UaSerializationException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        conversionFailure
                                                                                    .getStatusCode()
                                                                                    .getValue()
                                                                                == StatusCodes
                                                                                    .Bad_OutOfRange
                                                                            ? StatusCodes
                                                                                .Bad_OutOfRange
                                                                            : StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  } catch (ClassCastException
                                                                      | IllegalArgumentException
                                                                          conversionFailure) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        conversionFailure);
                                                                  }
                                                                }
                                                                return TemporaryFileTransferTypeGenerateFileForWriteOutputs
                                                                    .of(decoded0, decoded1);
                                                              });
                                                        } catch (UaException failure) {
                                                          throw new CompletionException(failure);
                                                        }
                                                      });
                                                } catch (Exception failure) {
                                                  return CompletableFuture.failedFuture(failure);
                                                }
                                              });
                                        });
                              } catch (Exception failure) {
                                return CompletableFuture.failedFuture(failure);
                              }
                            });
                  });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }

  @NullMarked
  @Override
  public UaMethodNode getCloseAndCommitMethodNode() throws UaException {
    return ClientMembers.await(getCloseAndCommitMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getCloseAndCommitMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CloseAndCommit",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:CloseAndCommit (declaration i=15751, owner i=15744)"));
  }

  @NullMarked
  @Override
  public @Nullable NodeId callCloseAndCommit(@Nullable UInteger fileHandle) throws UaException {
    return callCloseAndCommitDetailed(fileHandle).requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable NodeId> callCloseAndCommitAsync(
      @Nullable UInteger fileHandle) {
    CompletableFuture<@Nullable NodeId> result = new CompletableFuture<>();
    var call = callCloseAndCommitDetailedAsync(fileHandle);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable NodeId> callCloseAndCommitDetailed(
      @Nullable UInteger fileHandle) throws UaException {
    return callCloseAndCommitDetailed(MethodCallOptions.NONE, fileHandle);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable NodeId> callCloseAndCommitDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(callCloseAndCommitDetailedAsync(options, fileHandle), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCloseAndCommitDetailedAsync(@Nullable UInteger fileHandle) {
    return callCloseAndCommitDetailedAsync(MethodCallOptions.NONE, fileHandle);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCloseAndCommitDetailedAsync(MethodCallOptions options, @Nullable UInteger fileHandle) {
    CompletableFuture<MethodCallResult<@Nullable NodeId>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(fileHandle);
      var lookup = getCloseAndCommitMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable NodeId>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: CloseAndCommit"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=7"))
                            .subList(0, rawInputs.size()),
                        rawInputs.toArray());
                result.whenComplete(
                    (cancelledValue, cancelledFailure) -> {
                      if (result.isCancelled()) {
                        inputMetadata.cancel(false);
                      }
                    });
                return inputMetadata
                    .handle(
                        (inputDataTypeTree, inputMetadataFailure) -> {
                          if (inputMetadataFailure != null) {
                            var checkedMetadataFailureinputMetadataFailure =
                                UaException.extract(inputMetadataFailure);
                            if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                              throw new CompletionException(
                                  checkedMetadataFailureinputMetadataFailure.orElseThrow());
                            }
                            Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                            while (metadataCauseinputMetadataFailure != null) {
                              if (metadataCauseinputMetadataFailure
                                  instanceof
                                  UaSerializationException metadataCodecinputMetadataFailure) {
                                throw new CompletionException(
                                    new UaException(
                                        metadataCodecinputMetadataFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        metadataCodecinputMetadataFailure));
                              }
                              metadataCauseinputMetadataFailure =
                                  metadataCauseinputMetadataFailure.getCause();
                            }
                            throw new CompletionException(
                                UaException.extract(inputMetadataFailure)
                                    .orElseGet(() -> new UaException(inputMetadataFailure)));
                          }
                          return inputDataTypeTree;
                        })
                    .thenCompose(
                        dataTypeTree -> {
                          if (result.isCancelled()) {
                            return CompletableFuture.failedFuture(new CancellationException());
                          }
                          try {
                            List<Variant> inputArguments = new ArrayList<>();
                            if (rawInputs.size() > 0) {
                              Variant encoded0;
                              {
                                @Nullable UInteger convertedValue;
                                {
                                  Object methodValue = rawInputs.get(0);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=7")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument FileHandle (effective property i=15752,"
                                              + " DataType i=7) is unavailable in the effective"
                                              + " type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    methodValue =
                                        NumericValues.normalize(
                                            methodValue, dataTypeTree_, argumentDataTypeId);
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
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
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
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
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
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (UInteger) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  NumericValues.requireEncodable(wireValue);
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded0 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded0);
                            }
                            CallMethodRequest request =
                                new CallMethodRequest(
                                    getNodeId(),
                                    methodNode.getNodeId(),
                                    inputArguments.toArray(new Variant[0]));
                            return this.client
                                .getSessionAsync()
                                .thenCompose(
                                    session -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      RequestHeader originalHeader =
                                          this.client.newRequestHeader(
                                              session.getAuthenticationToken());
                                      RequestHeader requestHeader =
                                          new RequestHeader(
                                              originalHeader.getAuthenticationToken(),
                                              originalHeader.getTimestamp(),
                                              originalHeader.getRequestHandle(),
                                              options.returnDiagnostics(),
                                              originalHeader.getAuditEntryId(),
                                              originalHeader.getTimeoutHint(),
                                              originalHeader.getAdditionalHeader());
                                      var call =
                                          this.client.sendRequestAsync(
                                              new CallRequest(
                                                  requestHeader,
                                                  new CallMethodRequest[] {request}));
                                      result.whenComplete(
                                          (value, failure) -> {
                                            if (result.isCancelled()) {
                                              call.cancel(false);
                                            }
                                          });
                                      return call.thenCompose(
                                          message -> {
                                            if (result.isCancelled()) {
                                              throw new CancellationException();
                                            }
                                            try {
                                              if (!(message instanceof CallResponse response)
                                                  || response.getResponseHeader() == null
                                                  || response.getResponseHeader().getServiceResult()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Malformed Call response header");
                                              }
                                              if (!response
                                                  .getResponseHeader()
                                                  .getServiceResult()
                                                  .isGood()) {
                                                throw new UaException(
                                                    response
                                                        .getResponseHeader()
                                                        .getServiceResult());
                                              }
                                              if (response.getResults() == null
                                                  || response.getResults().length != 1
                                                  || response.getResults()[0] == null
                                                  || response.getResults()[0].getStatusCode()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Expected exactly one Call operation result");
                                              }
                                              CompletableFuture<DataTypeTree> outputMetadata =
                                                  response.getResults()[0].getStatusCode().isBad()
                                                      ? CompletableFuture.completedFuture(
                                                          dataTypeTree)
                                                      : ClientDataTypes.read(
                                                          this.client,
                                                          List.<ExpandedNodeId>of(
                                                              ExpandedNodeId.parse("i=17")),
                                                          (Object)
                                                              response.getResults()[0]
                                                                  .getOutputArguments());
                                              result.whenComplete(
                                                  (cancelledValue, cancelledFailure) -> {
                                                    if (result.isCancelled()) {
                                                      outputMetadata.cancel(false);
                                                    }
                                                  });
                                              return outputMetadata.handle(
                                                  (outputDataTypeTree, metadataFailure) -> {
                                                    if (result.isCancelled()) {
                                                      throw new CancellationException();
                                                    }
                                                    try {
                                                      return MethodCallResult.decode(
                                                          request,
                                                          requestHeader,
                                                          response,
                                                          0,
                                                          outputArguments -> {
                                                            if (metadataFailure != null) {
                                                              var
                                                                  checkedMetadataFailuremetadataFailure =
                                                                      UaException.extract(
                                                                          metadataFailure);
                                                              if (checkedMetadataFailuremetadataFailure
                                                                  .isPresent()) {
                                                                throw checkedMetadataFailuremetadataFailure
                                                                    .orElseThrow();
                                                              }
                                                              Throwable
                                                                  metadataCausemetadataFailure =
                                                                      metadataFailure;
                                                              while (metadataCausemetadataFailure
                                                                  != null) {
                                                                if (metadataCausemetadataFailure
                                                                    instanceof
                                                                    UaSerializationException
                                                                        metadataCodecmetadataFailure) {
                                                                  throw new UaException(
                                                                      metadataCodecmetadataFailure
                                                                                  .getStatusCode()
                                                                                  .getValue()
                                                                              == StatusCodes
                                                                                  .Bad_OutOfRange
                                                                          ? StatusCodes
                                                                              .Bad_OutOfRange
                                                                          : StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                      metadataCodecmetadataFailure);
                                                                }
                                                                metadataCausemetadataFailure =
                                                                    metadataCausemetadataFailure
                                                                        .getCause();
                                                              }
                                                              throw UaException.extract(
                                                                      metadataFailure)
                                                                  .orElseGet(
                                                                      () ->
                                                                          new UaException(
                                                                              metadataFailure));
                                                            }
                                                            if ((outputArguments == null
                                                                    ? 0
                                                                    : outputArguments.length)
                                                                != 1) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            if (outputArguments[0] == null) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Null Method output Variant");
                                                            }
                                                            @Nullable NodeId decoded0;
                                                            {
                                                              Object methodValue =
                                                                  outputArguments[0].getValue();
                                                              try {
                                                                if (methodValue instanceof Matrix
                                                                    && ((Matrix) methodValue)
                                                                        .isNull()) {
                                                                  methodValue = null;
                                                                }
                                                                NamespaceTable namespaceTable =
                                                                    this.client.getNamespaceTable();
                                                                DataTypeTree dataTypeTree_ =
                                                                    outputDataTypeTree;
                                                                NodeId argumentDataTypeId =
                                                                    ExpandedNodeId.parse("i=17")
                                                                        .toNodeId(namespaceTable)
                                                                        .orElseThrow(
                                                                            () ->
                                                                                new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_NodeIdInvalid,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " DataType"
                                                                                        + " namespace"
                                                                                        + " is unavailable"));
                                                                if (!OpcUaDataType.isBuiltin(
                                                                        argumentDataTypeId)
                                                                    && dataTypeTree_.getDataType(
                                                                            argumentDataTypeId)
                                                                        == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Method argument"
                                                                          + " CompletionStateMachine"
                                                                          + " (effective property"
                                                                          + " i=15753, DataType"
                                                                          + " i=17) is unavailable"
                                                                          + " in the effective type"
                                                                          + " tree; resolved"
                                                                          + " DataType: "
                                                                          + argumentDataTypeId);
                                                                }
                                                                methodValue =
                                                                    NumericValues.normalize(
                                                                        methodValue,
                                                                        dataTypeTree_,
                                                                        argumentDataTypeId);
                                                                if (methodValue != null) {
                                                                  Object shapeElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  int valueRank =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getValueRank()
                                                                          : ArrayUtil.getValueRank(
                                                                              methodValue);
                                                                  boolean emptyArray =
                                                                      methodValue
                                                                              .getClass()
                                                                              .isArray()
                                                                          && ArrayUtil.getValueRank(
                                                                                  methodValue)
                                                                              == 1
                                                                          && Array.getLength(
                                                                                  methodValue)
                                                                              == 0;
                                                                  if (!(valueRank == -1)) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        "Method argument ValueRank"
                                                                            + " mismatch");
                                                                  }
                                                                  if (methodValue
                                                                      instanceof Matrix) {
                                                                    int[] dimensions =
                                                                        ((Matrix) methodValue)
                                                                            .getDimensions();
                                                                    if (dimensions.length < 2
                                                                        || !shapeElements
                                                                            .getClass()
                                                                            .isArray()
                                                                        || ArrayUtil.getValueRank(
                                                                                shapeElements)
                                                                            != 1) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Malformed Method Matrix"
                                                                              + " representation");
                                                                    }
                                                                    long elementCount = 1;
                                                                    for (int dimension :
                                                                        dimensions) {
                                                                      if (dimension < 0
                                                                          || elementCount
                                                                              > Integer.MAX_VALUE) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Malformed Method"
                                                                                + " Matrix"
                                                                                + " dimensions");
                                                                      }
                                                                      elementCount *= dimension;
                                                                    }
                                                                    if (elementCount
                                                                        != Array.getLength(
                                                                            shapeElements)) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix dimensions"
                                                                              + " do not match its"
                                                                              + " elements");
                                                                    }
                                                                    if (!(((Matrix) methodValue)
                                                                        .getDataType()
                                                                        .equals(
                                                                            Variant.of(
                                                                                    shapeElements)
                                                                                .getDataType()))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix DataType"
                                                                              + " does not match"
                                                                              + " its elements");
                                                                    }
                                                                  }
                                                                  Variant.of(shapeElements);
                                                                }
                                                                if (methodValue != null) {
                                                                  Object typedElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  if (NodeIds.Structure.equals(
                                                                          argumentDataTypeId)
                                                                      || dataTypeTree_.isStructType(
                                                                          argumentDataTypeId)) {
                                                                    var declaredType =
                                                                        dataTypeTree_.getType(
                                                                            argumentDataTypeId);
                                                                    if (typedElements
                                                                        .getClass()
                                                                        .isArray()) {
                                                                      var structureCodec =
                                                                          this.client
                                                                              .getStaticEncodingContext()
                                                                              .getDataTypeManager()
                                                                              .getCodec(
                                                                                  argumentDataTypeId);
                                                                      Class<?> structureClass =
                                                                          structureCodec == null
                                                                              ? UaStructuredType
                                                                                  .class
                                                                              : structureCodec
                                                                                  .getType();
                                                                      Object decodedStructures =
                                                                          Array.newInstance(
                                                                              structureClass,
                                                                              Array.getLength(
                                                                                  typedElements));
                                                                      for (int structureIndex = 0;
                                                                          structureIndex
                                                                              < Array.getLength(
                                                                                  typedElements);
                                                                          structureIndex++) {
                                                                        Object structure =
                                                                            Array.get(
                                                                                typedElements,
                                                                                structureIndex);
                                                                        if (structure
                                                                            instanceof
                                                                            ExtensionObject) {
                                                                          structure =
                                                                              ((ExtensionObject)
                                                                                          structure)
                                                                                      .isNull()
                                                                                  ? null
                                                                                  : ((ExtensionObject)
                                                                                          structure)
                                                                                      .decode(
                                                                                          this
                                                                                              .client
                                                                                              .getStaticEncodingContext());
                                                                        }
                                                                        if (structure != null) {
                                                                          if (!(structure
                                                                              instanceof
                                                                              UaStructuredType)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method argument"
                                                                                    + " requires a"
                                                                                    + " Structure"
                                                                                    + " value");
                                                                          }
                                                                          if (NodeIds.Structure
                                                                                  .equals(
                                                                                      argumentDataTypeId)
                                                                              || declaredType
                                                                                      != null
                                                                                  && declaredType
                                                                                      .isAbstract()) {
                                                                            if (!dataTypeTree_
                                                                                .isSubtypeOf(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE),
                                                                                    argumentDataTypeId)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " is not a"
                                                                                      + " subtype"
                                                                                      + " of the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          } else {
                                                                            if (!argumentDataTypeId
                                                                                .equals(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE))) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " does not"
                                                                                      + " match the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          }
                                                                        }
                                                                        Array.set(
                                                                            decodedStructures,
                                                                            structureIndex,
                                                                            structure);
                                                                      }
                                                                      methodValue =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? new Matrix(
                                                                                  decodedStructures,
                                                                                  ((Matrix)
                                                                                          methodValue)
                                                                                      .getDimensions()
                                                                                      .clone())
                                                                              : decodedStructures;
                                                                    } else {
                                                                      if (typedElements
                                                                          instanceof
                                                                          ExtensionObject) {
                                                                        typedElements =
                                                                            ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .isNull()
                                                                                ? null
                                                                                : ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .decode(
                                                                                        this.client
                                                                                            .getStaticEncodingContext());
                                                                      }
                                                                      if (typedElements != null) {
                                                                        if (!(typedElements
                                                                            instanceof
                                                                            UaStructuredType)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " requires a"
                                                                                  + " Structure"
                                                                                  + " value");
                                                                        }
                                                                        if (NodeIds.Structure
                                                                                .equals(
                                                                                    argumentDataTypeId)
                                                                            || declaredType != null
                                                                                && declaredType
                                                                                    .isAbstract()) {
                                                                          if (!dataTypeTree_
                                                                              .isSubtypeOf(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE),
                                                                                  argumentDataTypeId)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " is not a"
                                                                                    + " subtype of"
                                                                                    + " the effective"
                                                                                    + " DataType");
                                                                          }
                                                                        } else {
                                                                          if (!argumentDataTypeId
                                                                              .equals(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE))) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " does not"
                                                                                    + " match the"
                                                                                    + " effective"
                                                                                    + " DataType");
                                                                          }
                                                                        }
                                                                      }
                                                                      methodValue = typedElements;
                                                                    }
                                                                  } else {
                                                                    Variant.of(typedElements);
                                                                    NodeId assignableDataTypeId =
                                                                        dataTypeTree_
                                                                                        .getBackingClass(
                                                                                            argumentDataTypeId)
                                                                                    == Number.class
                                                                                && dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        argumentDataTypeId,
                                                                                        NodeIds
                                                                                            .Integer)
                                                                            ? NodeIds.Integer
                                                                            : argumentDataTypeId;
                                                                    if (dataTypeTree_
                                                                                .getBackingClass(
                                                                                    argumentDataTypeId)
                                                                            != Variant.class
                                                                        && !dataTypeTree_
                                                                            .isAssignable(
                                                                                assignableDataTypeId,
                                                                                ArrayUtil
                                                                                    .getBoxedType(
                                                                                        typedElements))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument DataType"
                                                                              + " mismatch");
                                                                    }
                                                                  }
                                                                }
                                                                decoded0 = (NodeId) methodValue;
                                                              } catch (
                                                                  UaSerializationException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    conversionFailure
                                                                                .getStatusCode()
                                                                                .getValue()
                                                                            == StatusCodes
                                                                                .Bad_OutOfRange
                                                                        ? StatusCodes.Bad_OutOfRange
                                                                        : StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              } catch (ClassCastException
                                                                  | IllegalArgumentException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    StatusCodes.Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              }
                                                            }
                                                            return decoded0;
                                                          });
                                                    } catch (UaException failure) {
                                                      throw new CompletionException(failure);
                                                    }
                                                  });
                                            } catch (Exception failure) {
                                              return CompletableFuture.failedFuture(failure);
                                            }
                                          });
                                    });
                          } catch (Exception failure) {
                            return CompletableFuture.failedFuture(failure);
                          }
                        });
              });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }
}
