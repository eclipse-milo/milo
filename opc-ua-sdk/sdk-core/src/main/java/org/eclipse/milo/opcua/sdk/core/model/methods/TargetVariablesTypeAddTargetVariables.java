package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Arrays;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the AddTargetVariables Method of
 * TargetVariablesType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.2.2">Model
 *     documentation</a>
 */
public final class TargetVariablesTypeAddTargetVariables {
  private TargetVariablesTypeAddTargetVariables() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "AddTargetVariables");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "ConfigurationVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14593L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=14593")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "TargetVariablesToAdd",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14744L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=14744")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "AddResults",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=19")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(
            context,
            configurationVersion,
            0,
            ConfigurationVersionDataType.class,
            -1,
            new long[] {},
            null),
        MethodValues.encode(
            context, targetVariablesToAdd, 1, FieldTargetDataType.class, 1, new long[] {0L}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 2, 2);
      return new Inputs(
          (ConfigurationVersionDataType)
              MethodValues.decode(
                  context, values, 0, ConfigurationVersionDataType.class, -1, new long[] {}, null),
          (@Nullable FieldTargetDataType[])
              MethodValues.decode(
                  context, values, 1, FieldTargetDataType.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {configurationVersion, targetVariablesToAdd},
              new Object[] {value.configurationVersion, value.targetVariablesToAdd});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {configurationVersion, targetVariablesToAdd});
    }

    @Override
    public String toString() {
      return "Inputs"
          + Arrays.deepToString(new Object[] {configurationVersion, targetVariablesToAdd});
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(StatusCode @Nullable [] addResults) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, addResults, 0, StatusCode.class, 1, new long[] {0L}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Outputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 1, 1);
      return new Outputs(
          (StatusCode[])
              MethodValues.decode(context, values, 0, StatusCode.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(new Object[] {addResults}, new Object[] {value.addResults});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {addResults});
    }

    @Override
    public String toString() {
      return "Outputs" + Arrays.deepToString(new Object[] {addResults});
    }
  }
}
