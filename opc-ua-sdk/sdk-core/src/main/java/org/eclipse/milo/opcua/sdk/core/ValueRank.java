/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

public enum ValueRank {
  OneDimension(1),
  OneOrMoreDimensions(0),
  Scalar(-1),
  Any(-2),
  ScalarOrOneDimension(-3);

  private final int value;

  ValueRank(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
