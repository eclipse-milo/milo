package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SamplingIntervalDiagnosticsArrayType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.9">Model
 *     documentation</a>
 */
public interface SamplingIntervalDiagnosticsArrayType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2164L);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value);
}
