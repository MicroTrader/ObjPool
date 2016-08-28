/*
 * ${PROJECT_NAME} is available under either the terms of the Apache License, Version 2.0 (ASF 2.0)
 * the Academic Free License Version 3.0, (AFL 3.0) or MIT License (MIT). As a recipient of
 * ${PROJECT_NAME}, you may choose which license to receive this code or content under
 * (except as noted in per-module HEADER, COPYRIGHT, COPYING, and / or LICENSE files). Some modules may not
 * be the copyright of the Suminda Sirinath Salpitikorala Dharmasena and ${PROJECT_NAME} Contributors.
 * These modules contain explicit declarations of copyright in both the
 * HEADER, COPYRIGHT, COPYING, and / or LICENSE files in the directories in which they reside and in
 * the code or content itself.
 *
 * No external contributions are allowed under licenses which are fundamentally
 * incompatible with the ASL 2.0, AFL 3.0 and MIT that ${PROJECT_NAME} is distributed under.
 * By contributing to ${PROJECT_NAME} by means of including but not limited to patches,
 * pull requests, code submissions, issues, bug report, documentation, tutorials, wiki pages and edits,
 * code snippets, discussions, email message, chat messages, presentations,
 * and other submissions and contributions will be licensed under the terms of ASL 2.0, AFL 3.0 and MIT
 * where the recipients are free to choose under which license code or content is received under.
 *
 * _______________________________________________________________________________________________________________________
 *
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and ${PROJECT_NAME} Contributors
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
 * _______________________________________________________________________________________________________________________
 *
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and ${PROJECT_NAME} Contributors
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
 * _______________________________________________________________________________________________________________________
 *
 * The MIT License (MIT)
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and ${PROJECT_NAME} Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sakrio.objpool;

import org.jctools.queues.SpscUnboundedArrayQueue;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sirinath on 27/08/2016.
 */
public class JCToolUnBoundedQueueTest {
    @Test
    public void pollPastEnd() {
        SpscUnboundedArrayQueue<Dummy> arrayQueue = new SpscUnboundedArrayQueue<>(100);
        for (int i = 0; i < 10; i++)
            arrayQueue.offer(new Dummy());

        for (int i = 0; i < 10; i++) {
            Dummy d = arrayQueue.poll();
            Assert.assertEquals(i, d.getID());
        }

        Dummy d = arrayQueue.poll();
        Assert.assertNull(d);
    }

    public static class Dummy {
        public static final long defaultVal = 0;
        private static long count = 0;
        private long ID = count++;
        private long aLong = 0L;

        public long getaLong() {
            return aLong;
        }

        public void setaLong(long aLong) {
            this.aLong = aLong;
        }

        public long getID() {
            return ID;
        }

        @Override
        public String toString() {
            return String.format("ID: %d | Value: %d", getID(), getaLong());
        }
    }
}