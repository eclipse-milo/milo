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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ApplicationConfigurationType extends ServerConfigurationType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<String> PRODUCT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProductUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<ApplicationType> APPLICATION_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=307"),
          -1,
          ApplicationType.class);

  QualifiedProperty<Boolean> ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Enabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> IS_NON_UA_APPLICATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsNonUaApplication",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable String getApplicationUri();

  /** Sets the existing node's local value. */
  void setApplicationUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getApplicationUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getProductUri();

  /** Sets the existing node's local value. */
  void setProductUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getProductUriNode();

  /** Gets the existing node's local value. */
  @Nullable ApplicationType getApplicationType();

  /** Sets the existing node's local value. */
  void setApplicationType(@Nullable ApplicationType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getApplicationTypeNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getEnabled();

  /** Sets the existing node's local value. */
  void setEnabled(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEnabledNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getIsNonUaApplication();

  /** Sets the existing node's local value. */
  void setIsNonUaApplication(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIsNonUaApplicationNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable KeyCredentialConfigurationFolderType getKeyCredentialsNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AuthorizationServicesConfigurationFolderType getAuthorizationServicesNode();
}
