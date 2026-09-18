package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SamplingIntervalDiagnosticsType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.10">Model
 *     documentation</a>
 */
public interface SamplingIntervalDiagnosticsType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2165L);

  /**
   * Returns the mandatory DisabledMonitoredItemsSamplingCount child, a BaseDataVariableType with
   * DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDisabledMonitoredItemsSamplingCountNode();

  /**
   * Returns the Value of the DisabledMonitoredItemsSamplingCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getDisabledMonitoredItemsSamplingCount();

  /**
   * Sets the Value of the DisabledMonitoredItemsSamplingCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDisabledMonitoredItemsSamplingCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxSampledMonitoredItemsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxSampledMonitoredItemsCountNode();

  /**
   * Returns the Value of the MaxSampledMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxSampledMonitoredItemsCount();

  /**
   * Sets the Value of the MaxSampledMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxSampledMonitoredItemsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SampledMonitoredItemsCount child, a BaseDataVariableType with DataType
   * UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSampledMonitoredItemsCountNode();

  /**
   * Returns the Value of the SampledMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSampledMonitoredItemsCount();

  /**
   * Sets the Value of the SampledMonitoredItemsCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSampledMonitoredItemsCount(@Nullable UInteger value);

  /**
   * Returns the mandatory SamplingInterval child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSamplingIntervalNode();

  /**
   * Returns the Value of the SamplingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getSamplingInterval();

  /**
   * Sets the Value of the SamplingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSamplingInterval(@Nullable Double value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable SamplingIntervalDiagnosticsDataType getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable SamplingIntervalDiagnosticsDataType value);
}
