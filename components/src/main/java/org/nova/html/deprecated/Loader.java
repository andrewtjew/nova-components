package org.nova.html.deprecated;

import org.nova.html.attributes.Color;
import org.nova.html.attributes.Size;
import org.nova.html.attributes.unit;
import org.nova.html.bootstrap4.DataTables.DataTableBootstrap4;
import org.nova.html.ext.Head;
import org.nova.html.tags.div;
import org.nova.html.tags.style;

public class Loader extends div
{
    public Loader(Head head,Size size,Color topColor,Color bottomColor)
    {
        
        String key=DataTableBootstrap4.class.getCanonicalName();
        String style=".loader {  border: 16px solid "+bottomColor.toString()+";  border-radius: 50%;  border-top: 16px solid "+topColor.toString()+";  width: "+size.toString()+";  height: "+size.toString()+";  -webkit-animation: spin 2s linear infinite;animation: spin 1s linear infinite;} @-webkit-keyframes spin {  0% { -webkit-transform: rotate(0deg); }  100% { -webkit-transform: rotate(360deg); }}@keyframes spin {  0% { transform: rotate(0deg); }  100% { transform: rotate(360deg); }}head.add(key, style);}}";
        head.add(key, new style().addInner(style));
        this.addClass("loader");
    }
    public Loader(Head head)
    {
        this(head,new Size(80,unit.px),Color.rgb(0, 0, 0),Color.rgb(220, 220, 220));
    }
}