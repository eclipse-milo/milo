package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.2">Model
 *     documentation</a>
 */
public interface PubSubGroupType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14232L);

  /**
   * Returns the mandatory GroupProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getGroupPropertiesNode();

  /**
   * Returns the Value of the GroupProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable KeyValuePair @Nullable [] getGroupProperties();

  /**
   * Sets the Value of the GroupProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setGroupProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the mandatory MaxNetworkMessageSize child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxNetworkMessageSizeNode();

  /**
   * Returns the Value of the MaxNetworkMessageSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxNetworkMessageSize();

  /**
   * Sets the Value of the MaxNetworkMessageSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxNetworkMessageSize(@Nullable UInteger value);

  /**
   * Returns the optional SecurityGroupId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSecurityGroupIdNode();

  /**
   * Returns the Value of the SecurityGroupId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityGroupId();

  /**
   * Sets the Value of the SecurityGroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityGroupId(@Nullable String value);

  /**
   * Returns the optional SecurityKeyServices child, a PropertyType with DataType
   * EndpointDescription.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSecurityKeyServicesNode();

  /**
   * Returns the Value of the SecurityKeyServices child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EndpointDescription @Nullable [] getSecurityKeyServices();

  /**
   * Sets the Value of the SecurityKeyServices child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the mandatory SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecurityModeNode();

  /**
   * Returns the Value of the SecurityMode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable MessageSecurityMode getSecurityMode();

  /**
   * Sets the Value of the SecurityMode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusTypeNode getStatusNode();
}
