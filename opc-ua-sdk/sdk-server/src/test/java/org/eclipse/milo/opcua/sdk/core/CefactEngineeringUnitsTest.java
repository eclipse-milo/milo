/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.junit.jupiter.api.Test;

public class CefactEngineeringUnitsTest {

  @Test
  public void test() {
    assertTrue(CefactEngineeringUnits.getAll().length > 0);

    for (EUInformation eu : CefactEngineeringUnits.getAll()) {
      assertEquals(eu, CefactEngineeringUnits.getByUnitId(eu.getUnitId()));
    }
  }
}
