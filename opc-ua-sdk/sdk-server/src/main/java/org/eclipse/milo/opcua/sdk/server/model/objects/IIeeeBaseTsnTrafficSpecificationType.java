package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeBaseTsnTrafficSpecificationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8">Model
 *     documentation</a>
 */
public interface IIeeeBaseTsnTrafficSpecificationType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24179L);

  /**
   * Returns the mandatory Interval child, a BaseDataVariableType with DataType
   * UnsignedRationalNumber.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getIntervalNode();

  /**
   * Returns the Value of the Interval child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UnsignedRationalNumber getInterval();

  /**
   * Sets the Value of the Interval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInterval(@Nullable UnsignedRationalNumber value);

  /**
   * Returns the mandatory MaxFrameSize child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxFrameSizeNode();

  /**
   * Returns the Value of the MaxFrameSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxFrameSize();

  /**
   * Sets the Value of the MaxFrameSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxFrameSize(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxIntervalFrames child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxIntervalFramesNode();

  /**
   * Returns the Value of the MaxIntervalFrames child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxIntervalFrames();

  /**
   * Sets the Value of the MaxIntervalFrames child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxIntervalFrames(@Nullable UShort value);
}
