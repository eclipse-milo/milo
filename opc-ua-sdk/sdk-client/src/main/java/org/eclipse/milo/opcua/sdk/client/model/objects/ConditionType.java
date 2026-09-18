package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.2">Model
 *     documentation</a>
 */
public interface ConditionType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2782L);

  QualifiedProperty<String> ClientUserId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientUserId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> ConditionName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConditionName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Boolean> Retain_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Retain", ExpandedNodeId.of(Namespaces.OPC_UA, 1L), -1, Boolean.class);

  QualifiedProperty<NodeId> BranchId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BranchId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory ClientUserId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClientUserIdNode() throws UaException;

  /** Asynchronous form of {@link #getClientUserIdNode()}. */
  CompletableFuture<? extends PropertyType> getClientUserIdNodeAsync();

  /**
   * Reads the Value of the ClientUserId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readClientUserId() throws UaException;

  /**
   * Writes the Value of the ClientUserId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientUserId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readClientUserId()}. */
  CompletableFuture<? extends @Nullable String> readClientUserIdAsync();

  /** Asynchronous form of {@link #writeClientUserId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeClientUserIdAsync(@Nullable String value);

  /**
   * Resolves the mandatory EnabledState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /** Asynchronous form of {@link #getEnabledStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /**
   * Reads the Value of the EnabledState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readEnabledState() throws UaException;

  /**
   * Writes the Value of the EnabledState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readEnabledState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readEnabledStateAsync();

  /** Asynchronous form of {@link #writeEnabledState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEnabledStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory LastSeverity child, a ConditionVariableType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">ConditionVariableType
   *     documentation</a>
   */
  ConditionVariableType getLastSeverityNode() throws UaException;

  /** Asynchronous form of {@link #getLastSeverityNode()}. */
  CompletableFuture<? extends ConditionVariableType> getLastSeverityNodeAsync();

  /**
   * Reads the Value of the LastSeverity child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readLastSeverity() throws UaException;

  /**
   * Writes the Value of the LastSeverity child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastSeverity(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readLastSeverity()}. */
  CompletableFuture<? extends @Nullable UShort> readLastSeverityAsync();

  /** Asynchronous form of {@link #writeLastSeverity}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastSeverityAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory ConditionName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConditionNameNode() throws UaException;

  /** Asynchronous form of {@link #getConditionNameNode()}. */
  CompletableFuture<? extends PropertyType> getConditionNameNodeAsync();

  /**
   * Reads the Value of the ConditionName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readConditionName() throws UaException;

  /**
   * Writes the Value of the ConditionName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConditionName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readConditionName()}. */
  CompletableFuture<? extends @Nullable String> readConditionNameAsync();

  /** Asynchronous form of {@link #writeConditionName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConditionNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory ConditionClassId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConditionClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getConditionClassIdNode()}. */
  CompletableFuture<? extends PropertyType> getConditionClassIdNodeAsync();

  /**
   * Resolves the mandatory ConditionClassName child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConditionClassNameNode() throws UaException;

  /** Asynchronous form of {@link #getConditionClassNameNode()}. */
  CompletableFuture<? extends PropertyType> getConditionClassNameNodeAsync();

  /**
   * Resolves the optional ConditionSubClassId child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConditionSubClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getConditionSubClassIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassIdNodeAsync();

  /**
   * Resolves the optional ConditionSubClassName child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConditionSubClassNameNode() throws UaException;

  /** Asynchronous form of {@link #getConditionSubClassNameNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConditionSubClassNameNodeAsync();

  /**
   * Resolves the mandatory Retain child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRetainNode() throws UaException;

  /** Asynchronous form of {@link #getRetainNode()}. */
  CompletableFuture<? extends PropertyType> getRetainNodeAsync();

  /**
   * Reads the Value of the Retain child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readRetain() throws UaException;

  /**
   * Writes the Value of the Retain child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRetain(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readRetain()}. */
  CompletableFuture<? extends @Nullable Boolean> readRetainAsync();

  /** Asynchronous form of {@link #writeRetain}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRetainAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory Comment child, a ConditionVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">ConditionVariableType
   *     documentation</a>
   */
  ConditionVariableType getCommentNode() throws UaException;

  /** Asynchronous form of {@link #getCommentNode()}. */
  CompletableFuture<? extends ConditionVariableType> getCommentNodeAsync();

  /**
   * Reads the Value of the Comment child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readComment() throws UaException;

  /**
   * Writes the Value of the Comment child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeComment(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readComment()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readCommentAsync();

  /** Asynchronous form of {@link #writeComment}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCommentAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory Quality child, a ConditionVariableType with DataType StatusCode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.3">ConditionVariableType
   *     documentation</a>
   */
  ConditionVariableType getQualityNode() throws UaException;

  /** Asynchronous form of {@link #getQualityNode()}. */
  CompletableFuture<? extends ConditionVariableType> getQualityNodeAsync();

  /**
   * Reads the Value of the Quality child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readQuality() throws UaException;

  /**
   * Writes the Value of the Quality child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeQuality(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readQuality()}. */
  CompletableFuture<? extends @Nullable StatusCode> readQualityAsync();

  /** Asynchronous form of {@link #writeQuality}; completes with the operation status. */
  CompletableFuture<StatusCode> writeQualityAsync(@Nullable StatusCode value);

  /**
   * Resolves the mandatory BranchId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getBranchIdNode() throws UaException;

  /** Asynchronous form of {@link #getBranchIdNode()}. */
  CompletableFuture<? extends PropertyType> getBranchIdNodeAsync();

  /**
   * Reads the Value of the BranchId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readBranchId() throws UaException;

  /**
   * Writes the Value of the BranchId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBranchId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readBranchId()}. */
  CompletableFuture<? extends @Nullable NodeId> readBranchIdAsync();

  /** Asynchronous form of {@link #writeBranchId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBranchIdAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory AddComment Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6">Model
   *     documentation</a>
   */
  UaMethodNode getAddCommentMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddCommentMethodNode()}. */
  CompletableFuture<UaMethodNode> getAddCommentMethodNodeAsync();

  /**
   * Calls the AddComment Method and returns its outputs; requires a Good result.
   *
   * @param eventId the identifier for the event to comment.
   * @param comment the comment to add to the condition.
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.6">Model
   *     documentation</a>
   */
  void addComment(@Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the AddComment Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddComment(
      @Nullable ByteString eventId, @Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the AddComment Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddCommentWith(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment)
      throws UaException;

  /** Asynchronous form of {@link #addComment}. */
  CompletableFuture<Void> addCommentAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callAddComment}. */
  CompletableFuture<MethodCallResult<Void>> callAddCommentAsync(
      @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callAddCommentWith}. */
  CompletableFuture<MethodCallResult<Void>> callAddCommentWithAsync(
      MethodCallOptions options, @Nullable ByteString eventId, @Nullable LocalizedText comment);

  /**
   * Resolves the optional ConditionRefresh Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConditionRefreshMethodNode() throws UaException;

  /** Asynchronous form of {@link #getConditionRefreshMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getConditionRefreshMethodNodeAsync();

  /**
   * Resolves the optional ConditionRefresh2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getConditionRefresh2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getConditionRefresh2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getConditionRefresh2MethodNodeAsync();

  /**
   * Resolves the mandatory Disable Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4">Model
   *     documentation</a>
   */
  UaMethodNode getDisableMethodNode() throws UaException;

  /** Asynchronous form of {@link #getDisableMethodNode()}. */
  CompletableFuture<UaMethodNode> getDisableMethodNodeAsync();

  /**
   * Calls the Disable Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.4">Model
   *     documentation</a>
   */
  void disable() throws UaException;

  /**
   * Calls the Disable Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDisable() throws UaException;

  /**
   * Calls the Disable Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDisableWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #disable}. */
  CompletableFuture<Void> disableAsync();

  /** Asynchronous form of {@link #callDisable}. */
  CompletableFuture<MethodCallResult<Void>> callDisableAsync();

  /** Asynchronous form of {@link #callDisableWith}. */
  CompletableFuture<MethodCallResult<Void>> callDisableWithAsync(MethodCallOptions options);

  /**
   * Resolves the mandatory Enable Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5">Model
   *     documentation</a>
   */
  UaMethodNode getEnableMethodNode() throws UaException;

  /** Asynchronous form of {@link #getEnableMethodNode()}. */
  CompletableFuture<UaMethodNode> getEnableMethodNodeAsync();

  /**
   * Calls the Enable Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.5">Model
   *     documentation</a>
   */
  void enable() throws UaException;

  /**
   * Calls the Enable Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callEnable() throws UaException;

  /**
   * Calls the Enable Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callEnableWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #enable}. */
  CompletableFuture<Void> enableAsync();

  /** Asynchronous form of {@link #callEnable}. */
  CompletableFuture<MethodCallResult<Void>> callEnableAsync();

  /** Asynchronous form of {@link #callEnableWith}. */
  CompletableFuture<MethodCallResult<Void>> callEnableWithAsync(MethodCallOptions options);
}
