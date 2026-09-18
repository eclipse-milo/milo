package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the AnalogItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.3">Model
 *     documentation</a>
 */
public interface AnalogItemType extends BaseAnalogType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2368L);

  /**
   * Resolves the mandatory EURange child, a PropertyType with DataType Range.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEURangeNode() throws UaException;

  /** Asynchronous form of {@link #getEURangeNode()}. */
  CompletableFuture<? extends PropertyType> getEURangeNodeAsync();
}
