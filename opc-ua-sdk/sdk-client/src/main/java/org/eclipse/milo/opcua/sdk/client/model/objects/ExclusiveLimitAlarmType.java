package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Client API for the ExclusiveLimitAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.3">Model
 *     documentation</a>
 */
public interface ExclusiveLimitAlarmType extends LimitAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 9341L);

  /**
   * Resolves the mandatory LimitState child, a ExclusiveLimitStateMachineType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.2">ExclusiveLimitStateMachineType
   *     documentation</a>
   */
  ExclusiveLimitStateMachineType getLimitStateNode() throws UaException;

  /** Asynchronous form of {@link #getLimitStateNode()}. */
  CompletableFuture<? extends ExclusiveLimitStateMachineType> getLimitStateNodeAsync();

  /**
   * Resolves the mandatory ActiveState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getActiveStateNode() throws UaException;

  /** Asynchronous form of {@link #getActiveStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getActiveStateNodeAsync();
}
