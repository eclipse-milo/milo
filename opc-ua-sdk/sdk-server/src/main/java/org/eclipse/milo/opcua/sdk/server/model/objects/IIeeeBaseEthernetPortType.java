package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeBaseEthernetPortType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2">Model
 *     documentation</a>
 */
public interface IIeeeBaseEthernetPortType extends BaseInterfaceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24158L);

  /**
   * Returns the mandatory Duplex child, a BaseDataVariableType with DataType Duplex.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getDuplexNode();

  /**
   * Returns the Value of the Duplex child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Duplex getDuplex();

  /**
   * Sets the Value of the Duplex child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDuplex(@Nullable Duplex value);

  /**
   * Returns the mandatory MaxFrameLength child, a BaseDataVariableType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaxFrameLengthNode();

  /**
   * Returns the Value of the MaxFrameLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxFrameLength();

  /**
   * Sets the Value of the MaxFrameLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxFrameLength(@Nullable UShort value);

  /**
   * Returns the mandatory Speed child, a AnalogUnitType with DataType UInt64.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.4">AnalogUnitType
   *     documentation</a>
   */
  AnalogUnitTypeNode getSpeedNode();

  /**
   * Returns the Value of the Speed child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ULong getSpeed();

  /**
   * Sets the Value of the Speed child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSpeed(@Nullable ULong value);
}
