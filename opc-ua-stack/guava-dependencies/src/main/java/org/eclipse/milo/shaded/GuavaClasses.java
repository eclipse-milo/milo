/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.shaded;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GuavaClasses {

  /*
   * Referencing Guava classes here includes them in the minimized Guava jar
   * that is shaded into the milo-stack-core artifact.
   */
  static {
    com.google.common.base.Charsets.class.getName();
    com.google.common.base.MoreObjects.class.getName();
    com.google.common.base.MoreObjects.ToStringHelper.class.getName();
    com.google.common.base.Objects.class.getName();
    com.google.common.base.Preconditions.class.getName();
    com.google.common.base.Strings.class.getName();
    com.google.common.cache.Cache.class.getName();
    com.google.common.cache.CacheBuilder.class.getName();
    com.google.common.collect.ArrayListMultimap.class.getName();
    com.google.common.collect.BiMap.class.getName();
    com.google.common.collect.ConcurrentHashMultiset.class.getName();
    com.google.common.collect.ForwardingTable.class.getName();
    com.google.common.collect.HashBasedTable.class.getName();
    com.google.common.collect.HashBiMap.class.getName();
    com.google.common.collect.ImmutableBiMap.class.getName();
    com.google.common.collect.ImmutableMap.class.getName();
    com.google.common.collect.Iterators.class.getName();
    com.google.common.collect.LinkedHashMultiset.class.getName();
    com.google.common.collect.ListMultimap.class.getName();
    com.google.common.collect.Lists.class.getName();
    com.google.common.collect.Multimaps.class.getName();
    com.google.common.collect.Multiset.class.getName();
    com.google.common.collect.PeekingIterator.class.getName();
    com.google.common.collect.Sets.class.getName();
    com.google.common.collect.Streams.class.getName();
    com.google.common.collect.Table.class.getName();
    com.google.common.collect.Tables.class.getName();
    com.google.common.eventbus.EventBus.class.getName();
    com.google.common.eventbus.Subscribe.class.getName();
    com.google.common.io.ByteStreams.class.getName();
    com.google.common.io.CharStreams.class.getName();
    com.google.common.math.DoubleMath.class.getName();
    com.google.common.primitives.Bytes.class.getName();
    com.google.common.primitives.Ints.class.getName();
  }
}
