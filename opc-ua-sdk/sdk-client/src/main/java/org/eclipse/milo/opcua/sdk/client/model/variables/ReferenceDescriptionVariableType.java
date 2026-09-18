package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescriptionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceListEntryDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Client API for the ReferenceDescriptionVariableType VariableType. */
public interface ReferenceDescriptionVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32657L);

  QualifiedProperty<ReferenceListEntryDataType[]> ReferenceRefinement_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReferenceRefinement",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32660L),
          1,
          ReferenceListEntryDataType[].class);

  /**
   * Resolves the optional ReferenceRefinement child, a PropertyType with DataType
   * ReferenceListEntryDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getReferenceRefinementNode() throws UaException;

  /** Asynchronous form of {@link #getReferenceRefinementNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getReferenceRefinementNodeAsync();

  /**
   * Reads the Value of the ReferenceRefinement child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ReferenceListEntryDataType @Nullable [] readReferenceRefinement() throws UaException;

  /**
   * Writes the Value of the ReferenceRefinement child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReferenceRefinement(@Nullable ReferenceListEntryDataType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readReferenceRefinement()}. */
  CompletableFuture<? extends @Nullable ReferenceListEntryDataType @Nullable []>
      readReferenceRefinementAsync();

  /**
   * Asynchronous form of {@link #writeReferenceRefinement}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeReferenceRefinementAsync(
      @Nullable ReferenceListEntryDataType @Nullable [] value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ReferenceDescriptionDataType readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable ReferenceDescriptionDataType value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable ReferenceDescriptionDataType> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable ReferenceDescriptionDataType value);
}
