package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ConfigurationUpdatedAuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.8">Model
 *     documentation</a>
 */
public interface ConfigurationUpdatedAuditEventType extends AuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15541L);

  /**
   * Returns the mandatory NewVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNewVersionNode();

  /**
   * Returns the Value of the NewVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getNewVersion();

  /**
   * Sets the Value of the NewVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNewVersion(@Nullable UInteger value);

  /**
   * Returns the mandatory OldVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOldVersionNode();

  /**
   * Returns the Value of the OldVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getOldVersion();

  /**
   * Sets the Value of the OldVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOldVersion(@Nullable UInteger value);
}
