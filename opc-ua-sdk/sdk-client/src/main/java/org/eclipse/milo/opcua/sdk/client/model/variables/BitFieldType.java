package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.BitFieldDefinition;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the BitFieldType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.29">Model
 *     documentation</a>
 */
public interface BitFieldType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32431L);

  QualifiedProperty<BitFieldDefinition[]> BitFieldsDefinitions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BitFieldsDefinitions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 32421L),
          1,
          BitFieldDefinition[].class);

  /**
   * Resolves the mandatory BitFieldsDefinitions child, a PropertyType with DataType
   * BitFieldDefinition.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getBitFieldsDefinitionsNode() throws UaException;

  /** Asynchronous form of {@link #getBitFieldsDefinitionsNode()}. */
  CompletableFuture<? extends PropertyType> getBitFieldsDefinitionsNodeAsync();

  /**
   * Reads the Value of the BitFieldsDefinitions child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable BitFieldDefinition @Nullable [] readBitFieldsDefinitions() throws UaException;

  /**
   * Writes the Value of the BitFieldsDefinitions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBitFieldsDefinitions(@Nullable BitFieldDefinition @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readBitFieldsDefinitions()}. */
  CompletableFuture<? extends @Nullable BitFieldDefinition @Nullable []>
      readBitFieldsDefinitionsAsync();

  /**
   * Asynchronous form of {@link #writeBitFieldsDefinitions}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeBitFieldsDefinitionsAsync(
      @Nullable BitFieldDefinition @Nullable [] value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Variant> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Variant value);
}
