package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the OrientationType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.25">Model
 *     documentation</a>
 */
public interface OrientationType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18779L);

  QualifiedProperty<EUInformation> AngleUnit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AngleUnit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 887L),
          -1,
          EUInformation.class);

  /**
   * Resolves the optional AngleUnit child, a PropertyType with DataType EUInformation.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getAngleUnitNode() throws UaException;

  /** Asynchronous form of {@link #getAngleUnitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getAngleUnitNodeAsync();

  /**
   * Reads the Value of the AngleUnit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EUInformation readAngleUnit() throws UaException;

  /**
   * Writes the Value of the AngleUnit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAngleUnit(@Nullable EUInformation value) throws UaException;

  /** Asynchronous form of {@link #readAngleUnit()}. */
  CompletableFuture<? extends @Nullable EUInformation> readAngleUnitAsync();

  /** Asynchronous form of {@link #writeAngleUnit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAngleUnitAsync(@Nullable EUInformation value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Orientation readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Orientation value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Orientation> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Orientation value);
}
