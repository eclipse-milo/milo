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

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.20">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.20</a>
 */
public interface RationalNumberType extends BaseDataVariableType {
  BaseDataVariableType getNumeratorNode();

  Integer getNumerator();

  void setNumerator(Integer value);

  BaseDataVariableType getDenominatorNode();

  UInteger getDenominator();

  void setDenominator(UInteger value);
}
