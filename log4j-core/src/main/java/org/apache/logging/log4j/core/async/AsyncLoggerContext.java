/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.async;

import java.net.URI;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
import org.apache.logging.log4j.message.MessageFactory;

/**
 * {@code LoggerContext} that creates {@code AsyncLogger} objects.
 */
public class AsyncLoggerContext extends LoggerContext {

    private static final long serialVersionUID = 1L;
    
    private final AsyncLoggerHelper helper;

    public AsyncLoggerContext(final String name) {
        super(name);
        helper = new AsyncLoggerHelper();
    }

    public AsyncLoggerContext(final String name, final Object externalContext) {
        super(name, externalContext);
        helper = new AsyncLoggerHelper();
    }

    public AsyncLoggerContext(final String name, final Object externalContext,
            final URI configLocn) {
        super(name, externalContext, configLocn);
        helper = new AsyncLoggerHelper();
    }

    public AsyncLoggerContext(final String name, final Object externalContext,
            final String configLocn) {
        super(name, externalContext, configLocn);
        helper = new AsyncLoggerHelper();
    }

    @Override
    protected Logger newInstance(final LoggerContext ctx, final String name,
            final MessageFactory messageFactory) {
        return new AsyncLogger(ctx, name, messageFactory, helper);
    }

    /* (non-Javadoc)
     * @see org.apache.logging.log4j.core.LoggerContext#start()
     */
    @Override
    public void start() {
        helper.start();
        super.start();
    }

    /* (non-Javadoc)
     * @see org.apache.logging.log4j.core.LoggerContext#start(org.apache.logging.log4j.core.config.Configuration)
     */
    @Override
    public void start(Configuration config) {
        helper.start();
        super.start(config);
    }

    @Override
    public void stop() {
        helper.stop(); // first stop Disruptor
        super.stop();
    }

    /**
     * Creates and returns a new {@code RingBufferAdmin} that instruments the ringbuffer of the {@code AsyncLogger}
     * objects.
     *
     * @param contextName name of this {@code AsyncLoggerContext}
     * @return a new {@code RingBufferAdmin} that instruments the ringbuffer
     */
    public RingBufferAdmin createRingBufferAdmin() {
        return helper.createRingBufferAdmin(getName());
    }
}
