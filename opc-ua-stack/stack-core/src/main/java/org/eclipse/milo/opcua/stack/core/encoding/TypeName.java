/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import org.jspecify.annotations.Nullable;

/**
 * DataTypes have names that may be used in the JSON and XML encodings. Consequently, there are some
 * restrictions on the characters that can be used in the name.
 *
 * <p>When a DataType is defined in a UANodeSet it provides a browse name and, optionally, an
 * alternative symbolic name. When the browse name uses characters that cannot be encoded the
 * symbolic name is used as an alternative in the encoding.
 *
 * @param browseName the browse name of the type.
 * @param symbolicName the symbolic name of the type, or {@code null} if not needed.
 * @see <a
 *     href="https://reference.opcfoundation.org/Core/Part6/v105/docs/5.1.13">https://reference.opcfoundation.org/Core/Part6/v105/docs/5.1.13</a>
 */
public record TypeName(String browseName, @Nullable String symbolicName) {

  public TypeName(String browseName) {
    this(browseName, null);
  }

  /**
   * Get the name that is safe to use in JSON and XML encodings.
   *
   * <p>This is {@link #symbolicName} if it is not {@code null}, otherwise {@link #browseName}
   *
   * @return the name that is safe to use in JSON and XML encodings.
   */
  public String getEncodingName() {
    return symbolicName != null ? symbolicName : browseName;
  }
}
