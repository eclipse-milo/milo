/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;

public class OpcUaDataTypeManager extends DefaultDataTypeManager {

  public static OpcUaDataTypeManager getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {
    private static final OpcUaDataTypeManager INSTANCE;

    static {
      INSTANCE = new OpcUaDataTypeManager();

      new DataTypeInitializer().initialize(new NamespaceTable(), INSTANCE);
    }
  }

  private OpcUaDataTypeManager() {}
}
