/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/** A {@link UriArray} intended to contain Server URI entries. */
public class ServerTable extends UriArray<UInteger> {

  @Override
  protected UInteger create(Number index) {
    return UInteger.valueOf(index.longValue());
  }
}
