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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedDataItemsTemplateOutputs;
import org.eclipse.milo.opcua.sdk.core.model.methods.DataSetFolderTypeAddPublishedEventsOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1</a>
 */
public interface DataSetFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getAddPublishedDataItemsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddPublishedDataItemsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Invokes <code>AddPublishedDataItems</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  DataSetFolderTypeAddPublishedDataItemsOutputs callAddPublishedDataItems(
      @Nullable String name,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Invokes <code>AddPublishedDataItems</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends DataSetFolderTypeAddPublishedDataItemsOutputs>
      callAddPublishedDataItemsAsync(
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Invokes <code>AddPublishedDataItems</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsOutputs>
      callAddPublishedDataItemsDetailed(
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Invokes <code>AddPublishedDataItems</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsOutputs>
      callAddPublishedDataItemsDetailed(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Invokes <code>AddPublishedDataItems</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsOutputs>>
      callAddPublishedDataItemsDetailedAsync(
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.2
   *
   * <p>Invokes <code>AddPublishedDataItems</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsOutputs>>
      callAddPublishedDataItemsDetailedAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getAddPublishedEventsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddPublishedEventsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Invokes <code>AddPublishedEvents</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  DataSetFolderTypeAddPublishedEventsOutputs callAddPublishedEvents(
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Invokes <code>AddPublishedEvents</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends DataSetFolderTypeAddPublishedEventsOutputs>
      callAddPublishedEventsAsync(
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Invokes <code>AddPublishedEvents</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends DataSetFolderTypeAddPublishedEventsOutputs>
      callAddPublishedEventsDetailed(
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Invokes <code>AddPublishedEvents</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends DataSetFolderTypeAddPublishedEventsOutputs>
      callAddPublishedEventsDetailed(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Invokes <code>AddPublishedEvents</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends DataSetFolderTypeAddPublishedEventsOutputs>>
      callAddPublishedEventsDetailedAsync(
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3
   *
   * <p>Invokes <code>AddPublishedEvents</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param fieldNameAliases ; the supplied payload may be null.
   * @param fieldFlags ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends DataSetFolderTypeAddPublishedEventsOutputs>>
      callAddPublishedEventsDetailedAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable NodeId eventNotifier,
          @Nullable String @Nullable [] fieldNameAliases,
          @Nullable DataSetFieldFlags @Nullable [] fieldFlags,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getAddPublishedDataItemsTemplateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode>
      getAddPublishedDataItemsTemplateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Invokes <code>AddPublishedDataItemsTemplate</code> on this node's ObjectId using the
   * effective Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  DataSetFolderTypeAddPublishedDataItemsTemplateOutputs callAddPublishedDataItemsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Invokes <code>AddPublishedDataItemsTemplate</code> on this node's ObjectId using the
   * effective Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends DataSetFolderTypeAddPublishedDataItemsTemplateOutputs>
      callAddPublishedDataItemsTemplateAsync(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Invokes <code>AddPublishedDataItemsTemplate</code> on this node's ObjectId using the
   * effective Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsTemplateOutputs>
      callAddPublishedDataItemsTemplateDetailed(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Invokes <code>AddPublishedDataItemsTemplate</code> on this node's ObjectId using the
   * effective Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsTemplateOutputs>
      callAddPublishedDataItemsTemplateDetailed(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Invokes <code>AddPublishedDataItemsTemplate</code> on this node's ObjectId using the
   * effective Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends
              MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsTemplateOutputs>>
      callAddPublishedDataItemsTemplateDetailedAsync(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4
   *
   * <p>Invokes <code>AddPublishedDataItemsTemplate</code> on this node's ObjectId using the
   * effective Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param variablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends
              MethodCallResult<? extends DataSetFolderTypeAddPublishedDataItemsTemplateOutputs>>
      callAddPublishedDataItemsTemplateDetailedAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable PublishedVariableDataType @Nullable [] variablesToAdd);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getAddPublishedEventsTemplateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode>
      getAddPublishedEventsTemplateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Invokes <code>AddPublishedEventsTemplate</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddPublishedEventsTemplate(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Invokes <code>AddPublishedEventsTemplate</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddPublishedEventsTemplateAsync(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Invokes <code>AddPublishedEventsTemplate</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddPublishedEventsTemplateDetailed(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Invokes <code>AddPublishedEventsTemplate</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddPublishedEventsTemplateDetailed(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable NodeId eventNotifier,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Invokes <code>AddPublishedEventsTemplate</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddPublishedEventsTemplateDetailedAsync(
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable NodeId eventNotifier,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.5
   *
   * <p>Invokes <code>AddPublishedEventsTemplate</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param name ; the supplied payload may be null.
   * @param dataSetMetaData ; the supplied payload may be null.
   * @param eventNotifier ; the supplied payload may be null.
   * @param selectedFields ; the supplied payload may be null.
   * @param filter ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddPublishedEventsTemplateDetailedAsync(
          MethodCallOptions options,
          @Nullable String name,
          @Nullable DataSetMetaDataType dataSetMetaData,
          @Nullable NodeId eventNotifier,
          @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
          @Nullable ContentFilter filter);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getRemovePublishedDataSetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemovePublishedDataSetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Invokes <code>RemovePublishedDataSet</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param dataSetNodeId ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemovePublishedDataSet(@Nullable NodeId dataSetNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Invokes <code>RemovePublishedDataSet</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param dataSetNodeId ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemovePublishedDataSetAsync(
      @Nullable NodeId dataSetNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Invokes <code>RemovePublishedDataSet</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param dataSetNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemovePublishedDataSetDetailed(
      @Nullable NodeId dataSetNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Invokes <code>RemovePublishedDataSet</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param dataSetNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemovePublishedDataSetDetailed(
      MethodCallOptions options, @Nullable NodeId dataSetNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Invokes <code>RemovePublishedDataSet</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param dataSetNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePublishedDataSetDetailedAsync(@Nullable NodeId dataSetNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.6
   *
   * <p>Invokes <code>RemovePublishedDataSet</code> on this node's ObjectId using the effective
   * Method contract.
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
   * @param dataSetNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemovePublishedDataSetDetailedAsync(
          MethodCallOptions options, @Nullable NodeId dataSetNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getAddDataSetFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddDataSetFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Invokes <code>AddDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddDataSetFolder(@Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Invokes <code>AddDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddDataSetFolderAsync(@Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Invokes <code>AddDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddDataSetFolderDetailed(@Nullable String name)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Invokes <code>AddDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddDataSetFolderDetailed(
      MethodCallOptions options, @Nullable String name) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Invokes <code>AddDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddDataSetFolderDetailedAsync(@Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.7
   *
   * <p>Invokes <code>AddDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param name ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddDataSetFolderDetailedAsync(MethodCallOptions options, @Nullable String name);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveDataSetFolderMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveDataSetFolderMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Invokes <code>RemoveDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param dataSetFolderNodeId ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveDataSetFolder(@Nullable NodeId dataSetFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Invokes <code>RemoveDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param dataSetFolderNodeId ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveDataSetFolderAsync(
      @Nullable NodeId dataSetFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Invokes <code>RemoveDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param dataSetFolderNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveDataSetFolderDetailed(
      @Nullable NodeId dataSetFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Invokes <code>RemoveDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param dataSetFolderNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveDataSetFolderDetailed(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Invokes <code>RemoveDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param dataSetFolderNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveDataSetFolderDetailedAsync(@Nullable NodeId dataSetFolderNodeId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.8
   *
   * <p>Invokes <code>RemoveDataSetFolder</code> on this node's ObjectId using the effective Method
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
   * @param dataSetFolderNodeId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveDataSetFolderDetailedAsync(
          MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId);
}
