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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.3</a>
 */
public class UserConfigurationMask extends OptionSetUI32<UserConfigurationMask.Field> {
  public UserConfigurationMask(UInteger value) {
    super(value);
  }

  public boolean getNoDelete() {
    return get(Field.NoDelete);
  }

  public boolean getDisabled() {
    return get(Field.Disabled);
  }

  public boolean getNoChangeByUser() {
    return get(Field.NoChangeByUser);
  }

  public boolean getMustChangePassword() {
    return get(Field.MustChangePassword);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<UserConfigurationMask.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UserConfigurationMask.class.getSimpleName() + "[", "]");
    joiner.add("noDelete=" + getNoDelete());
    joiner.add("disabled=" + getDisabled());
    joiner.add("noChangeByUser=" + getNoChangeByUser());
    joiner.add("mustChangePassword=" + getMustChangePassword());
    return joiner.toString();
  }

  public static UserConfigurationMask of(UserConfigurationMask.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new UserConfigurationMask(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    NoDelete(0),

    Disabled(1),

    NoChangeByUser(2),

    MustChangePassword(3);

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
