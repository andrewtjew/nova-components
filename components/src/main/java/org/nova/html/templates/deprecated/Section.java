package org.nova.html.templates.deprecated;

import org.nova.html.elements.Composer;
import org.nova.html.templates.deprecated.ElementMap;

abstract class Section
{
	public abstract void write(Composer composer,ElementMap map) throws Throwable;
}
