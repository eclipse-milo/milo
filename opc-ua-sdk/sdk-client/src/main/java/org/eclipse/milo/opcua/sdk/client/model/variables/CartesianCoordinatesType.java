package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the CartesianCoordinatesType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.23">Model
 *     documentation</a>
 */
public interface CartesianCoordinatesType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18772L);

  QualifiedProperty<EUInformation> LengthUnit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LengthUnit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 887L),
          -1,
          EUInformation.class);

  /**
   * Resolves the optional LengthUnit child, a PropertyType with DataType EUInformation.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLengthUnitNode() throws UaException;

  /** Asynchronous form of {@link #getLengthUnitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLengthUnitNodeAsync();

  /**
   * Reads the Value of the LengthUnit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EUInformation readLengthUnit() throws UaException;

  /**
   * Writes the Value of the LengthUnit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLengthUnit(@Nullable EUInformation value) throws UaException;

  /** Asynchronous form of {@link #readLengthUnit()}. */
  CompletableFuture<? extends @Nullable EUInformation> readLengthUnitAsync();

  /** Asynchronous form of {@link #writeLengthUnit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLengthUnitAsync(@Nullable EUInformation value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable CartesianCoordinates readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable CartesianCoordinates value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable CartesianCoordinates> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable CartesianCoordinates value);
}
