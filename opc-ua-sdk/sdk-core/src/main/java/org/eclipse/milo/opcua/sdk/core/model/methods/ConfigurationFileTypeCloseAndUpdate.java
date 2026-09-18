package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the CloseAndUpdate Method of
 * ConfigurationFileType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2">Model
 *     documentation</a>
 */
public final class ConfigurationFileTypeCloseAndUpdate {
  private ConfigurationFileTypeCloseAndUpdate() {}

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
          "VersionToUpdate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=20998")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "Targets",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15538L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15538")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "RevertAfterTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=290")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "RestartDelayTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=290")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "UpdateResults",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=19")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "NewVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=20998")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "UpdateId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=14")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, fileHandle, 0, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, versionToUpdate, 1, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, targets, 2, ConfigurationUpdateTargetType.class, 1, new long[] {0L}, null),
        MethodValues.encode(context, revertAfterTime, 3, Double.class, -1, new long[] {}, null),
        MethodValues.encode(context, restartDelayTime, 4, Double.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 5, 5);
      return new Inputs(
          (UInteger)
              MethodValues.decode(context, values, 0, UInteger.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 1, UInteger.class, -1, new long[] {}, null),
          (@Nullable ConfigurationUpdateTargetType[])
              MethodValues.decode(
                  context,
                  values,
                  2,
                  ConfigurationUpdateTargetType.class,
                  1,
                  new long[] {0L},
                  null),
          (Double) MethodValues.decode(context, values, 3, Double.class, -1, new long[] {}, null),
          (Double) MethodValues.decode(context, values, 4, Double.class, -1, new long[] {}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {
                fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime
              },
              new Object[] {
                value.fileHandle,
                value.versionToUpdate,
                value.targets,
                value.revertAfterTime,
                value.restartDelayTime
              });
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(
          new Object[] {fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime});
    }

    @Override
    public String toString() {
      return "Inputs"
          + Arrays.deepToString(
              new Object[] {
                fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime
              });
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(
      StatusCode @Nullable [] updateResults,
      @Nullable UInteger newVersion,
      @Nullable UUID updateId) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, updateResults, 0, StatusCode.class, 1, new long[] {0L}, null),
        MethodValues.encode(context, newVersion, 1, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, updateId, 2, UUID.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Outputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 3, 3);
      return new Outputs(
          (StatusCode[])
              MethodValues.decode(context, values, 0, StatusCode.class, 1, new long[] {0L}, null),
          (UInteger)
              MethodValues.decode(context, values, 1, UInteger.class, -1, new long[] {}, null),
          (UUID) MethodValues.decode(context, values, 2, UUID.class, -1, new long[] {}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(
              new Object[] {updateResults, newVersion, updateId},
              new Object[] {value.updateResults, value.newVersion, value.updateId});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {updateResults, newVersion, updateId});
    }

    @Override
    public String toString() {
      return "Outputs" + Arrays.deepToString(new Object[] {updateResults, newVersion, updateId});
    }
  }
}
