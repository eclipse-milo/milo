package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the StandaloneSubscribedDataSetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5">Model
 *     documentation</a>
 */
public interface StandaloneSubscribedDataSetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23828L);

  /**
   * Returns the mandatory DataSetMetaData child, a PropertyType with DataType DataSetMetaDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetMetaDataNode();

  /**
   * Returns the Value of the DataSetMetaData child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DataSetMetaDataType getDataSetMetaData();

  /**
   * Sets the Value of the DataSetMetaData child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetMetaData(@Nullable DataSetMetaDataType value);

  /**
   * Returns the mandatory IsConnected child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIsConnectedNode();

  /**
   * Returns the Value of the IsConnected child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIsConnected();

  /**
   * Sets the Value of the IsConnected child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIsConnected(@Nullable Boolean value);

  /**
   * Returns the mandatory SubscribedDataSet child, a SubscribedDataSetType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.1">SubscribedDataSetType
   *     documentation</a>
   */
  SubscribedDataSetTypeNode getSubscribedDataSetNode();
}
