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

import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** Vendor-specific diagnostic information */
public final class DiagnosticInfo {

  public static final DiagnosticInfo NULL_VALUE =
      new DiagnosticInfo(-1, -1, -1, -1, null, null, null);

  /**
   * The symbolicId is defined within the context of a namespace. This namespace is represented as a
   * string and is conveyed to the Client in the stringTable parameter of the ResponseHeader
   * parameter defined in 7.27. The namespaceUri parameter contains the index into the stringTable
   * for this string. -1 indicates that no string is specified.
   *
   * <p>The namespaceUri shall not be the standard OPC UA namespace. There are no symbolicIds
   * provided for standard StatusCodes.
   */
  private final int namespaceUri;

  /**
   * The symbolicId shall be used to identify a vendor-specific error or condition; typically the
   * result of some server internal operation. The maximum length of this string is 32 characters.
   * Servers wishing to return a numeric return code should convert the return code into a string
   * and use this string as symbolicId (e.g., "0xC0040007" or "-4").
   *
   * <p>This symbolic identifier string is conveyed to the Client in the stringTable parameter of
   * the ResponseHeader parameter defined in 7.27. The symbolicId parameter contains the index into
   * the stringTable for this string. -1 indicates that no string is specified.
   *
   * <p>The symbolicId shall not contain StatusCodes. If the localizedText contains a translation
   * for the description of a StatusCode, the symbolicId is -1.
   */
  private final int symbolicId;

  /**
   * The locale part of the vendor-specific localized text describing the symbolic id.
   *
   * <p>This localized text string is conveyed to the Client in the stringTable parameter of the
   * ResponseHeader parameter defined in 7.27. The localizedText parameter contains the index into
   * the stringTable for this string. -1 indicates that no string is specified.
   */
  private final int locale;

  /**
   * A vendor-specific localized text string describes the symbolic id. The maximum length of this
   * text string is 256 characters.
   *
   * <p>This localized text string is conveyed to the Client in the stringTable parameter of the
   * ResponseHeader parameter defined in 7.27. The localizedTextIndex parameter contains the index
   * into the stringTable for this string. -1 indicates that no string is specified.
   *
   * <p>The localizedText refers to the symbolicId if present or the string that describes the
   * standard StatusCode if the server provides translations. If the index is -1, the server has no
   * translation to return and the client should use the invariant StatusCode description from the
   * specification.
   */
  private final int localizedText;

  /** Vendor-specific diagnostic information. */
  private final String additionalInfo;

  /**
   * The StatusCode from the inner operation.
   *
   * <p>Many applications will make calls into underlying systems during OPC UA request processing.
   * An OPC UA Server has the option of reporting the status from the underlying system in the
   * diagnostic info.
   */
  private final StatusCode innerStatusCode;

  /** The diagnostic info associated with the inner StatusCode. */
  private final DiagnosticInfo innerDiagnosticInfo;

  public DiagnosticInfo(
      int namespaceUri,
      int symbolicId,
      int locale,
      int localizedText,
      @Nullable String additionalInfo,
      @Nullable StatusCode innerStatusCode,
      @Nullable DiagnosticInfo innerDiagnosticInfo) {

    this.namespaceUri = namespaceUri;
    this.symbolicId = symbolicId;
    this.locale = locale;
    this.localizedText = localizedText;
    this.additionalInfo = additionalInfo;
    this.innerStatusCode = innerStatusCode;
    this.innerDiagnosticInfo = innerDiagnosticInfo;
  }

  public int getNamespaceUri() {
    return namespaceUri;
  }

  public int getSymbolicId() {
    return symbolicId;
  }

  public int getLocale() {
    return locale;
  }

  public int getLocalizedText() {
    return localizedText;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public StatusCode getInnerStatusCode() {
    return innerStatusCode;
  }

  public DiagnosticInfo getInnerDiagnosticInfo() {
    return innerDiagnosticInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DiagnosticInfo that = (DiagnosticInfo) o;

    return Objects.equals(symbolicId, that.symbolicId)
        && Objects.equals(namespaceUri, that.namespaceUri)
        && Objects.equals(localizedText, that.localizedText)
        && Objects.equals(locale, that.locale)
        && Objects.equals(additionalInfo, that.additionalInfo)
        && Objects.equals(innerStatusCode, that.innerStatusCode)
        && Objects.equals(innerDiagnosticInfo, that.innerDiagnosticInfo);
  }

  @Override
  public int hashCode() {
    int result = namespaceUri;
    result = 31 * result + symbolicId;
    result = 31 * result + locale;
    result = 31 * result + localizedText;
    result = 31 * result + (additionalInfo != null ? additionalInfo.hashCode() : 0);
    result = 31 * result + (innerStatusCode != null ? innerStatusCode.hashCode() : 0);
    result = 31 * result + (innerDiagnosticInfo != null ? innerDiagnosticInfo.hashCode() : 0);
    return result;
  }
}
