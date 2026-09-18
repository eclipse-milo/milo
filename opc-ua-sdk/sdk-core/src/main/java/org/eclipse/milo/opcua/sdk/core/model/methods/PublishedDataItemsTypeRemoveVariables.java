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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the RemoveVariables Method of
 * PublishedDataItemsType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.3.3">Model
 *     documentation</a>
 */
public final class PublishedDataItemsTypeRemoveVariables {
  private PublishedDataItemsTypeRemoveVariables() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "RemoveVariables");
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
          "VariablesToRemove",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=7")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "NewConfigurationVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14593L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=14593")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "RemoveResults",
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
      UInteger @Nullable [] variablesToRemove) {
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
        MethodValues.encode(context, variablesToRemove, 1, UInteger.class, 1, new long[] {0L}, null)
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
          (UInteger[])
              MethodValues.decode(context, values, 1, UInteger.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {configurationVersion, variablesToRemove},
              new Object[] {value.configurationVersion, value.variablesToRemove});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {configurationVersion, variablesToRemove});
    }

    @Override
    public String toString() {
      return "Inputs" + Arrays.deepToString(new Object[] {configurationVersion, variablesToRemove});
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(
      @Nullable ConfigurationVersionDataType newConfigurationVersion,
      StatusCode @Nullable [] removeResults) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(
            context,
            newConfigurationVersion,
            0,
            ConfigurationVersionDataType.class,
            -1,
            new long[] {},
            null),
        MethodValues.encode(context, removeResults, 1, StatusCode.class, 1, new long[] {0L}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Outputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 2, 2);
      return new Outputs(
          (ConfigurationVersionDataType)
              MethodValues.decode(
                  context, values, 0, ConfigurationVersionDataType.class, -1, new long[] {}, null),
          (StatusCode[])
              MethodValues.decode(context, values, 1, StatusCode.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(
              new Object[] {newConfigurationVersion, removeResults},
              new Object[] {value.newConfigurationVersion, value.removeResults});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {newConfigurationVersion, removeResults});
    }

    @Override
    public String toString() {
      return "Outputs" + Arrays.deepToString(new Object[] {newConfigurationVersion, removeResults});
    }
  }
}
