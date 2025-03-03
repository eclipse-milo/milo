/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5">https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5</a>
 */
public interface KeyCredentialConfigurationType extends BaseObjectType {
  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String[]> ENDPOINT_URLS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointUrls",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String> CREDENTIAL_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CredentialId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<StatusCode> SERVICE_STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceStatus",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  String getResourceUri();

  void setResourceUri(String value);

  PropertyType getResourceUriNode();

  String getProfileUri();

  void setProfileUri(String value);

  PropertyType getProfileUriNode();

  String[] getEndpointUrls();

  void setEndpointUrls(String[] value);

  PropertyType getEndpointUrlsNode();

  String getCredentialId();

  void setCredentialId(String value);

  PropertyType getCredentialIdNode();

  StatusCode getServiceStatus();

  void setServiceStatus(StatusCode value);

  PropertyType getServiceStatusNode();

  MethodNode getGetEncryptingKeyMethodNode();

  MethodNode getUpdateCredentialMethodNode();

  MethodNode getDeleteCredentialMethodNode();

  abstract class GetEncryptingKeyMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetEncryptingKeyMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "CredentialId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequestedSecurityPolicyUri",
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
                  "PublicKey",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RevisedSecurityPolicyUri",
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
      String credentialId = (String) inputValues[0].getValue();
      String requestedSecurityPolicyUri = (String) inputValues[1].getValue();
      Out<ByteString> publicKey = new Out<>();
      Out<String> revisedSecurityPolicyUri = new Out<>();
      invoke(
          context, credentialId, requestedSecurityPolicyUri, publicKey, revisedSecurityPolicyUri);
      return new Variant[] {
        new Variant(publicKey.get()), new Variant(revisedSecurityPolicyUri.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String credentialId,
        String requestedSecurityPolicyUri,
        Out<ByteString> publicKey,
        Out<String> revisedSecurityPolicyUri)
        throws UaException;
  }

  abstract class UpdateCredentialMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public UpdateCredentialMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "CredentialId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "CredentialSecret",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "CertificateThumbprint",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "SecurityPolicyUri",
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
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      String credentialId = (String) inputValues[0].getValue();
      ByteString credentialSecret = (ByteString) inputValues[1].getValue();
      String certificateThumbprint = (String) inputValues[2].getValue();
      String securityPolicyUri = (String) inputValues[3].getValue();
      invoke(context, credentialId, credentialSecret, certificateThumbprint, securityPolicyUri);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String credentialId,
        ByteString credentialSecret,
        String certificateThumbprint,
        String securityPolicyUri)
        throws UaException;
  }

  abstract class DeleteCredentialMethod extends AbstractMethodInvocationHandler {
    public DeleteCredentialMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {};
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      invoke(context);
      return new Variant[] {};
    }

    protected abstract void invoke(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }
}
