/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.5">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AnalogUnitRangeType extends AnalogItemType {
  QualifiedProperty<EUInformation> ENGINEERING_UNITS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EngineeringUnits",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=887"),
          -1,
          EUInformation.class);

  /** Gets the existing node's local value. */
  @Nullable EUInformation getEngineeringUnitsProperty() throws UaException;

  /** Sets the existing node's local value. */
  void setEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EUInformation readEngineeringUnitsProperty() throws UaException;

  /** Writes the value remotely. */
  void writeEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnitsPropertyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEngineeringUnitsPropertyAsync(@Nullable EUInformation value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEngineeringUnitsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEngineeringUnitsNodeAsync();
}
