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

import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public record StatusCode(long value) {

  private static final long SEVERITY_MASK = 0xC0000000L;
  private static final long SEVERITY_GOOD = 0x00000000L;
  private static final long SEVERITY_UNCERTAIN = 0x40000000L;
  private static final long SEVERITY_BAD = 0x80000000L;

  /** StatusCodes that are considered security-related errors for the purpose of diagnostics. */
  private static final Set<Long> SECURITY_ERRORS =
      Set.of(
          StatusCodes.Bad_UserSignatureInvalid,
          StatusCodes.Bad_UserAccessDenied,
          StatusCodes.Bad_SecurityPolicyRejected,
          StatusCodes.Bad_SecurityModeRejected,
          StatusCodes.Bad_SecurityChecksFailed,
          StatusCodes.Bad_SecureChannelTokenUnknown,
          StatusCodes.Bad_SecureChannelIdInvalid,
          StatusCodes.Bad_NoValidCertificates,
          StatusCodes.Bad_IdentityTokenInvalid,
          StatusCodes.Bad_IdentityTokenRejected,
          StatusCodes.Bad_IdentityChangeNotSupported,
          StatusCodes.Bad_CertificateUseNotAllowed,
          StatusCodes.Bad_CertificateUriInvalid,
          StatusCodes.Bad_CertificateUntrusted,
          StatusCodes.Bad_CertificateTimeInvalid,
          StatusCodes.Bad_CertificateRevoked,
          StatusCodes.Bad_CertificateRevocationUnknown,
          StatusCodes.Bad_CertificateIssuerUseNotAllowed,
          StatusCodes.Bad_CertificateIssuerTimeInvalid,
          StatusCodes.Bad_CertificateIssuerRevoked,
          StatusCodes.Bad_CertificateIssuerRevocationUnknown,
          StatusCodes.Bad_CertificateInvalid,
          StatusCodes.Bad_CertificateHostNameInvalid,
          StatusCodes.Bad_ApplicationSignatureInvalid);

  public static final StatusCode GOOD = new StatusCode(SEVERITY_GOOD);
  public static final StatusCode BAD = new StatusCode(SEVERITY_BAD);
  public static final StatusCode UNCERTAIN = new StatusCode(SEVERITY_UNCERTAIN);

  public StatusCode(int value) {
    this(value & 0xFFFFFFFFL);
  }

  public StatusCode(UInteger value) {
    this(value.longValue());
  }

  public long getValue() {
    return value;
  }

  public boolean isGood() {
    return (value & SEVERITY_MASK) == SEVERITY_GOOD;
  }

  public boolean isBad() {
    return (value & SEVERITY_MASK) == SEVERITY_BAD;
  }

  public boolean isUncertain() {
    return (value & SEVERITY_MASK) == SEVERITY_UNCERTAIN;
  }

  /**
   * Set the DataValue InfoType bits.
   *
   * @return a new {@link StatusCode} with DataValue InfoType bits set.
   */
  public StatusCode withDataValueInfoType() {
    return new StatusCode(value | 0x400);
  }

  /**
   * Clear the DataValue InfoType bits.
   *
   * @return a new {@link StatusCode} with DataValue InfoType bits cleared.
   */
  public StatusCode withoutDataValueInfoType() {
    return new StatusCode(value & ~0x400);
  }

  /**
   * Set the DataValue InfoType and Overflow InfoBits.
   *
   * @return a new {@link StatusCode} DataValue and Overflow bits set.
   */
  public StatusCode withOverflow() {
    return new StatusCode(value | 0x480);
  }

  /**
   * Clear the DataValue InfoType and Overflow InfoBits.
   *
   * @return a new {@link StatusCode} with DataValue and Overflow bits cleared.
   */
  public StatusCode withoutOverflow() {
    return new StatusCode(value & ~0x480);
  }

  /**
   * @return {@code true} if DataValue and Overflow bits are set.
   */
  public boolean isOverflowSet() {
    return (value & 0x480) == 0x480;
  }

  /**
   * @return {@code true} if the StatusCode is considered security-related for the purpose of
   *     diagnostics.
   */
  public boolean isSecurityError() {
    return SECURITY_ERRORS.contains(value);
  }

  /**
   * Get the type of information contained in the info bits.
   *
   * @return the {@link InfoType}.
   */
  public InfoType getInfoType() {
    // isolate bits 10 and 11
    long infoType = (value >> 10) & 0x3;
    if (infoType == 0L) {
      return InfoType.NotUsed;
    } else if (infoType == 1L) {
      return InfoType.DataValue;
    } else {
      return InfoType.Reserved;
    }
  }

  /**
   * @return {@link DataValueInfoBits} if the {@link InfoType} is {@link InfoType#DataValue}.
   */
  public Optional<DataValueInfoBits> getDataValueInfoBits() {
    if (getInfoType() == InfoType.DataValue) {
      // isolate bits 0-9 of value
      int infoBits = (int) (value & 0x3FF);
      return Optional.of(new DataValueInfoBits(infoBits));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", StatusCode.class.getSimpleName() + "[", "]")
        .add("value=0x%08X".formatted(value))
        .add("quality=" + quality(this))
        .toString();
  }

  /**
   * Create a new StatusCode with the given value.
   *
   * @param value the value of the StatusCode.
   * @return a new StatusCode.
   */
  public static StatusCode of(long value) {
    return new StatusCode(value);
  }

  private static String quality(StatusCode statusCode) {
    if (statusCode.isGood()) {
      return "good";
    } else if (statusCode.isBad()) {
      return "bad";
    } else if (statusCode.isUncertain()) {
      return "uncertain";
    } else {
      return "unknown";
    }
  }

  /** Describes the type of information contained in the info bits. */
  public enum InfoType {
    /** The info bits are not used and shall be set to zero. */
    NotUsed,

    /**
     * The StatusCode and its info bits are associated with a DataValue returned from the Server.
     */
    DataValue,

    /** Reserved for future use. The info bits shall be ignored. */
    Reserved
  }

  /** Additional information bits that qualify the StatusCode. */
  public interface InfoBits {

    /**
     * Get the underlying info bits value.
     *
     * @return the underlying info bits value.
     */
    int bits();
  }

  /**
   * Additional information bits that qualify the StatusCode when the {@link InfoType} is {@link
   * InfoType#DataValue}.
   */
  public record DataValueInfoBits(int bits) implements InfoBits {

    /**
     * @return the limit bits associated with the data value.
     */
    public int getLimitBits() {
      // isolate bits 8 and 9
      return (bits >> 8) & 0x3;
    }

    /**
     * @return {@code true} if the value is free to change.
     */
    public boolean isLimitNone() {
      return getLimitBits() == 0;
    }

    /**
     * @return {@code true} if the value is at the lower limit for the datasource.
     */
    public boolean isLimitLow() {
      return getLimitBits() == 1;
    }

    /**
     * @return {@code true} if the value is at the upper limit for the datasource.
     */
    public boolean isLimitHigh() {
      return getLimitBits() == 2;
    }

    /**
     * @return {@code true} if the value is constant and cannot change.
     */
    public boolean isLimitConstant() {
      return getLimitBits() == 3;
    }

    /**
     * This bit shall only be set if the MonitoredItem queue size is greater than 1.
     *
     * <p>If this bit is set, not every detected change has been returned since the Server's queue
     * for the MonitoredItem reached its limit and had to purge out data.
     *
     * @return {@code true} if the MonitoredItem queue overflowed at some point.
     */
    public boolean isOverflow() {
      // isolate bit 7
      return (bits & 0x80) == 0x80;
    }

    public int getReservedBits() {
      // isolate bits 5 and 6
      return (bits >> 5) & 0x3;
    }

    /**
     * These bits are set only when reading historical data. They indicate where the data value came
     * from and provide information that affects how the Client uses the data value.
     *
     * @return the historian bits.
     */
    public int getHistorianBits() {
      // isolate bits 0-4
      return bits & 0x1F;
    }

    /**
     * A raw data value.
     *
     * @return {@code true} if the Raw bits are set.
     */
    public boolean isHistoryRaw() {
      return (getHistorianBits() & 0b00011) == 0b00000;
    }

    /**
     * A data value which was calculated.
     *
     * @return {@code true} if the Calculated bits are set.
     */
    public boolean isHistoryCalculated() {
      return (getHistorianBits() & 0b00011) == 0b00001;
    }

    /**
     * A data value which was interpolated.
     *
     * @return {@code true} if the Interpolated bits are set.
     */
    public boolean isHistoryInterpolated() {
      return (getHistorianBits() & 0b00011) == 0b00010;
    }

    /** Undefined. This value is reserved for future use. */
    public boolean isHistoryReserved() {
      return (getHistorianBits() & 0b00011) == 0b00011;
    }

    /**
     * A data value which was calculated with an incomplete interval.
     *
     * @return {@code true} if the Partial bit is set.
     */
    public boolean isHistoryPartial() {
      return (getHistorianBits() & 0b00100) == 0b00100;
    }

    /**
     * A raw data value that hides other data at the same timestamp.
     *
     * @return {@code true} if the Extra Data bit is set.
     */
    public boolean isHistoryExtraData() {
      return (getHistorianBits() & 0b01000) == 0b01000;
    }

    /**
     * Multiple values match the Aggregate criteria (e.g. multiple minimum values at different
     * timestamps within the same interval).
     *
     * @return {@code true} if the Multi Value bit is set.
     */
    public boolean isHistoryMultiValue() {
      return (getHistorianBits() & 0b10000) == 0b10000;
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", DataValueInfoBits.class.getSimpleName() + "[", "]")
          .add("bits=" + Integer.toBinaryString(bits).substring(0, 10))
          .toString();
    }
  }
}
