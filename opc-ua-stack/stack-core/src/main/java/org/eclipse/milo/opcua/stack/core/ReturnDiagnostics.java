/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * The {@code returnDiagnostics} bit mask of a {@code RequestHeader}, with named constants for the
 * flags defined in OPC 10000-4, Table 178.
 *
 * <p>Combine flags with {@link #of(int...)} and pass the result where a request header or per-call
 * option asks for one:
 *
 * <pre>{@code
 * ReturnDiagnostics diagnostics =
 *     ReturnDiagnostics.of(
 *         ReturnDiagnostics.OPERATION_LEVEL_ADDITIONAL_INFO,
 *         ReturnDiagnostics.OPERATION_LEVEL_LOCALIZED_TEXT);
 * }</pre>
 *
 * Instances are immutable.
 */
public final class ReturnDiagnostics {

  /** ServiceLevel / SymbolicId. */
  public static final int SERVICE_LEVEL_SYMBOLIC_ID = 0x0001;

  /** ServiceLevel / LocalizedText. */
  public static final int SERVICE_LEVEL_LOCALIZED_TEXT = 0x0002;

  /** ServiceLevel / AdditionalInfo. */
  public static final int SERVICE_LEVEL_ADDITIONAL_INFO = 0x0004;

  /** ServiceLevel / Inner StatusCode. */
  public static final int SERVICE_LEVEL_INNER_STATUS_CODE = 0x0008;

  /** ServiceLevel / Inner Diagnostics. */
  public static final int SERVICE_LEVEL_INNER_DIAGNOSTICS = 0x0010;

  /** OperationLevel / SymbolicId. */
  public static final int OPERATION_LEVEL_SYMBOLIC_ID = 0x0020;

  /** OperationLevel / LocalizedText. */
  public static final int OPERATION_LEVEL_LOCALIZED_TEXT = 0x0040;

  /** OperationLevel / AdditionalInfo. */
  public static final int OPERATION_LEVEL_ADDITIONAL_INFO = 0x0080;

  /** OperationLevel / Inner StatusCode. */
  public static final int OPERATION_LEVEL_INNER_STATUS_CODE = 0x0100;

  /** OperationLevel / Inner Diagnostics. */
  public static final int OPERATION_LEVEL_INNER_DIAGNOSTICS = 0x0200;

  /** Every ServiceLevel flag. */
  public static final int SERVICE_LEVEL_ALL =
      SERVICE_LEVEL_SYMBOLIC_ID
          | SERVICE_LEVEL_LOCALIZED_TEXT
          | SERVICE_LEVEL_ADDITIONAL_INFO
          | SERVICE_LEVEL_INNER_STATUS_CODE
          | SERVICE_LEVEL_INNER_DIAGNOSTICS;

  /** Every OperationLevel flag. */
  public static final int OPERATION_LEVEL_ALL =
      OPERATION_LEVEL_SYMBOLIC_ID
          | OPERATION_LEVEL_LOCALIZED_TEXT
          | OPERATION_LEVEL_ADDITIONAL_INFO
          | OPERATION_LEVEL_INNER_STATUS_CODE
          | OPERATION_LEVEL_INNER_DIAGNOSTICS;

  /** No diagnostics requested. */
  public static final ReturnDiagnostics NONE = new ReturnDiagnostics(0);

  /** Every ServiceLevel and OperationLevel flag. */
  public static final ReturnDiagnostics ALL =
      new ReturnDiagnostics(SERVICE_LEVEL_ALL | OPERATION_LEVEL_ALL);

  private final int mask;

  private ReturnDiagnostics(int mask) {
    this.mask = mask;
  }

  /**
   * Combine one or more flags into a mask.
   *
   * @param flags the flags to combine; each is one of the {@code int} constants on this class, or a
   *     mask already built from them.
   * @return the combined mask.
   */
  public static ReturnDiagnostics of(int... flags) {
    int mask = 0;
    for (int flag : flags) {
      mask |= flag;
    }
    return mask == 0 ? NONE : new ReturnDiagnostics(mask);
  }

  /**
   * Wrap the raw mask carried by a request header.
   *
   * @param mask the raw mask; {@code null} is treated as no diagnostics.
   * @return the wrapped mask.
   */
  public static ReturnDiagnostics from(@Nullable UInteger mask) {
    return mask == null ? NONE : of(mask.intValue());
  }

  /**
   * Whether every flag in {@code flags} is set in this mask.
   *
   * @param flags a flag or combination of flags.
   * @return {@code true} if every bit in {@code flags} is set.
   */
  public boolean includes(int flags) {
    return (mask & flags) == flags;
  }

  /**
   * Whether any OperationLevel flag is set.
   *
   * @return {@code true} if the request asks for operation-level diagnostics.
   */
  public boolean includesOperationLevel() {
    return (mask & OPERATION_LEVEL_ALL) != 0;
  }

  /**
   * Whether any ServiceLevel flag is set.
   *
   * @return {@code true} if the request asks for service-level diagnostics.
   */
  public boolean includesServiceLevel() {
    return (mask & SERVICE_LEVEL_ALL) != 0;
  }

  /**
   * Get the raw mask as an {@code int}.
   *
   * @return the raw mask.
   */
  public int getMask() {
    return mask;
  }

  /**
   * Get the raw mask in the form a {@code RequestHeader} carries.
   *
   * @return the raw mask.
   */
  public UInteger toUInteger() {
    return uint(mask);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    return o instanceof ReturnDiagnostics other && other.mask == mask;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(mask);
  }

  @Override
  public String toString() {
    return "ReturnDiagnostics{0x" + Integer.toHexString(mask) + "}";
  }
}
