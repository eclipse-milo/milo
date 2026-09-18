package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the BuildInfoType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7">Model
 *     documentation</a>
 */
public interface BuildInfoType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3051L);

  /**
   * Returns the mandatory BuildDate child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getBuildDateNode();

  /**
   * Returns the Value of the BuildDate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getBuildDate();

  /**
   * Sets the Value of the BuildDate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBuildDate(@Nullable DateTime value);

  /**
   * Returns the mandatory BuildNumber child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getBuildNumberNode();

  /**
   * Returns the Value of the BuildNumber child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getBuildNumber();

  /**
   * Sets the Value of the BuildNumber child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBuildNumber(@Nullable String value);

  /**
   * Returns the mandatory ManufacturerName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getManufacturerNameNode();

  /**
   * Returns the Value of the ManufacturerName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getManufacturerName();

  /**
   * Sets the Value of the ManufacturerName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setManufacturerName(@Nullable String value);

  /**
   * Returns the mandatory ProductName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getProductNameNode();

  /**
   * Returns the Value of the ProductName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getProductName();

  /**
   * Sets the Value of the ProductName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setProductName(@Nullable String value);

  /**
   * Returns the mandatory ProductUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getProductUriNode();

  /**
   * Returns the Value of the ProductUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getProductUri();

  /**
   * Sets the Value of the ProductUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setProductUri(@Nullable String value);

  /**
   * Returns the mandatory SoftwareVersion child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getSoftwareVersionNode();

  /**
   * Returns the Value of the SoftwareVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSoftwareVersion();

  /**
   * Sets the Value of the SoftwareVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSoftwareVersion(@Nullable String value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable BuildInfo getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable BuildInfo value);
}
