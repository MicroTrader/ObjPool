/*
 * _______________________________________________________________________________
 *
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and
 *     Project Contributors
 *
 * ${PROJECT_NAME} (the / this "Project") is available under
 * either the terms of
 *  - the Apache License, Version 2.0 (ASF 2.0), or
 *  - the Academic Free License, Version 3.0 (AFL 3.0), or
 *  - the MIT License (MIT), collectively referred as the "Project Licenses".
 * As a recipient of this Project, you may choose which license to receive
 * these artifacts under (except as noted in other explicit declarations
 * and / or notices of intellectual property rights). Some artifacts may not
 * be the intellectual property of Suminda Sirinath Salpitikorala Dharmasena
 * and Project Contributors. These are noted in explicit declarations and / or
 * notices of intellectual property rights.
 *
 * No external contributions are allowed under licenses which are
 * fundamentally incompatible with the Project Licenses under which this
 * Project is licenced under. By contributing or making a submission to
 * this Project you agree that:
 *  - the contributions and / or submissions will be licensed under the terms
 *    of all the Project Licenses whereas the recipients are free to choose
 *    under which license the contributions and submission is received under;
 *  - you own to intellectual property rights (patent rights if patented and
 *    copyrights) of the contribution and / or submission and have the
 *    authority and ability to make the contribution and / or submission
 *    without encumbrances and restrictions;
 *  - you comply and adhere to the adopted code of conduct, norms, etiquettes
 *    and protocols.
 *
 * _______________________________________________________________________________
 *
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and
 *     Project Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * _______________________________________________________________________________
 *
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and
 *     Project Contributors
 *
 * Licensed under the Academic Free License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/AFL-3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * _______________________________________________________________________________
 *
 * The MIT License (MIT)
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and
 *     Project Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * _______________________________________________________________________________
 */

package com.sakrio.objpool;

import org.ObjectLayout.CtorAndArgs;
import org.ObjectLayout.Intrinsic;
import org.ObjectLayout.IntrinsicObjects;
import org.jctools.queues.SpscUnboundedArrayQueue;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

/**
 * Created by sirinath on 26/08/2016.
 */
public final class Pooler<T> implements Supplier<T> {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @Intrinsic
    private final PoolThreadLocal<T> poolThreadLocal;
    private final Supplier<T> factory;

    public Pooler(final Supplier<T> factory, final int chunks, final int numInitObjs) {
        this.factory = factory;

        poolThreadLocal = (PoolThreadLocal<T>) IntrinsicObjects.constructWithin(lookup, "poolThreadLocal", this, new CtorAndArgs(lookup, PoolThreadLocal.class, new Class[]{Integer.TYPE}, chunks));  // new PoolThreadLocal<T>(chunks);

        final Pool<T> pool = poolThreadLocal.get();
        for (int i = 0; i < numInitObjs; i++)
            pool.offer(factory.get());
    }

    @Override
    public final T get() {
        final Pool<T> pool = poolThreadLocal.get();
        final T next = pool.poll();

        if (next == null)
            return factory.get();

        return next;
    }

    public final void returnToPool(final T obj) {
        poolThreadLocal.get().offer(obj);
    }

    public final void trim(final int trimAt) {
        final Pool<T> pool = poolThreadLocal.get();

        while (pool.size() > trimAt) {
            pool.poll();
        }
    }

    private static final class Pool<O> extends SpscUnboundedArrayQueue<O> {
        public Pool(final int chunks) {
            super(chunks);
        }
    }

    private static final class PoolThreadLocal<O> extends ThreadLocal<Pool<O>> {
        private final int chunks;

        public PoolThreadLocal(final int chunks) {
            this.chunks = chunks;
        }

        @Override
        protected Pool<O> initialValue() {
            return new Pool<O>(chunks);
        }
    }
}
