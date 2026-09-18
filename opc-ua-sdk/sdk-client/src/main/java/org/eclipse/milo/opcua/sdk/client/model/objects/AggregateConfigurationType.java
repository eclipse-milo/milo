package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AggregateConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2">Model
 *     documentation</a>
 */
public interface AggregateConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11187L);

  QualifiedProperty<UByte> PercentDataBad_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PercentDataBad",
          ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
          -1,
          UByte.class);

  QualifiedProperty<UByte> PercentDataGood_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PercentDataGood",
          ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
          -1,
          UByte.class);

  QualifiedProperty<Boolean> TreatUncertainAsBad_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TreatUncertainAsBad",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> UseSlopedExtrapolation_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UseSlopedExtrapolation",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the mandatory PercentDataBad child, a PropertyType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPercentDataBadNode() throws UaException;

  /** Asynchronous form of {@link #getPercentDataBadNode()}. */
  CompletableFuture<? extends PropertyType> getPercentDataBadNodeAsync();

  /**
   * Reads the Value of the PercentDataBad child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readPercentDataBad() throws UaException;

  /**
   * Writes the Value of the PercentDataBad child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePercentDataBad(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readPercentDataBad()}. */
  CompletableFuture<? extends @Nullable UByte> readPercentDataBadAsync();

  /** Asynchronous form of {@link #writePercentDataBad}; completes with the operation status. */
  CompletableFuture<StatusCode> writePercentDataBadAsync(@Nullable UByte value);

  /**
   * Resolves the mandatory PercentDataGood child, a PropertyType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPercentDataGoodNode() throws UaException;

  /** Asynchronous form of {@link #getPercentDataGoodNode()}. */
  CompletableFuture<? extends PropertyType> getPercentDataGoodNodeAsync();

  /**
   * Reads the Value of the PercentDataGood child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readPercentDataGood() throws UaException;

  /**
   * Writes the Value of the PercentDataGood child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePercentDataGood(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readPercentDataGood()}. */
  CompletableFuture<? extends @Nullable UByte> readPercentDataGoodAsync();

  /** Asynchronous form of {@link #writePercentDataGood}; completes with the operation status. */
  CompletableFuture<StatusCode> writePercentDataGoodAsync(@Nullable UByte value);

  /**
   * Resolves the mandatory TreatUncertainAsBad child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTreatUncertainAsBadNode() throws UaException;

  /** Asynchronous form of {@link #getTreatUncertainAsBadNode()}. */
  CompletableFuture<? extends PropertyType> getTreatUncertainAsBadNodeAsync();

  /**
   * Reads the Value of the TreatUncertainAsBad child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readTreatUncertainAsBad() throws UaException;

  /**
   * Writes the Value of the TreatUncertainAsBad child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTreatUncertainAsBad(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readTreatUncertainAsBad()}. */
  CompletableFuture<? extends @Nullable Boolean> readTreatUncertainAsBadAsync();

  /**
   * Asynchronous form of {@link #writeTreatUncertainAsBad}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeTreatUncertainAsBadAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory UseSlopedExtrapolation child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUseSlopedExtrapolationNode() throws UaException;

  /** Asynchronous form of {@link #getUseSlopedExtrapolationNode()}. */
  CompletableFuture<? extends PropertyType> getUseSlopedExtrapolationNodeAsync();

  /**
   * Reads the Value of the UseSlopedExtrapolation child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readUseSlopedExtrapolation() throws UaException;

  /**
   * Writes the Value of the UseSlopedExtrapolation child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUseSlopedExtrapolation(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readUseSlopedExtrapolation()}. */
  CompletableFuture<? extends @Nullable Boolean> readUseSlopedExtrapolationAsync();

  /**
   * Asynchronous form of {@link #writeUseSlopedExtrapolation}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeUseSlopedExtrapolationAsync(@Nullable Boolean value);
}
