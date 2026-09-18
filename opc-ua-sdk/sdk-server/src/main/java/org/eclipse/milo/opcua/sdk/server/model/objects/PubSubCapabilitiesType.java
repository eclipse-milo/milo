package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">Model
 *     documentation</a>
 */
public interface PubSubCapabilitiesType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23832L);

  /**
   * Returns the mandatory MaxDataSetReaders child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxDataSetReadersNode();

  /**
   * Returns the Value of the MaxDataSetReaders child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxDataSetReaders();

  /**
   * Sets the Value of the MaxDataSetReaders child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxDataSetReaders(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxDataSetWriters child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxDataSetWritersNode();

  /**
   * Returns the Value of the MaxDataSetWriters child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxDataSetWriters();

  /**
   * Sets the Value of the MaxDataSetWriters child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxDataSetWriters(@Nullable UInteger value);

  /**
   * Returns the optional MaxDataSetWritersPerGroup child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxDataSetWritersPerGroupNode();

  /**
   * Returns the Value of the MaxDataSetWritersPerGroup child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxDataSetWritersPerGroup();

  /**
   * Sets the Value of the MaxDataSetWritersPerGroup child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxDataSetWritersPerGroup(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxFieldsPerDataSet child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxFieldsPerDataSetNode();

  /**
   * Returns the Value of the MaxFieldsPerDataSet child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxFieldsPerDataSet();

  /**
   * Sets the Value of the MaxFieldsPerDataSet child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxFieldsPerDataSet(@Nullable UInteger value);

  /**
   * Returns the optional MaxNetworkMessageSizeBroker child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNetworkMessageSizeBrokerNode();

  /**
   * Returns the Value of the MaxNetworkMessageSizeBroker child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNetworkMessageSizeBroker();

  /**
   * Sets the Value of the MaxNetworkMessageSizeBroker child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNetworkMessageSizeBroker(@Nullable UInteger value);

  /**
   * Returns the optional MaxNetworkMessageSizeDatagram child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxNetworkMessageSizeDatagramNode();

  /**
   * Returns the Value of the MaxNetworkMessageSizeDatagram child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNetworkMessageSizeDatagram();

  /**
   * Sets the Value of the MaxNetworkMessageSizeDatagram child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNetworkMessageSizeDatagram(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxPubSubConnections child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxPubSubConnectionsNode();

  /**
   * Returns the Value of the MaxPubSubConnections child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxPubSubConnections();

  /**
   * Sets the Value of the MaxPubSubConnections child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxPubSubConnections(@Nullable UInteger value);

  /**
   * Returns the optional MaxPublishedDataSets child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxPublishedDataSetsNode();

  /**
   * Returns the Value of the MaxPublishedDataSets child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxPublishedDataSets();

  /**
   * Sets the Value of the MaxPublishedDataSets child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxPublishedDataSets(@Nullable UInteger value);

  /**
   * Returns the optional MaxPushTargets child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxPushTargetsNode();

  /**
   * Returns the Value of the MaxPushTargets child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxPushTargets();

  /**
   * Sets the Value of the MaxPushTargets child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxPushTargets(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxReaderGroups child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxReaderGroupsNode();

  /**
   * Returns the Value of the MaxReaderGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxReaderGroups();

  /**
   * Sets the Value of the MaxReaderGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxReaderGroups(@Nullable UInteger value);

  /**
   * Returns the optional MaxSecurityGroups child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxSecurityGroupsNode();

  /**
   * Returns the Value of the MaxSecurityGroups child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxSecurityGroups();

  /**
   * Sets the Value of the MaxSecurityGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxSecurityGroups(@Nullable UInteger value);

  /**
   * Returns the optional MaxStandaloneSubscribedDataSets child, a PropertyType with DataType
   * UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxStandaloneSubscribedDataSetsNode();

  /**
   * Returns the Value of the MaxStandaloneSubscribedDataSets child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxStandaloneSubscribedDataSets();

  /**
   * Sets the Value of the MaxStandaloneSubscribedDataSets child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxStandaloneSubscribedDataSets(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxWriterGroups child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxWriterGroupsNode();

  /**
   * Returns the Value of the MaxWriterGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxWriterGroups();

  /**
   * Sets the Value of the MaxWriterGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxWriterGroups(@Nullable UInteger value);

  /**
   * Returns the optional SupportSecurityKeyPull child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSupportSecurityKeyPullNode();

  /**
   * Returns the Value of the SupportSecurityKeyPull child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getSupportSecurityKeyPull();

  /**
   * Sets the Value of the SupportSecurityKeyPull child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportSecurityKeyPull(@Nullable Boolean value);

  /**
   * Returns the optional SupportSecurityKeyPush child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSupportSecurityKeyPushNode();

  /**
   * Returns the Value of the SupportSecurityKeyPush child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getSupportSecurityKeyPush();

  /**
   * Sets the Value of the SupportSecurityKeyPush child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportSecurityKeyPush(@Nullable Boolean value);

  /**
   * Returns the optional SupportSecurityKeyServer child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSupportSecurityKeyServerNode();

  /**
   * Returns the Value of the SupportSecurityKeyServer child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getSupportSecurityKeyServer();

  /**
   * Sets the Value of the SupportSecurityKeyServer child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportSecurityKeyServer(@Nullable Boolean value);
}
