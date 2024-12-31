/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaDefaultBinaryEncoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEncodingManager implements EncodingManager {

  private final Map<QualifiedName, DataTypeEncoding> encodings = new ConcurrentHashMap<>();

  @Override
  public void registerEncoding(DataTypeEncoding encoding) {
    encodings.put(encoding.getEncodingName(), encoding);
  }

  @Override
  public boolean hasEncoding(QualifiedName encodingName) {
    return encodings.containsKey(encodingName);
  }

  @Override
  public @Nullable DataTypeEncoding getEncoding(QualifiedName encodingName) {
    return encodings.get(encodingName);
  }

  @Override
  public @Nullable DataTypeEncoding removeEncoding(QualifiedName encodingName) {
    return encodings.remove(encodingName);
  }

  private static final String XML_ENCODING_CLASS =
      "org.eclipse.milo.opcua.stack.core.encoding.xml.OpcUaDefaultXmlEncoding";
  private static final String JSON_ENCODING_CLASS =
      "org.eclipse.milo.opcua.stack.core.encoding.json.OpcUaDefaultJsonEncoding";

  public static DefaultEncodingManager createAndInitialize() {
    DefaultEncodingManager encodingManager = new DefaultEncodingManager();
    encodingManager.registerEncoding(OpcUaDefaultBinaryEncoding.getInstance());

    Logger logger = LoggerFactory.getLogger(DefaultEncodingManager.class);

    try {
      Class<?> clazz = DefaultEncodingManager.class.getClassLoader().loadClass(XML_ENCODING_CLASS);
      Method getInstance = clazz.getMethod("getInstance");
      DataTypeEncoding encoding = (DataTypeEncoding) getInstance.invoke(null);

      encodingManager.registerEncoding(encoding);

      logger.debug("Default XML encoding found");
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      logger.debug("Default XML encoding not found");
    }

    try {
      Class<?> clazz = DefaultEncodingManager.class.getClassLoader().loadClass(JSON_ENCODING_CLASS);
      Method getInstance = clazz.getMethod("getInstance");
      DataTypeEncoding encoding = (DataTypeEncoding) getInstance.invoke(null);

      encodingManager.registerEncoding(encoding);

      logger.debug("Default JSON encoding found");
    } catch (ClassNotFoundException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      logger.debug("Default JSON encoding not found");
    }

    return encodingManager;
  }
}
