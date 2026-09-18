package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the HistoricalExternalEventSourceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2">Model
 *     documentation</a>
 */
public interface HistoricalExternalEventSourceType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32625L);

  /**
   * Returns the optional EndpointUrl child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEndpointUrlNode();

  /**
   * Returns the Value of the EndpointUrl child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getEndpointUrl();

  /**
   * Sets the Value of the EndpointUrl child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndpointUrl(@Nullable String value);

  /**
   * Returns the mandatory HistoricalEventFilter child, a PropertyType with DataType EventFilter.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getHistoricalEventFilterNode();

  /**
   * Returns the Value of the HistoricalEventFilter child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EventFilter getHistoricalEventFilter();

  /**
   * Sets the Value of the HistoricalEventFilter child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHistoricalEventFilter(@Nullable EventFilter value);

  /**
   * Returns the optional IdentityTokenPolicy child, a PropertyType with DataType UserTokenPolicy.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getIdentityTokenPolicyNode();

  /**
   * Returns the Value of the IdentityTokenPolicy child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UserTokenPolicy getIdentityTokenPolicy();

  /**
   * Sets the Value of the IdentityTokenPolicy child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIdentityTokenPolicy(@Nullable UserTokenPolicy value);

  /**
   * Returns the optional SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSecurityModeNode();

  /**
   * Returns the Value of the SecurityMode child.
   *
   * @return the value, or null if the child is absent or the Value is null.
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
   * Returns the optional SecurityPolicyUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSecurityPolicyUriNode();

  /**
   * Returns the Value of the SecurityPolicyUri child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityPolicyUri();

  /**
   * Sets the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the optional Server child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getServerNode();

  /**
   * Returns the Value of the Server child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getServer();

  /**
   * Sets the Value of the Server child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServer(@Nullable String value);

  /**
   * Returns the optional TransportProfileUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getTransportProfileUriNode();

  /**
   * Returns the Value of the TransportProfileUri child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getTransportProfileUri();

  /**
   * Sets the Value of the TransportProfileUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransportProfileUri(@Nullable String value);
}
