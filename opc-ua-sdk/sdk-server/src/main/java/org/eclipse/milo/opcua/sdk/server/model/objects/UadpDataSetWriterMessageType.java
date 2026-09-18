package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the UadpDataSetWriterMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.2">Model
 *     documentation</a>
 */
public interface UadpDataSetWriterMessageType extends DataSetWriterMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21111L);

  /**
   * Returns the mandatory ConfiguredSize child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConfiguredSizeNode();

  /**
   * Returns the Value of the ConfiguredSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getConfiguredSize();

  /**
   * Sets the Value of the ConfiguredSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConfiguredSize(@Nullable UShort value);

  /**
   * Returns the mandatory DataSetMessageContentMask child, a PropertyType with DataType
   * UadpDataSetMessageContentMask.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetMessageContentMaskNode();

  /**
   * Returns the Value of the DataSetMessageContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UadpDataSetMessageContentMask getDataSetMessageContentMask();

  /**
   * Sets the Value of the DataSetMessageContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value);

  /**
   * Returns the mandatory DataSetOffset child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetOffsetNode();

  /**
   * Returns the Value of the DataSetOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getDataSetOffset();

  /**
   * Sets the Value of the DataSetOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetOffset(@Nullable UShort value);

  /**
   * Returns the mandatory NetworkMessageNumber child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNetworkMessageNumberNode();

  /**
   * Returns the Value of the NetworkMessageNumber child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getNetworkMessageNumber();

  /**
   * Sets the Value of the NetworkMessageNumber child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNetworkMessageNumber(@Nullable UShort value);
}
