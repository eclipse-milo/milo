/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

/**
 * An opaque handle identifying a runtime component of a {@link PubSubService}.
 *
 * <p>Handle identity is object identity: {@code equals} and {@code hashCode} are inherited from
 * {@link Object}. The {@link #componentType()} and {@link #path()} are diagnostic information, not
 * identity; two handles with the same path are distinct if obtained across a reconfiguration that
 * replaced the component.
 *
 * <p>Handles are created by the runtime and obtained from {@link PubSubComponents} or runtime
 * events. A handle is invalidated when its component is removed by reconfiguration; registry
 * lookups for invalidated handles return empty.
 */
public final class PubSubHandle {

  private final ComponentType componentType;
  private final String path;

  /**
   * Create a new {@link PubSubHandle}. Handles are created by the runtime; application code should
   * obtain handles from {@link PubSubComponents}.
   *
   * @param componentType the type of the component this handle identifies.
   * @param path the diagnostic path of the component, e.g. {@code "conn/group/writer"}.
   */
  public PubSubHandle(ComponentType componentType, String path) {
    this.componentType = componentType;
    this.path = path;
  }

  /**
   * Get the type of the component this handle identifies.
   *
   * @return the {@link ComponentType} of the component.
   */
  public ComponentType componentType() {
    return componentType;
  }

  /**
   * Get the diagnostic path of the component this handle identifies, e.g. {@code
   * "udp-fast/fast-uadp/sensor-writer"}.
   *
   * @return the diagnostic path of the component.
   */
  public String path() {
    return path;
  }

  @Override
  public String toString() {
    return "PubSubHandle{componentType=%s, path=%s}".formatted(componentType, path);
  }
}
