package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the OperationLimitsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11">Model
 *     documentation</a>
 */
public interface OperationLimitsType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11564L);

  /**
   * Returns the optional MaxMonitoredItemsPerCall child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxMonitoredItemsPerCallNode();

  /**
   * Returns the Value of the MaxMonitoredItemsPerCall child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxMonitoredItemsPerCall();

  /**
   * Sets the Value of the MaxMonitoredItemsPerCall child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxMonitoredItemsPerCall(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerBrowse child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerBrowseNode();

  /**
   * Returns the Value of the MaxNodesPerBrowse child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerBrowse();

  /**
   * Sets the Value of the MaxNodesPerBrowse child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerBrowse(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerHistoryReadData child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerHistoryReadDataNode();

  /**
   * Returns the Value of the MaxNodesPerHistoryReadData child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerHistoryReadData();

  /**
   * Sets the Value of the MaxNodesPerHistoryReadData child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerHistoryReadData(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerHistoryReadEvents child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerHistoryReadEventsNode();

  /**
   * Returns the Value of the MaxNodesPerHistoryReadEvents child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerHistoryReadEvents();

  /**
   * Sets the Value of the MaxNodesPerHistoryReadEvents child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerHistoryReadEvents(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerHistoryUpdateData child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateDataNode();

  /**
   * Returns the Value of the MaxNodesPerHistoryUpdateData child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerHistoryUpdateData();

  /**
   * Sets the Value of the MaxNodesPerHistoryUpdateData child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerHistoryUpdateData(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerHistoryUpdateEvents child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateEventsNode();

  /**
   * Returns the Value of the MaxNodesPerHistoryUpdateEvents child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerHistoryUpdateEvents();

  /**
   * Sets the Value of the MaxNodesPerHistoryUpdateEvents child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerMethodCall child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerMethodCallNode();

  /**
   * Returns the Value of the MaxNodesPerMethodCall child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerMethodCall();

  /**
   * Sets the Value of the MaxNodesPerMethodCall child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerMethodCall(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerNodeManagement child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerNodeManagementNode();

  /**
   * Returns the Value of the MaxNodesPerNodeManagement child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerNodeManagement();

  /**
   * Sets the Value of the MaxNodesPerNodeManagement child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerNodeManagement(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerRead child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerReadNode();

  /**
   * Returns the Value of the MaxNodesPerRead child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerRead();

  /**
   * Sets the Value of the MaxNodesPerRead child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerRead(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerRegisterNodes child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerRegisterNodesNode();

  /**
   * Returns the Value of the MaxNodesPerRegisterNodes child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerRegisterNodes();

  /**
   * Sets the Value of the MaxNodesPerRegisterNodes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerRegisterNodes(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerTranslateBrowsePathsToNodeIds child, a PropertyType with
   * DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();

  /**
   * Returns the Value of the MaxNodesPerTranslateBrowsePathsToNodeIds child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds();

  /**
   * Sets the Value of the MaxNodesPerTranslateBrowsePathsToNodeIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value);

  /**
   * Returns the optional MaxNodesPerWrite child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNodesPerWriteNode();

  /**
   * Returns the Value of the MaxNodesPerWrite child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNodesPerWrite();

  /**
   * Sets the Value of the MaxNodesPerWrite child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNodesPerWrite(@Nullable UInteger value);
}
