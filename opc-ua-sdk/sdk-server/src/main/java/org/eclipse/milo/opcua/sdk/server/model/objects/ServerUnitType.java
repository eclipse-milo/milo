/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerUnitType extends UnitType {
  QualifiedProperty<ConversionLimitEnum> CONVERSION_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConversionLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32436"),
          -1,
          ConversionLimitEnum.class);

  /** Gets the existing node's local value. */
  @Nullable ConversionLimitEnum getConversionLimit();

  /** Sets the existing node's local value. */
  void setConversionLimit(@Nullable ConversionLimitEnum value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConversionLimitNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseObjectType getAlternativeUnitsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable UnitType getCoherentUnitNode();
}
