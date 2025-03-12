/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.factories;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

public class BrowsePath {

  static final BrowsePath ROOT = new BrowsePath(null, new QualifiedName(0, "/"));

  final BrowsePath parent;
  final QualifiedName browseName;

  BrowsePath(BrowsePath parent, QualifiedName browseName) {
    this.parent = parent;
    this.browseName = browseName;
  }

  /**
   * Joins the components of this browse path with "/" as the separator.
   *
   * @return a String with the components of this browse path joined with "/" as the separator.
   */
  public String join() {
    return join("/", true);
  }

  /**
   * Join the components of this browse path with {@code separator}.
   *
   * @param separator the separator to join with.
   * @param includeNamespaceIndex include the namespace index of each browse name component.
   * @return a String with the components of this browse path joined by {@code separator}.
   */
  public String join(String separator, boolean includeNamespaceIndex) {
    if (parent == null) {
      return "";
    } else {
      String s = parent.join(separator, includeNamespaceIndex);
      if (!s.endsWith(separator)) {
        s += separator;
      }
      if (includeNamespaceIndex) {
        s += browseName.namespaceIndex() + ":";
      }
      return s + browseName.name();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BrowsePath that = (BrowsePath) o;
    return Objects.equals(parent, that.parent) && Objects.equals(browseName, that.browseName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parent, browseName);
  }

  @Override
  public String toString() {
    return join();
  }
}
