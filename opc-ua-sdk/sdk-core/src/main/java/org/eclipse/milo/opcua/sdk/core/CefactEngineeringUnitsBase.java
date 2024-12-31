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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;

abstract class CefactEngineeringUnitsBase {
  protected static final String CEFACT_NAMESPACE_URI =
      "http://www.opcfoundation.org/UA/units/un/cefact";

  protected static final Map<Integer, EUInformation> BY_UNIT_ID = new ConcurrentHashMap<>();
}
