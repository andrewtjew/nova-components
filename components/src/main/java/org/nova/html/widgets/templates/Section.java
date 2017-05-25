package org.nova.html.widgets.templates;

import java.io.OutputStream;
import java.util.Map;

import org.nova.html.elements.Composer;

abstract class Section
{
	public abstract void write(Composer composer,ElementMap map) throws Throwable;
}
