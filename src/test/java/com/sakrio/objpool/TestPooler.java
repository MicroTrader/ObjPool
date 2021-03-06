/*
 * _______________________________________________________________________________
 *
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and
 *     ObjPool Project Contributors
 *
 * ObjPool, is a collection of works and / or artifacts submitted
 * and / or contributed by multiple authors ("Project Contributors"),
 * collectively creating a larger work(s) and / or artifact(s) (the / this
 * "Project"). This project is licensed under the terms of either:
 *  - the Apache License, Version 2.0 (ASL 2.0), or
 *  - the Academic Free License, Version 3.0 (AFL 3.0), or
 *  - the MIT License (MIT), collectively referred as the "Project Licenses".
 * As a recipient of this Project, you may choose which license to receive
 * the Project under (except as noted in other explicit declarations and / or
 * notices of intellectual property rights). Some artifacts and / or works may
 * not be the intellectual property of the Project Contributors. These are
 * noted in explicit declarations and / or notices of intellectual property
 * rights.
 *
 * This Project uses a shared copyright model. Each contributor maintains
 * copyright over their contributions and / or submissions to this Project.
 * Contributions and / or submissions are typically transformation,
 * modification or adaptation of existing artifacts or works (underlying work)
 * resulting in derivative works. Thus, the Project artifacts, in its entirety
 * is not the copyright of any single person or institution. Instead, it is the
 * collective copyright of all those who have made contributions and / or
 * submissions to the Project.
 *
 * No contributions and / or submissions are allowed under licenses which are
 * fundamentally incompatible with the Project Licenses under which this
 * Project is licenced under. By contributing or making a submission to this
 * Project you hereby understand and agree to the following:
 *  - your contributions and / or submissions are licensed under the terms of
 *    all the Project Licenses whereas the recipients are free to choose under
 *    which license the contributions and submission is received under;
 *  - you will only make contributions and / or submissions which you own the
 *    intellectual property right or have appropriate and adequate licenses,
 *    and in addition have the authority and ability to make the contributions
 *    and / or submissions, under the terms of the Project Licenses, without
 *    encumbrances, limitations and restrictions whatsoever;
 *  - you comply and adhere to the adopted: code of conduct, norms, etiquettes
 *    and protocols of the Project in all you dealing with the Project.
 *
 * _______________________________________________________________________________
 *
 * Copyright (c) 2016. Suminda Sirinath Salpitikorala Dharmasena and
 *     ObjPool Project Contributors
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
 *     ObjPool Project Contributors
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
 *     ObjPool Project Contributors
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

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static com.sakrio.objpool.TestPooler.Dummy.defaultVal;

/**
 * Created by sirinath on 26/08/2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPooler {
    private static final int initObjs = 10;
    private static final int initBuff = 100;
    private static final boolean isBatched = false;
    private static final int trimSize = 15;
    private static final long mutVal = 1;
    private static final int mult = 2;
    private static Pooler<Dummy> pooler = new Pooler<>(() -> new Dummy(), isBatched, initBuff, initObjs);

    @Test
    public void Test1_InitialObjectCreation() {
        System.out.println("===========================");
        System.out.println("Test1_InitialObjectCreation");
        System.out.println("===========================");

        for (int i = 0; i < initObjs * mult; i++) {
            Dummy d = pooler.get();

            System.out.println(d);

            Assert.assertEquals("Pool has only the specified number of objects", i % initObjs, d.getID());

            pooler.returnToPool(d);
        }
    }

    @Test
    public void Test2_MoreObjectCreationThanBuffer() {
        System.out.println("==================================");
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
        System.out.println("===============================");
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
        System.out.println("=====================================================");
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
        System.out.println("=====================================================");
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
        System.out.println("=====================================================");
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
        System.out.println("=====================================================");
        System.out.println("Test4D_TrimmingRetainsOnlyTheSpecifiedNumberOfObjects");
        System.out.println("=====================================================");

        for (int i = 0; i < trimSize; i++) {
            Dummy d = pooler.get();

            System.out.println(d);

            Assert.assertEquals("The pool has object with default value after getting exiting values", defaultVal, d.getaLong());
        }
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
