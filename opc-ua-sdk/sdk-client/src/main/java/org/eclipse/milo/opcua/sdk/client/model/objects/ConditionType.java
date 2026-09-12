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
import org.eclipse.milo.opcua.sdk.client.model.variables.ConditionVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ConditionType extends BaseEventType {
  QualifiedProperty<NodeId> CONDITION_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<LocalizedText> CONDITION_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<NodeId[]> CONDITION_SUB_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionSubClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<LocalizedText[]> CONDITION_SUB_CLASS_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionSubClassName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<String> CONDITION_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConditionName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<NodeId> BRANCH_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BranchId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> RETAIN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Retain",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORTS_FILTERED_RETAIN =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportsFilteredRetain",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String> CLIENT_USER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientUserId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getConditionClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionClassId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readConditionClassId() throws UaException;

  /** Writes the value remotely. */
  void writeConditionClassId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readConditionClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionClassIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConditionClassName() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionClassName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readConditionClassName() throws UaException;

  /** Writes the value remotely. */
  void writeConditionClassName(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readConditionClassNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionClassNameAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionClassNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConditionClassNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getConditionSubClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionSubClassId(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId @Nullable [] readConditionSubClassId() throws UaException;

  /** Writes the value remotely. */
  void writeConditionSubClassId(@Nullable NodeId @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId @Nullable []> readConditionSubClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionSubClassIdAsync(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getConditionSubClassName() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionSubClassName(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText @Nullable [] readConditionSubClassName() throws UaException;

  /** Writes the value remotely. */
  void writeConditionSubClassName(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText @Nullable []>
      readConditionSubClassNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionSubClassNameAsync(
      @Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConditionSubClassNameNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getConditionName() throws UaException;

  /** Sets the existing node's local value. */
  void setConditionName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readConditionName() throws UaException;

  /** Writes the value remotely. */
  void writeConditionName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readConditionNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConditionNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConditionNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConditionNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getBranchId() throws UaException;

  /** Sets the existing node's local value. */
  void setBranchId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readBranchId() throws UaException;

  /** Writes the value remotely. */
  void writeBranchId(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readBranchIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBranchIdAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getBranchIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getBranchIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getRetain() throws UaException;

  /** Sets the existing node's local value. */
  void setRetain(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readRetain() throws UaException;

  /** Writes the value remotely. */
  void writeRetain(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readRetainAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRetainAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRetainNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRetainNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportsFilteredRetain() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportsFilteredRetain(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readSupportsFilteredRetain() throws UaException;

  /** Writes the value remotely. */
  void writeSupportsFilteredRetain(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSupportsFilteredRetainAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportsFilteredRetainAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportsFilteredRetainNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSupportsFilteredRetainNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getClientUserId() throws UaException;

  /** Sets the existing node's local value. */
  void setClientUserId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readClientUserId() throws UaException;

  /** Writes the value remotely. */
  void writeClientUserId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readClientUserIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientUserIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientUserIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getClientUserIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getEnabledState() throws UaException;

  /** Sets the existing node's local value. */
  void setEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readEnabledState() throws UaException;

  /** Writes the value remotely. */
  void writeEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readEnabledStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnabledStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable StatusCode getQuality() throws UaException;

  /** Sets the existing node's local value. */
  void setQuality(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusCode readQuality() throws UaException;

  /** Writes the value remotely. */
  void writeQuality(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusCode> readQualityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeQualityAsync(@Nullable StatusCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ConditionVariableType getQualityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ConditionVariableType> getQualityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getLastSeverity() throws UaException;

  /** Sets the existing node's local value. */
  void setLastSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readLastSeverity() throws UaException;

  /** Writes the value remotely. */
  void writeLastSeverity(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readLastSeverityAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastSeverityAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ConditionVariableType getLastSeverityNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ConditionVariableType> getLastSeverityNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getComment() throws UaException;

  /** Sets the existing node's local value. */
  void setComment(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readComment() throws UaException;

  /** Writes the value remotely. */
  void writeComment(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readCommentAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCommentAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ConditionVariableType getCommentNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ConditionVariableType> getCommentNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getDisableMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getDisableMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Invokes <code>Disable</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callDisable() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Invokes <code>Disable</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callDisableAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Invokes <code>Disable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDisableDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Invokes <code>Disable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callDisableDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Invokes <code>Disable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDisableDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4
   *
   * <p>Invokes <code>Disable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callDisableDetailedAsync(
      MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getEnableMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getEnableMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Invokes <code>Enable</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callEnable() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Invokes <code>Enable</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callEnableAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Invokes <code>Enable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callEnableDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Invokes <code>Enable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callEnableDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Invokes <code>Enable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callEnableDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5
   *
   * <p>Invokes <code>Enable</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callEnableDetailedAsync(
      MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getAddCommentMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getAddCommentMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Invokes <code>AddComment</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callAddComment(@Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Invokes <code>AddComment</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callAddCommentAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Invokes <code>AddComment</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddCommentDetailed(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Invokes <code>AddComment</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callAddCommentDetailed(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Invokes <code>AddComment</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddCommentDetailedAsync(@Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6
   *
   * <p>Invokes <code>AddComment</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param eventId The identifier for the event to comment.
   * @param comment The comment to add to the condition.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddCommentDetailedAsync(
          MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getConditionRefreshMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getConditionRefreshMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Invokes <code>ConditionRefresh</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callConditionRefresh(@Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Invokes <code>ConditionRefresh</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callConditionRefreshAsync(
      @Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Invokes <code>ConditionRefresh</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConditionRefreshDetailed(
      @Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Invokes <code>ConditionRefresh</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId The identifier for the subscription to refresh.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConditionRefreshDetailed(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Invokes <code>ConditionRefresh</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callConditionRefreshDetailedAsync(@Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7
   *
   * <p>Invokes <code>ConditionRefresh</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId The identifier for the subscription to refresh.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callConditionRefreshDetailedAsync(
          MethodCallOptions options, @Nullable UInteger subscriptionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getConditionRefresh2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getConditionRefresh2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Invokes <code>ConditionRefresh2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @param monitoredItemId The identifier for the monitored item to refresh.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callConditionRefresh2(@Nullable UInteger subscriptionId, @Nullable UInteger monitoredItemId)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Invokes <code>ConditionRefresh2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @param monitoredItemId The identifier for the monitored item to refresh.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callConditionRefresh2Async(
      @Nullable UInteger subscriptionId, @Nullable UInteger monitoredItemId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Invokes <code>ConditionRefresh2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @param monitoredItemId The identifier for the monitored item to refresh.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConditionRefresh2Detailed(
      @Nullable UInteger subscriptionId, @Nullable UInteger monitoredItemId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Invokes <code>ConditionRefresh2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId The identifier for the subscription to refresh.
   * @param monitoredItemId The identifier for the monitored item to refresh.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConditionRefresh2Detailed(
      MethodCallOptions options,
      @Nullable UInteger subscriptionId,
      @Nullable UInteger monitoredItemId)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Invokes <code>ConditionRefresh2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param subscriptionId The identifier for the subscription to refresh.
   * @param monitoredItemId The identifier for the monitored item to refresh.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callConditionRefresh2DetailedAsync(
          @Nullable UInteger subscriptionId, @Nullable UInteger monitoredItemId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8
   *
   * <p>Invokes <code>ConditionRefresh2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId The identifier for the subscription to refresh.
   * @param monitoredItemId The identifier for the monitored item to refresh.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callConditionRefresh2DetailedAsync(
          MethodCallOptions options,
          @Nullable UInteger subscriptionId,
          @Nullable UInteger monitoredItemId);
}
