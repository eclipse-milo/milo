/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.util;

import static org.eclipse.milo.opcua.sdk.core.util.GroupMapCollate.groupMapCollate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class GroupMapCollateTest {

  /**
   * Services route each operation of a request to the AddressSpace fragment that owns it, and Part
   * 4 requires the response's results in the order of the request's operations (for Read, Part 4
   * §5.11.2.2). Groups interleave in a request, so collation must restore the original order. Group
   * "a" holds more items than a group's initial index capacity.
   */
  @Test
  void resultsFollowTheOrderOfTheItemsAcrossInterleavedGroups() {
    List<String> items =
        List.of("a1", "b1", "a2", "c1", "a3", "a4", "b2", "a5", "a6", "a7", "a8", "c2", "a9");

    List<String> results =
        groupMapCollate(
            items,
            item -> item.charAt(0),
            key -> group -> group.stream().map(String::toUpperCase).toList());

    assertEquals(items.stream().map(String::toUpperCase).toList(), results);
  }

  // A fragment pairs its results with its own operations by position, so each group must reach
  // its mapper in the order the operations appeared in the request.
  @Test
  void eachMapperSeesItsGroupOnceInOriginalOrder() {
    List<String> items = List.of("a1", "b1", "a2", "b2", "a3");
    Map<Character, List<List<String>>> calls = new LinkedHashMap<>();

    groupMapCollate(
        items,
        item -> item.charAt(0),
        key ->
            group -> {
              calls.computeIfAbsent(key, k -> new ArrayList<>()).add(List.copyOf(group));
              return group;
            });

    assertEquals(
        Map.of('a', List.of(List.of("a1", "a2", "a3")), 'b', List.of(List.of("b1", "b2"))), calls);
  }

  // A mapper that drops or adds results would misalign every later result in the response.
  @Test
  void mapperReturningTheWrongNumberOfResultsIsRejected() {
    Function<Character, Function<List<String>, List<String>>> dropsOne =
        key -> group -> group.subList(1, group.size());

    assertThrows(
        RuntimeException.class,
        () -> groupMapCollate(List.of("a1", "a2"), item -> item.charAt(0), dropsOne));
  }

  // The list a mapper receives is its own to change; the result count is checked against the
  // group as it was handed over.
  @Test
  void mapperThatClearsItsInputStillHasItsResultsCollated() {
    List<String> results =
        groupMapCollate(
            List.of("a1", "a2"),
            item -> item.charAt(0),
            key ->
                group -> {
                  List<String> mapped = group.stream().map(String::toUpperCase).toList();
                  group.clear();
                  return mapped;
                });

    assertEquals(List.of("A1", "A2"), results);
  }

  // Control for the case above: clearing the input does not excuse returning too few results.
  @Test
  void mapperThatClearsItsInputAndReturnsNothingIsRejected() {
    assertThrows(
        RuntimeException.class,
        () ->
            groupMapCollate(
                List.of("a1", "a2"),
                item -> item.charAt(0),
                key ->
                    group -> {
                      group.clear();
                      return group;
                    }));
  }

  @Test
  void nullGroupKeyIsRejected() {
    assertThrows(
        NullPointerException.class,
        () -> groupMapCollate(List.of("a1"), item -> null, key -> group -> group));
  }
}
