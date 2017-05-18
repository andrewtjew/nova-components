package org.nova.html.widgets;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.ul;
import org.nova.html.tags.a;
import org.nova.html.tags.div;
import org.nova.html.tags.li;
import org.nova.html.tags.link;
import org.nova.html.tags.span;
import org.nova.html.tags.sub;

public class MenuBar extends Element
{
    static class Item
    {
        public ArrayList<Item> subItems;
        public String href;
        final public String name;
        public Item(String name)
        {
            this.name=name;
        }
    }
    
    private final ArrayList<Item> rootItems=new ArrayList<>();
    
    public MenuBar(Head head,String id,String sourcePath,String cssFile)
    {
        head.add(MenuBar.class.getCanonicalName(), new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile));
    }
    public MenuBar(Head head,String id)
    {
        this(head,id, "/resources/html","/widgets/MenuBar/style.css");
    }

    public MenuBar()
    {
    }
    
    Item find(ArrayList<Item> items,String name)
    {
        for (Item item:items)
        {
            if (name.equals(item.name))
            {
                return item;
            }
        }
        return null;
    }
    
    public MenuBar add(String href,String...names)
    {
        add(href,names,0,rootItems);
        return this;
    }
    
    private void add(String href,String[] names,int level,ArrayList<Item> items)
    {
        String name=names[level];
        Item item=find(items,name);
        if (item==null)
        {
            item=new Item(name);
            items.add(item);
        }
        if (level==names.length-1)
        {
            item.href=href;
        }
        else
        {
            if (item.subItems==null)
            {
                item.subItems=new ArrayList<>();
            }
            add(href,names,level+1,item.subItems);
        }
        
    }

    @Override
    public void build(Builder builder) throws Throwable
    {
        ul menu=new ul();
        menu.class_("menu-level-0");
        write(menu,0,this.rootItems);
        menu.build(builder);
    }
    
    private void write(ul ul,int level,ArrayList<Item> items)
    {
        int longestSubMenu=0;
        if (level>0)
        {
            for (Item item:items)
            {
                ArrayList<Item> subItems=item.subItems;
                int length=item.name.length();
                if (length>longestSubMenu)
                {
                    longestSubMenu=length;
                }
            }
        }
        longestSubMenu=longestSubMenu+2; //to be safe
        for (Item item:items)
        {
            li li=ul.returnAddInner(new li());
            a a=li.returnAddInner(new a());
            String href=item.href;
            if (href==null)
            {
                a.href("#");
            }
            else
            {
                a.href(href);
            }
            ArrayList<Item> subItems=item.subItems;
            if ((subItems!=null)&&(level>0))
            {
                a.addInner(item.name);
            //    a.addInner(new span().class_("menu-expand").addInner("&#9658;"));
                a.addInner(new span().class_("menu-expand").addInner("&#x27a4;"));
                ;            
            }
            else
            {
                a.addInner(item.name);
            }
            if (subItems!=null)
            {
                if (level>0)
                {
                    li.style("width:"+longestSubMenu+"em;");
                }
                ul subMenu=li.returnAddInner(new ul());
                subMenu.class_("menu-level-"+(level+1));
                write(subMenu,level+1,subItems);
            }
        }
    }
}
