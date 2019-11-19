/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
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
package org.nova.io;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

import org.nova.utils.TypeUtils;

public class MessageDigestOutputStream extends OutputStream
{
	final private OutputStream outputStream;
	final private MessageDigest message;
	
	public MessageDigestOutputStream(MessageDigest messageDigest,OutputStream outputStream)
	{
		this.outputStream=outputStream;
		this.message=messageDigest;
	}

	@Override
	public void write(int b) throws IOException
	{
		this.outputStream.write(b);
		this.message.update(TypeUtils.bigEndianIntToBytes(b));
	}

	@Override
    public void write(byte b[], int off, int len) throws IOException 
	{
		this.outputStream.write(b, off, len);
		this.message.update(b, off, len);
	}
	
	@Override
    public void flush() throws IOException
	{
		this.outputStream.flush();
	}

	@Override
    public void close() throws IOException
	{
		this.outputStream.close();
	}

}
