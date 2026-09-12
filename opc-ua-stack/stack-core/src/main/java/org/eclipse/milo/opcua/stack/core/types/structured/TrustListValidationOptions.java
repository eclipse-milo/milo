/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUI32;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.10">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.10</a>
 */
public class TrustListValidationOptions extends OptionSetUI32<TrustListValidationOptions.Field> {
  public TrustListValidationOptions(UInteger value) {
    super(value);
  }

  public boolean getSuppressCertificateExpired() {
    return get(Field.SuppressCertificateExpired);
  }

  public boolean getSuppressHostNameInvalid() {
    return get(Field.SuppressHostNameInvalid);
  }

  public boolean getSuppressRevocationStatusUnknown() {
    return get(Field.SuppressRevocationStatusUnknown);
  }

  public boolean getSuppressIssuerCertificateExpired() {
    return get(Field.SuppressIssuerCertificateExpired);
  }

  public boolean getSuppressIssuerRevocationStatusUnknown() {
    return get(Field.SuppressIssuerRevocationStatusUnknown);
  }

  public boolean getCheckRevocationStatusOnline() {
    return get(Field.CheckRevocationStatusOnline);
  }

  public boolean getCheckRevocationStatusOffline() {
    return get(Field.CheckRevocationStatusOffline);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<TrustListValidationOptions.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", TrustListValidationOptions.class.getSimpleName() + "[", "]");
    joiner.add("suppressCertificateExpired=" + getSuppressCertificateExpired());
    joiner.add("suppressHostNameInvalid=" + getSuppressHostNameInvalid());
    joiner.add("suppressRevocationStatusUnknown=" + getSuppressRevocationStatusUnknown());
    joiner.add("suppressIssuerCertificateExpired=" + getSuppressIssuerCertificateExpired());
    joiner.add(
        "suppressIssuerRevocationStatusUnknown=" + getSuppressIssuerRevocationStatusUnknown());
    joiner.add("checkRevocationStatusOnline=" + getCheckRevocationStatusOnline());
    joiner.add("checkRevocationStatusOffline=" + getCheckRevocationStatusOffline());
    return joiner.toString();
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              0L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "", "Ignore errors related to the validity time of the Certificate."),
              "SuppressCertificateExpired"),
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Ignore mismatches between the host name or ApplicationUri."),
              "SuppressHostNameInvalid"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Ignore errors if the revocation list cannot be found for the issuer of the"
                      + " Certificate."),
              "SuppressRevocationStatusUnknown"),
          new EnumField(
              3L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Ignore errors if an issuer has an expired Certificate."),
              "SuppressIssuerCertificateExpired"),
          new EnumField(
              4L,
              LocalizedText.NULL_VALUE,
              new LocalizedText(
                  "",
                  "Ignore errors if the revocation list cannot be found for any issuer of issuer"
                      + " Certificates."),
              "SuppressIssuerRevocationStatusUnknown"),
          new EnumField(
              5L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Check the revocation status online."),
              "CheckRevocationStatusOnline"),
          new EnumField(
              6L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Check the revocation status offline."),
              "CheckRevocationStatusOffline")
        });
  }

  public static TrustListValidationOptions of(TrustListValidationOptions.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new TrustListValidationOptions(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    SuppressCertificateExpired(0),

    SuppressHostNameInvalid(1),

    SuppressRevocationStatusUnknown(2),

    SuppressIssuerCertificateExpired(3),

    SuppressIssuerRevocationStatusUnknown(4),

    CheckRevocationStatusOnline(5),

    CheckRevocationStatusOffline(6);

    private final int bitIndex;

    Field(int bitIndex) {
      this.bitIndex = bitIndex;
    }

    @Override
    public int getBitIndex() {
      return bitIndex;
    }
  }
}
