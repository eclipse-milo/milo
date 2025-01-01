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

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/** This Built-in DataType contains a qualified name. It is, for example, used as BrowseName. */
public final class QualifiedName {

  public static final QualifiedName NULL_VALUE = new QualifiedName(ushort(0), null);

  private final UShort namespaceIndex;
  private final String name;

  /**
   * The name part of the QualifiedName is restricted to 512 characters.
   *
   * @param namespaceIndex index that identifies the namespace that defines the name. This index is
   *     the index of that namespace in the local Server’s NamespaceArray.
   * @param name the text portion of the QualifiedName.
   */
  public QualifiedName(int namespaceIndex, @Nullable String name) {
    this(ushort(namespaceIndex), name);
  }

  /**
   * The name part of the QualifiedName is restricted to 512 characters.
   *
   * @param namespaceIndex index that identifies the namespace that defines the name. This index is
   *     the index of that namespace in the local Server’s NamespaceArray.
   * @param name the text portion of the QualifiedName.
   */
  public QualifiedName(@NonNull UShort namespaceIndex, @Nullable String name) {
    Preconditions.checkNotNull(namespaceIndex);
    Preconditions.checkArgument(name == null || name.length() <= 512, "name");

    this.namespaceIndex = namespaceIndex;
    this.name = name;
  }

  public UShort getNamespaceIndex() {
    return namespaceIndex;
  }

  @Nullable
  public String getName() {
    return name;
  }

  public boolean isNull() {
    return name == null || name.isEmpty();
  }

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

    if (namespaceIndex != null && !namespaceIndex.equals(getNamespaceIndex())) {
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
  public QualifiedName withNamespaceIndex(@NonNull UShort namespaceIndex) {
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

  @Override
  public int hashCode() {
    int result = namespaceIndex.hashCode();
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("namespaceIndex", namespaceIndex)
        .toString();
  }

  public String toParseableString() {
    return String.format("%s:%s", namespaceIndex.intValue(), name);
  }

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
