package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ApplicationConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14">Model
 *     documentation</a>
 */
public interface ApplicationConfigurationType extends ServerConfigurationType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25731L);

  /**
   * Returns the mandatory ApplicationType child, a PropertyType with DataType ApplicationType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getApplicationTypeNode();

  /**
   * Returns the mandatory ApplicationUri child, a PropertyType with DataType UriString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getApplicationUriNode();

  /**
   * Returns the optional AuthorizationServices child, a
   * AuthorizationServicesConfigurationFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.3">Model
   *     documentation</a>
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.2">AuthorizationServicesConfigurationFolderType
   *     documentation</a>
   */
  @Nullable AuthorizationServicesConfigurationFolderTypeNode getAuthorizationServicesNode();

  /**
   * Returns the mandatory Enabled child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEnabledNode();

  /**
   * Returns the Value of the Enabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getEnabled();

  /**
   * Sets the Value of the Enabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEnabled(@Nullable Boolean value);

  /**
   * Returns the optional IsNonUaApplication child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getIsNonUaApplicationNode();

  /**
   * Returns the Value of the IsNonUaApplication child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIsNonUaApplication();

  /**
   * Sets the Value of the IsNonUaApplication child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIsNonUaApplication(@Nullable Boolean value);

  /**
   * Returns the optional KeyCredentials child, a KeyCredentialConfigurationFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2">KeyCredentialConfigurationFolderType
   *     documentation</a>
   */
  @Nullable KeyCredentialConfigurationFolderTypeNode getKeyCredentialsNode();

  /**
   * Returns the mandatory ProductUri child, a PropertyType with DataType UriString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getProductUriNode();

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends ServerConfigurationType.Methods {}
}
