package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeBaseTsnStreamType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">Model
 *     documentation</a>
 */
public interface IIeeeBaseTsnStreamType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24173L);

  /**
   * Returns the optional AccumulatedLatency child, a BaseDataVariableType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getAccumulatedLatencyNode();

  /**
   * Returns the Value of the AccumulatedLatency child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getAccumulatedLatency();

  /**
   * Sets the Value of the AccumulatedLatency child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAccumulatedLatency(@Nullable UInteger value);

  /**
   * Returns the optional SrClassId child, a BaseDataVariableType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getSrClassIdNode();

  /**
   * Returns the Value of the SrClassId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getSrClassId();

  /**
   * Sets the Value of the SrClassId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSrClassId(@Nullable UByte value);

  /**
   * Returns the mandatory State child, a BaseDataVariableType with DataType TsnStreamState.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getStateNode();

  /**
   * Returns the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TsnStreamState getState();

  /**
   * Sets the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setState(@Nullable TsnStreamState value);

  /**
   * Returns the mandatory StreamId child, a BaseDataVariableType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getStreamIdNode();

  /**
   * Returns the Value of the StreamId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  UByte @Nullable [] getStreamId();

  /**
   * Sets the Value of the StreamId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStreamId(UByte @Nullable [] value);

  /**
   * Returns the mandatory StreamName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getStreamNameNode();

  /**
   * Returns the Value of the StreamName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getStreamName();

  /**
   * Sets the Value of the StreamName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStreamName(@Nullable String value);
}
