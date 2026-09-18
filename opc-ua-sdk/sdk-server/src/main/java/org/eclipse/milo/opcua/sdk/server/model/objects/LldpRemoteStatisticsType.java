package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the LldpRemoteStatisticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public interface LldpRemoteStatisticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 18996L);

  /**
   * Returns the mandatory LastChangeTime child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getLastChangeTimeNode();

  /**
   * Returns the Value of the LastChangeTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getLastChangeTime();

  /**
   * Sets the Value of the LastChangeTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastChangeTime(@Nullable UInteger value);

  /**
   * Returns the mandatory RemoteAgeouts child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRemoteAgeoutsNode();

  /**
   * Returns the Value of the RemoteAgeouts child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRemoteAgeouts();

  /**
   * Sets the Value of the RemoteAgeouts child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteAgeouts(@Nullable UInteger value);

  /**
   * Returns the mandatory RemoteDeletes child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRemoteDeletesNode();

  /**
   * Returns the Value of the RemoteDeletes child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRemoteDeletes();

  /**
   * Sets the Value of the RemoteDeletes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteDeletes(@Nullable UInteger value);

  /**
   * Returns the mandatory RemoteDrops child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRemoteDropsNode();

  /**
   * Returns the Value of the RemoteDrops child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRemoteDrops();

  /**
   * Sets the Value of the RemoteDrops child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteDrops(@Nullable UInteger value);

  /**
   * Returns the mandatory RemoteInserts child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRemoteInsertsNode();

  /**
   * Returns the Value of the RemoteInserts child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRemoteInserts();

  /**
   * Sets the Value of the RemoteInserts child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteInserts(@Nullable UInteger value);
}
