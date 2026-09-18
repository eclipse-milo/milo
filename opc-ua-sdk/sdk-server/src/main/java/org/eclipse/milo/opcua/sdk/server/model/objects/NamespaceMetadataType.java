package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the NamespaceMetadataType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.13">Model
 *     documentation</a>
 */
public interface NamespaceMetadataType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11616L);

  /**
   * Returns the optional ConfigurationVersion child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConfigurationVersionNode();

  /**
   * Returns the Value of the ConfigurationVersion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getConfigurationVersion();

  /**
   * Sets the Value of the ConfigurationVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConfigurationVersion(@Nullable UInteger value);

  /**
   * Returns the optional DefaultAccessRestrictions child, a PropertyType with DataType
   * AccessRestrictionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefaultAccessRestrictionsNode();

  /**
   * Returns the Value of the DefaultAccessRestrictions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AccessRestrictionType getDefaultAccessRestrictions();

  /**
   * Sets the Value of the DefaultAccessRestrictions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefaultAccessRestrictions(@Nullable AccessRestrictionType value);

  /**
   * Returns the optional DefaultRolePermissions child, a PropertyType with DataType
   * RolePermissionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefaultRolePermissionsNode();

  /**
   * Returns the Value of the DefaultRolePermissions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable RolePermissionType @Nullable [] getDefaultRolePermissions();

  /**
   * Sets the Value of the DefaultRolePermissions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefaultRolePermissions(@Nullable RolePermissionType @Nullable [] value);

  /**
   * Returns the optional DefaultUserRolePermissions child, a PropertyType with DataType
   * RolePermissionType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefaultUserRolePermissionsNode();

  /**
   * Returns the Value of the DefaultUserRolePermissions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable RolePermissionType @Nullable [] getDefaultUserRolePermissions();

  /**
   * Sets the Value of the DefaultUserRolePermissions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefaultUserRolePermissions(@Nullable RolePermissionType @Nullable [] value);

  /**
   * Returns the mandatory IsNamespaceSubset child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIsNamespaceSubsetNode();

  /**
   * Returns the Value of the IsNamespaceSubset child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIsNamespaceSubset();

  /**
   * Sets the Value of the IsNamespaceSubset child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIsNamespaceSubset(@Nullable Boolean value);

  /**
   * Returns the optional ModelVersion child, a PropertyType with DataType SemanticVersionString.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getModelVersionNode();

  /**
   * Returns the Value of the ModelVersion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getModelVersion();

  /**
   * Sets the Value of the ModelVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setModelVersion(@Nullable String value);

  /**
   * Returns the optional NamespaceFile child, a AddressSpaceFileType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.12">AddressSpaceFileType
   *     documentation</a>
   */
  @Nullable AddressSpaceFileTypeNode getNamespaceFileNode();

  /**
   * Returns the mandatory NamespacePublicationDate child, a PropertyType with DataType DateTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNamespacePublicationDateNode();

  /**
   * Returns the Value of the NamespacePublicationDate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getNamespacePublicationDate();

  /**
   * Sets the Value of the NamespacePublicationDate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNamespacePublicationDate(@Nullable DateTime value);

  /**
   * Returns the mandatory NamespaceUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNamespaceUriNode();

  /**
   * Returns the Value of the NamespaceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getNamespaceUri();

  /**
   * Sets the Value of the NamespaceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNamespaceUri(@Nullable String value);

  /**
   * Returns the mandatory NamespaceVersion child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNamespaceVersionNode();

  /**
   * Returns the Value of the NamespaceVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getNamespaceVersion();

  /**
   * Sets the Value of the NamespaceVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNamespaceVersion(@Nullable String value);

  /**
   * Returns the mandatory StaticNodeIdTypes child, a PropertyType with DataType IdType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStaticNodeIdTypesNode();

  /**
   * Returns the Value of the StaticNodeIdTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  IdType @Nullable [] getStaticNodeIdTypes();

  /**
   * Sets the Value of the StaticNodeIdTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStaticNodeIdTypes(IdType @Nullable [] value);

  /**
   * Returns the mandatory StaticNumericNodeIdRange child, a PropertyType with DataType
   * NumericRange.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStaticNumericNodeIdRangeNode();

  /**
   * Returns the Value of the StaticNumericNodeIdRange child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getStaticNumericNodeIdRange();

  /**
   * Sets the Value of the StaticNumericNodeIdRange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStaticNumericNodeIdRange(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory StaticStringNodeIdPattern child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStaticStringNodeIdPatternNode();

  /**
   * Returns the Value of the StaticStringNodeIdPattern child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getStaticStringNodeIdPattern();

  /**
   * Sets the Value of the StaticStringNodeIdPattern child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStaticStringNodeIdPattern(@Nullable String value);
}
