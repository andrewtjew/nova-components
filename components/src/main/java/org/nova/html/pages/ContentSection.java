package org.nova.html.pages;

import java.nio.charset.StandardCharsets;

class ContentSection extends Section
{
	final private byte[] bytes;
	ContentSection(String text)
	{
		this.bytes=text.getBytes(StandardCharsets.UTF_8);
	}
	ContentSection(byte[] bytes)
	{
		this.bytes=bytes;
	}
	@Override
	public byte[] getBytes(ElementMap parameters)
	{
		return this.bytes;
	}
}
