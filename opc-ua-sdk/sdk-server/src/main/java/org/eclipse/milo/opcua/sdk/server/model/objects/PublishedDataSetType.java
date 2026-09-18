package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PublishedDataSetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1">Model
 *     documentation</a>
 */
public interface PublishedDataSetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14509L);

  /**
   * Returns the mandatory ConfigurationVersion child, a PropertyType with DataType
   * ConfigurationVersionDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConfigurationVersionNode();

  /**
   * Returns the Value of the ConfigurationVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ConfigurationVersionDataType getConfigurationVersion();

  /**
   * Sets the Value of the ConfigurationVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConfigurationVersion(@Nullable ConfigurationVersionDataType value);

  /**
   * Returns the optional CyclicDataSet child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getCyclicDataSetNode();

  /**
   * Returns the Value of the CyclicDataSet child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getCyclicDataSet();

  /**
   * Sets the Value of the CyclicDataSet child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCyclicDataSet(@Nullable Boolean value);

  /**
   * Returns the optional DataSetClassId child, a PropertyType with DataType Guid.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDataSetClassIdNode();

  /**
   * Returns the Value of the DataSetClassId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UUID getDataSetClassId();

  /**
   * Sets the Value of the DataSetClassId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetClassId(@Nullable UUID value);

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
   * Returns the optional ExtensionFields child, a ExtensionFieldsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.2">ExtensionFieldsType
   *     documentation</a>
   */
  @Nullable ExtensionFieldsTypeNode getExtensionFieldsNode();
}
