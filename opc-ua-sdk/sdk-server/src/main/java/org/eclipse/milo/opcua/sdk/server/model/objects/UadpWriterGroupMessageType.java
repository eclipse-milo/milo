package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the UadpWriterGroupMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1">Model
 *     documentation</a>
 */
public interface UadpWriterGroupMessageType extends WriterGroupMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21105L);

  /**
   * Returns the mandatory DataSetOrdering child, a PropertyType with DataType DataSetOrderingType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetOrderingNode();

  /**
   * Returns the Value of the DataSetOrdering child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DataSetOrderingType getDataSetOrdering();

  /**
   * Sets the Value of the DataSetOrdering child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetOrdering(@Nullable DataSetOrderingType value);

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
   * Returns the mandatory PublishingOffset child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPublishingOffsetNode();

  /**
   * Returns the Value of the PublishingOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  Double @Nullable [] getPublishingOffset();

  /**
   * Sets the Value of the PublishingOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPublishingOffset(Double @Nullable [] value);

  /**
   * Returns the optional SamplingOffset child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSamplingOffsetNode();

  /**
   * Returns the Value of the SamplingOffset child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getSamplingOffset();

  /**
   * Sets the Value of the SamplingOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSamplingOffset(@Nullable Double value);
}
