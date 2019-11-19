/*******************************************************************************
 * Copyright (C) 2016-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.metrics;

public class LevelSample
{
    final private long level;
    final private long maxLevel;
    final private long maxLevelInstantMs;
    final private long minLevel;
    final private long minLevelInstantMs;
    final private long createdMs;
    final private long baseBase;
    LevelSample(long level,long initial,long maxLevel,long maxLevelInstantMs,long minLevel,long minLevelInstantMs)
    {
        this.baseBase=initial;
        this.level=level;
        this.maxLevel=maxLevel;
        this.maxLevelInstantMs=maxLevelInstantMs;
        this.minLevel=minLevel;
        this.minLevelInstantMs=minLevelInstantMs;
        this.createdMs=System.currentTimeMillis();
    }
    public long getBaseLevel()
    {
        return baseBase;                                                                                                                                                                                                      
    }
    public long getLevel()
    {
        return level;
    }
    public long getMaxLevel()
    {
        return maxLevel;
    }
    public long getMaxLevelInstantMs()
    {
        return maxLevelInstantMs;
    }
    public long getCreatedMs()
    {
        return createdMs;
    }
    public long getMinLevel()
    {
        return minLevel;
    }
    public long getMinLevelInstantMs()
    {
        return minLevelInstantMs;
    }
    
    
}
