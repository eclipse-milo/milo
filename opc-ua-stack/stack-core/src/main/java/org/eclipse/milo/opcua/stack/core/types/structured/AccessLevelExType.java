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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part3/8.58">https://reference.opcfoundation.org/v105/Core/docs/Part3/8.58</a>
 */
public class AccessLevelExType extends OptionSetUI32<AccessLevelExType.Field> {
  public AccessLevelExType(UInteger value) {
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

  public boolean getNonatomicRead() {
    return get(Field.NonatomicRead);
  }

  public boolean getNonatomicWrite() {
    return get(Field.NonatomicWrite);
  }

  public boolean getWriteFullArrayOnly() {
    return get(Field.WriteFullArrayOnly);
  }

  public boolean getNoSubDataTypes() {
    return get(Field.NoSubDataTypes);
  }

  public boolean getNonVolatile() {
    return get(Field.NonVolatile);
  }

  public boolean getConstant() {
    return get(Field.Constant);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<AccessLevelExType.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AccessLevelExType.class.getSimpleName() + "[", "]");
    joiner.add("currentRead=" + getCurrentRead());
    joiner.add("currentWrite=" + getCurrentWrite());
    joiner.add("historyRead=" + getHistoryRead());
    joiner.add("historyWrite=" + getHistoryWrite());
    joiner.add("semanticChange=" + getSemanticChange());
    joiner.add("statusWrite=" + getStatusWrite());
    joiner.add("timestampWrite=" + getTimestampWrite());
    joiner.add("nonatomicRead=" + getNonatomicRead());
    joiner.add("nonatomicWrite=" + getNonatomicWrite());
    joiner.add("writeFullArrayOnly=" + getWriteFullArrayOnly());
    joiner.add("noSubDataTypes=" + getNoSubDataTypes());
    joiner.add("nonVolatile=" + getNonVolatile());
    joiner.add("constant=" + getConstant());
    return joiner.toString();
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "CurrentRead"),
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "CurrentWrite"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "HistoryRead"),
          new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "HistoryWrite"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "SemanticChange"),
          new EnumField(5L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "StatusWrite"),
          new EnumField(6L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "TimestampWrite"),
          new EnumField(8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NonatomicRead"),
          new EnumField(9L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NonatomicWrite"),
          new EnumField(
              10L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "WriteFullArrayOnly"),
          new EnumField(11L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NoSubDataTypes"),
          new EnumField(12L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NonVolatile"),
          new EnumField(13L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Constant")
        });
  }

  public static AccessLevelExType of(AccessLevelExType.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new AccessLevelExType(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    CurrentRead(0),

    CurrentWrite(1),

    HistoryRead(2),

    HistoryWrite(3),

    SemanticChange(4),

    StatusWrite(5),

    TimestampWrite(6),

    NonatomicRead(8),

    NonatomicWrite(9),

    WriteFullArrayOnly(10),

    NoSubDataTypes(11),

    NonVolatile(12),

    Constant(13);

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
