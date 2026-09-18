package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the AnalogUnitRangeType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.5">Model
 *     documentation</a>
 */
public interface AnalogUnitRangeType extends AnalogItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17570L);

  /**
   * Resolves the mandatory EngineeringUnits child, a PropertyType with DataType EUInformation.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEngineeringUnits_Node() throws UaException;

  /** Asynchronous form of {@link #getEngineeringUnits_Node()}. */
  CompletableFuture<? extends PropertyType> getEngineeringUnits_NodeAsync();
}
