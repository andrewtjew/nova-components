package org.nova.html.jsTree;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.QuotationMark;
import org.nova.html.ext.Text;
import org.nova.html.tags.li;
import org.nova.html.tags.ul;
import org.nova.json.ObjectMapper;

public class TreeNode extends Element
{
    private final ul ul;
    
    static class Attributes
    {
        public boolean opened;
        public boolean selected;
        public boolean disabled;
        public String icon;
    }
    private final Attributes attributes;
    private final String id;
    private final Element element;
    
    public TreeNode(Element element,String id)
    {
        if (id==null)
        {
            id="_"+Integer.toString(this.hashCode());
        }
        this.id=id;
        this.attributes=new Attributes();
        this.ul=new ul();
        this.element=element;
    }
    
    public TreeNode(Element element)
    {
        this(element,null);
    }
    
    public TreeNode(Object object)
    {
        this(new Text(object),null);
    }
    
    public TreeNode opened(boolean value)
    {
        this.attributes.opened=value;
        return this;
    }

    public TreeNode selected(boolean value)
    {
        this.attributes.selected=value;
        return this;
    }
    
    public TreeNode icon(String value)
    {
        this.attributes.icon=value;
        return this;
    }
    
    public TreeNode disabled(boolean value)
    {
        this.attributes.disabled=value;
        return this;
    }
    
    public TreeNode add(TreeNode node)
    {
        this.ul.addInner(node);
        return this;
    }
    public TreeNode add(Object object)
    {
        TreeNode node=new TreeNode(object);
        node.icon("false");
        this.ul.addInner(node);
        return this;
    }
    public TreeNode add(Element element)
    {
        TreeNode node=new TreeNode(element);
        node.icon("false");
        this.ul.addInner(node);
        return this;
    }
    
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        li li=new li().id(this.id);
        String jsonText=ObjectMapper.writeObjectToString(this.attributes);
        li.attr("data-jstree",jsonText,QuotationMark.SINGLE);
        li.addInner(element);
        li.addInner(this.ul);
        composer.compose(li);
    }

}
