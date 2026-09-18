package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerDiagnosticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">Model
 *     documentation</a>
 */
public interface ServerDiagnosticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2020L);

  QualifiedProperty<Boolean> EnabledFlag_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EnabledFlag",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the mandatory EnabledFlag child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEnabledFlagNode() throws UaException;

  /** Asynchronous form of {@link #getEnabledFlagNode()}. */
  CompletableFuture<? extends PropertyType> getEnabledFlagNodeAsync();

  /**
   * Reads the Value of the EnabledFlag child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readEnabledFlag() throws UaException;

  /**
   * Writes the Value of the EnabledFlag child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEnabledFlag(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readEnabledFlag()}. */
  CompletableFuture<? extends @Nullable Boolean> readEnabledFlagAsync();

  /** Asynchronous form of {@link #writeEnabledFlag}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEnabledFlagAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory ServerDiagnosticsSummary child, a ServerDiagnosticsSummaryType with
   * DataType ServerDiagnosticsSummaryDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">ServerDiagnosticsSummaryType
   *     documentation</a>
   */
  ServerDiagnosticsSummaryType getServerDiagnosticsSummaryNode() throws UaException;

  /** Asynchronous form of {@link #getServerDiagnosticsSummaryNode()}. */
  CompletableFuture<? extends ServerDiagnosticsSummaryType> getServerDiagnosticsSummaryNodeAsync();

  /**
   * Reads the Value of the ServerDiagnosticsSummary child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ServerDiagnosticsSummaryDataType readServerDiagnosticsSummary() throws UaException;

  /**
   * Writes the Value of the ServerDiagnosticsSummary child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value)
      throws UaException;

  /** Asynchronous form of {@link #readServerDiagnosticsSummary()}. */
  CompletableFuture<? extends @Nullable ServerDiagnosticsSummaryDataType>
      readServerDiagnosticsSummaryAsync();

  /**
   * Asynchronous form of {@link #writeServerDiagnosticsSummary}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeServerDiagnosticsSummaryAsync(
      @Nullable ServerDiagnosticsSummaryDataType value);

  /**
   * Resolves the mandatory SessionsDiagnosticsSummary child, a SessionsDiagnosticsSummaryType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.4">SessionsDiagnosticsSummaryType
   *     documentation</a>
   */
  SessionsDiagnosticsSummaryType getSessionsDiagnosticsSummaryNode() throws UaException;

  /** Asynchronous form of {@link #getSessionsDiagnosticsSummaryNode()}. */
  CompletableFuture<? extends SessionsDiagnosticsSummaryType>
      getSessionsDiagnosticsSummaryNodeAsync();

  /**
   * Resolves the mandatory SubscriptionDiagnosticsArray child, a SubscriptionDiagnosticsArrayType
   * with DataType SubscriptionDiagnosticsDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.11">SubscriptionDiagnosticsArrayType
   *     documentation</a>
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode() throws UaException;

  /** Asynchronous form of {@link #getSubscriptionDiagnosticsArrayNode()}. */
  CompletableFuture<? extends SubscriptionDiagnosticsArrayType>
      getSubscriptionDiagnosticsArrayNodeAsync();

  /**
   * Reads the Value of the SubscriptionDiagnosticsArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] readSubscriptionDiagnosticsArray()
      throws UaException;

  /**
   * Writes the Value of the SubscriptionDiagnosticsArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSubscriptionDiagnosticsArray()}. */
  CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readSubscriptionDiagnosticsArrayAsync();

  /**
   * Asynchronous form of {@link #writeSubscriptionDiagnosticsArray}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);

  /**
   * Resolves the optional SamplingIntervalDiagnosticsArray child, a
   * SamplingIntervalDiagnosticsArrayType with DataType SamplingIntervalDiagnosticsDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.9">SamplingIntervalDiagnosticsArrayType
   *     documentation</a>
   */
  @Nullable SamplingIntervalDiagnosticsArrayType getSamplingIntervalDiagnosticsArrayNode()
      throws UaException;

  /** Asynchronous form of {@link #getSamplingIntervalDiagnosticsArrayNode()}. */
  CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsArrayType>
      getSamplingIntervalDiagnosticsArrayNodeAsync();

  /**
   * Reads the Value of the SamplingIntervalDiagnosticsArray child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] readSamplingIntervalDiagnosticsArray()
      throws UaException;

  /**
   * Writes the Value of the SamplingIntervalDiagnosticsArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSamplingIntervalDiagnosticsArray()}. */
  CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsDataType @Nullable []>
      readSamplingIntervalDiagnosticsArrayAsync();

  /**
   * Asynchronous form of {@link #writeSamplingIntervalDiagnosticsArray}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsArrayAsync(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value);
}
