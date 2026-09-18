package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the RoleType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.1">Model
 *     documentation</a>
 */
public interface RoleType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15620L);

  QualifiedProperty<IdentityMappingRuleType[]> Identities_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Identities",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15634L),
          1,
          IdentityMappingRuleType[].class);

  QualifiedProperty<String[]> Applications_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Applications",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  QualifiedProperty<Boolean> EndpointsExclude_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndpointsExclude",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> ApplicationsExclude_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ApplicationsExclude",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> CustomConfiguration_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CustomConfiguration",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<EndpointType[]> Endpoints_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Endpoints",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15528L),
          1,
          EndpointType[].class);

  /**
   * Resolves the mandatory Identities child, a PropertyType with DataType IdentityMappingRuleType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIdentitiesNode() throws UaException;

  /** Asynchronous form of {@link #getIdentitiesNode()}. */
  CompletableFuture<? extends PropertyType> getIdentitiesNodeAsync();

  /**
   * Reads the Value of the Identities child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable IdentityMappingRuleType @Nullable [] readIdentities() throws UaException;

  /**
   * Writes the Value of the Identities child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIdentities(@Nullable IdentityMappingRuleType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readIdentities()}. */
  CompletableFuture<? extends @Nullable IdentityMappingRuleType @Nullable []> readIdentitiesAsync();

  /** Asynchronous form of {@link #writeIdentities}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIdentitiesAsync(
      @Nullable IdentityMappingRuleType @Nullable [] value);

  /**
   * Resolves the optional Applications child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getApplicationsNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationsNodeAsync();

  /**
   * Reads the Value of the Applications child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readApplications() throws UaException;

  /**
   * Writes the Value of the Applications child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeApplications(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readApplications()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readApplicationsAsync();

  /** Asynchronous form of {@link #writeApplications}; completes with the operation status. */
  CompletableFuture<StatusCode> writeApplicationsAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the optional EndpointsExclude child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEndpointsExcludeNode() throws UaException;

  /** Asynchronous form of {@link #getEndpointsExcludeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointsExcludeNodeAsync();

  /**
   * Reads the Value of the EndpointsExclude child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readEndpointsExclude() throws UaException;

  /**
   * Writes the Value of the EndpointsExclude child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndpointsExclude(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readEndpointsExclude()}. */
  CompletableFuture<? extends @Nullable Boolean> readEndpointsExcludeAsync();

  /** Asynchronous form of {@link #writeEndpointsExclude}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndpointsExcludeAsync(@Nullable Boolean value);

  /**
   * Resolves the optional ApplicationsExclude child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getApplicationsExcludeNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationsExcludeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getApplicationsExcludeNodeAsync();

  /**
   * Reads the Value of the ApplicationsExclude child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readApplicationsExclude() throws UaException;

  /**
   * Writes the Value of the ApplicationsExclude child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeApplicationsExclude(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readApplicationsExclude()}. */
  CompletableFuture<? extends @Nullable Boolean> readApplicationsExcludeAsync();

  /**
   * Asynchronous form of {@link #writeApplicationsExclude}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeApplicationsExcludeAsync(@Nullable Boolean value);

  /**
   * Resolves the optional CustomConfiguration child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCustomConfigurationNode() throws UaException;

  /** Asynchronous form of {@link #getCustomConfigurationNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCustomConfigurationNodeAsync();

  /**
   * Reads the Value of the CustomConfiguration child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readCustomConfiguration() throws UaException;

  /**
   * Writes the Value of the CustomConfiguration child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCustomConfiguration(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readCustomConfiguration()}. */
  CompletableFuture<? extends @Nullable Boolean> readCustomConfigurationAsync();

  /**
   * Asynchronous form of {@link #writeCustomConfiguration}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeCustomConfigurationAsync(@Nullable Boolean value);

  /**
   * Resolves the optional Endpoints child, a PropertyType with DataType EndpointType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEndpointsNode() throws UaException;

  /** Asynchronous form of {@link #getEndpointsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointsNodeAsync();

  /**
   * Reads the Value of the Endpoints child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EndpointType @Nullable [] readEndpoints() throws UaException;

  /**
   * Writes the Value of the Endpoints child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndpoints(@Nullable EndpointType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readEndpoints()}. */
  CompletableFuture<? extends @Nullable EndpointType @Nullable []> readEndpointsAsync();

  /** Asynchronous form of {@link #writeEndpoints}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndpointsAsync(@Nullable EndpointType @Nullable [] value);

  /**
   * Resolves the optional AddApplication Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddApplicationMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddApplicationMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddApplicationMethodNodeAsync();

  /**
   * Calls the AddApplication Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.7">Model
   *     documentation</a>
   */
  void addApplication(@Nullable String applicationUri) throws UaException;

  /**
   * Calls the AddApplication Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddApplication(@Nullable String applicationUri) throws UaException;

  /**
   * Calls the AddApplication Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddApplicationWith(
      MethodCallOptions options, @Nullable String applicationUri) throws UaException;

  /** Asynchronous form of {@link #addApplication}. */
  CompletableFuture<Void> addApplicationAsync(@Nullable String applicationUri);

  /** Asynchronous form of {@link #callAddApplication}. */
  CompletableFuture<MethodCallResult<Void>> callAddApplicationAsync(
      @Nullable String applicationUri);

  /** Asynchronous form of {@link #callAddApplicationWith}. */
  CompletableFuture<MethodCallResult<Void>> callAddApplicationWithAsync(
      MethodCallOptions options, @Nullable String applicationUri);

  /**
   * Resolves the optional AddEndpoint Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddEndpointMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddEndpointMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddEndpointMethodNodeAsync();

  /**
   * Calls the AddEndpoint Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.9">Model
   *     documentation</a>
   */
  void addEndpoint(@Nullable EndpointType endpoint) throws UaException;

  /**
   * Calls the AddEndpoint Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddEndpoint(@Nullable EndpointType endpoint) throws UaException;

  /**
   * Calls the AddEndpoint Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddEndpointWith(
      MethodCallOptions options, @Nullable EndpointType endpoint) throws UaException;

  /** Asynchronous form of {@link #addEndpoint}. */
  CompletableFuture<Void> addEndpointAsync(@Nullable EndpointType endpoint);

  /** Asynchronous form of {@link #callAddEndpoint}. */
  CompletableFuture<MethodCallResult<Void>> callAddEndpointAsync(@Nullable EndpointType endpoint);

  /** Asynchronous form of {@link #callAddEndpointWith}. */
  CompletableFuture<MethodCallResult<Void>> callAddEndpointWithAsync(
      MethodCallOptions options, @Nullable EndpointType endpoint);

  /**
   * Resolves the optional AddIdentity Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddIdentityMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddIdentityMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddIdentityMethodNodeAsync();

  /**
   * Calls the AddIdentity Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.5">Model
   *     documentation</a>
   */
  void addIdentity(@Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * Calls the AddIdentity Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddIdentity(@Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * Calls the AddIdentity Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callAddIdentityWith(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) throws UaException;

  /** Asynchronous form of {@link #addIdentity}. */
  CompletableFuture<Void> addIdentityAsync(@Nullable IdentityMappingRuleType rule);

  /** Asynchronous form of {@link #callAddIdentity}. */
  CompletableFuture<MethodCallResult<Void>> callAddIdentityAsync(
      @Nullable IdentityMappingRuleType rule);

  /** Asynchronous form of {@link #callAddIdentityWith}. */
  CompletableFuture<MethodCallResult<Void>> callAddIdentityWithAsync(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule);

  /**
   * Resolves the optional RemoveApplication Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveApplicationMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveApplicationMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveApplicationMethodNodeAsync();

  /**
   * Calls the RemoveApplication Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.8">Model
   *     documentation</a>
   */
  void removeApplication(@Nullable String applicationUri) throws UaException;

  /**
   * Calls the RemoveApplication Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveApplication(@Nullable String applicationUri) throws UaException;

  /**
   * Calls the RemoveApplication Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveApplicationWith(
      MethodCallOptions options, @Nullable String applicationUri) throws UaException;

  /** Asynchronous form of {@link #removeApplication}. */
  CompletableFuture<Void> removeApplicationAsync(@Nullable String applicationUri);

  /** Asynchronous form of {@link #callRemoveApplication}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveApplicationAsync(
      @Nullable String applicationUri);

  /** Asynchronous form of {@link #callRemoveApplicationWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveApplicationWithAsync(
      MethodCallOptions options, @Nullable String applicationUri);

  /**
   * Resolves the optional RemoveEndpoint Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveEndpointMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveEndpointMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveEndpointMethodNodeAsync();

  /**
   * Calls the RemoveEndpoint Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.10">Model
   *     documentation</a>
   */
  void removeEndpoint(@Nullable EndpointType endpoint) throws UaException;

  /**
   * Calls the RemoveEndpoint Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveEndpoint(@Nullable EndpointType endpoint) throws UaException;

  /**
   * Calls the RemoveEndpoint Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveEndpointWith(
      MethodCallOptions options, @Nullable EndpointType endpoint) throws UaException;

  /** Asynchronous form of {@link #removeEndpoint}. */
  CompletableFuture<Void> removeEndpointAsync(@Nullable EndpointType endpoint);

  /** Asynchronous form of {@link #callRemoveEndpoint}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveEndpointAsync(
      @Nullable EndpointType endpoint);

  /** Asynchronous form of {@link #callRemoveEndpointWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveEndpointWithAsync(
      MethodCallOptions options, @Nullable EndpointType endpoint);

  /**
   * Resolves the optional RemoveIdentity Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveIdentityMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveIdentityMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveIdentityMethodNodeAsync();

  /**
   * Calls the RemoveIdentity Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.6">Model
   *     documentation</a>
   */
  void removeIdentity(@Nullable IdentityMappingRuleType rule) throws UaException;

  /**
   * Calls the RemoveIdentity Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveIdentity(@Nullable IdentityMappingRuleType rule)
      throws UaException;

  /**
   * Calls the RemoveIdentity Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveIdentityWith(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule) throws UaException;

  /** Asynchronous form of {@link #removeIdentity}. */
  CompletableFuture<Void> removeIdentityAsync(@Nullable IdentityMappingRuleType rule);

  /** Asynchronous form of {@link #callRemoveIdentity}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveIdentityAsync(
      @Nullable IdentityMappingRuleType rule);

  /** Asynchronous form of {@link #callRemoveIdentityWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveIdentityWithAsync(
      MethodCallOptions options, @Nullable IdentityMappingRuleType rule);
}
