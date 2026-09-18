package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the BrokerWriterGroupTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.2">Model
 *     documentation</a>
 */
public interface BrokerWriterGroupTransportType extends WriterGroupTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21136L);

  /**
   * Returns the mandatory AuthenticationProfileUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAuthenticationProfileUriNode();

  /**
   * Returns the Value of the AuthenticationProfileUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getAuthenticationProfileUri();

  /**
   * Sets the Value of the AuthenticationProfileUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAuthenticationProfileUri(@Nullable String value);

  /**
   * Returns the mandatory QueueName child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getQueueNameNode();

  /**
   * Returns the Value of the QueueName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getQueueName();

  /**
   * Sets the Value of the QueueName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setQueueName(@Nullable String value);

  /**
   * Returns the mandatory RequestedDeliveryGuarantee child, a PropertyType with DataType
   * BrokerTransportQualityOfService.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRequestedDeliveryGuaranteeNode();

  /**
   * Returns the Value of the RequestedDeliveryGuarantee child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable BrokerTransportQualityOfService getRequestedDeliveryGuarantee();

  /**
   * Sets the Value of the RequestedDeliveryGuarantee child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value);

  /**
   * Returns the mandatory ResourceUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getResourceUriNode();

  /**
   * Returns the Value of the ResourceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getResourceUri();

  /**
   * Sets the Value of the ResourceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setResourceUri(@Nullable String value);
}
