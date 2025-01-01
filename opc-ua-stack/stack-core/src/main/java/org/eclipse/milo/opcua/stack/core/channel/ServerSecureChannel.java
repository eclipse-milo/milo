/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import com.google.common.base.MoreObjects;
import io.netty.util.DefaultAttributeMap;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

public class ServerSecureChannel extends DefaultAttributeMap implements SecureChannel {

  private volatile long channelId = 0;
  private volatile ChannelSecurity channelSecurity;
  private volatile ByteString localNonce = ByteString.NULL_VALUE;
  private volatile ByteString remoteNonce = ByteString.NULL_VALUE;

  private volatile KeyPair keyPair;
  private volatile X509Certificate localCertificate;
  private volatile List<X509Certificate> localCertificateChain;

  private volatile X509Certificate remoteCertificate;
  private volatile List<X509Certificate> remoteCertificateChain;

  private volatile SecurityPolicy securityPolicy;
  private volatile MessageSecurityMode messageSecurityMode;

  public void setChannelId(long channelId) {
    this.channelId = channelId;
  }

  public void setLocalNonce(ByteString localNonce) {
    this.localNonce = localNonce;
  }

  public void setRemoteNonce(ByteString remoteNonce) {
    this.remoteNonce = remoteNonce;
  }

  public void setChannelSecurity(ChannelSecurity channelSecurity) {
    this.channelSecurity = channelSecurity;
  }

  public void setKeyPair(KeyPair keyPair) {
    this.keyPair = keyPair;
  }

  public void setLocalCertificate(X509Certificate localCertificate) {
    this.localCertificate = localCertificate;
  }

  public void setLocalCertificateChain(X509Certificate[] localCertificateChain) {
    this.localCertificateChain = Arrays.asList(localCertificateChain);
  }

  public void setRemoteCertificate(byte[] certificateBytes) throws UaException {
    remoteCertificate = CertificateUtil.decodeCertificate(certificateBytes);
    remoteCertificateChain = CertificateUtil.decodeCertificates(certificateBytes);
  }

  public void setSecurityPolicy(SecurityPolicy securityPolicy) {
    this.securityPolicy = securityPolicy;
  }

  public void setMessageSecurityMode(MessageSecurityMode messageSecurityMode) {
    this.messageSecurityMode = messageSecurityMode;
  }

  @Override
  public KeyPair getKeyPair() {
    return keyPair;
  }

  @Override
  public X509Certificate getLocalCertificate() {
    return localCertificate;
  }

  @Override
  public List<X509Certificate> getLocalCertificateChain() {
    return localCertificateChain;
  }

  @Override
  public X509Certificate getRemoteCertificate() {
    return remoteCertificate;
  }

  @Override
  public List<X509Certificate> getRemoteCertificateChain() {
    return remoteCertificateChain;
  }

  @Override
  public SecurityPolicy getSecurityPolicy() {
    return securityPolicy;
  }

  @Override
  public MessageSecurityMode getMessageSecurityMode() {
    return messageSecurityMode;
  }

  @Override
  public long getChannelId() {
    return channelId;
  }

  @Override
  public ChannelSecurity getChannelSecurity() {
    return channelSecurity;
  }

  @Override
  public ChannelSecurity.SecretKeys getEncryptionKeys(ChannelSecurity.SecurityKeys securityKeys) {
    return securityKeys.getServerKeys();
  }

  @Override
  public ChannelSecurity.SecretKeys getDecryptionKeys(ChannelSecurity.SecurityKeys securityKeys) {
    return securityKeys.getClientKeys();
  }

  @Override
  public ByteString getLocalNonce() {
    return localNonce;
  }

  @Override
  public ByteString getRemoteNonce() {
    return remoteNonce;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("channelId", channelId)
        .add("securityPolicy", securityPolicy)
        .toString();
  }
}
