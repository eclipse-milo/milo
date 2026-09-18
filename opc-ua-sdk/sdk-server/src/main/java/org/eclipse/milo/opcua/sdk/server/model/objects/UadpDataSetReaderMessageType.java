package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the UadpDataSetReaderMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3">Model
 *     documentation</a>
 */
public interface UadpDataSetReaderMessageType extends DataSetReaderMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21116L);

  /**
   * Returns the mandatory DataSetClassId child, a PropertyType with DataType Guid.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetClassIdNode();

  /**
   * Returns the Value of the DataSetClassId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UUID getDataSetClassId();

  /**
   * Sets the Value of the DataSetClassId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetClassId(@Nullable UUID value);

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
   * Returns the mandatory GroupVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getGroupVersionNode();

  /**
   * Returns the Value of the GroupVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getGroupVersion();

  /**
   * Sets the Value of the GroupVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setGroupVersion(@Nullable UInteger value);

  /**
   * Returns the mandatory NetworkMessageContentMask child, a PropertyType with DataType
   * UadpNetworkMessageContentMask.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNetworkMessageContentMaskNode();

  /**
   * Returns the Value of the NetworkMessageContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UadpNetworkMessageContentMask getNetworkMessageContentMask();

  /**
   * Sets the Value of the NetworkMessageContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value);

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

  /**
   * Returns the mandatory ProcessingOffset child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getProcessingOffsetNode();

  /**
   * Returns the Value of the ProcessingOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getProcessingOffset();

  /**
   * Sets the Value of the ProcessingOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setProcessingOffset(@Nullable Double value);

  /**
   * Returns the mandatory PublishingInterval child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPublishingIntervalNode();

  /**
   * Returns the Value of the PublishingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getPublishingInterval();

  /**
   * Sets the Value of the PublishingInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishingInterval(@Nullable Double value);

  /**
   * Returns the mandatory ReceiveOffset child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getReceiveOffsetNode();

  /**
   * Returns the Value of the ReceiveOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getReceiveOffset();

  /**
   * Sets the Value of the ReceiveOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReceiveOffset(@Nullable Double value);
}
