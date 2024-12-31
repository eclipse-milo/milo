/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.util.ArrayList;
import java.util.EnumSet;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;

public class UaEnumUtil {

  public static EnumSet<NodeClass> nodeClasses(long mask) {
    var list = new ArrayList<NodeClass>();

    for (NodeClass nc : NodeClass.values()) {
      if ((mask & nc.getValue()) == nc.getValue()) {
        list.add(nc);
      }
    }

    return EnumSet.copyOf(list);
  }

  public static EnumSet<BrowseResultMask> browseResultMasks(long mask) {
    var list = new ArrayList<BrowseResultMask>();

    for (BrowseResultMask brm : BrowseResultMask.values()) {
      if ((mask & brm.getValue()) == brm.getValue()) {
        list.add(brm);
      }
    }

    return EnumSet.copyOf(list);
  }
}
