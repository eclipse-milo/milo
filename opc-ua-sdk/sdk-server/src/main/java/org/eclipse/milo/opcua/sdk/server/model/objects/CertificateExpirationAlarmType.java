package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the CertificateExpirationAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">Model
 *     documentation</a>
 */
public interface CertificateExpirationAlarmType extends SystemOffNormalAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 13225L);

  /**
   * Returns the mandatory Certificate child, a PropertyType with DataType ByteString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCertificateNode();

  /**
   * Returns the Value of the Certificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getCertificate();

  /**
   * Sets the Value of the Certificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCertificate(@Nullable ByteString value);

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

  /**
   * Returns the mandatory ExpirationDate child, a PropertyType with DataType DateTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getExpirationDateNode();

  /**
   * Returns the Value of the ExpirationDate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getExpirationDate();

  /**
   * Sets the Value of the ExpirationDate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setExpirationDate(@Nullable DateTime value);

  /**
   * Returns the optional ExpirationLimit child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getExpirationLimitNode();

  /**
   * Returns the Value of the ExpirationLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getExpirationLimit();

  /**
   * Sets the Value of the ExpirationLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setExpirationLimit(@Nullable Double value);

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
  interface Methods extends SystemOffNormalAlarmType.Methods {}
}
