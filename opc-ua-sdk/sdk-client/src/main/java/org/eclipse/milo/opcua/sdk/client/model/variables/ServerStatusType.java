package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerStatusType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">Model
 *     documentation</a>
 */
public interface ServerStatusType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2138L);

  /**
   * Resolves the mandatory CurrentTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getCurrentTimeNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentTimeNode()}. */
  CompletableFuture<? extends VariableNode> getCurrentTimeNodeAsync();

  /**
   * Reads the Value of the CurrentTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readCurrentTime() throws UaException;

  /**
   * Writes the Value of the CurrentTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readCurrentTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readCurrentTimeAsync();

  /** Asynchronous form of {@link #writeCurrentTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCurrentTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory ShutdownReason child, a BaseDataVariableType with DataType
   * LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getShutdownReasonNode() throws UaException;

  /** Asynchronous form of {@link #getShutdownReasonNode()}. */
  CompletableFuture<? extends VariableNode> getShutdownReasonNodeAsync();

  /**
   * Reads the Value of the ShutdownReason child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readShutdownReason() throws UaException;

  /**
   * Writes the Value of the ShutdownReason child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeShutdownReason(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readShutdownReason()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readShutdownReasonAsync();

  /** Asynchronous form of {@link #writeShutdownReason}; completes with the operation status. */
  CompletableFuture<StatusCode> writeShutdownReasonAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory SecondsTillShutdown child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSecondsTillShutdownNode() throws UaException;

  /** Asynchronous form of {@link #getSecondsTillShutdownNode()}. */
  CompletableFuture<? extends VariableNode> getSecondsTillShutdownNodeAsync();

  /**
   * Reads the Value of the SecondsTillShutdown child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readSecondsTillShutdown() throws UaException;

  /**
   * Writes the Value of the SecondsTillShutdown child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecondsTillShutdown(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readSecondsTillShutdown()}. */
  CompletableFuture<? extends @Nullable UInteger> readSecondsTillShutdownAsync();

  /**
   * Asynchronous form of {@link #writeSecondsTillShutdown}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSecondsTillShutdownAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory State child, a BaseDataVariableType with DataType ServerState.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getStateNode() throws UaException;

  /** Asynchronous form of {@link #getStateNode()}. */
  CompletableFuture<? extends VariableNode> getStateNodeAsync();

  /**
   * Reads the Value of the State child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServerState readState() throws UaException;

  /**
   * Writes the Value of the State child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeState(@Nullable ServerState value) throws UaException;

  /** Asynchronous form of {@link #readState()}. */
  CompletableFuture<? extends @Nullable ServerState> readStateAsync();

  /** Asynchronous form of {@link #writeState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStateAsync(@Nullable ServerState value);

  /**
   * Resolves the mandatory BuildInfo child, a BuildInfoType with DataType BuildInfo.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7">BuildInfoType
   *     documentation</a>
   */
  BuildInfoType getBuildInfoNode() throws UaException;

  /** Asynchronous form of {@link #getBuildInfoNode()}. */
  CompletableFuture<? extends BuildInfoType> getBuildInfoNodeAsync();

  /**
   * Reads the Value of the BuildInfo child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable BuildInfo readBuildInfo() throws UaException;

  /**
   * Writes the Value of the BuildInfo child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBuildInfo(@Nullable BuildInfo value) throws UaException;

  /** Asynchronous form of {@link #readBuildInfo()}. */
  CompletableFuture<? extends @Nullable BuildInfo> readBuildInfoAsync();

  /** Asynchronous form of {@link #writeBuildInfo}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBuildInfoAsync(@Nullable BuildInfo value);

  /**
   * Resolves the mandatory StartTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getStartTimeNode() throws UaException;

  /** Asynchronous form of {@link #getStartTimeNode()}. */
  CompletableFuture<? extends VariableNode> getStartTimeNodeAsync();

  /**
   * Reads the Value of the StartTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartTime() throws UaException;

  /**
   * Writes the Value of the StartTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartTimeAsync();

  /** Asynchronous form of {@link #writeStartTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStartTimeAsync(@Nullable DateTime value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServerStatusDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable ServerStatusDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable ServerStatusDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable ServerStatusDataType value);
}
