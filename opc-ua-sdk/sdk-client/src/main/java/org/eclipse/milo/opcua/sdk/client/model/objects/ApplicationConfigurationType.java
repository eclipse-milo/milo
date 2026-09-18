package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ApplicationConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14">Model
 *     documentation</a>
 */
public interface ApplicationConfigurationType extends ServerConfigurationType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25731L);

  QualifiedProperty<Boolean> IsNonUaApplication_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IsNonUaApplication",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> Enabled_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Enabled",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the mandatory ProductUri child, a PropertyType with DataType UriString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getProductUriNode() throws UaException;

  /** Asynchronous form of {@link #getProductUriNode()}. */
  CompletableFuture<? extends PropertyType> getProductUriNodeAsync();

  /**
   * Resolves the mandatory ApplicationUri child, a PropertyType with DataType UriString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getApplicationUriNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationUriNode()}. */
  CompletableFuture<? extends PropertyType> getApplicationUriNodeAsync();

  /**
   * Resolves the optional KeyCredentials child, a KeyCredentialConfigurationFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2">KeyCredentialConfigurationFolderType
   *     documentation</a>
   */
  @Nullable KeyCredentialConfigurationFolderType getKeyCredentialsNode() throws UaException;

  /** Asynchronous form of {@link #getKeyCredentialsNode()}. */
  CompletableFuture<? extends @Nullable KeyCredentialConfigurationFolderType>
      getKeyCredentialsNodeAsync();

  /**
   * Resolves the mandatory ApplicationType child, a PropertyType with DataType ApplicationType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getApplicationTypeNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationTypeNode()}. */
  CompletableFuture<? extends PropertyType> getApplicationTypeNodeAsync();

  /**
   * Resolves the optional IsNonUaApplication child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getIsNonUaApplicationNode() throws UaException;

  /** Asynchronous form of {@link #getIsNonUaApplicationNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getIsNonUaApplicationNodeAsync();

  /**
   * Reads the Value of the IsNonUaApplication child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIsNonUaApplication() throws UaException;

  /**
   * Writes the Value of the IsNonUaApplication child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIsNonUaApplication(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIsNonUaApplication()}. */
  CompletableFuture<? extends @Nullable Boolean> readIsNonUaApplicationAsync();

  /** Asynchronous form of {@link #writeIsNonUaApplication}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIsNonUaApplicationAsync(@Nullable Boolean value);

  /**
   * Resolves the optional AuthorizationServices child, a
   * AuthorizationServicesConfigurationFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.3">Model
   *     documentation</a>
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.2">AuthorizationServicesConfigurationFolderType
   *     documentation</a>
   */
  @Nullable AuthorizationServicesConfigurationFolderType getAuthorizationServicesNode()
      throws UaException;

  /** Asynchronous form of {@link #getAuthorizationServicesNode()}. */
  CompletableFuture<? extends @Nullable AuthorizationServicesConfigurationFolderType>
      getAuthorizationServicesNodeAsync();

  /**
   * Resolves the mandatory Enabled child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEnabledNode() throws UaException;

  /** Asynchronous form of {@link #getEnabledNode()}. */
  CompletableFuture<? extends PropertyType> getEnabledNodeAsync();

  /**
   * Reads the Value of the Enabled child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readEnabled() throws UaException;

  /**
   * Writes the Value of the Enabled child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEnabled(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readEnabled()}. */
  CompletableFuture<? extends @Nullable Boolean> readEnabledAsync();

  /** Asynchronous form of {@link #writeEnabled}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEnabledAsync(@Nullable Boolean value);
}
