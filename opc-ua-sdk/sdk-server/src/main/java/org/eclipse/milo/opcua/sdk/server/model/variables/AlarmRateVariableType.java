/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3</a>
 */
public interface AlarmRateVariableType extends BaseDataVariableType {
  QualifiedProperty<UShort> RATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Rate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  UShort getRate();

  void setRate(UShort value);

  PropertyType getRateNode();
}
