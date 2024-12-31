/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

public class Tree<A> {

  private final List<Tree<A>> children = new ArrayList<>();

  private final Tree<A> parent;
  private final A value;

  public Tree(@Nullable Tree<A> parent, A value) {
    this.parent = parent;
    this.value = value;
  }

  public Tree<A> addChild(A value) {
    return addChild(new Tree<>(this, value));
  }

  public Tree<A> addChild(Tree<A> child) {
    children.add(child);
    return child;
  }

  @Nullable
  public Tree<A> getParent() {
    return parent;
  }

  public List<Tree<A>> getChildren() {
    return children;
  }

  public A getValue() {
    return value;
  }

  /**
   * Traverse this tree consuming the {@code value} at each node.
   *
   * @param c the value {@link Consumer}.
   */
  public void traverse(Consumer<A> c) {
    traverse(this, (a, integer) -> c.accept(a), 0, null);
  }

  /**
   * Traverse this tree consuming each {@link Tree} node.
   *
   * @param c the node {@link Consumer}.
   */
  public void traverseNodes(Consumer<Tree<A>> c) {
    traverseNodes(this, (t, integer) -> c.accept(t), 0);
  }

  /**
   * Traverse this tree consuming the {@code value} and current depth at each node.
   *
   * @param c the value and depth {@link BiConsumer}.
   */
  public void traverseWithDepth(BiConsumer<A, Integer> c) {
    traverse(this, c, 0, null);
  }

  /**
   * Traverse this tree consuming the {@code value} and current depth at each node.
   *
   * @param c the value and depth {@link BiConsumer}.
   * @param comparator a {@link Comparator} to sort the children of each node.
   */
  public void traverseWithDepth(BiConsumer<A, Integer> c, Comparator<Tree<A>> comparator) {
    traverse(this, c, 0, comparator);
  }

  /**
   * Traverse this tree consuming pairs of ({@code value}, {@code parent.value}).
   *
   * <p>{@code parent.value} may be null if this node is the root.
   *
   * @param c the {@link BiConsumer}.
   */
  public void traverseWithParent(BiConsumer<A, A> c) {
    traverse(this, c);
  }

  /**
   * Map this tree into a new tree using the transformation {@code f}.
   *
   * @param f a function that transforms from {@code A} to {@code B}.
   * @param <B> the new type.
   * @return a new tree in which the values have been mapped from {@code A} to {@code B} using
   *     {@code f}.
   */
  public <B> Tree<B> map(Function<A, B> f) {
    return map(this, f);
  }

  private static <A, B> Tree<B> map(Tree<A> tA, Function<A, B> f) {
    Tree<B> tB = new Tree<>(null, f.apply(tA.value));

    map(tA, tB, f);

    return tB;
  }

  private static <A, B> void map(Tree<A> tA, Tree<B> tB, Function<A, B> f) {
    tA.children.forEach(
        cA -> {
          Tree<B> cB = tB.addChild(f.apply(cA.value));
          map(cA, cB, f);
        });
  }

  private static <T> void traverse(
      Tree<T> tree, BiConsumer<T, Integer> c, int depth, @Nullable Comparator<Tree<T>> comparator) {
    T value = tree.value;
    c.accept(value, depth);

    if (comparator != null) {
      tree.children.stream().sorted(comparator).forEach(t -> traverse(t, c, depth + 1, comparator));
    } else {
      tree.children.forEach(t -> traverse(t, c, depth + 1, comparator));
    }
  }

  private static <T> void traverseNodes(Tree<T> tree, BiConsumer<Tree<T>, Integer> c, int depth) {
    c.accept(tree, depth);
    tree.children.forEach(t -> traverseNodes(t, c, depth + 1));
  }

  private static <T> void traverse(Tree<T> tree, BiConsumer<T, T> c) {
    T parentValue = tree.parent != null ? tree.parent.value : null;
    T value = tree.value;
    c.accept(value, parentValue);
    tree.children.forEach(t -> traverse(t, c));
  }
}
