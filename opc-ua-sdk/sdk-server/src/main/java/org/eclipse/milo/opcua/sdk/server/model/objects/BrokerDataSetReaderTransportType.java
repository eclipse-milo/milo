/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.4</a>
 */
public interface BrokerDataSetReaderTransportType extends DataSetReaderTransportType {
  QualifiedProperty<String> QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QueueName",
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

  QualifiedProperty<String> META_DATA_QUEUE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MetaDataQueueName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  String getQueueName();

  void setQueueName(String value);

  PropertyType getQueueNameNode();

  String getResourceUri();

  void setResourceUri(String value);

  PropertyType getResourceUriNode();

  String getAuthenticationProfileUri();

  void setAuthenticationProfileUri(String value);

  PropertyType getAuthenticationProfileUriNode();

  BrokerTransportQualityOfService getRequestedDeliveryGuarantee();

  void setRequestedDeliveryGuarantee(BrokerTransportQualityOfService value);

  PropertyType getRequestedDeliveryGuaranteeNode();

  String getMetaDataQueueName();

  void setMetaDataQueueName(String value);

  PropertyType getMetaDataQueueNameNode();
}
