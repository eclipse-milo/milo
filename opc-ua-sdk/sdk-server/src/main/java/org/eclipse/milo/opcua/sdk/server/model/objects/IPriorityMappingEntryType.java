package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IPriorityMappingEntryType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">Model
 *     documentation</a>
 */
public interface IPriorityMappingEntryType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24205L);

  /**
   * Returns the mandatory MappingUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMappingUriNode();

  /**
   * Returns the Value of the MappingUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getMappingUri();

  /**
   * Sets the Value of the MappingUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMappingUri(@Nullable String value);

  /**
   * Returns the mandatory PriorityLabel child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPriorityLabelNode();

  /**
   * Returns the Value of the PriorityLabel child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getPriorityLabel();

  /**
   * Sets the Value of the PriorityLabel child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriorityLabel(@Nullable String value);

  /**
   * Returns the optional PriorityValue_DSCP child, a BaseDataVariableType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getPriorityValue_DSCPNode();

  /**
   * Returns the Value of the PriorityValue_DSCP child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getPriorityValue_DSCP();

  /**
   * Sets the Value of the PriorityValue_DSCP child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriorityValue_DSCP(@Nullable UInteger value);

  /**
   * Returns the optional PriorityValue_PCP child, a BaseDataVariableType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getPriorityValue_PCPNode();

  /**
   * Returns the Value of the PriorityValue_PCP child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getPriorityValue_PCP();

  /**
   * Sets the Value of the PriorityValue_PCP child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPriorityValue_PCP(@Nullable UByte value);
}
