package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NamespaceMetadataType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13">Model
 *     documentation</a>
 */
public interface NamespaceMetadataType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11616L);

  QualifiedProperty<String> ModelVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ModelVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24263L),
          -1,
          String.class);

  QualifiedProperty<String> NamespaceUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NamespaceUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> NamespaceVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NamespaceVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Boolean> IsNamespaceSubset_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IsNamespaceSubset",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<IdType[]> StaticNodeIdTypes_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StaticNodeIdTypes",
          ExpandedNodeId.of(Namespaces.OPC_UA, 256L),
          1,
          IdType[].class);

  QualifiedProperty<UInteger> ConfigurationVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConfigurationVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  QualifiedProperty<RolePermissionType[]> DefaultRolePermissions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DefaultRolePermissions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 96L),
          1,
          RolePermissionType[].class);

  QualifiedProperty<DateTime> NamespacePublicationDate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NamespacePublicationDate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
          -1,
          DateTime.class);

  QualifiedProperty<String[]> StaticNumericNodeIdRange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StaticNumericNodeIdRange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 291L),
          1,
          String[].class);

  QualifiedProperty<AccessRestrictionType> DefaultAccessRestrictions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DefaultAccessRestrictions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 95L),
          -1,
          AccessRestrictionType.class);

  QualifiedProperty<String> StaticStringNodeIdPattern_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StaticStringNodeIdPattern",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<RolePermissionType[]> DefaultUserRolePermissions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DefaultUserRolePermissions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 96L),
          1,
          RolePermissionType[].class);

  /**
   * Resolves the optional ModelVersion child, a PropertyType with DataType SemanticVersionString.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getModelVersionNode() throws UaException;

  /** Asynchronous form of {@link #getModelVersionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getModelVersionNodeAsync();

  /**
   * Reads the Value of the ModelVersion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readModelVersion() throws UaException;

  /**
   * Writes the Value of the ModelVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeModelVersion(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readModelVersion()}. */
  CompletableFuture<? extends @Nullable String> readModelVersionAsync();

  /** Asynchronous form of {@link #writeModelVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeModelVersionAsync(@Nullable String value);

  /**
   * Resolves the mandatory NamespaceUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNamespaceUriNode() throws UaException;

  /** Asynchronous form of {@link #getNamespaceUriNode()}. */
  CompletableFuture<? extends PropertyType> getNamespaceUriNodeAsync();

  /**
   * Reads the Value of the NamespaceUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readNamespaceUri() throws UaException;

  /**
   * Writes the Value of the NamespaceUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNamespaceUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readNamespaceUri()}. */
  CompletableFuture<? extends @Nullable String> readNamespaceUriAsync();

  /** Asynchronous form of {@link #writeNamespaceUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNamespaceUriAsync(@Nullable String value);

  /**
   * Resolves the optional NamespaceFile child, a AddressSpaceFileType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.12">AddressSpaceFileType
   *     documentation</a>
   */
  @Nullable AddressSpaceFileType getNamespaceFileNode() throws UaException;

  /** Asynchronous form of {@link #getNamespaceFileNode()}. */
  CompletableFuture<? extends @Nullable AddressSpaceFileType> getNamespaceFileNodeAsync();

  /**
   * Resolves the mandatory NamespaceVersion child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNamespaceVersionNode() throws UaException;

  /** Asynchronous form of {@link #getNamespaceVersionNode()}. */
  CompletableFuture<? extends PropertyType> getNamespaceVersionNodeAsync();

  /**
   * Reads the Value of the NamespaceVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readNamespaceVersion() throws UaException;

  /**
   * Writes the Value of the NamespaceVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNamespaceVersion(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readNamespaceVersion()}. */
  CompletableFuture<? extends @Nullable String> readNamespaceVersionAsync();

  /** Asynchronous form of {@link #writeNamespaceVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNamespaceVersionAsync(@Nullable String value);

  /**
   * Resolves the mandatory IsNamespaceSubset child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIsNamespaceSubsetNode() throws UaException;

  /** Asynchronous form of {@link #getIsNamespaceSubsetNode()}. */
  CompletableFuture<? extends PropertyType> getIsNamespaceSubsetNodeAsync();

  /**
   * Reads the Value of the IsNamespaceSubset child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readIsNamespaceSubset() throws UaException;

  /**
   * Writes the Value of the IsNamespaceSubset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIsNamespaceSubset(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readIsNamespaceSubset()}. */
  CompletableFuture<? extends @Nullable Boolean> readIsNamespaceSubsetAsync();

  /** Asynchronous form of {@link #writeIsNamespaceSubset}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIsNamespaceSubsetAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory StaticNodeIdTypes child, a PropertyType with DataType IdType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStaticNodeIdTypesNode() throws UaException;

  /** Asynchronous form of {@link #getStaticNodeIdTypesNode()}. */
  CompletableFuture<? extends PropertyType> getStaticNodeIdTypesNodeAsync();

  /**
   * Reads the Value of the StaticNodeIdTypes child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  IdType @Nullable [] readStaticNodeIdTypes() throws UaException;

  /**
   * Writes the Value of the StaticNodeIdTypes child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStaticNodeIdTypes(IdType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readStaticNodeIdTypes()}. */
  CompletableFuture<? extends IdType @Nullable []> readStaticNodeIdTypesAsync();

  /** Asynchronous form of {@link #writeStaticNodeIdTypes}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStaticNodeIdTypesAsync(IdType @Nullable [] value);

  /**
   * Resolves the optional ConfigurationVersion child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConfigurationVersionNode() throws UaException;

  /** Asynchronous form of {@link #getConfigurationVersionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConfigurationVersionNodeAsync();

  /**
   * Reads the Value of the ConfigurationVersion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readConfigurationVersion() throws UaException;

  /**
   * Writes the Value of the ConfigurationVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConfigurationVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readConfigurationVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readConfigurationVersionAsync();

  /**
   * Asynchronous form of {@link #writeConfigurationVersion}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(@Nullable UInteger value);

  /**
   * Resolves the optional DefaultRolePermissions child, a PropertyType with DataType
   * RolePermissionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDefaultRolePermissionsNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultRolePermissionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultRolePermissionsNodeAsync();

  /**
   * Reads the Value of the DefaultRolePermissions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable RolePermissionType @Nullable [] readDefaultRolePermissions() throws UaException;

  /**
   * Writes the Value of the DefaultRolePermissions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readDefaultRolePermissions()}. */
  CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultRolePermissionsAsync();

  /**
   * Asynchronous form of {@link #writeDefaultRolePermissions}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDefaultRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] value);

  /**
   * Resolves the mandatory NamespacePublicationDate child, a PropertyType with DataType DateTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNamespacePublicationDateNode() throws UaException;

  /** Asynchronous form of {@link #getNamespacePublicationDateNode()}. */
  CompletableFuture<? extends PropertyType> getNamespacePublicationDateNodeAsync();

  /**
   * Reads the Value of the NamespacePublicationDate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readNamespacePublicationDate() throws UaException;

  /**
   * Writes the Value of the NamespacePublicationDate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNamespacePublicationDate(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readNamespacePublicationDate()}. */
  CompletableFuture<? extends @Nullable DateTime> readNamespacePublicationDateAsync();

  /**
   * Asynchronous form of {@link #writeNamespacePublicationDate}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeNamespacePublicationDateAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory StaticNumericNodeIdRange child, a PropertyType with DataType
   * NumericRange.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStaticNumericNodeIdRangeNode() throws UaException;

  /** Asynchronous form of {@link #getStaticNumericNodeIdRangeNode()}. */
  CompletableFuture<? extends PropertyType> getStaticNumericNodeIdRangeNodeAsync();

  /**
   * Reads the Value of the StaticNumericNodeIdRange child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readStaticNumericNodeIdRange() throws UaException;

  /**
   * Writes the Value of the StaticNumericNodeIdRange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStaticNumericNodeIdRange(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readStaticNumericNodeIdRange()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readStaticNumericNodeIdRangeAsync();

  /**
   * Asynchronous form of {@link #writeStaticNumericNodeIdRange}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeStaticNumericNodeIdRangeAsync(
      @Nullable String @Nullable [] value);

  /**
   * Resolves the optional DefaultAccessRestrictions child, a PropertyType with DataType
   * AccessRestrictionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDefaultAccessRestrictionsNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultAccessRestrictionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultAccessRestrictionsNodeAsync();

  /**
   * Reads the Value of the DefaultAccessRestrictions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AccessRestrictionType readDefaultAccessRestrictions() throws UaException;

  /**
   * Writes the Value of the DefaultAccessRestrictions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefaultAccessRestrictions(@Nullable AccessRestrictionType value) throws UaException;

  /** Asynchronous form of {@link #readDefaultAccessRestrictions()}. */
  CompletableFuture<? extends @Nullable AccessRestrictionType> readDefaultAccessRestrictionsAsync();

  /**
   * Asynchronous form of {@link #writeDefaultAccessRestrictions}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDefaultAccessRestrictionsAsync(
      @Nullable AccessRestrictionType value);

  /**
   * Resolves the mandatory StaticStringNodeIdPattern child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStaticStringNodeIdPatternNode() throws UaException;

  /** Asynchronous form of {@link #getStaticStringNodeIdPatternNode()}. */
  CompletableFuture<? extends PropertyType> getStaticStringNodeIdPatternNodeAsync();

  /**
   * Reads the Value of the StaticStringNodeIdPattern child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readStaticStringNodeIdPattern() throws UaException;

  /**
   * Writes the Value of the StaticStringNodeIdPattern child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStaticStringNodeIdPattern(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readStaticStringNodeIdPattern()}. */
  CompletableFuture<? extends @Nullable String> readStaticStringNodeIdPatternAsync();

  /**
   * Asynchronous form of {@link #writeStaticStringNodeIdPattern}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeStaticStringNodeIdPatternAsync(@Nullable String value);

  /**
   * Resolves the optional DefaultUserRolePermissions child, a PropertyType with DataType
   * RolePermissionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDefaultUserRolePermissionsNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultUserRolePermissionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultUserRolePermissionsNodeAsync();

  /**
   * Reads the Value of the DefaultUserRolePermissions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable RolePermissionType @Nullable [] readDefaultUserRolePermissions() throws UaException;

  /**
   * Writes the Value of the DefaultUserRolePermissions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readDefaultUserRolePermissions()}. */
  CompletableFuture<? extends @Nullable RolePermissionType @Nullable []>
      readDefaultUserRolePermissionsAsync();

  /**
   * Asynchronous form of {@link #writeDefaultUserRolePermissions}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDefaultUserRolePermissionsAsync(
      @Nullable RolePermissionType @Nullable [] value);
}
