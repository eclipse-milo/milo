package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TrustListOutOfDateAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.11">Model
 *     documentation</a>
 */
public interface TrustListOutOfDateAlarmType extends SystemOffNormalAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19297L);

  /**
   * Returns the mandatory LastUpdateTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastUpdateTimeNode();

  /**
   * Returns the Value of the LastUpdateTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastUpdateTime();

  /**
   * Sets the Value of the LastUpdateTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastUpdateTime(@Nullable DateTime value);

  /**
   * Returns the mandatory TrustListId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getTrustListIdNode();

  /**
   * Returns the Value of the TrustListId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getTrustListId();

  /**
   * Sets the Value of the TrustListId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTrustListId(@Nullable NodeId value);

  /**
   * Returns the mandatory UpdateFrequency child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUpdateFrequencyNode();

  /**
   * Returns the Value of the UpdateFrequency child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getUpdateFrequency();

  /**
   * Sets the Value of the UpdateFrequency child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUpdateFrequency(@Nullable Double value);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends SystemOffNormalAlarmType.Methods {}
}
