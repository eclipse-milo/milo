/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;

public abstract class OptionSetUI8<F extends Enum<F> & OptionSetUInteger.BitIndex>
    extends OptionSetUInteger<F> {

  public OptionSetUI8(UByte value) {
    super(value);
  }

  @Override
  public UByte getValue() {
    return (UByte) value;
  }
}
