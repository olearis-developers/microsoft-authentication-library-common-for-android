//  Copyright (c) Microsoft Corporation.
//  All rights reserved.
//
//  This code is licensed under the MIT License.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files(the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions :
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.
package com.microsoft.identity.client.ui.automation.logging;

import androidx.annotation.NonNull;

public interface ILogger {

    /**
     * Send a {@link LogLevel#ERROR} log message without PII.
     *
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The error message to log.
     */
    void e(@NonNull final String tag,
           @NonNull final String message);

    /**
     * Send a {@link LogLevel#ERROR} log message without PII.
     *
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The error message to log.
     * @param exception An exception to log
     */
    void e(@NonNull final String tag,
           @NonNull final String message,
           @NonNull final Throwable exception);


    /**
     * Send a {@link LogLevel#WARN} log message without PII.
     *
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The error message to log.
     */
    void w(@NonNull final String tag,
           @NonNull final String message);

    /**
     * Send a {@link LogLevel#WARN} log message without PII.
     *
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The error message to log.
     * @param exception An exception to log
     */
    void w(@NonNull final String tag,
           @NonNull final String message,
           @NonNull final Throwable exception);

    /**
     * Send a {@link LogLevel#INFO} log message without PII.
     *
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The error message to log.
     */
    void i(@NonNull final String tag,
           @NonNull final String message);

    /**
     * Send a {@link LogLevel#INFO} log message without PII.
     *
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The error message to log.
     * @param exception An exception to log
     */
    void i(@NonNull final String tag,
           @NonNull final String message,
           @NonNull final Throwable exception);

    /**
     * Send a {@link LogLevel#VERBOSE} log message without PII.
     *
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The error message to log.
     */
    void v(@NonNull final String tag,
           @NonNull final String message);

    /**
     * Send a {@link LogLevel#VERBOSE} log message without PII.
     *
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The error message to log.
     * @param exception An exception to log
     */
    void v(@NonNull final String tag,
           @NonNull final String message,
           @NonNull final Throwable exception);


}
