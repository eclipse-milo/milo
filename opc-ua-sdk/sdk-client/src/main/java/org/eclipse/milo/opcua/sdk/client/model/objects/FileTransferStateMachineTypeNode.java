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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class FileTransferStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements FileTransferStateMachineType {
  public FileTransferStateMachineTypeNode(
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
  public static ClientViews createViews(FileTransferStateMachineTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public InitialStateTypeNode getIdleNode() throws UaException {
    return ClientMembers.await(getIdleNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends InitialStateTypeNode> getIdleNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        InitialStateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Idle",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Idle (declaration i=15815, owner i=15803)"));
  }

  @Override
  public StateTypeNode getReadPrepareNode() throws UaException {
    return ClientMembers.await(getReadPrepareNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getReadPrepareNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadPrepare",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadPrepare (declaration i=15817, owner i=15803)"));
  }

  @Override
  public StateTypeNode getReadTransferNode() throws UaException {
    return ClientMembers.await(getReadTransferNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getReadTransferNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadTransfer",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadTransfer (declaration i=15819, owner i=15803)"));
  }

  @Override
  public StateTypeNode getApplyWriteNode() throws UaException {
    return ClientMembers.await(getApplyWriteNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getApplyWriteNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ApplyWrite",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ApplyWrite (declaration i=15821, owner i=15803)"));
  }

  @Override
  public StateTypeNode getErrorNode() throws UaException {
    return ClientMembers.await(getErrorNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getErrorNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Error",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Error (declaration i=15823, owner i=15803)"));
  }

  @Override
  public TransitionTypeNode getIdleToReadPrepareNode() throws UaException {
    return ClientMembers.await(getIdleToReadPrepareNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getIdleToReadPrepareNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "IdleToReadPrepare",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:IdleToReadPrepare (declaration i=15825, owner i=15803)"));
  }

  @Override
  public TransitionTypeNode getReadPrepareToReadTransferNode() throws UaException {
    return ClientMembers.await(getReadPrepareToReadTransferNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadPrepareToReadTransferNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadPrepareToReadTransfer",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadPrepareToReadTransfer (declaration i=15827, owner"
                + " i=15803)"));
  }

  @Override
  public TransitionTypeNode getReadTransferToIdleNode() throws UaException {
    return ClientMembers.await(getReadTransferToIdleNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadTransferToIdleNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadTransferToIdle",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadTransferToIdle (declaration i=15829, owner"
                + " i=15803)"));
  }

  @Override
  public TransitionTypeNode getIdleToApplyWriteNode() throws UaException {
    return ClientMembers.await(getIdleToApplyWriteNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getIdleToApplyWriteNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "IdleToApplyWrite",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:IdleToApplyWrite (declaration i=15831, owner i=15803)"));
  }

  @Override
  public TransitionTypeNode getApplyWriteToIdleNode() throws UaException {
    return ClientMembers.await(getApplyWriteToIdleNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getApplyWriteToIdleNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ApplyWriteToIdle",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ApplyWriteToIdle (declaration i=15833, owner i=15803)"));
  }

  @Override
  public TransitionTypeNode getReadPrepareToErrorNode() throws UaException {
    return ClientMembers.await(getReadPrepareToErrorNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadPrepareToErrorNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadPrepareToError",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadPrepareToError (declaration i=15835, owner"
                + " i=15803)"));
  }

  @Override
  public TransitionTypeNode getReadTransferToErrorNode() throws UaException {
    return ClientMembers.await(getReadTransferToErrorNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadTransferToErrorNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadTransferToError",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadTransferToError (declaration i=15837, owner"
                + " i=15803)"));
  }

  @Override
  public TransitionTypeNode getApplyWriteToErrorNode() throws UaException {
    return ClientMembers.await(getApplyWriteToErrorNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getApplyWriteToErrorNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ApplyWriteToError",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ApplyWriteToError (declaration i=15839, owner i=15803)"));
  }

  @Override
  public TransitionTypeNode getErrorToIdleNode() throws UaException {
    return ClientMembers.await(getErrorToIdleNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getErrorToIdleNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ErrorToIdle",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ErrorToIdle (declaration i=15841, owner i=15803)"));
  }

  @NullMarked
  @Override
  public UaMethodNode getResetMethodNode() throws UaException {
    return ClientMembers.await(getResetMethodNodeAsync(), false);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getResetMethodNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        UaMethodNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Reset",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Method,
            false,
            "http://opcfoundation.org/UA/:Reset (declaration i=15843, owner i=15803)"));
  }

  @NullMarked
  @Override
  public void callReset() throws UaException {
    callResetDetailed().requireGood();
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callResetAsync() {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callResetDetailedAsync();
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
  public MethodCallResult<? extends @Nullable Void> callResetDetailed() throws UaException {
    return callResetDetailed(MethodCallOptions.NONE);
  }

  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callResetDetailed(MethodCallOptions options)
      throws UaException {
    Objects.requireNonNull(options, "options");
    return ClientMembers.await(callResetDetailedAsync(options), true);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResetDetailedAsync() {
    return callResetDetailedAsync(MethodCallOptions.NONE);
  }

  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResetDetailedAsync(MethodCallOptions options) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      var lookup = getResetMethodNodeAsync();
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
                          "Method node is required for invocation: Reset"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of().subList(0, rawInputs.size()),
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
}
