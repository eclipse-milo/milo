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

/**
 * A singleton {@link EncodingManager} initialized with the default OPC UA encodings.
 *
 * <p>The "Default Binary" encoding is always registered. The "Default XML" and "Default JSON"
 * encodings are registered if they are found on the classpath at the time the singleton is first
 * instantiated.
 */
public class OpcUaEncodingManager implements EncodingManager {

  /**
   * Get the shared {@link OpcUaEncodingManager} instance.
   *
   * @return the shared {@link OpcUaEncodingManager} instance.
   */
  public static OpcUaEncodingManager getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private final EncodingManager delegate;

  private OpcUaEncodingManager(EncodingManager delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean hasEncoding(QualifiedName encodingName) {
    return delegate.hasEncoding(encodingName);
  }

  @Override
  public @Nullable DataTypeEncoding getEncoding(QualifiedName encodingName) {
    return delegate.getEncoding(encodingName);
  }

  /**
   * Operation not supported, this instance is immutable.
   *
   * @throws UnsupportedOperationException always, this instance is immutable.
   */
  @Override
  public void registerEncoding(DataTypeEncoding encoding) {
    throw new UnsupportedOperationException("singleton instance is immutable");
  }

  /**
   * Operation not supported, this instance is immutable.
   *
   * @throws UnsupportedOperationException always, this instance is immutable.
   */
  @Override
  public @Nullable DataTypeEncoding removeEncoding(QualifiedName encodingName) {
    throw new UnsupportedOperationException("singleton instance is immutable");
  }

  private static class InstanceHolder {
    private static final OpcUaEncodingManager INSTANCE;

    static {
      INSTANCE = new OpcUaEncodingManager(DefaultEncodingManager.createAndInitialize());
    }
  }
}
