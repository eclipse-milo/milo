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
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class AddressSpaceFileTypeNode extends FileTypeNode implements AddressSpaceFileType {
  public AddressSpaceFileTypeNode(
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
  public static ClientViews createViews(AddressSpaceFileTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Override
  public @Nullable UaMethodNode getExportNamespaceMethodNode() throws UaException {
    try {
      return getExportNamespaceMethodNodeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode> getExportNamespaceMethodNodeAsync() {
    CompletableFuture<UaMethodNode> result = new CompletableFuture<>();
    try {
      CompletableFuture<NodeId> lookup = CompletableFuture.completedFuture(getNodeId());
      CompletableFuture<UaNode> hop0 =
          lookup.thenCompose(
              parent -> {
                NodeId parentId = parent;
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (parentId == null) {
                  return CompletableFuture.completedFuture(null);
                }
                CompletableFuture<Void> namespaceReady;
                if (client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/") == null
                    || client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/")
                        == null) {
                  namespaceReady = client.readNamespaceTableAsync().thenApply(ignored -> null);
                } else {
                  namespaceReady = CompletableFuture.completedFuture(null);
                }
                return namespaceReady.thenCompose(
                    ignored -> {
                      if (result.isCancelled()) {
                        return CompletableFuture.failedFuture(new CancellationException());
                      }
                      var namespaceIndex =
                          client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/");
                      var referenceId =
                          ExpandedNodeId.parse("i=47").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:ExportNamespace (declaration i=11615,"
                                    + " owner i=11595) on "
                                    + getNodeId()));
                      }
                      var browsePath =
                          new BrowsePath(
                              parentId,
                              new RelativePath(
                                  new RelativePathElement[] {
                                    new RelativePathElement(
                                        referenceId.orElseThrow(),
                                        false,
                                        true,
                                        new QualifiedName(namespaceIndex, "ExportNamespace"))
                                  }));
                      return client
                          .translateBrowsePathsAsync(List.of(browsePath))
                          .thenCompose(
                              response -> {
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                var results = response == null ? null : response.getResults();
                                if (results == null
                                    || results.length != 1
                                    || results[0] == null
                                    || results[0].getStatusCode() == null) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ExportNamespace"
                                              + " (declaration i=11615, owner i=11595) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.completedFuture(null);
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ExportNamespace"
                                              + " (declaration i=11615, owner i=11595)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ExportNamespace"
                                              + " (declaration i=11615, owner i=11595) on "
                                              + getNodeId()));
                                }
                                var identities = new ArrayList<CompletableFuture<NodeId>>();
                                for (var target : targets) {
                                  if (target == null
                                      || target.getTargetId() == null
                                      || target.getRemainingPathIndex() == null
                                      || target.getRemainingPathIndex().longValue()
                                          != 0xffffffffL) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_UnexpectedError,
                                            "http://opcfoundation.org/UA/:ExportNamespace"
                                                + " (declaration i=11615, owner i=11595) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ExportNamespace"
                                                + " (declaration i=11615, owner i=11595) on "
                                                + getNodeId()));
                                  }
                                  if (result.isCancelled()) {
                                    return CompletableFuture.failedFuture(
                                        new CancellationException());
                                  }
                                  var localTarget =
                                      target.getTargetId().toNodeId(client.getNamespaceTable());
                                  if (localTarget.isPresent()) {
                                    identities.add(
                                        CompletableFuture.completedFuture(
                                            localTarget.orElseThrow()));
                                  } else {
                                    identities.add(
                                        client
                                            .readNamespaceTableAsync()
                                            .thenCompose(
                                                namespaceTable -> {
                                                  var resolvedTarget =
                                                      target.getTargetId().toNodeId(namespaceTable);
                                                  if (resolvedTarget.isEmpty()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "http://opcfoundation.org/UA/:ExportNamespace"
                                                                + " (declaration i=11615, owner"
                                                                + " i=11595) on "
                                                                + getNodeId()));
                                                  }
                                                  return CompletableFuture.completedFuture(
                                                      resolvedTarget.orElseThrow());
                                                }));
                                  }
                                }
                                return CompletableFuture.allOf(
                                        identities.toArray(CompletableFuture[]::new))
                                    .thenCompose(
                                        ready -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          var unique = new LinkedHashSet<NodeId>();
                                          identities.forEach(
                                              identity -> unique.add(identity.join()));
                                          if (unique.stream().anyMatch(NodeId::isNull)) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_NodeIdInvalid,
                                                    "http://opcfoundation.org/UA/:ExportNamespace"
                                                        + " (declaration i=11615, owner i=11595) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ExportNamespace"
                                                        + " (declaration i=11615, owner i=11595) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Method) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:ExportNamespace"
                                                                  + " (declaration i=11615, owner"
                                                                  + " i=11595) on "
                                                                  + getNodeId()));
                                                    }
                                                    return CompletableFuture.completedFuture(node);
                                                  });
                                        });
                              });
                    });
              });
      hop0.whenComplete(
          (node, failure) -> {
            if (failure != null) {
              result.completeExceptionally(failure);
            } else if (node != null && !(node instanceof UaMethodNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ExportNamespace (declaration i=11615, owner"
                          + " i=11595)"));
            } else {
              result.complete((UaMethodNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * Invokes <code>ExportNamespace</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public void callExportNamespace() throws UaException {
    callExportNamespaceDetailed().requireGood();
  }

  /**
   * Invokes <code>ExportNamespace</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callExportNamespaceAsync() {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callExportNamespaceDetailedAsync();
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

  /**
   * Invokes <code>ExportNamespace</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callExportNamespaceDetailed()
      throws UaException {
    return callExportNamespaceDetailed(MethodCallOptions.NONE);
  }

  /**
   * Invokes <code>ExportNamespace</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callExportNamespaceDetailed(
      MethodCallOptions options) throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod = callExportNamespaceDetailedAsync(options);
    try {
      return awaitedMethod.get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      awaitedMethod.cancel(false);
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  /**
   * Invokes <code>ExportNamespace</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callExportNamespaceDetailedAsync() {
    return callExportNamespaceDetailedAsync(MethodCallOptions.NONE);
  }

  /**
   * Invokes <code>ExportNamespace</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callExportNamespaceDetailedAsync(MethodCallOptions options) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      var lookup = getExportNamespaceMethodNodeAsync();
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
                          "Method node is required for invocation: ExportNamespace"));
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
