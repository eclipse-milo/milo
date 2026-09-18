package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.UserConfigurationMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the ModifyUser Method of UserManagementType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.6">Model
 *     documentation</a>
 */
public final class UserManagementTypeModifyUser {
  private UserManagementTypeModifyUser() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "ModifyUser");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "UserName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "ModifyPassword",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=1")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "Password",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "ModifyUserConfiguration",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=1")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "UserConfiguration",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24279L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=24279")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "ModifyDescription",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=1")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "Description",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
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
      @Nullable String userName,
      @Nullable Boolean modifyPassword,
      @Nullable String password,
      @Nullable Boolean modifyUserConfiguration,
      @Nullable UserConfigurationMask userConfiguration,
      @Nullable Boolean modifyDescription,
      @Nullable String description) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, userName, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, modifyPassword, 1, Boolean.class, -1, new long[] {}, null),
        MethodValues.encode(context, password, 2, String.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, modifyUserConfiguration, 3, Boolean.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, userConfiguration, 4, UserConfigurationMask.class, -1, new long[] {}, null),
        MethodValues.encode(context, modifyDescription, 5, Boolean.class, -1, new long[] {}, null),
        MethodValues.encode(context, description, 6, String.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 7, 7);
      return new Inputs(
          (String) MethodValues.decode(context, values, 0, String.class, -1, new long[] {}, null),
          (Boolean) MethodValues.decode(context, values, 1, Boolean.class, -1, new long[] {}, null),
          (String) MethodValues.decode(context, values, 2, String.class, -1, new long[] {}, null),
          (Boolean) MethodValues.decode(context, values, 3, Boolean.class, -1, new long[] {}, null),
          (UserConfigurationMask)
              MethodValues.decode(
                  context, values, 4, UserConfigurationMask.class, -1, new long[] {}, null),
          (Boolean) MethodValues.decode(context, values, 5, Boolean.class, -1, new long[] {}, null),
          (String) MethodValues.decode(context, values, 6, String.class, -1, new long[] {}, null));
    }
  }
}
