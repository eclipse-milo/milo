package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the AnalogNumberUnitRangeType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.7">Model
 *     documentation</a>
 */
public interface AnalogNumberUnitRangeType extends AnalogUnitRangeType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23918L);

  /**
   * Resolves the mandatory EUNumberRange child, a PropertyType with DataType NumberRange.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEUNumberRangeNode() throws UaException;

  /** Asynchronous form of {@link #getEUNumberRangeNode()}. */
  CompletableFuture<? extends PropertyType> getEUNumberRangeNodeAsync();
}
