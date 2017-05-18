package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.nova.core.NameObject;
import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.json.ObjectMapper;

public class DataTable extends Table
{
    static class Objects extends Element
    {
        final private ArrayList<NameObject> list;
        final String id;
        
        public Objects(String id)
        {
            this.id=id;
            this.list=new ArrayList<>();
        }
        public void add(String name,Object object)
        {
            this.list.add(new NameObject(name, object));
        }
        
        @Override
        public String toString()
        {
            try
            {
                StringBuilder sb=new StringBuilder();
                sb.append("$(document).ready(function(){$('#").append(this.id).append("').DataTable(");
                boolean commaNeeded=false;
                for (NameObject item:this.list)
                {
                    if (commaNeeded)
                    {
                        sb.append(',');
                    }
                    else
                    {
                        commaNeeded=true;
                    }
                    sb.append("{'"+item.getName()+"':"+ObjectMapper.write(item.getValue())+"}");
                }
                /*
                if ((this.lengthMenu!=null)&&(this.lengthMenu.length>0))
                {
                    ArrayList<String> labels=new ArrayList<>();
                    for (int i=0;i<this.lengthMenu.length;i++)
                    {
                        if (this.lengthMenu[i]>=0)
                        {
                            labels.add(Integer.toString(this.lengthMenu[i]));
                        }
                        else
                        {
                            labels.add("'All'");
                        }
                    }
                    sb.append("{'lengthMenu': [["+Utils.combine(Utils.toList(this.lengthMenu), ",")+"], ["+Utils.combine(labels, ",")+"]]}");
                }
                */
                sb.append(");});");
                return sb.toString();
            }
            catch (Throwable t)
            {
                return t.getMessage();
            }
        }
        @Override
        public void build(Builder builder) throws Throwable
        {
            builder.getOutputStream().write(this.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
    
    final private String id;
    final private Objects objects;
    
    public DataTable(String id)
    {
        this(null,id);
    }
    
    public DataTable(Head head,String id)
    {
        this(head,id,"display","/resources/html/widgets/DataTable","/datatables.css");
    }
    
    public DataTable(Head head,String id,String class_,String sourcePath,String cssFile)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        id(id);
        class_(class_);
        this.id=id;
        this.objects=new Objects(id);
        this.addInner(new script().addInner(this.objects));
        script script=new script().src(sourcePath+"/datatables.min.js");
        link link=new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile);
        if (head!=null)
        {
            head.add(DataTable.class.getCanonicalName(),script);
            head.add(DataTable.class.getCanonicalName(),link);
        }
        else
        {
            this.addInner(script);
            this.addInner(link);
            
        }
    }

    public DataTable lengthMenu(int...lengths)
    {
        String[] labels=new String[lengths.length];
        for (int i=0;i<labels.length;i++)
        {
            if (lengths[i]>=0)
            {
                labels[i]=Integer.toString(lengths[i]);
            }
            else
            {
                labels[i]="All";
            }
        }
        return lengthMenu(lengths,labels);
    }
    
    public DataTable lengthMenu(int[] lengths,String[] labels)
    {
        this.objects.add("lengthMenu", new Object[]{lengths,labels});
        return this;
    }
    public DataTable addObject(String name,Object object)
    {
        this.objects.add(name, object);
        return this;
    }
    
}
