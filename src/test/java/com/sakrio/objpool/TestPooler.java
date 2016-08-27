/*
 * ${PROJECT_NAME} is available under either the terms of the Apache License, Version 2.0 (ASF 2.0)
 * the Academic Free License Version 3.0, (AFL 3.0) or MIT License (MIT). As a recipient of
 * ${PROJECT_NAME}, you may choose which license to receive this code or content under
 * (except as noted in per-module LICENSE files). Some modules may not be the copyright
 * of the Suminda Sirinath Salpitikorala Dharmasena and Project Contributors.
 * These modules contain explicit declarations of copyright in both the LICENSE files
 * in the directories in which they reside and in the code or content itself.
 *
 * No external contributions are allowed under licenses which are fundamentally
 * incompatible with the ASL 2.0, AFL 3.0 and MIT that ${PROJECT_NAME} is distributed under.
 * By contributing to ${PROJECT_NAME} by means of including but not limited to patches,
 * pull requests, code submissions, issues, bug report, code snippets, discussions,
 * email message, chat messages such content will be licensed under the terms of
 * ASL 2.0, AFL 3.0 and MIT where the recipients are free to choose under which license
 * code or content is received under.
 *
 * ______________________________________________________________________________________
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
 * ______________________________________________________________________________________
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
 * ______________________________________________________________________________________
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

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static com.sakrio.objpool.Dummy.defaultVal;

/**
 * Created by sirinath on 26/08/2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPooler {
    private static final int initObjs = 10;
    private static final int initBuff = 100;
    private static final int trimSize = 15;
    private static final long mutVal = 1;
    private static final int mult = 2;
    private static final Pooler<Dummy> pooler = new Pooler<>(() -> new Dummy(), initBuff, initObjs);

    @Test
    public void Test1_InitialObjectCreation() {
        System.out.println("Test1_InitialObjectCreation");
        System.out.println("===========================");

        for (int i = 0; i < initBuff * mult; i++) {
            Dummy d = pooler.get();

            System.out.println("Default: " + d);

            Assert.assertEquals("Pool has only the specified number of objects", i % initObjs, d.getID());

            pooler.returnToPool(d);
        }
    }

    @Test
    public void Test2_MoreObjectCreationThanBuffer() {
        System.out.println("Test2_MoreObjectCreationThanBuffer");
        System.out.println("==================================");

        ArrayList<Dummy> arrayList = new ArrayList<>();

        for (int i = 0; i < initBuff * mult; i++) {
            Dummy d = pooler.get();

            System.out.println("Default: " + d);

            Assert.assertEquals("Pool has objects with default value", defaultVal, d.getaLong());
            d.setaLong(1L);

            System.out.println("Mutated: " + d);

            Assert.assertNotEquals("The pool object is mutated", defaultVal, d.getaLong());
            arrayList.add(d);
        }

        for (Dummy d : arrayList) {
            System.out.println("Returning: " + d);
            pooler.returnToPool(d);
        }
    }

    @Test
    public void Test3_PoolRetainsCreatedObjects() {
        System.out.println("Test3_PoolRetainsCreatedObjects");
        System.out.println("===============================");

        for (int i = 0; i < initBuff * mult; i++) {
            Dummy d = pooler.get();

            System.out.println(d);

            Assert.assertEquals("The pool has object with new value", mutVal, d.getaLong());
            pooler.returnToPool(d);
        }
    }

    @Test
    public void Test4A_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects() {
        System.out.println("Test4A_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects");
        System.out.println("=====================================================");

        pooler.trim(trimSize);

        for (int i = 0; i < trimSize; i++) {
            Dummy d = pooler.get();

            System.out.println(d);

            Assert.assertEquals("The pool has objects with mutated value up to the trimmed value", mutVal, d.getaLong());
            pooler.returnToPool(d);
        }
    }

    @Test
    public void Test4B_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects() {
        System.out.println("Test4B_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects");
        System.out.println("=====================================================");

        for (int i = 0; i < trimSize * mult; i++) {
            Dummy d = pooler.get();

            System.out.println(d);

            Assert.assertNotEquals("All the pool objects are mutated", defaultVal, d.getaLong());
            pooler.returnToPool(d);
        }
    }

    @Test
    public void Test4C_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects() {
        System.out.println("Test4C_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects");
        System.out.println("=====================================================");

        for (int i = 0; i < trimSize; i++) {
            Dummy d = pooler.get();

            System.out.println(d);

            Assert.assertEquals("All the pool objects are mutated", mutVal, d.getaLong());
        }
    }

    @Test
    public void Test4D_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects() {
        System.out.println("Test4D_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects");
        System.out.println("=====================================================");

        for (int i = 0; i < trimSize; i++) {
            Dummy d = pooler.get();

            System.out.println(d);

            Assert.assertEquals("The pool has object with default value after getting exiting values", defaultVal, d.getaLong());
        }
    }
}
