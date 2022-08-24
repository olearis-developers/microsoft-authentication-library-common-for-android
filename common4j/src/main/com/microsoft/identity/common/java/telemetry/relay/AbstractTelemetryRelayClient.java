// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package com.microsoft.identity.common.java.telemetry.relay;

import com.microsoft.identity.common.java.logging.Logger;
import com.microsoft.identity.common.java.telemetry.observers.ITelemetryObserver;

import java.util.Map;

import lombok.NonNull;

/**
 * A relay client gives the flexibility to send telemetry events to a database.
 * It extends a {@link ITelemetryObserver} and applies filter on every event captured before relaying the event to the database.
 *
 * @param <T>
 */
public abstract class AbstractTelemetryRelayClient<T> implements ITelemetryObserver<T> {

    private static final String TAG = AbstractTelemetryRelayClient.class.getSimpleName();
    // Filter to apply on the events captured.
    private ITelemetryEventFilter<T> mEventFilter = null;

    @Override
    public void onReceived(T telemetryData) {
        final String methodTag = TAG + ":onReceived";

        T filteredData = telemetryData;

        if (mEventFilter != null) {
            filteredData = mEventFilter.apply(telemetryData);
        }

        if (filteredData != null) {
            try {
                relayEvent(filteredData);
            } catch (TelemetryRelayException e) {
                Logger.error(methodTag, "Error relaying telemetry data", e);
            }
        }
    }

    /**
     * Add an event filter.
     */
    public void setFilter(ITelemetryEventFilter<T> filter) {
        this.mEventFilter = filter;
    }

    /**
     * Logs a telemetry event to the underlying telemetry implementation.
     */
    public abstract void relayEvent(@NonNull final T eventData) throws TelemetryRelayException;

    /**
     * Logs a telemetry event to the underlying telemetry implementation with the specified tableName.
     */
    public abstract void relayEvent(@NonNull final String tableName, @NonNull final Map<String, String> eventData) throws TelemetryRelayException;

    /**
     * Flush telemetry events to the database
     */
    public abstract void flush();
}
