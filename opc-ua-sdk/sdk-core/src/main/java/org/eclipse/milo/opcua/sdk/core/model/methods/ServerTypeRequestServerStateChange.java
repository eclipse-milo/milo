package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the RequestServerStateChange Method of
 * ServerType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4">Model
 *     documentation</a>
 */
public final class ServerTypeRequestServerStateChange {
  private ServerTypeRequestServerStateChange() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "RequestServerStateChange");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "State",
          ExpandedNodeId.of(Namespaces.OPC_UA, 852L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=852")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "EstimatedReturnTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=13")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "SecondsTillShutdown",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=7")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "Reason",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=21")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "Restart",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=1")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {};
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(
            context, state, 0, ServerState.class, -1, new long[] {}, ServerState::from),
        MethodValues.encode(
            context, estimatedReturnTime, 1, DateTime.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, secondsTillShutdown, 2, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, reason, 3, LocalizedText.class, -1, new long[] {}, null),
        MethodValues.encode(context, restart, 4, Boolean.class, -1, new long[] {}, null)
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
          (ServerState)
              MethodValues.decode(
                  context, values, 0, ServerState.class, -1, new long[] {}, ServerState::from),
          (DateTime)
              MethodValues.decode(context, values, 1, DateTime.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 2, UInteger.class, -1, new long[] {}, null),
          (LocalizedText)
              MethodValues.decode(context, values, 3, LocalizedText.class, -1, new long[] {}, null),
          (Boolean)
              MethodValues.decode(context, values, 4, Boolean.class, -1, new long[] {}, null));
    }
  }
}
