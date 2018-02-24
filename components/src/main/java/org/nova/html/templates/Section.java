package org.nova.html.templates;

import java.io.OutputStream;
import java.util.Map;

import org.nova.html.elements.Composer;
import org.nova.html.templates.ElementMap;

abstract class Section
{
	public abstract void write(Composer composer,ElementMap map) throws Throwable;
}
