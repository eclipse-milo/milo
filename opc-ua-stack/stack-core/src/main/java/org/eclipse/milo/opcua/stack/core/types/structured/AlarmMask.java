package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUI16;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/8.3</a>
 */
public class AlarmMask extends OptionSetUI16<AlarmMask.Field> {
  public AlarmMask(UShort value) {
    super(value);
  }

  public boolean getActive() {
    return get(Field.Active);
  }

  public boolean getUnacknowledged() {
    return get(Field.Unacknowledged);
  }

  public boolean getUnconfirmed() {
    return get(Field.Unconfirmed);
  }

  @Override
  public UShort getValue() {
    return (UShort) value;
  }

  @Override
  public Set<AlarmMask.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AlarmMask.class.getSimpleName() + "[", "]");
    joiner.add("active=" + getActive());
    joiner.add("unacknowledged=" + getUnacknowledged());
    joiner.add("unconfirmed=" + getUnconfirmed());
    return joiner.toString();
  }

  public static AlarmMask of(AlarmMask.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new AlarmMask(UShort.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    Active(0),

    Unacknowledged(1),

    Unconfirmed(2);

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
