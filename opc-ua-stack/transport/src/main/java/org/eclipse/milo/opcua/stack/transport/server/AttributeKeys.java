package org.eclipse.milo.opcua.stack.transport.server;

import io.netty.util.AttributeKey;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public final class AttributeKeys {

  /** AttributeKey for an attribute holding the `System.nanoTime()` of a Channel's creation. */
  public static final AttributeKey<Long> CREATED_AT = AttributeKey.valueOf("created-at");

  /**
   * AttributeKey for an attribute holding the SessionId of the Session associated with a Channel,
   * if there is one.
   */
  public static final AttributeKey<NodeId> SESSION_ID = AttributeKey.valueOf("session-id");

  /** AttributeKey for an attribute holding the endpoint URL from the Client's Hello message. */
  public static final AttributeKey<String> ENDPOINT_URL_KEY = AttributeKey.valueOf("endpoint-url");

  private AttributeKeys() {}
}
