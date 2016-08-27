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
