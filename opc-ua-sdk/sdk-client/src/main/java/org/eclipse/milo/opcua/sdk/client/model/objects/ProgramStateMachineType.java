package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnostic2Type;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ProgramStateMachineType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1">Model
 *     documentation</a>
 */
public interface ProgramStateMachineType extends FiniteStateMachineType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2391L);

  QualifiedProperty<Boolean> AutoDelete_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AutoDelete",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Integer> RecycleCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RecycleCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
          -1,
          Integer.class);

  QualifiedProperty<Boolean> Deletable_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Deletable",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the mandatory AutoDelete child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAutoDeleteNode() throws UaException;

  /** Asynchronous form of {@link #getAutoDeleteNode()}. */
  CompletableFuture<? extends PropertyType> getAutoDeleteNodeAsync();

  /**
   * Reads the Value of the AutoDelete child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readAutoDelete() throws UaException;

  /**
   * Writes the Value of the AutoDelete child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAutoDelete(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readAutoDelete()}. */
  CompletableFuture<? extends @Nullable Boolean> readAutoDeleteAsync();

  /** Asynchronous form of {@link #writeAutoDelete}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAutoDeleteAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory CurrentState child, a FiniteStateVariableType with DataType
   * LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.6">FiniteStateVariableType
   *     documentation</a>
   */
  FiniteStateVariableType getCurrentStateNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentStateNode()}. */
  CompletableFuture<? extends FiniteStateVariableType> getCurrentStateNodeAsync();

  /**
   * Resolves the mandatory RecycleCount child, a PropertyType with DataType Int32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRecycleCountNode() throws UaException;

  /** Asynchronous form of {@link #getRecycleCountNode()}. */
  CompletableFuture<? extends PropertyType> getRecycleCountNodeAsync();

  /**
   * Reads the Value of the RecycleCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Integer readRecycleCount() throws UaException;

  /**
   * Writes the Value of the RecycleCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRecycleCount(@Nullable Integer value) throws UaException;

  /** Asynchronous form of {@link #readRecycleCount()}. */
  CompletableFuture<? extends @Nullable Integer> readRecycleCountAsync();

  /** Asynchronous form of {@link #writeRecycleCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRecycleCountAsync(@Nullable Integer value);

  /**
   * Resolves the mandatory LastTransition child, a FiniteTransitionVariableType with DataType
   * LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.7">FiniteTransitionVariableType
   *     documentation</a>
   */
  FiniteTransitionVariableType getLastTransitionNode() throws UaException;

  /** Asynchronous form of {@link #getLastTransitionNode()}. */
  CompletableFuture<? extends FiniteTransitionVariableType> getLastTransitionNodeAsync();

  /**
   * Resolves the optional FinalResultData child, a BaseObjectType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/A.2.6/#A.2.6.2">Model
   *     documentation</a>
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  @Nullable ObjectNode getFinalResultDataNode() throws UaException;

  /** Asynchronous form of {@link #getFinalResultDataNode()}. */
  CompletableFuture<? extends @Nullable ObjectNode> getFinalResultDataNodeAsync();

  /**
   * Resolves the optional ProgramDiagnostic child, a ProgramDiagnostic2Type with DataType
   * ProgramDiagnostic2DataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">ProgramDiagnostic2Type
   *     documentation</a>
   */
  @Nullable ProgramDiagnostic2Type getProgramDiagnosticNode() throws UaException;

  /** Asynchronous form of {@link #getProgramDiagnosticNode()}. */
  CompletableFuture<? extends @Nullable ProgramDiagnostic2Type> getProgramDiagnosticNodeAsync();

  /**
   * Reads the Value of the ProgramDiagnostic child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ProgramDiagnostic2DataType readProgramDiagnostic() throws UaException;

  /**
   * Writes the Value of the ProgramDiagnostic child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value) throws UaException;

  /** Asynchronous form of {@link #readProgramDiagnostic()}. */
  CompletableFuture<? extends @Nullable ProgramDiagnostic2DataType> readProgramDiagnosticAsync();

  /** Asynchronous form of {@link #writeProgramDiagnostic}; completes with the operation status. */
  CompletableFuture<StatusCode> writeProgramDiagnosticAsync(
      @Nullable ProgramDiagnostic2DataType value);

  /**
   * Resolves the mandatory Deletable child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDeletableNode() throws UaException;

  /** Asynchronous form of {@link #getDeletableNode()}. */
  CompletableFuture<? extends PropertyType> getDeletableNodeAsync();

  /**
   * Reads the Value of the Deletable child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readDeletable() throws UaException;

  /**
   * Writes the Value of the Deletable child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeletable(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readDeletable()}. */
  CompletableFuture<? extends @Nullable Boolean> readDeletableAsync();

  /** Asynchronous form of {@link #writeDeletable}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDeletableAsync(@Nullable Boolean value);
}
