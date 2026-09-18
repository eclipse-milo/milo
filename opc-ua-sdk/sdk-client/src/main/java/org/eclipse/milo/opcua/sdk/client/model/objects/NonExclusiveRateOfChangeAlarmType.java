package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the NonExclusiveRateOfChangeAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.23/#5.8.23.2">Model
 *     documentation</a>
 */
public interface NonExclusiveRateOfChangeAlarmType extends NonExclusiveLimitAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 10214L);

  QualifiedProperty<EUInformation> EngineeringUnits_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EngineeringUnits",
          ExpandedNodeId.of(Namespaces.OPC_UA, 887L),
          -1,
          EUInformation.class);

  /**
   * Resolves the optional EngineeringUnits child, a PropertyType with DataType EUInformation.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEngineeringUnitsNode() throws UaException;

  /** Asynchronous form of {@link #getEngineeringUnitsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEngineeringUnitsNodeAsync();

  /**
   * Reads the Value of the EngineeringUnits child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EUInformation readEngineeringUnits() throws UaException;

  /**
   * Writes the Value of the EngineeringUnits child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEngineeringUnits(@Nullable EUInformation value) throws UaException;

  /** Asynchronous form of {@link #readEngineeringUnits()}. */
  CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnitsAsync();

  /** Asynchronous form of {@link #writeEngineeringUnits}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEngineeringUnitsAsync(@Nullable EUInformation value);
}
