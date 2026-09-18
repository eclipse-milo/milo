package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the HistoricalEventConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.4.3">Model
 *     documentation</a>
 */
public interface HistoricalEventConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32621L);

  /**
   * Returns the mandatory EventTypes child, a FolderType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderTypeNode getEventTypesNode();

  /**
   * Returns the optional SortByEventFields child, a PropertyType with DataType
   * SimpleAttributeOperand.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSortByEventFieldsNode();

  /**
   * Returns the Value of the SortByEventFields child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SimpleAttributeOperand @Nullable [] getSortByEventFields();

  /**
   * Sets the Value of the SortByEventFields child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Returns the optional StartOfArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getStartOfArchiveNode();

  /**
   * Returns the Value of the StartOfArchive child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartOfArchive();

  /**
   * Sets the Value of the StartOfArchive child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartOfArchive(@Nullable DateTime value);

  /**
   * Returns the optional StartOfOnlineArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getStartOfOnlineArchiveNode();

  /**
   * Returns the Value of the StartOfOnlineArchive child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartOfOnlineArchive();

  /**
   * Sets the Value of the StartOfOnlineArchive child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartOfOnlineArchive(@Nullable DateTime value);
}
