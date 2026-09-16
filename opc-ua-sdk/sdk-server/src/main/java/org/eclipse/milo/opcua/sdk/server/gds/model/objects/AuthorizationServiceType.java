/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.gds.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseObjectType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/GDS/docs/9.6.4">https://reference.opcfoundation.org/GDS/docs/9.6.4</a>
 */
public interface AuthorizationServiceType extends BaseObjectType {
  QualifiedProperty<String> SERVICE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/GDS/",
          "ServiceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<ByteString> SERVICE_CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/GDS/",
          "ServiceCertificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  QualifiedProperty<UserTokenPolicy[]> USER_TOKEN_POLICIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/GDS/",
          "UserTokenPolicies",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=304"),
          1,
          UserTokenPolicy[].class);

  QualifiedProperty<String[]> SUPPORTED_ROLES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/GDS/",
          "SupportedRoles",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  String getServiceUri();

  void setServiceUri(String value);

  PropertyType getServiceUriNode();

  ByteString getServiceCertificate();

  void setServiceCertificate(ByteString value);

  PropertyType getServiceCertificateNode();

  UserTokenPolicy[] getUserTokenPolicies();

  void setUserTokenPolicies(UserTokenPolicy[] value);

  PropertyType getUserTokenPoliciesNode();

  String[] getSupportedRoles();

  void setSupportedRoles(String[] value);

  PropertyType getSupportedRolesNode();

  MethodNode getGetServiceDescriptionMethodNode();

  MethodNode getRequestAccessTokenMethodNode();

  MethodNode getStartRequestTokenMethodNode();

  MethodNode getFinishRequestTokenMethodNode();

  MethodNode getRefreshTokenMethodNode();

  abstract class GetServiceDescriptionMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetServiceDescriptionMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {};
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ServiceUri",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "ServiceCertificate",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "UserTokenPolicies",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=304")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      Out<String> serviceUri = new Out<>();
      Out<ByteString> serviceCertificate = new Out<>();
      Out<UserTokenPolicy[]> userTokenPolicies = new Out<>();
      invoke(context, serviceUri, serviceCertificate, userTokenPolicies);
      return new Variant[] {
        new Variant(serviceUri.get()),
        new Variant(serviceCertificate.get()),
        new Variant(userTokenPolicies.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        Out<String> serviceUri,
        Out<ByteString> serviceCertificate,
        Out<UserTokenPolicy[]> userTokenPolicies)
        throws UaException;
  }

  abstract class RequestAccessTokenMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public RequestAccessTokenMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "IdentityToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=316")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "ResourceId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "AccessToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      UserIdentityToken identityToken = (UserIdentityToken) inputValues[0].getValue();
      String resourceId = (String) inputValues[1].getValue();
      Out<String> accessToken = new Out<>();
      invoke(context, identityToken, resourceId, accessToken);
      return new Variant[] {new Variant(accessToken.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UserIdentityToken identityToken,
        String resourceId,
        Out<String> accessToken)
        throws UaException;
  }

  abstract class StartRequestTokenMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public StartRequestTokenMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ResourceId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "PolicyId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequestorData",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ServiceData",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequestId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      String resourceId = (String) inputValues[0].getValue();
      String policyId = (String) inputValues[1].getValue();
      ByteString requestorData = (ByteString) inputValues[2].getValue();
      Out<ByteString> serviceData = new Out<>();
      Out<UUID> requestId = new Out<>();
      invoke(context, resourceId, policyId, requestorData, serviceData, requestId);
      return new Variant[] {new Variant(serviceData.get()), new Variant(requestId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String resourceId,
        String policyId,
        ByteString requestorData,
        Out<ByteString> serviceData,
        Out<UUID> requestId)
        throws UaException;
  }

  abstract class FinishRequestTokenMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public FinishRequestTokenMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "RequestId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequestedRoles",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "UserIdentityToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=316")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "UserTokenSignature",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=456")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "AccessToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "AccessTokenExpiryTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RefreshToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RefreshTokenExpiryTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      UUID requestId = (UUID) inputValues[0].getValue();
      String[] requestedRoles = (String[]) inputValues[1].getValue();
      UserIdentityToken userIdentityToken = (UserIdentityToken) inputValues[2].getValue();
      SignatureData userTokenSignature = (SignatureData) inputValues[3].getValue();
      Out<String> accessToken = new Out<>();
      Out<DateTime> accessTokenExpiryTime = new Out<>();
      Out<String> refreshToken = new Out<>();
      Out<DateTime> refreshTokenExpiryTime = new Out<>();
      invoke(
          context,
          requestId,
          requestedRoles,
          userIdentityToken,
          userTokenSignature,
          accessToken,
          accessTokenExpiryTime,
          refreshToken,
          refreshTokenExpiryTime);
      return new Variant[] {
        new Variant(accessToken.get()),
        new Variant(accessTokenExpiryTime.get()),
        new Variant(refreshToken.get()),
        new Variant(refreshTokenExpiryTime.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UUID requestId,
        String[] requestedRoles,
        UserIdentityToken userIdentityToken,
        SignatureData userTokenSignature,
        Out<String> accessToken,
        Out<DateTime> accessTokenExpiryTime,
        Out<String> refreshToken,
        Out<DateTime> refreshTokenExpiryTime)
        throws UaException;
  }

  abstract class RefreshTokenMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public RefreshTokenMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ResourceId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "CurrentRefreshToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "AccessToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "AccessTokenExpiryTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "NewRefreshToken",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "NewRefreshTokenExpiryTime",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      String resourceId = (String) inputValues[0].getValue();
      String currentRefreshToken = (String) inputValues[1].getValue();
      Out<String> accessToken = new Out<>();
      Out<DateTime> accessTokenExpiryTime = new Out<>();
      Out<String> newRefreshToken = new Out<>();
      Out<DateTime> newRefreshTokenExpiryTime = new Out<>();
      invoke(
          context,
          resourceId,
          currentRefreshToken,
          accessToken,
          accessTokenExpiryTime,
          newRefreshToken,
          newRefreshTokenExpiryTime);
      return new Variant[] {
        new Variant(accessToken.get()),
        new Variant(accessTokenExpiryTime.get()),
        new Variant(newRefreshToken.get()),
        new Variant(newRefreshTokenExpiryTime.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String resourceId,
        String currentRefreshToken,
        Out<String> accessToken,
        Out<DateTime> accessTokenExpiryTime,
        Out<String> newRefreshToken,
        Out<DateTime> newRefreshTokenExpiryTime)
        throws UaException;
  }
}
