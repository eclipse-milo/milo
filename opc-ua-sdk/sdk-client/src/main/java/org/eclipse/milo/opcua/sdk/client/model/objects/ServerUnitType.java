package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerUnitType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.3">Model
 *     documentation</a>
 */
public interface ServerUnitType extends UnitType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32447L);

  QualifiedProperty<ConversionLimitEnum> ConversionLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConversionLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32436L),
          -1,
          ConversionLimitEnum.class);

  /**
   * Resolves the optional CoherentUnit child, a UnitType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2">UnitType
   *     documentation</a>
   */
  @Nullable UnitType getCoherentUnitNode() throws UaException;

  /** Asynchronous form of {@link #getCoherentUnitNode()}. */
  CompletableFuture<? extends @Nullable UnitType> getCoherentUnitNodeAsync();

  /**
   * Resolves the mandatory ConversionLimit child, a PropertyType with DataType ConversionLimitEnum.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConversionLimitNode() throws UaException;

  /** Asynchronous form of {@link #getConversionLimitNode()}. */
  CompletableFuture<? extends PropertyType> getConversionLimitNodeAsync();

  /**
   * Reads the Value of the ConversionLimit child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ConversionLimitEnum readConversionLimit() throws UaException;

  /**
   * Writes the Value of the ConversionLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConversionLimit(@Nullable ConversionLimitEnum value) throws UaException;

  /** Asynchronous form of {@link #readConversionLimit()}. */
  CompletableFuture<? extends @Nullable ConversionLimitEnum> readConversionLimitAsync();

  /** Asynchronous form of {@link #writeConversionLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConversionLimitAsync(@Nullable ConversionLimitEnum value);

  /**
   * Resolves the optional AlternativeUnits child, a BaseObjectType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  @Nullable ObjectNode getAlternativeUnitsNode() throws UaException;

  /** Asynchronous form of {@link #getAlternativeUnitsNode()}. */
  CompletableFuture<? extends @Nullable ObjectNode> getAlternativeUnitsNodeAsync();
}
