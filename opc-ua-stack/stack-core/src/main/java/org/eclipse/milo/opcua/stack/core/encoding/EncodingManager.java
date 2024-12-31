/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

public interface EncodingManager {

  void registerEncoding(DataTypeEncoding encoding);

  boolean hasEncoding(QualifiedName encodingName);

  @Nullable DataTypeEncoding getEncoding(QualifiedName encodingName);

  @Nullable DataTypeEncoding removeEncoding(QualifiedName encodingName);
}
