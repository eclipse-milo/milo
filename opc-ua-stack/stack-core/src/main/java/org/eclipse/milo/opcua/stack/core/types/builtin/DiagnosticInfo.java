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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Vendor-specific diagnostic information
 *
 * @param namespaceUri The symbolicId is defined within the context of a namespace. This namespace
 *     is represented as a string and is conveyed to the Client in the stringTable parameter of the
 *     ResponseHeader parameter defined in 7.27. The namespaceUri parameter contains the index into
 *     the stringTable for this string. -1 indicates that no string is specified.
 *     <p>The namespaceUri shall not be the standard OPC UA namespace. There are no symbolicIds
 *     provided for standard StatusCodes.
 * @param symbolicId The symbolicId shall be used to identify a vendor-specific error or condition;
 *     typically the result of some server internal operation. The maximum length of this string is
 *     32 characters. Servers wishing to return a numeric return code should convert the return code
 *     into a string and use this string as symbolicId (e.g., "0xC0040007" or "-4").
 *     <p>This symbolic identifier string is conveyed to the Client in the stringTable parameter of
 *     the ResponseHeader parameter defined in 7.27. The symbolicId parameter contains the index
 *     into the stringTable for this string. -1 indicates that no string is specified.
 *     <p>The symbolicId shall not contain StatusCodes. If the localizedText contains a translation
 *     for the description of a StatusCode, the symbolicId is -1.
 * @param locale The locale part of the vendor-specific localized text describing the symbolic id.
 *     <p>This localized text string is conveyed to the Client in the stringTable parameter of the
 *     ResponseHeader parameter defined in 7.27. The localizedText parameter contains the index into
 *     the stringTable for this string. -1 indicates that no string is specified.
 * @param localizedText A vendor-specific localized text string describes the symbolic id. The
 *     maximum length of this text string is 256 characters.
 *     <p>This localized text string is conveyed to the Client in the stringTable parameter of the
 *     ResponseHeader parameter defined in 7.27. The localizedTextIndex parameter contains the index
 *     into the stringTable for this string. -1 indicates that no string is specified.
 *     <p>The localizedText refers to the symbolicId if present or the string that describes the
 *     standard StatusCode if the server provides translations. If the index is -1, the server has
 *     no translation to return and the client should use the invariant StatusCode description from
 *     the specification.
 * @param additionalInfo Vendor-specific diagnostic information.
 * @param innerStatusCode The StatusCode from the inner operation.
 *     <p>Many applications will make calls into underlying systems during OPC UA request
 *     processing. An OPC UA Server has the option of reporting the status from the underlying
 *     system in the diagnostic info.
 * @param innerDiagnosticInfo The diagnostic info associated with the inner StatusCode.
 */
@NullMarked
public record DiagnosticInfo(
    int namespaceUri,
    int symbolicId,
    int locale,
    int localizedText,
    @Nullable String additionalInfo,
    @Nullable StatusCode innerStatusCode,
    @Nullable DiagnosticInfo innerDiagnosticInfo) {

  public static final DiagnosticInfo NULL_VALUE =
      new DiagnosticInfo(-1, -1, -1, -1, null, null, null);
}
