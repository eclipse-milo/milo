package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PriorityMappingTableType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2">Model
 *     documentation</a>
 */
public interface PriorityMappingTableType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25227L);

  QualifiedProperty<PriorityMappingEntryType[]> PriorityMapppingEntries_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PriorityMapppingEntries",
          ExpandedNodeId.of(Namespaces.OPC_UA, 25220L),
          1,
          PriorityMappingEntryType[].class);

  /**
   * Resolves the mandatory PriorityMapppingEntries child, a PropertyType with DataType
   * PriorityMappingEntryType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPriorityMapppingEntriesNode() throws UaException;

  /** Asynchronous form of {@link #getPriorityMapppingEntriesNode()}. */
  CompletableFuture<? extends PropertyType> getPriorityMapppingEntriesNodeAsync();

  /**
   * Reads the Value of the PriorityMapppingEntries child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PriorityMappingEntryType @Nullable [] readPriorityMapppingEntries() throws UaException;

  /**
   * Writes the Value of the PriorityMapppingEntries child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readPriorityMapppingEntries()}. */
  CompletableFuture<? extends @Nullable PriorityMappingEntryType @Nullable []>
      readPriorityMapppingEntriesAsync();

  /**
   * Asynchronous form of {@link #writePriorityMapppingEntries}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writePriorityMapppingEntriesAsync(
      @Nullable PriorityMappingEntryType @Nullable [] value);

  /**
   * Resolves the optional AddPriorityMappingEntry Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPriorityMappingEntryMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddPriorityMappingEntryMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddPriorityMappingEntryMethodNodeAsync();

  /**
   * Calls the AddPriorityMappingEntry Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3">Model
   *     documentation</a>
   */
  void addPriorityMappingEntry(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP)
      throws UaException;

  /**
   * Calls the AddPriorityMappingEntry Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddPriorityMappingEntry(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP)
      throws UaException;

  /**
   * Calls the AddPriorityMappingEntry Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddPriorityMappingEntryWith(
      MethodCallOptions options,
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP)
      throws UaException;

  /** Asynchronous form of {@link #addPriorityMappingEntry}. */
  CompletableFuture<Void> addPriorityMappingEntryAsync(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP);

  /** Asynchronous form of {@link #callAddPriorityMappingEntry}. */
  CompletableFuture<MethodCallResult<Void>> callAddPriorityMappingEntryAsync(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP);

  /** Asynchronous form of {@link #callAddPriorityMappingEntryWith}. */
  CompletableFuture<MethodCallResult<Void>> callAddPriorityMappingEntryWithAsync(
      MethodCallOptions options,
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValue_PCP,
      @Nullable UInteger priorityValue_DSCP);

  /**
   * Resolves the optional DeletePriorityMappingEntry Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getDeletePriorityMappingEntryMethodNode() throws UaException;

  /** Asynchronous form of {@link #getDeletePriorityMappingEntryMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getDeletePriorityMappingEntryMethodNodeAsync();

  /**
   * Calls the DeletePriorityMappingEntry Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4">Model
   *     documentation</a>
   */
  void deletePriorityMappingEntry(@Nullable String mappingUri, @Nullable String priorityLabel)
      throws UaException;

  /**
   * Calls the DeletePriorityMappingEntry Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDeletePriorityMappingEntry(
      @Nullable String mappingUri, @Nullable String priorityLabel) throws UaException;

  /**
   * Calls the DeletePriorityMappingEntry Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callDeletePriorityMappingEntryWith(
      MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel)
      throws UaException;

  /** Asynchronous form of {@link #deletePriorityMappingEntry}. */
  CompletableFuture<Void> deletePriorityMappingEntryAsync(
      @Nullable String mappingUri, @Nullable String priorityLabel);

  /** Asynchronous form of {@link #callDeletePriorityMappingEntry}. */
  CompletableFuture<MethodCallResult<Void>> callDeletePriorityMappingEntryAsync(
      @Nullable String mappingUri, @Nullable String priorityLabel);

  /** Asynchronous form of {@link #callDeletePriorityMappingEntryWith}. */
  CompletableFuture<MethodCallResult<Void>> callDeletePriorityMappingEntryWithAsync(
      MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel);
}
