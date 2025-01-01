/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.nodes;

public interface MethodNode extends Node {

  /**
   * The Executable attribute indicates whether the Method is executable, not taking user access
   * rights into account. If the OPC UA Server cannot get the Executable information from the
   * underlying system, it should state that it is executable.
   *
   * <p>See OPC-UA Part 3, Section 5.7.
   *
   * @return {@code true} if this method is executable, not taking user access rights into account.
   */
  Boolean isExecutable();

  /**
   * The UserExecutable attribute indicates whether the Method is executable, taking user access
   * rights into account. If the OPC UA Server cannot get any user rights related information from
   * the underlying system, it should use the same value as used in the Executable attribute.
   *
   * <p>See OPC-UA Part 3, Section 5.7.
   *
   * @return {@code true} if this method is executable, taking user access rights into account.
   */
  Boolean isUserExecutable();

  /**
   * Set the Executable attribute of this Method.
   *
   * @param executable {@code true} if the method is executable.
   */
  void setExecutable(Boolean executable);

  /**
   * Set the UserExecutable attribute of this Method.
   *
   * @param userExecutable {@code true} if the method is executable, taking access rights into
   *     account.
   */
  void setUserExecutable(Boolean userExecutable);
}
