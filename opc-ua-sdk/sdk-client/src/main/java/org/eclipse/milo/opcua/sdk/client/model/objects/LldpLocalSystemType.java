package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the LldpLocalSystemType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">Model
 *     documentation</a>
 */
public interface LldpLocalSystemType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19002L);

  QualifiedProperty<String> SystemName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SystemName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<ChassisIdSubtype> ChassisIdSubtype_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ChassisIdSubtype",
          ExpandedNodeId.of(Namespaces.OPC_UA, 18947L),
          -1,
          ChassisIdSubtype.class);

  QualifiedProperty<String> SystemDescription_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SystemDescription",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<LldpSystemCapabilitiesMap> SystemCapabilitiesEnabled_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SystemCapabilitiesEnabled",
          ExpandedNodeId.of(Namespaces.OPC_UA, 18956L),
          -1,
          LldpSystemCapabilitiesMap.class);

  QualifiedProperty<LldpSystemCapabilitiesMap> SystemCapabilitiesSupported_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SystemCapabilitiesSupported",
          ExpandedNodeId.of(Namespaces.OPC_UA, 18956L),
          -1,
          LldpSystemCapabilitiesMap.class);

  QualifiedProperty<String> ChassisId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ChassisId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory SystemName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSystemNameNode() throws UaException;

  /** Asynchronous form of {@link #getSystemNameNode()}. */
  CompletableFuture<? extends PropertyType> getSystemNameNodeAsync();

  /**
   * Reads the Value of the SystemName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSystemName() throws UaException;

  /**
   * Writes the Value of the SystemName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSystemName()}. */
  CompletableFuture<? extends @Nullable String> readSystemNameAsync();

  /** Asynchronous form of {@link #writeSystemName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSystemNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory ChassisIdSubtype child, a PropertyType with DataType ChassisIdSubtype.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getChassisIdSubtypeNode() throws UaException;

  /** Asynchronous form of {@link #getChassisIdSubtypeNode()}. */
  CompletableFuture<? extends PropertyType> getChassisIdSubtypeNodeAsync();

  /**
   * Reads the Value of the ChassisIdSubtype child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ChassisIdSubtype readChassisIdSubtype() throws UaException;

  /**
   * Writes the Value of the ChassisIdSubtype child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeChassisIdSubtype(@Nullable ChassisIdSubtype value) throws UaException;

  /** Asynchronous form of {@link #readChassisIdSubtype()}. */
  CompletableFuture<? extends @Nullable ChassisIdSubtype> readChassisIdSubtypeAsync();

  /** Asynchronous form of {@link #writeChassisIdSubtype}; completes with the operation status. */
  CompletableFuture<StatusCode> writeChassisIdSubtypeAsync(@Nullable ChassisIdSubtype value);

  /**
   * Resolves the mandatory SystemDescription child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSystemDescriptionNode() throws UaException;

  /** Asynchronous form of {@link #getSystemDescriptionNode()}. */
  CompletableFuture<? extends PropertyType> getSystemDescriptionNodeAsync();

  /**
   * Reads the Value of the SystemDescription child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSystemDescription() throws UaException;

  /**
   * Writes the Value of the SystemDescription child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemDescription(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSystemDescription()}. */
  CompletableFuture<? extends @Nullable String> readSystemDescriptionAsync();

  /** Asynchronous form of {@link #writeSystemDescription}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSystemDescriptionAsync(@Nullable String value);

  /**
   * Resolves the optional SystemCapabilitiesEnabled child, a PropertyType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSystemCapabilitiesEnabledNode() throws UaException;

  /** Asynchronous form of {@link #getSystemCapabilitiesEnabledNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSystemCapabilitiesEnabledNodeAsync();

  /**
   * Reads the Value of the SystemCapabilitiesEnabled child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesEnabled() throws UaException;

  /**
   * Writes the Value of the SystemCapabilitiesEnabled child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value) throws UaException;

  /** Asynchronous form of {@link #readSystemCapabilitiesEnabled()}. */
  CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesEnabledAsync();

  /**
   * Asynchronous form of {@link #writeSystemCapabilitiesEnabled}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSystemCapabilitiesEnabledAsync(
      @Nullable LldpSystemCapabilitiesMap value);

  /**
   * Resolves the optional SystemCapabilitiesSupported child, a PropertyType with DataType
   * LldpSystemCapabilitiesMap.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSystemCapabilitiesSupportedNode() throws UaException;

  /** Asynchronous form of {@link #getSystemCapabilitiesSupportedNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSystemCapabilitiesSupportedNodeAsync();

  /**
   * Reads the Value of the SystemCapabilitiesSupported child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LldpSystemCapabilitiesMap readSystemCapabilitiesSupported() throws UaException;

  /**
   * Writes the Value of the SystemCapabilitiesSupported child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value)
      throws UaException;

  /** Asynchronous form of {@link #readSystemCapabilitiesSupported()}. */
  CompletableFuture<? extends @Nullable LldpSystemCapabilitiesMap>
      readSystemCapabilitiesSupportedAsync();

  /**
   * Asynchronous form of {@link #writeSystemCapabilitiesSupported}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSystemCapabilitiesSupportedAsync(
      @Nullable LldpSystemCapabilitiesMap value);

  /**
   * Resolves the mandatory ChassisId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getChassisIdNode() throws UaException;

  /** Asynchronous form of {@link #getChassisIdNode()}. */
  CompletableFuture<? extends PropertyType> getChassisIdNodeAsync();

  /**
   * Reads the Value of the ChassisId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readChassisId() throws UaException;

  /**
   * Writes the Value of the ChassisId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeChassisId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readChassisId()}. */
  CompletableFuture<? extends @Nullable String> readChassisIdAsync();

  /** Asynchronous form of {@link #writeChassisId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeChassisIdAsync(@Nullable String value);
}
