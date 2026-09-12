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
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeCreateFileOutputs;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
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

public class FileDirectoryTypeNode extends FolderTypeNode implements FileDirectoryType {
  public FileDirectoryTypeNode(
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
  public static ClientViews createViews(FileDirectoryTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @NullMarked
  @Override
  public UaMethodNode getCreateDirectoryMethodNode() throws UaException {
    return ClientMembers.await(getCreateDirectoryMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getCreateDirectoryMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateDirectory",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:CreateDirectory (declaration i=13387, owner i=13353)"));
  }

  @NullMarked
  @Override
  public @Nullable NodeId callCreateDirectory(@Nullable String directoryName) throws UaException {
    return callCreateDirectoryDetailed(directoryName).requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable NodeId> callCreateDirectoryAsync(
      @Nullable String directoryName) {
    CompletableFuture<@Nullable NodeId> result = new CompletableFuture<>();
    var call = callCreateDirectoryDetailedAsync(directoryName);
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
  public MethodCallResult<? extends @Nullable NodeId> callCreateDirectoryDetailed(
      @Nullable String directoryName) throws UaException {
    return callCreateDirectoryDetailed(MethodCallOptions.NONE, directoryName);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable NodeId> callCreateDirectoryDetailed(
      MethodCallOptions options, @Nullable String directoryName) throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(callCreateDirectoryDetailedAsync(options, directoryName), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDirectoryDetailedAsync(@Nullable String directoryName) {
    return callCreateDirectoryDetailedAsync(MethodCallOptions.NONE, directoryName);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDirectoryDetailedAsync(MethodCallOptions options, @Nullable String directoryName) {
    CompletableFuture<MethodCallResult<@Nullable NodeId>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(directoryName);
      var lookup = getCreateDirectoryMethodNodeAsync();
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
                          "Method node is required for invocation: CreateDirectory"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=12"))
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
                                @Nullable String convertedValue;
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
                                        ExpandedNodeId.parse("i=12")
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
                                          "Method argument DirectoryName (effective property"
                                              + " i=13388, DataType i=12) is unavailable in the"
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
                                    convertedValue = (String) methodValue;
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
                                                                          + " DirectoryNodeId"
                                                                          + " (effective property"
                                                                          + " i=13389, DataType"
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

  @NullMarked
  @Override
  public UaMethodNode getCreateFileMethodNode() throws UaException {
    return ClientMembers.await(getCreateFileMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getCreateFileMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CreateFile",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:CreateFile (declaration i=13390, owner i=13353)"));
  }

  @NullMarked
  @Override
  public FileDirectoryTypeCreateFileOutputs callCreateFile(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException {
    return callCreateFileDetailed(fileName, requestFileOpen).requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends FileDirectoryTypeCreateFileOutputs> callCreateFileAsync(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) {
    CompletableFuture<FileDirectoryTypeCreateFileOutputs> result = new CompletableFuture<>();
    var call = callCreateFileDetailedAsync(fileName, requestFileOpen);
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
  public MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs> callCreateFileDetailed(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException {
    return callCreateFileDetailed(MethodCallOptions.NONE, fileName, requestFileOpen);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs> callCreateFileDetailed(
      MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen)
      throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(
        callCreateFileDetailedAsync(options, fileName, requestFileOpen), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs>>
      callCreateFileDetailedAsync(@Nullable String fileName, @Nullable Boolean requestFileOpen) {
    return callCreateFileDetailedAsync(MethodCallOptions.NONE, fileName, requestFileOpen);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends FileDirectoryTypeCreateFileOutputs>>
      callCreateFileDetailedAsync(
          MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen) {
    CompletableFuture<MethodCallResult<FileDirectoryTypeCreateFileOutputs>> result =
        new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(fileName);
      rawInputs.add(requestFileOpen);
      var lookup = getCreateFileMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<FileDirectoryTypeCreateFileOutputs>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: CreateFile"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=12"), ExpandedNodeId.parse("i=1"))
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
                                @Nullable String convertedValue;
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
                                        ExpandedNodeId.parse("i=12")
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
                                          "Method argument FileName (effective property i=13391,"
                                              + " DataType i=12) is unavailable in the effective"
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
                                    convertedValue = (String) methodValue;
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
                            if (rawInputs.size() > 1) {
                              Variant encoded1;
                              {
                                @Nullable Boolean convertedValue;
                                {
                                  Object methodValue = rawInputs.get(1);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=1")
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
                                          "Method argument RequestFileOpen (effective property"
                                              + " i=13391, DataType i=1) is unavailable in the"
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
                                    convertedValue = (Boolean) methodValue;
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
                                  encoded1 = Variant.of(wireValue);
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
                              inputArguments.add(encoded1);
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
                                                                      "Method argument FileNodeId"
                                                                          + " (effective property"
                                                                          + " i=13392, DataType"
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
                                                                    ExpandedNodeId.parse("i=7")
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
                                                                      "Method argument FileHandle"
                                                                          + " (effective property"
                                                                          + " i=13392, DataType"
                                                                          + " i=7) is unavailable"
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
                                                                decoded1 = (UInteger) methodValue;
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
                                                            return FileDirectoryTypeCreateFileOutputs
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
  public UaMethodNode getDeleteFileSystemObjectMethodNode() throws UaException {
    return ClientMembers.await(getDeleteFileSystemObjectMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getDeleteFileSystemObjectMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Delete",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:Delete (declaration i=13393, owner i=13353)"));
  }

  @NullMarked
  @Override
  public void callDeleteFileSystemObject(@Nullable NodeId objectToDelete) throws UaException {
    callDeleteFileSystemObjectDetailed(objectToDelete).requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callDeleteFileSystemObjectAsync(
      @Nullable NodeId objectToDelete) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callDeleteFileSystemObjectDetailedAsync(objectToDelete);
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
  public MethodCallResult<? extends @Nullable Void> callDeleteFileSystemObjectDetailed(
      @Nullable NodeId objectToDelete) throws UaException {
    return callDeleteFileSystemObjectDetailed(MethodCallOptions.NONE, objectToDelete);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callDeleteFileSystemObjectDetailed(
      MethodCallOptions options, @Nullable NodeId objectToDelete) throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(
        callDeleteFileSystemObjectDetailedAsync(options, objectToDelete), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteFileSystemObjectDetailedAsync(@Nullable NodeId objectToDelete) {
    return callDeleteFileSystemObjectDetailedAsync(MethodCallOptions.NONE, objectToDelete);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeleteFileSystemObjectDetailedAsync(
          MethodCallOptions options, @Nullable NodeId objectToDelete) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(objectToDelete);
      var lookup = getDeleteFileSystemObjectMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable Void>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: Delete"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=17"))
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
                                @Nullable NodeId convertedValue;
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
                                        ExpandedNodeId.parse("i=17")
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
                                          "Method argument ObjectToDelete (effective property"
                                              + " i=13394, DataType i=17) is unavailable in the"
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
                                    convertedValue = (NodeId) methodValue;
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
                                                          List.<ExpandedNodeId>of(),
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
                                                                != 0) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            return null;
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
  public UaMethodNode getMoveOrCopyMethodNode() throws UaException {
    return ClientMembers.await(getMoveOrCopyMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getMoveOrCopyMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MoveOrCopy",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:MoveOrCopy (declaration i=13395, owner i=13353)"));
  }

  @NullMarked
  @Override
  public @Nullable NodeId callMoveOrCopy(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException {
    return callMoveOrCopyDetailed(objectToMoveOrCopy, targetDirectory, createCopy, newName)
        .requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable NodeId> callMoveOrCopyAsync(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName) {
    CompletableFuture<@Nullable NodeId> result = new CompletableFuture<>();
    var call =
        callMoveOrCopyDetailedAsync(objectToMoveOrCopy, targetDirectory, createCopy, newName);
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
  public MethodCallResult<? extends @Nullable NodeId> callMoveOrCopyDetailed(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException {
    return callMoveOrCopyDetailed(
        MethodCallOptions.NONE, objectToMoveOrCopy, targetDirectory, createCopy, newName);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable NodeId> callMoveOrCopyDetailed(
      MethodCallOptions options,
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(
        callMoveOrCopyDetailedAsync(
            options, objectToMoveOrCopy, targetDirectory, createCopy, newName),
        true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callMoveOrCopyDetailedAsync(
          @Nullable NodeId objectToMoveOrCopy,
          @Nullable NodeId targetDirectory,
          @Nullable Boolean createCopy,
          @Nullable String newName) {
    return callMoveOrCopyDetailedAsync(
        MethodCallOptions.NONE, objectToMoveOrCopy, targetDirectory, createCopy, newName);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callMoveOrCopyDetailedAsync(
          MethodCallOptions options,
          @Nullable NodeId objectToMoveOrCopy,
          @Nullable NodeId targetDirectory,
          @Nullable Boolean createCopy,
          @Nullable String newName) {
    CompletableFuture<MethodCallResult<@Nullable NodeId>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(objectToMoveOrCopy);
      rawInputs.add(targetDirectory);
      rawInputs.add(createCopy);
      rawInputs.add(newName);
      var lookup = getMoveOrCopyMethodNodeAsync();
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
                          "Method node is required for invocation: MoveOrCopy"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=17"),
                                ExpandedNodeId.parse("i=17"),
                                ExpandedNodeId.parse("i=1"),
                                ExpandedNodeId.parse("i=12"))
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
                                @Nullable NodeId convertedValue;
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
                                        ExpandedNodeId.parse("i=17")
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
                                          "Method argument ObjectToMoveOrCopy (effective property"
                                              + " i=13396, DataType i=17) is unavailable in the"
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
                                    convertedValue = (NodeId) methodValue;
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
                            if (rawInputs.size() > 1) {
                              Variant encoded1;
                              {
                                @Nullable NodeId convertedValue;
                                {
                                  Object methodValue = rawInputs.get(1);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=17")
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
                                          "Method argument TargetDirectory (effective property"
                                              + " i=13396, DataType i=17) is unavailable in the"
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
                                    convertedValue = (NodeId) methodValue;
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
                                  encoded1 = Variant.of(wireValue);
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
                              inputArguments.add(encoded1);
                            }
                            if (rawInputs.size() > 2) {
                              Variant encoded2;
                              {
                                @Nullable Boolean convertedValue;
                                {
                                  Object methodValue = rawInputs.get(2);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=1")
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
                                          "Method argument CreateCopy (effective property i=13396,"
                                              + " DataType i=1) is unavailable in the effective"
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
                                    convertedValue = (Boolean) methodValue;
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
                                  encoded2 = Variant.of(wireValue);
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
                              inputArguments.add(encoded2);
                            }
                            if (rawInputs.size() > 3) {
                              Variant encoded3;
                              {
                                @Nullable String convertedValue;
                                {
                                  Object methodValue = rawInputs.get(3);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=12")
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
                                          "Method argument NewName (effective property i=13396,"
                                              + " DataType i=12) is unavailable in the effective"
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
                                    convertedValue = (String) methodValue;
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
                                  encoded3 = Variant.of(wireValue);
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
                              inputArguments.add(encoded3);
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
                                                                      "Method argument NewNodeId"
                                                                          + " (effective property"
                                                                          + " i=13397, DataType"
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
