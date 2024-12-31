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

public class ValueRanks {

  public static final int OneDimension = 1;
  public static final int OneOrMoreDimensions = 0;
  public static final int Scalar = -1;
  public static final int Any = -2;
  public static final int ScalarOrOneDimension = -3;
}
