/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import org.jspecify.annotations.Nullable;

/**
 * Thrown when a PubSub configuration fails validation, e.g. in a config builder's {@code build()}
 * method or while mapping to or from the Part 14 {@code PubSubConfiguration2DataType}.
 *
 * <p>The message names the offending configuration element so violations can be traced back to the
 * authored config.
 */
public final class PubSubConfigValidationException extends RuntimeException {

  /**
   * Create a new {@link PubSubConfigValidationException}.
   *
   * @param message a message describing the violation, naming the offending element.
   */
  public PubSubConfigValidationException(String message) {
    super(message);
  }

  /**
   * Create a new {@link PubSubConfigValidationException}.
   *
   * @param message a message describing the violation, naming the offending element.
   * @param cause the underlying cause of the violation.
   */
  public PubSubConfigValidationException(String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
