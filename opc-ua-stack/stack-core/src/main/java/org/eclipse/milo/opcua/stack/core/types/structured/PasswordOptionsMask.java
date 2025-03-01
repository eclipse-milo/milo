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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.2</a>
 */
public class PasswordOptionsMask extends OptionSetUI32<PasswordOptionsMask.Field> {
  public PasswordOptionsMask(UInteger value) {
    super(value);
  }

  public boolean getSupportInitialPasswordChange() {
    return get(Field.SupportInitialPasswordChange);
  }

  public boolean getSupportDisableUser() {
    return get(Field.SupportDisableUser);
  }

  public boolean getSupportDisableDeleteForUser() {
    return get(Field.SupportDisableDeleteForUser);
  }

  public boolean getSupportNoChangeForUser() {
    return get(Field.SupportNoChangeForUser);
  }

  public boolean getSupportDescriptionForUser() {
    return get(Field.SupportDescriptionForUser);
  }

  public boolean getRequiresUpperCaseCharacters() {
    return get(Field.RequiresUpperCaseCharacters);
  }

  public boolean getRequiresLowerCaseCharacters() {
    return get(Field.RequiresLowerCaseCharacters);
  }

  public boolean getRequiresDigitCharacters() {
    return get(Field.RequiresDigitCharacters);
  }

  public boolean getRequiresSpecialCharacters() {
    return get(Field.RequiresSpecialCharacters);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<PasswordOptionsMask.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PasswordOptionsMask.class.getSimpleName() + "[", "]");
    joiner.add("supportInitialPasswordChange=" + getSupportInitialPasswordChange());
    joiner.add("supportDisableUser=" + getSupportDisableUser());
    joiner.add("supportDisableDeleteForUser=" + getSupportDisableDeleteForUser());
    joiner.add("supportNoChangeForUser=" + getSupportNoChangeForUser());
    joiner.add("supportDescriptionForUser=" + getSupportDescriptionForUser());
    joiner.add("requiresUpperCaseCharacters=" + getRequiresUpperCaseCharacters());
    joiner.add("requiresLowerCaseCharacters=" + getRequiresLowerCaseCharacters());
    joiner.add("requiresDigitCharacters=" + getRequiresDigitCharacters());
    joiner.add("requiresSpecialCharacters=" + getRequiresSpecialCharacters());
    return joiner.toString();
  }

  public static PasswordOptionsMask of(PasswordOptionsMask.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new PasswordOptionsMask(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    SupportInitialPasswordChange(0),

    SupportDisableUser(1),

    SupportDisableDeleteForUser(2),

    SupportNoChangeForUser(3),

    SupportDescriptionForUser(4),

    RequiresUpperCaseCharacters(5),

    RequiresLowerCaseCharacters(6),

    RequiresDigitCharacters(7),

    RequiresSpecialCharacters(8);

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
