package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the CertificateUpdatedAuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.27">Model
 *     documentation</a>
 */
public interface CertificateUpdatedAuditEventType extends AuditUpdateMethodEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12620L);

  /**
   * Returns the mandatory CertificateGroup child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCertificateGroupNode();

  /**
   * Returns the Value of the CertificateGroup child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getCertificateGroup();

  /**
   * Sets the Value of the CertificateGroup child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCertificateGroup(@Nullable NodeId value);

  /**
   * Returns the mandatory CertificateType child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCertificateTypeNode();

  /**
   * Returns the Value of the CertificateType child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getCertificateType();

  /**
   * Sets the Value of the CertificateType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCertificateType(@Nullable NodeId value);
}
