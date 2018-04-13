package org.nova.html.widgets;

import java.util.ArrayList;

import org.nova.core.NameObject;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.link;
import org.nova.html.tags.script;
import org.nova.json.ObjectMapper;

//!!! Requires jquery

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
                sb.append('{');
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
                    sb.append('\''+item.getName()+"':");
                    Object value=item.getValue();
                    if (value==null)
                    {
                        sb.append("null");
                        continue;
                    }
                    Class<?> type=value.getClass();
                    if (type==Boolean.class)
                    {
                        sb.append(value);
                        
                    }
                    else if (type==Float.class)
                    {
                        sb.append(value);
                        
                    }
                    else if (type==Double.class)
                    {
                        sb.append(value);
                        
                    }
                    else if (type==Short.class)
                    {
                        sb.append(value);
                        
                    }
                    else if (type==Integer.class)
                    {
                        sb.append(value);
                        
                    }
                    else if (type==Long.class)
                    {
                        sb.append(value);
                        
                    }
                    else if (type==String.class)
                    {
                        //Properly do this the JSON way
                        sb.append('"');
                        sb.append(value);
                        sb.append('"');
                    }
                    else
                    {
//                        sb.append("{'"+item.getName()+"':"+ObjectMapper.write(item.getValue())+"}");
                        sb.append(ObjectMapper.write(value));
                    }
                }
                sb.append('}');
                sb.append(");});");
                return sb.toString();
            }
            catch (Throwable t)
            {
                return t.getMessage();
            }
        }
        @Override
        public void compose(Composer composer) throws Throwable
        {
            composer.getStringBuilder().append(toString());
        }
    }
    
    final private Objects objects;
    
    public DataTable(Head head)
    {
        this(head,null);
    }
    
    public DataTable(Head head,String class_)
    {
        this(head,null,class_,"/resources/html/widgets/DataTable/datatables.min.css","/resources/html/widgets/DataTable/datatables.min.js");
    }
    
    public DataTable(Head head,String id,String class_,String cssFilePath,String scriptFilePath)
    {
        super(head,class_,cssFilePath);
        if (id==null)
        {
            id=HtmlUtils.generateId(this);
        }
        this.table().id(id);
        this.objects=new Objects(id);
        this.table().addInner(new script().addInner(this.objects));
        script script=new script().src(scriptFilePath);
        if (head!=null)
        {
            head.add(DataTable.class.getCanonicalName(),script);
        }
        else
        {
            this.table().addInner(script);
        }
    }

    public void lengthMenu(int...lengths)
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
        lengthMenu(lengths,labels);
    }

    public void order(DataTableColumnOrder...orders)
    {
        Object[][] objects=new Object[orders.length][];
        for (int i=0;i<orders.length;i++)
        {
            DataTableColumnOrder order=orders[i];
        
            objects[i]=new Object[]{order.column,order};
        }
        this.objects.add("order", objects);
    }

    static class Orderable
    {
        boolean orderable=false;
        int targets;
    }
    
    public void disableOrderables(int...columns)
    {
        Orderable[] orderables=new Orderable[columns.length];
        for (int i=0;i<columns.length;i++)
        {
            Orderable orderable=new Orderable();
            orderable.targets=columns[i];
            orderables[i]=orderable;
        }
        this.objects.add("columnDefs", orderables);
    }
    
    
    public void lengthMenu(int[] lengths,String[] labels)
    {
        this.objects.add("lengthMenu", new Object[]{lengths,labels});
    }
    public void paginate(boolean on)
    {
        this.objects.add("bPaginate",on);
    }
    public void filter(boolean on)
    {
        this.objects.add("bFilter",on);
    }

    public void info(boolean on)
    {
        this.objects.add("bInfo",on);
    }
    public void sort(boolean on)
    {
        this.objects.add("bSort",on);
    }

    public void addObject(String name,Object object)
    {
        this.objects.add(name, object);
    }
    
}
