/*
 * This file is part of helper, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.buzz.woolwars.game.utils.bucket.factory;

import me.buzz.woolwars.game.utils.bucket.Bucket;
import me.buzz.woolwars.game.utils.bucket.partitioning.PartitioningStrategy;

import java.util.Set;
import java.util.function.Supplier;

/**
 * A set of methods for creating {@link Bucket}s.
 */
public final class BucketFactory {

    private BucketFactory() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static <E> Bucket<E> newBucket(int size, PartitioningStrategy<E> strategy, Supplier<Set<E>> setSupplier) {
        return new SetSuppliedBucket<>(size, strategy, setSupplier);
    }

    public static <E> Bucket<E> newHashSetBucket(int size, PartitioningStrategy<E> strategy) {
        return new HashSetBucket<>(size, strategy);
    }

    public static <E> Bucket<E> newSynchronizedHashSetBucket(int size, PartitioningStrategy<E> strategy) {
        return new SynchronizedHashSetBucket<>(size, strategy);
    }

    public static <E> Bucket<E> newConcurrentBucket(int size, PartitioningStrategy<E> strategy) {
        return new ConcurrentBucket<>(size, strategy);
    }

}