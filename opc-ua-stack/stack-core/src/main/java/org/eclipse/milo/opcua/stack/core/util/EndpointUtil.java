/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class EndpointUtil {

  private static final Pattern ENDPOINT_URL_PATTERN =
      Pattern.compile(
          "(opc.tcp|http|https|opc.http|opc.https|opc.ws|opc.wss)://([^:/]+|\\[.*])(:\\d+)?(/.*)?");

  public static @Nullable String getScheme(String endpointUrl) {
    if (endpointUrl != null) {
      Matcher matcher = ENDPOINT_URL_PATTERN.matcher(endpointUrl);

      if (matcher.matches()) {
        return matcher.group(1);
      }
    }

    return null;
  }

  public static @Nullable String getHost(String endpointUrl) {
    if (endpointUrl != null) {
      Matcher matcher = ENDPOINT_URL_PATTERN.matcher(endpointUrl);

      if (matcher.matches()) {
        return matcher.group(2);
      }
    }

    return null;
  }

  public static int getPort(String endpointUrl) {
    if (endpointUrl != null) {
      Matcher matcher = ENDPOINT_URL_PATTERN.matcher(endpointUrl);

      if (matcher.matches()) {
        try {
          String group = matcher.group(3);
          if (group != null && group.startsWith(":")) {
            group = group.substring(1);
            return Integer.parseInt(group);
          }
        } catch (NumberFormatException ignored) {
          // ignored
        }
      }
    }

    return 4840;
  }

  /**
   * Get the path component from an endpoint URL.
   *
   * <p>An empty path becomes "/" and trailing "/" are removed from non-empty paths.
   *
   * @param endpointUrl the endpoint URL.
   * @return the path component from the endpoint URL.
   */
  public static @NonNull String getPath(String endpointUrl) {
    if (endpointUrl != null) {
      Matcher matcher = ENDPOINT_URL_PATTERN.matcher(endpointUrl);

      if (matcher.matches()) {
        String path = matcher.group(4);

        if (path == null || path.isEmpty()) {
          path = "/";
        } else if (path.length() > 1 && path.endsWith("/")) {
          path = path.substring(0, path.length() - 1);
        }

        return path;
      }
    }

    return "/";
  }

  /**
   * Replace the hostname in the endpoint URL field of {@code endpoint} with a new one.
   *
   * <p>All other fields of the original {@link EndpointDescription} remain equal.
   *
   * <p>If the endpoint URL does not match the expected format the original endpoint is returned.
   *
   * @param endpoint the {@link EndpointDescription} to modify.
   * @param hostname the new hostname to use. A null value will result in the original hostname
   *     being used.
   * @return an updated {@link EndpointDescription} in which the hostname of the endpoint URL has
   *     been replaced with {@code hostname}.
   */
  public static EndpointDescription updateUrl(
      @NonNull EndpointDescription endpoint, @Nullable String hostname) {

    return updateUrl(endpoint, hostname, -1);
  }

  /**
   * Replace the hostname and port in the endpoint URL field of {@code endpoint} with new ones.
   *
   * <p>All other fields of the original {@link EndpointDescription} remain equal.
   *
   * <p>If the endpoint URL does not match the expected format the original endpoint is returned.
   *
   * @param endpoint the {@link EndpointDescription} to modify.
   * @param hostname the new hostname to use. A null value will result in the original hostname
   *     being used.
   * @param port the new port to use. Any value less than or equal to 0 will result in the original
   *     port being used.
   * @return an updated {@link EndpointDescription} in which the hostname of the endpoint URL has
   *     been replaced with {@code hostname}.
   */
  public static EndpointDescription updateUrl(
      @NonNull EndpointDescription endpoint, @Nullable String hostname, int port) {

    return new EndpointDescription(
        updateUrl(endpoint.getEndpointUrl(), hostname, port),
        endpoint.getServer(),
        endpoint.getServerCertificate(),
        endpoint.getSecurityMode(),
        endpoint.getSecurityPolicyUri(),
        endpoint.getUserIdentityTokens(),
        endpoint.getTransportProfileUri(),
        endpoint.getSecurityLevel());
  }

  static String updateUrl(String endpointUrl, @Nullable String hostname) {
    return updateUrl(endpointUrl, hostname, -1);
  }

  static String updateUrl(String endpointUrl, @Nullable String hostname, int port) {
    Matcher matcher = ENDPOINT_URL_PATTERN.matcher(endpointUrl);

    if (matcher.matches()) {
      String scheme = matcher.group(1);

      String newHostname = matcher.group(2);
      if (hostname != null) {
        newHostname = hostname;
      }

      String newPort = matcher.group(3); // e.g. ":4840"
      if (port >= 0) {
        newPort = ":" + port;
      } else if (newPort == null) {
        newPort = "";
      }

      String path = matcher.group(4); // e.g. "/" or "/foo" or "/foo/bar"
      if (path == null) path = "";

      return scheme + "://" + newHostname + newPort + path;
    } else {
      return endpointUrl;
    }
  }
}
