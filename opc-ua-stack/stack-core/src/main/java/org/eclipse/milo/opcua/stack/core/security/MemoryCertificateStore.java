/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class MemoryCertificateStore implements CertificateStore {

  private final Map<NodeId, Entry> keys = new ConcurrentHashMap<>();

  @Override
  public boolean contains(NodeId certificateTypeId) throws Exception {
    return keys.containsKey(certificateTypeId);
  }

  @Override
  public Entry get(NodeId certificateTypeId) throws Exception {
    return keys.get(certificateTypeId);
  }

  @Override
  public Entry remove(NodeId certificateTypeId) throws Exception {
    return keys.remove(certificateTypeId);
  }

  @Override
  public void set(NodeId certificateTypeId, Entry entry) throws Exception {
    keys.put(certificateTypeId, entry);
  }
}
