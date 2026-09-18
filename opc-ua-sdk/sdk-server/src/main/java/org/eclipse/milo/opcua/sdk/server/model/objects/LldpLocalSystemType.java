package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the LldpLocalSystemType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public interface LldpLocalSystemType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19002L);

  /**
   * Returns the mandatory ChassisId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getChassisIdNode();

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
   * Returns the mandatory ChassisIdSubtype child, a PropertyType with DataType ChassisIdSubtype.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getChassisIdSubtypeNode();

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
   * Returns the optional SystemCapabilitiesEnabled child, a PropertyType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSystemCapabilitiesEnabledNode();

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
   * Returns the optional SystemCapabilitiesSupported child, a PropertyType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSystemCapabilitiesSupportedNode();

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
   * Returns the mandatory SystemDescription child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSystemDescriptionNode();

  /**
   * Returns the Value of the SystemDescription child.
   *
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
   * Returns the mandatory SystemName child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSystemNameNode();

  /**
   * Returns the Value of the SystemName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSystemName();

  /**
   * Sets the Value of the SystemName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSystemName(@Nullable String value);
}
