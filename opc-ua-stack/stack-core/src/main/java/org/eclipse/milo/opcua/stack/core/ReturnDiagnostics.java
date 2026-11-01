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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The diagnostics a client asks a server to return, the {@code returnDiagnostics} mask of a {@link
 * RequestHeader} (OPC 10000-4, 7.32).
 *
 * <p>The mask names the {@link Field}s to return at two levels. Service-level fields fill the
 * {@code serviceDiagnostics} of the response header, which explains the service result, such as the
 * reason for a ServiceFault. Operation-level fields fill the lists that hold one diagnostic per
 * operation of the request, and lists nested in operation results, such as the per-argument
 * diagnostics of a Method call. A server returns only the fields that were requested.
 *
 * <pre>{@code
 * OpcUaClientConfigBuilder builder = OpcUaClientConfig.builder();
 * builder.setReturnDiagnostics(
 *     ReturnDiagnostics.of(Field.SYMBOLIC_ID, Field.LOCALIZED_TEXT, Field.INNER_STATUS_CODE));
 * }</pre>
 *
 * @param serviceLevel the fields requested for the service-level diagnostic.
 * @param operationLevel the fields requested for per-operation diagnostics.
 */
@NullMarked
public record ReturnDiagnostics(Set<Field> serviceLevel, Set<Field> operationLevel) {

  /** Request no diagnostics. A request with this mask carries {@code returnDiagnostics} 0. */
  public static final ReturnDiagnostics NONE = new ReturnDiagnostics(Set.of(), Set.of());

  /** Request every field at both levels. */
  public static final ReturnDiagnostics ALL =
      new ReturnDiagnostics(EnumSet.allOf(Field.class), EnumSet.allOf(Field.class));

  private static final int OPERATION_SHIFT = 5;

  /**
   * @param serviceLevel the fields requested for the service-level diagnostic.
   * @param operationLevel the fields requested for per-operation diagnostics.
   */
  public ReturnDiagnostics {
    serviceLevel = immutableCopy(requireNonNull(serviceLevel, "serviceLevel"));
    operationLevel = immutableCopy(requireNonNull(operationLevel, "operationLevel"));
  }

  /**
   * Request the same fields at both the service and the operation level.
   *
   * @param fields the fields to request.
   * @return a new {@link ReturnDiagnostics}.
   */
  public static ReturnDiagnostics of(Field... fields) {
    EnumSet<Field> set = EnumSet.noneOf(Field.class);
    Collections.addAll(set, fields);

    return new ReturnDiagnostics(set, set);
  }

  /**
   * Decode a {@code returnDiagnostics} mask. Reserved bits are ignored.
   *
   * @param mask the mask from a {@link RequestHeader}; {@code null} is treated as 0.
   * @return the requested diagnostics.
   */
  public static ReturnDiagnostics decode(@Nullable UInteger mask) {
    int bits = mask == null ? 0 : mask.intValue();

    if ((bits & 0x3FF) == 0) {
      return NONE;
    }

    EnumSet<Field> serviceLevel = EnumSet.noneOf(Field.class);
    EnumSet<Field> operationLevel = EnumSet.noneOf(Field.class);

    for (Field field : Field.values()) {
      if ((bits & field.bit) != 0) {
        serviceLevel.add(field);
      }
      if ((bits & (field.bit << OPERATION_SHIFT)) != 0) {
        operationLevel.add(field);
      }
    }

    return new ReturnDiagnostics(serviceLevel, operationLevel);
  }

  /**
   * Encode this as a {@code returnDiagnostics} mask.
   *
   * @return the mask for a {@link RequestHeader}.
   */
  public UInteger encode() {
    int bits = 0;

    for (Field field : serviceLevel) {
      bits |= field.bit;
    }
    for (Field field : operationLevel) {
      bits |= field.bit << OPERATION_SHIFT;
    }

    return uint(bits);
  }

  /**
   * Get the fields requested at {@code level}.
   *
   * @param level the level to get the fields for.
   * @return the fields requested at {@code level}.
   */
  public Set<Field> fields(Level level) {
    return switch (level) {
      case SERVICE -> serviceLevel;
      case OPERATION -> operationLevel;
    };
  }

  /**
   * Check whether any field is requested at {@code level}.
   *
   * @param level the level to check.
   * @return {@code true} if at least one field is requested at {@code level}.
   */
  public boolean isRequested(Level level) {
    return !fields(level).isEmpty();
  }

  /**
   * Check whether no field is requested at either level.
   *
   * @return {@code true} if no diagnostics are requested.
   */
  public boolean isEmpty() {
    return serviceLevel.isEmpty() && operationLevel.isEmpty();
  }

  private static Set<Field> immutableCopy(Collection<Field> fields) {
    if (fields.isEmpty()) {
      return Set.of();
    } else {
      return Collections.unmodifiableSet(EnumSet.copyOf(fields));
    }
  }

  /** A field of a diagnostic a client can request. */
  public enum Field {

    /** The symbolic id and the namespace that qualifies it. */
    SYMBOLIC_ID(0x01),

    /** The localized text and its locale. */
    LOCALIZED_TEXT(0x02),

    /** The vendor-specific additional information. */
    ADDITIONAL_INFO(0x04),

    /** The status of an inner operation. */
    INNER_STATUS_CODE(0x08),

    /**
     * The diagnostic of an inner operation. Its content is determined by the other fields requested
     * at the same level.
     */
    INNER_DIAGNOSTICS(0x10);

    private final int bit;

    Field(int bit) {
      this.bit = bit;
    }
  }

  /** The level a diagnostic belongs to. */
  public enum Level {

    /** The diagnostic of the service as a whole, in the response header. */
    SERVICE,

    /** The diagnostic of one operation, or of a part of one, such as an input argument. */
    OPERATION
  }
}
