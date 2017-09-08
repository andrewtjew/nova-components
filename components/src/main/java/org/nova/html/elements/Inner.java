
package org.nova.html.elements;


public interface Inner<ELEMENT>
{
    public ELEMENT addInner(Element element);
    public ELEMENT setInner(Element element);
    public ELEMENT addInners(Element...elements);
    public ELEMENT addInner(Object object);
}
