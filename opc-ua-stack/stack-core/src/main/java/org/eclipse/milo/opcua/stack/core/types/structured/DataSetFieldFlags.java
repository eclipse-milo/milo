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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.5</a>
 */
public class DataSetFieldFlags extends OptionSetUI16<DataSetFieldFlags.Field> {
  public DataSetFieldFlags(UShort value) {
    super(value);
  }

  public boolean getPromotedField() {
    return get(Field.PromotedField);
  }

  @Override
  public UShort getValue() {
    return (UShort) value;
  }

  @Override
  public Set<DataSetFieldFlags.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DataSetFieldFlags.class.getSimpleName() + "[", "]");
    joiner.add("promotedField=" + getPromotedField());
    return joiner.toString();
  }

  public static DataSetFieldFlags of(DataSetFieldFlags.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new DataSetFieldFlags(UShort.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    PromotedField(0);

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
