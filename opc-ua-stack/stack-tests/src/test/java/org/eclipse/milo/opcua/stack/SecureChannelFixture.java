/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.NonceUtil.generateNonce;

import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;

public abstract class SecureChannelFixture extends SecurityFixture {

  protected SecureChannel[] generateChannels(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurity) throws Exception {

    super.setUp();

    ByteString clientNonce = generateNonce(securityPolicy);
    ByteString serverNonce = generateNonce(securityPolicy);

    ClientSecureChannel clientChannel =
        new ClientSecureChannel(
            securityPolicy == SecurityPolicy.None ? null : clientKeyPair,
            securityPolicy == SecurityPolicy.None ? null : clientCertificate,
            securityPolicy == SecurityPolicy.None ? null : List.of(clientCertificate),
            securityPolicy == SecurityPolicy.None ? null : serverCertificate,
            securityPolicy == SecurityPolicy.None ? null : List.of(serverCertificate),
            securityPolicy,
            messageSecurity);

    clientChannel.setLocalNonce(clientNonce);
    clientChannel.setRemoteNonce(serverNonce);

    ServerSecureChannel serverChannel = new ServerSecureChannel();
    serverChannel.setSecurityPolicy(securityPolicy);
    serverChannel.setMessageSecurityMode(messageSecurity);
    serverChannel.setLocalNonce(serverNonce);
    serverChannel.setRemoteNonce(clientNonce);

    if (securityPolicy != SecurityPolicy.None) {
      serverChannel.setKeyPair(serverKeyPair);
      serverChannel.setLocalCertificate(serverCertificate);
      serverChannel.setLocalCertificateChain(new X509Certificate[] {serverCertificate});
      serverChannel.setRemoteCertificate(clientCertificateBytes);
    }

    // Configure the ChannelSecurityToken for clientChannel
    ChannelSecurityToken clientToken =
        new ChannelSecurityToken(uint(0), uint(1), DateTime.now(), uint(60000));

    if (messageSecurity == MessageSecurityMode.None) {
      clientChannel.setChannelSecurity(new ChannelSecurity(null, clientToken));
    } else {
      ChannelSecurity.SecurityKeys clientSecrets =
          ChannelSecurity.generateKeyPair(
              clientChannel, clientChannel.getLocalNonce(), clientChannel.getRemoteNonce());

      clientChannel.setChannelSecurity(new ChannelSecurity(clientSecrets, clientToken));
    }

    // Configure the ChannelSecurityToken for serverChannel
    ChannelSecurityToken serverToken =
        new ChannelSecurityToken(uint(0), uint(1), DateTime.now(), uint(60000));

    if (messageSecurity == MessageSecurityMode.None) {
      serverChannel.setChannelSecurity(new ChannelSecurity(null, serverToken));
    } else {
      ChannelSecurity.SecurityKeys serverSecrets =
          ChannelSecurity.generateKeyPair(
              serverChannel, serverChannel.getRemoteNonce(), serverChannel.getLocalNonce());

      serverChannel.setChannelSecurity(new ChannelSecurity(serverSecrets, serverToken));
    }

    return new SecureChannel[] {clientChannel, serverChannel};
  }

  protected SecureChannel[] generateChannels4096() throws Exception {
    super.setUp();

    SecurityPolicy securityPolicy = SecurityPolicy.Basic256Sha256;
    MessageSecurityMode messageSecurity = MessageSecurityMode.SignAndEncrypt;

    ByteString clientNonce = generateNonce(securityPolicy);
    ByteString serverNonce = generateNonce(securityPolicy);

    ClientSecureChannel clientChannel =
        new ClientSecureChannel(
            clientKeyPair4096,
            clientCertificate4096,
            List.of(clientCertificate4096),
            serverCertificate4096,
            List.of(serverCertificate4096),
            securityPolicy,
            messageSecurity);

    clientChannel.setLocalNonce(clientNonce);
    clientChannel.setRemoteNonce(serverNonce);

    ServerSecureChannel serverChannel = new ServerSecureChannel();
    serverChannel.setSecurityPolicy(securityPolicy);
    serverChannel.setMessageSecurityMode(messageSecurity);
    serverChannel.setLocalNonce(serverNonce);
    serverChannel.setRemoteNonce(clientNonce);

    serverChannel.setKeyPair(serverKeyPair4096);
    serverChannel.setLocalCertificate(serverCertificate4096);
    serverChannel.setLocalCertificateChain(new X509Certificate[] {serverCertificate4096});
    serverChannel.setRemoteCertificate(clientCertificateBytes4096);

    // Configure the ChannelSecurityToken for clientChannel
    ChannelSecurityToken clientToken =
        new ChannelSecurityToken(uint(0), uint(1), DateTime.now(), uint(60000));

    ChannelSecurity.SecurityKeys clientSecrets =
        ChannelSecurity.generateKeyPair(
            clientChannel, clientChannel.getLocalNonce(), clientChannel.getRemoteNonce());

    clientChannel.setChannelSecurity(new ChannelSecurity(clientSecrets, clientToken));

    // Configure the ChannelSecurityToken for serverChannel
    ChannelSecurityToken serverToken =
        new ChannelSecurityToken(uint(0), uint(1), DateTime.now(), uint(60000));

    ChannelSecurity.SecurityKeys serverSecrets =
        ChannelSecurity.generateKeyPair(
            serverChannel, serverChannel.getRemoteNonce(), serverChannel.getLocalNonce());

    serverChannel.setChannelSecurity(new ChannelSecurity(serverSecrets, serverToken));

    return new SecureChannel[] {clientChannel, serverChannel};
  }
}
