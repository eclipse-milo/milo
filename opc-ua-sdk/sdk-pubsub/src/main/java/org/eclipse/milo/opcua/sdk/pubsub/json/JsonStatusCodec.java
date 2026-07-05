/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DecodedStatusMessage;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/** JSON Status message codec for OPC UA Part 14 §7.2.5.5.5. */
public final class JsonStatusCodec {

  private JsonStatusCodec() {}

  /**
   * Encode a JSON {@code ua-status} message.
   *
   * <p>{@code IsCyclic} is always emitted, including {@code false} Last Will/offline messages.
   * {@code Timestamp} and {@code NextReportTime} are emitted only for cyclic messages.
   *
   * @param publisherId the PublisherId to report.
   * @param status the PubSub state to report.
   * @param cyclic whether the status is cyclic.
   * @param timestamp the status timestamp; required when {@code cyclic} is true.
   * @param nextReportTime the next expected report time; required when {@code cyclic} is true.
   * @return the UTF-8 encoded JSON document.
   * @throws UaException if a cyclic message is missing its required time fields.
   */
  public static byte[] encode(
      PublisherId publisherId,
      PubSubState status,
      boolean cyclic,
      @Nullable DateTime timestamp,
      @Nullable DateTime nextReportTime)
      throws UaException {

    return encode(
        UUID.randomUUID().toString(), publisherId, status, cyclic, timestamp, nextReportTime);
  }

  /**
   * Encode a JSON {@code ua-status} message with an explicit MessageId.
   *
   * @param messageId the MessageId to emit.
   * @param publisherId the PublisherId to report.
   * @param status the PubSub state to report.
   * @param cyclic whether the status is cyclic.
   * @param timestamp the status timestamp; required when {@code cyclic} is true.
   * @param nextReportTime the next expected report time; required when {@code cyclic} is true.
   * @return the UTF-8 encoded JSON document.
   * @throws UaException if a cyclic message is missing its required time fields.
   */
  public static byte[] encode(
      String messageId,
      PublisherId publisherId,
      PubSubState status,
      boolean cyclic,
      @Nullable DateTime timestamp,
      @Nullable DateTime nextReportTime)
      throws UaException {

    if (cyclic && (timestamp == null || nextReportTime == null)) {
      throw new UaException(
          StatusCodes.Bad_EncodingError,
          "cyclic JSON status messages require Timestamp and NextReportTime");
    }

    try {
      var out = new StringWriter();
      try (var writer = new JsonWriter(out)) {
        writer.setHtmlSafe(false);
        writer.beginObject();
        writer.name("MessageId").value(messageId);
        writer.name("MessageType").value("ua-status");
        writer.name("PublisherId").value(publisherId.toCanonicalString());
        if (cyclic) {
          writer.name("Timestamp").value(timestamp.toIso8601String());
        }
        writer.name("IsCyclic").value(cyclic);
        writer.name("Status").value(status.getValue());
        if (cyclic) {
          writer.name("NextReportTime").value(nextReportTime.toIso8601String());
        }
        writer.endObject();
      }
      return out.toString().getBytes(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UaException(StatusCodes.Bad_EncodingError, "failed to encode JSON status", e);
    }
  }

  static DecodedStatusMessage decode(JsonObject object) throws UaException {
    String messageId = requiredString(object, "MessageId");
    String publisherId = requiredString(object, "PublisherId");
    boolean cyclic = requiredBoolean(object, "IsCyclic");
    PubSubState status = requiredStatus(object, "Status");

    DateTime timestamp = null;
    DateTime nextReportTime = null;
    if (cyclic) {
      timestamp = requiredDateTime(object, "Timestamp");
      nextReportTime = requiredDateTime(object, "NextReportTime");
    }

    return new DecodedStatusMessage(
        messageId, PublisherId.string(publisherId), timestamp, cyclic, status, nextReportTime);
  }

  private static String requiredString(JsonObject object, String name) throws UaException {
    JsonElement element = object.get(name);
    if (element != null && element.isJsonPrimitive()) {
      try {
        return element.getAsString();
      } catch (RuntimeException e) {
        // handled below
      }
    }
    throw badStatus("ua-status message carries no valid " + name);
  }

  private static boolean requiredBoolean(JsonObject object, String name) throws UaException {
    JsonElement element = object.get(name);
    if (element != null && element.isJsonPrimitive()) {
      try {
        return element.getAsBoolean();
      } catch (RuntimeException e) {
        // handled below
      }
    }
    throw badStatus("ua-status message carries no valid " + name);
  }

  private static PubSubState requiredStatus(JsonObject object, String name) throws UaException {
    JsonElement element = object.get(name);
    if (element != null && element.isJsonPrimitive()) {
      try {
        PubSubState state = PubSubState.from(element.getAsInt());
        if (state != null) {
          return state;
        }
      } catch (RuntimeException e) {
        // try the string form below
      }

      try {
        String value = element.getAsString();
        for (PubSubState state : PubSubState.values()) {
          if (state.name().equals(value)) {
            return state;
          }
        }
      } catch (RuntimeException e) {
        // handled below
      }
    }
    throw badStatus("ua-status message carries no valid " + name);
  }

  private static DateTime requiredDateTime(JsonObject object, String name) throws UaException {
    String value = requiredString(object, name);
    try {
      return new DateTime(Instant.parse(value));
    } catch (RuntimeException e) {
      try {
        return new DateTime(OffsetDateTime.parse(value).toInstant());
      } catch (RuntimeException ignored) {
        throw badStatus("ua-status message carries no valid " + name);
      }
    }
  }

  private static UaException badStatus(String message) {
    return new UaException(StatusCodes.Bad_DecodingError, message);
  }
}
