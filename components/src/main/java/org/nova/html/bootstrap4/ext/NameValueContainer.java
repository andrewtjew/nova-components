package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Col;
import org.nova.html.bootstrap4.Container;
import org.nova.html.bootstrap4.Row;
import org.nova.html.bootstrap4.classes.DeviceClass;
import org.nova.html.bootstrap4.classes.Edge;
import org.nova.html.bootstrap4.classes.Font;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.bootstrap4.classes.TextAlign;
import org.nova.html.elements.Element;

public class NameValueContainer extends Container
{
    final private DeviceClass deviceClass;
    final private int nameSize;

    public NameValueContainer(DeviceClass deviceClass, int nameSize)
    {

        this.deviceClass = deviceClass;
        this.nameSize = nameSize;
    }

    public NameValueContainer add(Object name, Object value, TextAlign valueAlign)
    {
        Row row = returnAddInner(new Row()).my(2);
        addName(row,name);
//        row.returnAddInner(new Col(this.deviceClass, this.nameSize)).pt(1).addInner(name).text(StyleColor.dark).text(TextAlign.right);
        row.returnAddInner(new Col(this.deviceClass, 12 - this.nameSize)).py(1).addInner(value).text(valueAlign);
        return this;
    }

    public NameValueContainer add(Object name, Element element)
    {
        Row row = returnAddInner(new Row()).my(2);
        addName(row,name);
//        row.returnAddInner(new Col(this.deviceClass, this.nameSize)).pt(1).addInner(name).text(StyleColor.dark).text(TextAlign.right);
        row.returnAddInner(new Col(this.deviceClass, 12 - this.nameSize)).py(1).addInner(element);
        return this;
    }

    private void addName(Row row, Object name)
    {
        row.returnAddInner(new Col(this.deviceClass, this.nameSize)).pt(1).text(TextAlign.right).font(Font.weight_bold).addInner(name);

    }

    public NameValueContainer add(Object name, Object value)
    {
        return add(name, value, TextAlign.left);
    }

    public NameValueContainer addLine(Object value, TextAlign valueAlign)
    {
        Row row = returnAddInner(new Row()).my(2);
        row.returnAddInner(new Col(this.deviceClass, 12)).py(1).text(StyleColor.dark).font(Font.weight_bold).addInner(value).text(valueAlign);
        return this;
    }

    public NameValueContainer addLine(Object value)
    {
        return addLine(value, TextAlign.center);
    }

    public NameValueContainer addSpace()
    {
        Row row = returnAddInner(new Row()).my(1).addInner("&nbsp;");
        return this;
    }

    public NameValueContainer addLine()
    {
        Row row = returnAddInner(new Row());
        row.border(Edge.bottom);
        return this;
    }
}
