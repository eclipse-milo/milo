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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.4">https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuthorizationServiceConfigurationType extends BaseObjectType {
  QualifiedProperty<String> SERVICE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<ByteString> SERVICE_CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceCertificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  QualifiedProperty<String> ISSUER_ENDPOINT_URL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IssuerEndpointUrl",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getServiceUri();

  /** Sets the existing node's local value. */
  void setServiceUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServiceUriNode();

  /** Gets the existing node's local value. */
  @Nullable ByteString getServiceCertificate();

  /** Sets the existing node's local value. */
  void setServiceCertificate(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServiceCertificateNode();

  /** Gets the existing node's local value. */
  @Nullable String getIssuerEndpointUrl();

  /** Sets the existing node's local value. */
  void setIssuerEndpointUrl(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIssuerEndpointUrlNode();
}
