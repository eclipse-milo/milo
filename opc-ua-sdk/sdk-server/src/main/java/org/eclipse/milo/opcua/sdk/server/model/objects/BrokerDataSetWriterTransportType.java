/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BrokerDataSetWriterTransportType extends DataSetWriterTransportType {
  QualifiedProperty<String> QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QueueName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> META_DATA_QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MetaDataQueueName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AUTHENTICATION_PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AuthenticationProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<BrokerTransportQualityOfService> REQUESTED_DELIVERY_GUARANTEE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestedDeliveryGuarantee",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15008"),
          -1,
          BrokerTransportQualityOfService.class);

  QualifiedProperty<Double> META_DATA_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MetaDataUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable String getQueueName();

  /** Sets the existing node's local value. */
  void setQueueName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getQueueNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getMetaDataQueueName();

  /** Sets the existing node's local value. */
  void setMetaDataQueueName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMetaDataQueueNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getResourceUri();

  /** Sets the existing node's local value. */
  void setResourceUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResourceUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getAuthenticationProfileUri();

  /** Sets the existing node's local value. */
  void setAuthenticationProfileUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAuthenticationProfileUriNode();

  /** Gets the existing node's local value. */
  @Nullable BrokerTransportQualityOfService getRequestedDeliveryGuarantee();

  /** Sets the existing node's local value. */
  void setRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestedDeliveryGuaranteeNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMetaDataUpdateTime();

  /** Sets the existing node's local value. */
  void setMetaDataUpdateTime(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMetaDataUpdateTimeNode();
}
