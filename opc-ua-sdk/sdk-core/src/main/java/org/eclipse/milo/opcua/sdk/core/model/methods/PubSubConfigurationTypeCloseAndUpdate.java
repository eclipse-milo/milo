package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Arrays;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the CloseAndUpdate Method of
 * PubSubConfigurationType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.6">Model
 *     documentation</a>
 */
public final class PubSubConfigurationTypeCloseAndUpdate {
  private PubSubConfigurationTypeCloseAndUpdate() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "CloseAndUpdate");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "FileHandle",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=7")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "RequireCompleteUpdate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=1")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "ConfigurationReferences",
          ExpandedNodeId.of(Namespaces.OPC_UA, 25519L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=25519")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "ChangesApplied",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=1")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "ReferencesResults",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=19")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "ConfigurationValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 25520L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=25520")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "ConfigurationObjects",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=17")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, fileHandle, 0, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, requireCompleteUpdate, 1, Boolean.class, -1, new long[] {}, null),
        MethodValues.encode(
            context,
            configurationReferences,
            2,
            PubSubConfigurationRefDataType.class,
            1,
            new long[] {0L},
            null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 3, 3);
      return new Inputs(
          (UInteger)
              MethodValues.decode(context, values, 0, UInteger.class, -1, new long[] {}, null),
          (Boolean) MethodValues.decode(context, values, 1, Boolean.class, -1, new long[] {}, null),
          (@Nullable PubSubConfigurationRefDataType[])
              MethodValues.decode(
                  context,
                  values,
                  2,
                  PubSubConfigurationRefDataType.class,
                  1,
                  new long[] {0L},
                  null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {fileHandle, requireCompleteUpdate, configurationReferences},
              new Object[] {
                value.fileHandle, value.requireCompleteUpdate, value.configurationReferences
              });
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(
          new Object[] {fileHandle, requireCompleteUpdate, configurationReferences});
    }

    @Override
    public String toString() {
      return "Inputs"
          + Arrays.deepToString(
              new Object[] {fileHandle, requireCompleteUpdate, configurationReferences});
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(
      @Nullable Boolean changesApplied,
      StatusCode @Nullable [] referencesResults,
      @Nullable PubSubConfigurationValueDataType @Nullable [] configurationValues,
      NodeId @Nullable [] configurationObjects) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, changesApplied, 0, Boolean.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, referencesResults, 1, StatusCode.class, 1, new long[] {0L}, null),
        MethodValues.encode(
            context,
            configurationValues,
            2,
            PubSubConfigurationValueDataType.class,
            1,
            new long[] {0L},
            null),
        MethodValues.encode(
            context, configurationObjects, 3, NodeId.class, 1, new long[] {0L}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Outputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 4, 4);
      return new Outputs(
          (Boolean) MethodValues.decode(context, values, 0, Boolean.class, -1, new long[] {}, null),
          (StatusCode[])
              MethodValues.decode(context, values, 1, StatusCode.class, 1, new long[] {0L}, null),
          (@Nullable PubSubConfigurationValueDataType[])
              MethodValues.decode(
                  context,
                  values,
                  2,
                  PubSubConfigurationValueDataType.class,
                  1,
                  new long[] {0L},
                  null),
          (NodeId[])
              MethodValues.decode(context, values, 3, NodeId.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(
              new Object[] {
                changesApplied, referencesResults, configurationValues, configurationObjects
              },
              new Object[] {
                value.changesApplied,
                value.referencesResults,
                value.configurationValues,
                value.configurationObjects
              });
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(
          new Object[] {
            changesApplied, referencesResults, configurationValues, configurationObjects
          });
    }

    @Override
    public String toString() {
      return "Outputs"
          + Arrays.deepToString(
              new Object[] {
                changesApplied, referencesResults, configurationValues, configurationObjects
              });
    }
  }
}
