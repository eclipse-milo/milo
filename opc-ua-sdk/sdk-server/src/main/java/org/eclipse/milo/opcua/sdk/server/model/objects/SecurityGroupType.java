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
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1</a>
 */
public interface SecurityGroupType extends BaseObjectType {
  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Double> KEY_LIFETIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyLifetime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UInteger> MAX_FUTURE_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxFutureKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PAST_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPastKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  String getSecurityGroupId();

  void setSecurityGroupId(String value);

  PropertyType getSecurityGroupIdNode();

  Double getKeyLifetime();

  void setKeyLifetime(Double value);

  PropertyType getKeyLifetimeNode();

  String getSecurityPolicyUri();

  void setSecurityPolicyUri(String value);

  PropertyType getSecurityPolicyUriNode();

  UInteger getMaxFutureKeyCount();

  void setMaxFutureKeyCount(UInteger value);

  PropertyType getMaxFutureKeyCountNode();

  UInteger getMaxPastKeyCount();

  void setMaxPastKeyCount(UInteger value);

  PropertyType getMaxPastKeyCountNode();

  MethodNode getInvalidateKeysMethodNode();

  MethodNode getForceKeyRotationMethodNode();

  abstract class InvalidateKeysMethod extends AbstractMethodInvocationHandler {
    public InvalidateKeysMethod(UaMethodNode node) {
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

  abstract class ForceKeyRotationMethod extends AbstractMethodInvocationHandler {
    public ForceKeyRotationMethod(UaMethodNode node) {
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
