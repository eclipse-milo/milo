package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUI8;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part3/8.57">https://reference.opcfoundation.org/v105/Core/docs/Part3/8.57</a>
 */
public class AccessLevelType extends OptionSetUI8<AccessLevelType.Field> {
  public AccessLevelType(UByte value) {
    super(value);
  }

  public boolean getCurrentRead() {
    return get(Field.CurrentRead);
  }

  public boolean getCurrentWrite() {
    return get(Field.CurrentWrite);
  }

  public boolean getHistoryRead() {
    return get(Field.HistoryRead);
  }

  public boolean getHistoryWrite() {
    return get(Field.HistoryWrite);
  }

  public boolean getSemanticChange() {
    return get(Field.SemanticChange);
  }

  public boolean getStatusWrite() {
    return get(Field.StatusWrite);
  }

  public boolean getTimestampWrite() {
    return get(Field.TimestampWrite);
  }

  @Override
  public UByte getValue() {
    return (UByte) value;
  }

  @Override
  public Set<AccessLevelType.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AccessLevelType.class.getSimpleName() + "[", "]");
    joiner.add("currentRead=" + getCurrentRead());
    joiner.add("currentWrite=" + getCurrentWrite());
    joiner.add("historyRead=" + getHistoryRead());
    joiner.add("historyWrite=" + getHistoryWrite());
    joiner.add("semanticChange=" + getSemanticChange());
    joiner.add("statusWrite=" + getStatusWrite());
    joiner.add("timestampWrite=" + getTimestampWrite());
    return joiner.toString();
  }

  public static AccessLevelType of(AccessLevelType.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new AccessLevelType(UByte.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    CurrentRead(0),

    CurrentWrite(1),

    HistoryRead(2),

    HistoryWrite(3),

    SemanticChange(4),

    StatusWrite(5),

    TimestampWrite(6);

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
