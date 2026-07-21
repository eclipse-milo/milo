/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

/**
 * A relative sequence of BrowseNames identifying one InstanceDeclaration occurrence within a {@link
 * TypeInstantiationModel} (OPC UA Part 3 §6.2.5).
 *
 * <p>BrowsePath is the model's primary key: a TypeDefinitionNode or InstanceDeclaration never
 * references two nodes with the same BrowseName by forward hierarchical references (Part 3 §4.6.4,
 * §6.2.6), so every declaration is uniquely identified by its path from the type root. The same
 * declaration node reached through multiple BrowsePaths is a distinct occurrence per path (Part 3
 * §6.3.3.2).
 *
 * <p>Elements are {@link QualifiedName}s whose namespace indexes resolve against the namespace
 * table of the server the owning model snapshot was compiled from. The representation is
 * deliberately a final class with factory methods rather than a record so a URI-carrying element
 * form can be added later without breaking callers.
 *
 * <p>Ordering is deterministic: element-wise by namespace index then name, with a proper prefix
 * ordering before its extensions.
 */
public final class BrowsePath implements Comparable<BrowsePath> {

  private static final BrowsePath ROOT = new BrowsePath(List.of());

  private static final Comparator<QualifiedName> ELEMENT_COMPARATOR =
      Comparator.comparing((QualifiedName qn) -> qn.namespaceIndex().intValue())
          .thenComparing(qn -> qn.name() != null ? qn.name() : "");

  private final List<QualifiedName> elements;

  private BrowsePath(List<QualifiedName> elements) {
    this.elements = List.copyOf(elements);
  }

  /**
   * @return the root path (the type definition node itself; zero elements).
   */
  public static BrowsePath root() {
    return ROOT;
  }

  /**
   * Create a path from the given elements.
   *
   * @param elements the BrowseName elements, outermost first.
   * @return a new {@link BrowsePath}.
   */
  public static BrowsePath of(QualifiedName... elements) {
    return elements.length == 0 ? ROOT : new BrowsePath(Arrays.asList(elements));
  }

  /**
   * Create a path from the given elements.
   *
   * @param elements the BrowseName elements, outermost first.
   * @return a new {@link BrowsePath}.
   */
  public static BrowsePath of(List<QualifiedName> elements) {
    return elements.isEmpty() ? ROOT : new BrowsePath(elements);
  }

  /**
   * @return the BrowseName elements of this path, outermost first; empty for the root path.
   */
  public List<QualifiedName> elements() {
    return elements;
  }

  /**
   * @return {@code true} if this is the root path.
   */
  public boolean isRoot() {
    return elements.isEmpty();
  }

  /**
   * @return the number of elements in this path.
   */
  public int depth() {
    return elements.size();
  }

  /**
   * @return the last element of this path, or {@code null} for the root path.
   */
  public @Nullable QualifiedName lastElement() {
    return elements.isEmpty() ? null : elements.get(elements.size() - 1);
  }

  /**
   * @param element the element to append.
   * @return a new path with {@code element} appended.
   */
  public BrowsePath append(QualifiedName element) {
    List<QualifiedName> newElements = new ArrayList<>(elements.size() + 1);
    newElements.addAll(elements);
    newElements.add(element);
    return new BrowsePath(newElements);
  }

  /**
   * @param other the path to append.
   * @return a new path consisting of this path's elements followed by {@code other}'s.
   */
  public BrowsePath concat(BrowsePath other) {
    if (other.isRoot()) {
      return this;
    }
    if (isRoot()) {
      return other;
    }
    List<QualifiedName> newElements = new ArrayList<>(elements.size() + other.elements.size());
    newElements.addAll(elements);
    newElements.addAll(other.elements);
    return new BrowsePath(newElements);
  }

  /**
   * @return the parent path; the root path if this path has one or zero elements.
   */
  public BrowsePath parent() {
    return elements.size() <= 1 ? ROOT : new BrowsePath(elements.subList(0, elements.size() - 1));
  }

  /**
   * @param prefix the candidate prefix.
   * @return {@code true} if this path starts with (or equals) {@code prefix}.
   */
  public boolean startsWith(BrowsePath prefix) {
    if (prefix.elements.size() > elements.size()) {
      return false;
    }
    return elements.subList(0, prefix.elements.size()).equals(prefix.elements);
  }

  @Override
  public int compareTo(BrowsePath other) {
    int commonLength = Math.min(elements.size(), other.elements.size());
    for (int i = 0; i < commonLength; i++) {
      int c = ELEMENT_COMPARATOR.compare(elements.get(i), other.elements.get(i));
      if (c != 0) {
        return c;
      }
    }
    return Integer.compare(elements.size(), other.elements.size());
  }

  @Override
  public boolean equals(@Nullable Object o) {
    return o instanceof BrowsePath other && elements.equals(other.elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elements);
  }

  @Override
  public String toString() {
    if (elements.isEmpty()) {
      return "/";
    }
    StringBuilder sb = new StringBuilder();
    for (QualifiedName element : elements) {
      sb.append('/').append(element.namespaceIndex()).append(':').append(element.name());
    }
    return sb.toString();
  }
}
