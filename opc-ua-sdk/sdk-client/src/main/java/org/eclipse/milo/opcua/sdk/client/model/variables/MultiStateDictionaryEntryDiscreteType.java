package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the MultiStateDictionaryEntryDiscreteType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.2">Model
 *     documentation</a>
 */
public interface MultiStateDictionaryEntryDiscreteType
    extends MultiStateDictionaryEntryDiscreteBaseType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19084L);

  /**
   * Resolves the mandatory ValueAsDictionaryEntries child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getValueAsDictionaryEntriesNode() throws UaException;

  /** Asynchronous form of {@link #getValueAsDictionaryEntriesNode()}. */
  CompletableFuture<? extends PropertyType> getValueAsDictionaryEntriesNodeAsync();
}
