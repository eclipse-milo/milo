package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Vector;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the VectorType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.21">Model
 *     documentation</a>
 */
public interface VectorType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17714L);

  QualifiedProperty<EUInformation> VectorUnit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "VectorUnit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 887L),
          -1,
          EUInformation.class);

  /**
   * Resolves the optional VectorUnit child, a PropertyType with DataType EUInformation.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getVectorUnitNode() throws UaException;

  /** Asynchronous form of {@link #getVectorUnitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getVectorUnitNodeAsync();

  /**
   * Reads the Value of the VectorUnit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EUInformation readVectorUnit() throws UaException;

  /**
   * Writes the Value of the VectorUnit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeVectorUnit(@Nullable EUInformation value) throws UaException;

  /** Asynchronous form of {@link #readVectorUnit()}. */
  CompletableFuture<? extends @Nullable EUInformation> readVectorUnitAsync();

  /** Asynchronous form of {@link #writeVectorUnit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeVectorUnitAsync(@Nullable EUInformation value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Vector readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Vector value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Vector> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Vector value);
}
