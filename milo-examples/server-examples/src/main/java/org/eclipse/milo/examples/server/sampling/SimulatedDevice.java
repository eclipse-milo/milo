/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.server.sampling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

/**
 * A stand-in for a device driver: a few named registers read in one asynchronous bulk request, and
 * a lock an operator can set that withholds the registers from everyone but the admin user.
 *
 * <p>The values are a slow waveform of the time since the device was created, so a subscribed
 * client sees them move.
 */
public final class SimulatedDevice {

  /** The registers the device has, by name. */
  public static final List<String> REGISTERS =
      List.of("Temperature", "Pressure", "FlowRate", "Level");

  private static final long LATENCY_MILLIS = 5;

  private final long startNanos = System.nanoTime();

  private volatile boolean locked = false;

  private final ScheduledExecutorService scheduler;

  public SimulatedDevice(ScheduledExecutorService scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * Read {@code registers} in one request.
   *
   * <p>The future completes on the scheduler after a simulated transport latency, with a value for
   * every register the device has. A register it does not have is absent from the map.
   *
   * @param registers the registers to read.
   * @return a future that completes with the value of each register read.
   */
  public CompletableFuture<Map<String, DataValue>> read(List<String> registers) {
    var future = new CompletableFuture<Map<String, DataValue>>();

    scheduler.schedule(
        () -> {
          var now = DateTime.now();
          var values = new HashMap<String, DataValue>();

          for (String register : registers) {
            if (REGISTERS.contains(register)) {
              values.put(
                  register, new DataValue(new Variant(valueOf(register)), StatusCode.GOOD, now));
            }
          }

          future.complete(values);
        },
        LATENCY_MILLIS,
        TimeUnit.MILLISECONDS);

    return future;
  }

  /**
   * Whether an operator has locked the device, withholding its registers from non-admin users.
   *
   * @return {@code true} if the device is locked.
   */
  public boolean isLocked() {
    return locked;
  }

  /**
   * Lock or unlock the device.
   *
   * @param locked {@code true} to lock the device.
   */
  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  private double valueOf(String register) {
    double seconds = (System.nanoTime() - startNanos) / 1e9;
    int period = 10 * (REGISTERS.indexOf(register) + 1);

    return Math.round((50 + 25 * Math.sin(2 * Math.PI * seconds / period)) * 100) / 100.0;
  }
}
