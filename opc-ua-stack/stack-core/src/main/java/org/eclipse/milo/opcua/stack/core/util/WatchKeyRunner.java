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

import java.nio.file.ClosedWatchServiceException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchKeyRunner implements Runnable {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final WatchService watchService;
  private final Map<WatchKey, Runnable> watchKeys;

  public WatchKeyRunner(WatchService watchService, Map<WatchKey, Runnable> watchKeys) {
    this.watchService = watchService;
    this.watchKeys = watchKeys;
  }

  @Override
  public void run() {
    while (true) {
      try {
        WatchKey key = watchService.take();

        if (watchKeys.containsKey(key)) {
          boolean runKey =
              key.pollEvents().stream().anyMatch(e -> e.kind() != StandardWatchEventKinds.OVERFLOW);

          if (runKey) {
            watchKeys.get(key).run();
          }
        }

        if (!key.reset()) {
          logger.warn("Failed to reset watch key");
          break;
        }
      } catch (ClosedWatchServiceException e) {
        logger.info("Watcher got closed");
        return;
      } catch (InterruptedException e) {
        logger.error("Watcher interrupted.", e);
      }
    }
  }
}
