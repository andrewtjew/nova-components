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
package org.nova.validation;

import java.util.HashSet;

public class LongValidator extends Validator<Long>
{
    private Long max;
    private Long min;
    private boolean required;
    private HashSet<Long> options;

    public LongValidator(Long min,Long max,boolean required,long[] options)
    {
        this.max=max;
        this.min=min;
        this.required=required;
        if (options!=null)
        {
            this.options=new HashSet<>();
            for (long option:options)
            {
                this.options.add(option);
            }
        }
    }
    
    @Override
    public ValidationViolation validate(Long value) throws Throwable
    {
        if (value!=null)
        {
            if ((this.min!=null)&&(value<this.min))
            {
                return new ValidationViolation(Violation.MIN);
            }
            if ((this.max!=null)&&(value<this.max))
            {
                return new ValidationViolation(Violation.MAX);
            }
            if (this.options!=null)
            {
                if (this.options.contains(value)==false)
                {
                    return new ValidationViolation(Violation.OPTIONS);
                }
            }
        }
        else if (this.required)
        {
            return new ValidationViolation(Violation.REQUIRED);
        }
        return null;
        
    }

    
}
