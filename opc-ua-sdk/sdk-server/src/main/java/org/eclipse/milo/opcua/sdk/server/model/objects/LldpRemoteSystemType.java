package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the LldpRemoteSystemType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">Model
 *     documentation</a>
 */
public interface LldpRemoteSystemType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19033L);

  /**
   * Returns the mandatory ChassisId child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getChassisIdNode();

  /**
   * Returns the Value of the ChassisId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getChassisId();

  /**
   * Sets the Value of the ChassisId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setChassisId(@Nullable String value);

  /**
   * Returns the mandatory ChassisIdSubtype child, a BaseDataVariableType with DataType
   * ChassisIdSubtype.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getChassisIdSubtypeNode();

  /**
   * Returns the Value of the ChassisIdSubtype child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ChassisIdSubtype getChassisIdSubtype();

  /**
   * Sets the Value of the ChassisIdSubtype child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setChassisIdSubtype(@Nullable ChassisIdSubtype value);

  /**
   * Returns the optional ManagementAddress child, a BaseDataVariableType with DataType
   * LldpManagementAddressType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getManagementAddressNode();

  /**
   * Returns the Value of the ManagementAddress child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LldpManagementAddressType @Nullable [] getManagementAddress();

  /**
   * Sets the Value of the ManagementAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value);

  /**
   * Returns the optional PortDescription child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getPortDescriptionNode();

  /**
   * Returns the Value of the PortDescription child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getPortDescription();

  /**
   * Sets the Value of the PortDescription child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPortDescription(@Nullable String value);

  /**
   * Returns the mandatory PortId child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPortIdNode();

  /**
   * Returns the Value of the PortId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getPortId();

  /**
   * Sets the Value of the PortId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPortId(@Nullable String value);

  /**
   * Returns the mandatory PortIdSubtype child, a BaseDataVariableType with DataType PortIdSubtype.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getPortIdSubtypeNode();

  /**
   * Returns the Value of the PortIdSubtype child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PortIdSubtype getPortIdSubtype();

  /**
   * Sets the Value of the PortIdSubtype child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPortIdSubtype(@Nullable PortIdSubtype value);

  /**
   * Returns the optional RemoteChanges child, a BaseDataVariableType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getRemoteChangesNode();

  /**
   * Returns the Value of the RemoteChanges child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getRemoteChanges();

  /**
   * Sets the Value of the RemoteChanges child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteChanges(@Nullable Boolean value);

  /**
   * Returns the mandatory RemoteIndex child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getRemoteIndexNode();

  /**
   * Returns the Value of the RemoteIndex child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getRemoteIndex();

  /**
   * Sets the Value of the RemoteIndex child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteIndex(@Nullable UInteger value);

  /**
   * Returns the optional RemoteTooManyNeighbors child, a BaseDataVariableType with DataType
   * Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getRemoteTooManyNeighborsNode();

  /**
   * Returns the Value of the RemoteTooManyNeighbors child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getRemoteTooManyNeighbors();

  /**
   * Sets the Value of the RemoteTooManyNeighbors child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteTooManyNeighbors(@Nullable Boolean value);

  /**
   * Returns the optional RemoteUnknownTlv child, a BaseDataVariableType with DataType LldpTlvType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getRemoteUnknownTlvNode();

  /**
   * Returns the Value of the RemoteUnknownTlv child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LldpTlvType @Nullable [] getRemoteUnknownTlv();

  /**
   * Sets the Value of the RemoteUnknownTlv child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value);

  /**
   * Returns the optional SystemCapabilitiesEnabled child, a BaseDataVariableType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getSystemCapabilitiesEnabledNode();

  /**
   * Returns the Value of the SystemCapabilitiesEnabled child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled();

  /**
   * Sets the Value of the SystemCapabilitiesEnabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value);

  /**
   * Returns the optional SystemCapabilitiesSupported child, a BaseDataVariableType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getSystemCapabilitiesSupportedNode();

  /**
   * Returns the Value of the SystemCapabilitiesSupported child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesSupported();

  /**
   * Sets the Value of the SystemCapabilitiesSupported child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value);

  /**
   * Returns the optional SystemDescription child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getSystemDescriptionNode();

  /**
   * Returns the Value of the SystemDescription child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSystemDescription();

  /**
   * Sets the Value of the SystemDescription child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSystemDescription(@Nullable String value);

  /**
   * Returns the optional SystemName child, a BaseDataVariableType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getSystemNameNode();

  /**
   * Returns the Value of the SystemName child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSystemName();

  /**
   * Sets the Value of the SystemName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSystemName(@Nullable String value);

  /**
   * Returns the mandatory TimeMark child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getTimeMarkNode();

  /**
   * Returns the Value of the TimeMark child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getTimeMark();

  /**
   * Sets the Value of the TimeMark child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTimeMark(@Nullable UInteger value);
}
