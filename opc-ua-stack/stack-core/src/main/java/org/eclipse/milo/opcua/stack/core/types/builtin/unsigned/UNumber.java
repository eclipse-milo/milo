/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin.unsigned;

import java.math.BigInteger;

/**
 * A base type for unsigned numbers.
 *
 * @author Lukas Eder
 */
public abstract class UNumber extends Number {

  /** Generated UID */
  private static final long serialVersionUID = -7666221938815339843L;

  /**
   * Get this number as a {@link BigInteger}. This is a convenience method for calling <code>
   * new BigInteger(toString())</code>
   */
  public BigInteger toBigInteger() {
    return new BigInteger(toString());
  }
}
