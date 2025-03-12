/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This Built-in DataType contains a qualified name. It is, for example, used as BrowseName.
 *
 * <p>The name part of the QualifiedName is restricted to 512 characters.
 *
 * @param namespaceIndex index of the namespace that qualifies the name. This index is the index of
 *     that namespace in the local Server’s NamespaceArray.
 * @param name the name portion of the QualifiedName.
 */
@NullMarked
public record QualifiedName(UShort namespaceIndex, @Nullable String name) {

  public static final QualifiedName NULL_VALUE = new QualifiedName(ushort(0), null);

  public QualifiedName {
    Preconditions.checkNotNull(namespaceIndex);
    Preconditions.checkArgument(name == null || name.length() <= 512, "name");
  }

  /**
   * @param namespaceIndex index of the namespace that qualifies the name. This index is the index
   *     of that namespace in the local Server’s NamespaceArray.
   * @param name the name portion of the QualifiedName.
   * @see #QualifiedName(UShort, String)
   */
  public QualifiedName(int namespaceIndex, @Nullable String name) {
    this(ushort(namespaceIndex), name);
  }

  /**
   * @return the namespace index of the namespace that qualifies the name.
   */
  public UShort getNamespaceIndex() {
    return namespaceIndex;
  }

  /**
   * @return the name portion of the QualifiedName.
   */
  public @Nullable String getName() {
    return name;
  }

  /**
   * Check if this {@link QualifiedName} is "null".
   *
   * <p>A {@link QualifiedName} is considered "null" if its name is {@code null} or empty.
   *
   * @return {@code true} if this {@link QualifiedName} is "null", {@code false} otherwise.
   */
  public boolean isNull() {
    return name == null || name.isEmpty();
  }

  /**
   * Check if this {@link QualifiedName} is not "null".
   *
   * <p>A {@link QualifiedName} is considered "not null" if its name is not {@code null} or empty.
   *
   * <p>This is the inverse of {@link #isNull()}.
   *
   * @return {@code true} if this {@link QualifiedName} is not "null", {@code false} otherwise.
   */
  public boolean isNotNull() {
    return !isNull();
  }

  /**
   * Re-index this {@link QualifiedName} from its current namespace index to the index for {@code
   * namespaceUri}.
   *
   * <p>If the target namespace URI is not present in the namespace table this {@link QualifiedName}
   * is returned.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @param namespaceUri the target namespace URI.
   * @return a new {@link QualifiedName} in the namespace index indicated by {@code namespaceUri}.
   */
  public QualifiedName reindex(NamespaceTable namespaceTable, String namespaceUri) {
    UShort namespaceIndex = namespaceTable.getIndex(namespaceUri);

    if (namespaceIndex != null && !namespaceIndex.equals(namespaceIndex())) {
      return withNamespaceIndex(namespaceIndex);
    } else {
      return this;
    }
  }

  /**
   * Return a new {@link QualifiedName} with {@code name}.
   *
   * @param name the new name.
   * @return a new {@link QualifiedName} with {@code name}.
   */
  public QualifiedName withName(@Nullable String name) {
    return new QualifiedName(namespaceIndex, name);
  }

  /**
   * Return a new {@link QualifiedName} with {@code namespaceIndex}.
   *
   * @param namespaceIndex thew new namespace index.
   * @return a new {@link QualifiedName} with {@code namespaceIndex}.
   */
  public QualifiedName withNamespaceIndex(UShort namespaceIndex) {
    return new QualifiedName(namespaceIndex, name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    QualifiedName that = (QualifiedName) o;

    return namespaceIndex.equals(that.namespaceIndex)
        && (Objects.equals(name, that.name) || isNull() && that.isNull());
  }

  /**
   * @return a parseable string representation of this {@link QualifiedName}.
   */
  public String toParseableString() {
    return String.format("%s:%s", namespaceIndex.intValue(), name);
  }

  /**
   * Parse a {@link QualifiedName} from its parseable string representation.
   *
   * @param s the string to parse.
   * @return the parsed {@link QualifiedName}.
   */
  public static QualifiedName parse(String s) {
    String[] ss = s.split(":", 2);

    int namespaceIndex = 0;
    String name = s;

    if (ss.length > 1) {
      try {
        namespaceIndex = Short.parseShort(ss[0]);
      } catch (NumberFormatException ignored) {
      }
      name = ss[1];
    }

    return new QualifiedName(ushort(namespaceIndex), name);
  }
}
