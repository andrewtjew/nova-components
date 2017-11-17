package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
        public void compose(Composer composer) throws Throwable
        {
            composer.getStringBuilder().append(toString());
        }
    }
    
    final private String id;
    final private Objects objects;
    
    public DataTable(Head head)
    {
        this(head,null);
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
        style("width:100%;");
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

    public DataTable order(DataTableColumnOrder...orders)
    {
        Object[][] objects=new Object[orders.length][];
        for (int i=0;i<orders.length;i++)
        {
            DataTableColumnOrder order=orders[i];
        
            objects[i]=new Object[]{order.column,order};
        }
        this.objects.add("order", objects);
        return this;
    }

    static class Orderable
    {
        boolean orderable=false;
        int targets;
    }
    
    public DataTable disableOrderables(int...columns)
    {
        Orderable[] orderables=new Orderable[columns.length];
        for (int i=0;i<columns.length;i++)
        {
            Orderable orderable=new Orderable();
            orderable.targets=columns[i];
            orderables[i]=orderable;
        }
        this.objects.add("columnDefs", orderables);
        return this;
    }
    
    
    public DataTable lengthMenu(int[] lengths,String[] labels)
    {
        this.objects.add("lengthMenu", new Object[]{lengths,labels});
        return this;
    }
    public DataTable paginate(boolean on)
    {
        this.objects.add("bPaginate",on);
        return this;
    }
    public DataTable filter(boolean on)
    {
        this.objects.add("bFilter",on);
        return this;
    }
    public DataTable info(boolean on)
    {
        this.objects.add("bInfo",on);
        return this;
    }
    public DataTable sort(boolean on)
    {
        this.objects.add("bSort",on);
        return this;
    }

    public DataTable addObject(String name,Object object)
    {
        this.objects.add(name, object);
        return this;
    }
    
}
