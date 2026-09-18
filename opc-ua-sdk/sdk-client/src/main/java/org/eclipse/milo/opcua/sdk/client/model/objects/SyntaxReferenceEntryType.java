package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SyntaxReferenceEntryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.3">Model
 *     documentation</a>
 */
public interface SyntaxReferenceEntryType extends DictionaryEntryType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32439L);

  QualifiedProperty<String> CommonName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CommonName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory CommonName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCommonNameNode() throws UaException;

  /** Asynchronous form of {@link #getCommonNameNode()}. */
  CompletableFuture<? extends PropertyType> getCommonNameNodeAsync();

  /**
   * Reads the Value of the CommonName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readCommonName() throws UaException;

  /**
   * Writes the Value of the CommonName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCommonName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readCommonName()}. */
  CompletableFuture<? extends @Nullable String> readCommonNameAsync();

  /** Asynchronous form of {@link #writeCommonName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCommonNameAsync(@Nullable String value);
}
