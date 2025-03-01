package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUI32;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.8">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.8</a>
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
