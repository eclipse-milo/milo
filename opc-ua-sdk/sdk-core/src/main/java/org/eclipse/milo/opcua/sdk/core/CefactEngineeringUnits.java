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

import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;

public abstract class CefactEngineeringUnits extends CefactEngineeringUnits0 {
  private CefactEngineeringUnits() {}

  public static EUInformation[] getAll() {
    return BY_UNIT_ID.values().toArray(new EUInformation[0]);
  }

  public static EUInformation getByUnitId(int unitId) {
    return BY_UNIT_ID.get(unitId);
  }
}
