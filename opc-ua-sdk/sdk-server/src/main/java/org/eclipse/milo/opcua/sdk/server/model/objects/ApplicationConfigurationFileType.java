package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ApplicationConfigurationFileType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.20">Model
 *     documentation</a>
 */
public interface ApplicationConfigurationFileType extends ConfigurationFileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15550L);

  /**
   * Returns the mandatory AvailableNetworks child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAvailableNetworksNode();

  /**
   * Returns the Value of the AvailableNetworks child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getAvailableNetworks();

  /**
   * Sets the Value of the AvailableNetworks child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAvailableNetworks(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory AvailablePorts child, a PropertyType with DataType NumericRange.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAvailablePortsNode();

  /**
   * Returns the Value of the AvailablePorts child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getAvailablePorts();

  /**
   * Sets the Value of the AvailablePorts child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAvailablePorts(@Nullable String value);

  /**
   * Returns the mandatory CertificateGroupPurposes child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6">Model
   *     documentation</a>
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCertificateGroupPurposesNode();

  /**
   * Returns the Value of the CertificateGroupPurposes child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getCertificateGroupPurposes();

  /**
   * Sets the Value of the CertificateGroupPurposes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCertificateGroupPurposes(NodeId @Nullable [] value);

  /**
   * Returns the mandatory CertificateTypes child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCertificateTypesNode();

  /**
   * Returns the Value of the CertificateTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getCertificateTypes();

  /**
   * Sets the Value of the CertificateTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCertificateTypes(NodeId @Nullable [] value);

  /**
   * Returns the mandatory MaxCertificateGroups child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxCertificateGroupsNode();

  /**
   * Returns the Value of the MaxCertificateGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxCertificateGroups();

  /**
   * Sets the Value of the MaxCertificateGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxCertificateGroups(@Nullable UShort value);

  /**
   * Returns the mandatory MaxEndpoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxEndpointsNode();

  /**
   * Returns the Value of the MaxEndpoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxEndpoints();

  /**
   * Sets the Value of the MaxEndpoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxEndpoints(@Nullable UShort value);

  /**
   * Returns the mandatory SecurityPolicyUris child, a PropertyType with DataType UriString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecurityPolicyUrisNode();

  /**
   * Returns the Value of the SecurityPolicyUris child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getSecurityPolicyUris();

  /**
   * Sets the Value of the SecurityPolicyUris child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityPolicyUris(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory UserTokenTypes child, a PropertyType with DataType UserTokenPolicy.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUserTokenTypesNode();

  /**
   * Returns the Value of the UserTokenTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UserTokenPolicy @Nullable [] getUserTokenTypes();

  /**
   * Sets the Value of the UserTokenTypes child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUserTokenTypes(@Nullable UserTokenPolicy @Nullable [] value);

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
  interface Methods extends ConfigurationFileType.Methods {}
}
