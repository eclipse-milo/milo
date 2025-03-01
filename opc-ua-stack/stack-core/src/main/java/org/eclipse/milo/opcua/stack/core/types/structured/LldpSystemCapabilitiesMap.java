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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.3/#5.3.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.3/#5.3.3.1</a>
 */
public class LldpSystemCapabilitiesMap extends OptionSetUI32<LldpSystemCapabilitiesMap.Field> {
  public LldpSystemCapabilitiesMap(UInteger value) {
    super(value);
  }

  public boolean getOther() {
    return get(Field.Other);
  }

  public boolean getRepeater() {
    return get(Field.Repeater);
  }

  public boolean getBridge() {
    return get(Field.Bridge);
  }

  public boolean getWlanAccessPoint() {
    return get(Field.WlanAccessPoint);
  }

  public boolean getRouter() {
    return get(Field.Router);
  }

  public boolean getTelephone() {
    return get(Field.Telephone);
  }

  public boolean getDocsisCableDevice() {
    return get(Field.DocsisCableDevice);
  }

  public boolean getStationOnly() {
    return get(Field.StationOnly);
  }

  public boolean getCvlanComponent() {
    return get(Field.CvlanComponent);
  }

  public boolean getSvlanComponent() {
    return get(Field.SvlanComponent);
  }

  public boolean getTwoPortMacRelay() {
    return get(Field.TwoPortMacRelay);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<LldpSystemCapabilitiesMap.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LldpSystemCapabilitiesMap.class.getSimpleName() + "[", "]");
    joiner.add("other=" + getOther());
    joiner.add("repeater=" + getRepeater());
    joiner.add("bridge=" + getBridge());
    joiner.add("wlanAccessPoint=" + getWlanAccessPoint());
    joiner.add("router=" + getRouter());
    joiner.add("telephone=" + getTelephone());
    joiner.add("docsisCableDevice=" + getDocsisCableDevice());
    joiner.add("stationOnly=" + getStationOnly());
    joiner.add("cvlanComponent=" + getCvlanComponent());
    joiner.add("svlanComponent=" + getSvlanComponent());
    joiner.add("twoPortMacRelay=" + getTwoPortMacRelay());
    return joiner.toString();
  }

  public static LldpSystemCapabilitiesMap of(LldpSystemCapabilitiesMap.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new LldpSystemCapabilitiesMap(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    Other(0),

    Repeater(1),

    Bridge(2),

    WlanAccessPoint(3),

    Router(4),

    Telephone(5),

    DocsisCableDevice(6),

    StationOnly(7),

    CvlanComponent(8),

    SvlanComponent(9),

    TwoPortMacRelay(10);

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
