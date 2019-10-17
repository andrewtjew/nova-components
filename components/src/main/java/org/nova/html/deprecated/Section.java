package org.nova.html.deprecated;

import org.nova.html.deprecated.ElementMap;
import org.nova.html.elements.Composer;

abstract class Section
{
	public abstract void write(Composer composer,ElementMap map) throws Throwable;
}
