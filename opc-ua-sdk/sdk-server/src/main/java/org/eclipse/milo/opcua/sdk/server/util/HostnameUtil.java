/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.LoggerFactory;

public class HostnameUtil {

  /**
   * @return the local hostname, if possible. Failure results in "localhost".
   */
  public static String getHostname() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "localhost";
    }
  }

  /**
   * Given an address resolve it to as many unique addresses or hostnames as can be found.
   *
   * @param address the address to resolve.
   * @return the addresses and hostnames that were resolved from {@code address}.
   */
  public static Set<String> getHostnames(String address) {
    return getHostnames(address, true);
  }

  /**
   * Given an address resolve it to as many unique addresses or hostnames as can be found.
   *
   * @param address the address to resolve.
   * @param includeLoopback if {@code true} loopback addresses will be included in the returned set.
   * @return the addresses and hostnames that were resolved from {@code address}.
   */
  public static Set<String> getHostnames(String address, boolean includeLoopback) {
    return getHostnames(address, includeLoopback, true);
  }

  /**
   * Given an address resolve it to as many unique addresses or hostnames as can be found.
   *
   * @param address the address to resolve.
   * @param includeLoopback if {@code true} loopback addresses will be included in the returned set.
   * @param resolveHostNames if {@code true} a hostname lookup may be performed for each address.
   * @return the addresses and hostnames that were resolved from {@code address}.
   */
  public static Set<String> getHostnames(
      String address, boolean includeLoopback, boolean resolveHostNames) {
    var hostnames = new HashSet<String>();

    try {
      InetAddress inetAddress = InetAddress.getByName(address);

      if (inetAddress.isAnyLocalAddress()) {
        try {
          Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

          for (NetworkInterface ni : Collections.list(nis)) {
            Collections.list(ni.getInetAddresses())
                .forEach(
                    ia -> {
                      if (ia instanceof Inet4Address) {
                        if (includeLoopback || !ia.isLoopbackAddress()) {
                          hostnames.add(ia.getHostAddress());

                          if (resolveHostNames) {
                            hostnames.add(ia.getHostName());
                            hostnames.add(ia.getCanonicalHostName());
                          }
                        }
                      }
                    });
          }
        } catch (SocketException e) {
          LoggerFactory.getLogger(HostnameUtil.class)
              .warn("Failed to NetworkInterfaces for bind address: {}", address, e);
        }
      } else {
        if (includeLoopback || !inetAddress.isLoopbackAddress()) {
          hostnames.add(inetAddress.getHostAddress());

          if (resolveHostNames) {
            hostnames.add(inetAddress.getHostName());
            hostnames.add(inetAddress.getCanonicalHostName());
          }
        }
      }
    } catch (UnknownHostException e) {
      LoggerFactory.getLogger(HostnameUtil.class)
          .warn("Failed to get InetAddress for bind address: {}", address, e);
    }

    return hostnames;
  }
}
