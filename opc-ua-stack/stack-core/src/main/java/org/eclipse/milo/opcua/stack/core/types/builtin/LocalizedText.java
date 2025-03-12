/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import java.util.Locale;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @param locale the locale of the text.
 * @param text the text in the specified locale.
 */
@NullMarked
public record LocalizedText(@Nullable String locale, @Nullable String text) {

  public static final LocalizedText NULL_VALUE = new LocalizedText(null, null);

  /**
   * Create a {@link LocalizedText} in English locale.
   *
   * @param text the text in English locale.
   */
  public LocalizedText(@Nullable String text) {
    this(Locale.ENGLISH.getLanguage(), text);
  }

  /**
   * @return the locale of the text.
   */
  public @Nullable String getLocale() {
    return locale;
  }

  /**
   * @return the text in the specified locale.
   */
  public @Nullable String getText() {
    return text;
  }

  /**
   * Check if the {@link LocalizedText} is "null".
   *
   * <p>A {@link LocalizedText} is "null" if both the {@code locale} and {@code text} are null.
   *
   * @return {@code true} if the {@link LocalizedText} is null, {@code false} otherwise.
   */
  public boolean isNull() {
    return locale == null && text == null;
  }

  /**
   * Check if the {@link LocalizedText} is not "null".
   *
   * <p>A {@link LocalizedText} is not "null" if either the {@code locale} or {@code text} is not
   * null.
   *
   * @return {@code true} if the {@link LocalizedText} is not null, {@code false} otherwise.
   */
  public boolean isNotNull() {
    return !isNull();
  }

  /**
   * Create a {@link LocalizedText} in English locale.
   *
   * @param text the text in English locale.
   * @return a {@link LocalizedText} in English locale.
   */
  public static LocalizedText english(@Nullable String text) {
    return new LocalizedText(text);
  }
}
