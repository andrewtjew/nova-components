package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.nova.core.NameObject;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.enums.link_rel;
import org.nova.html.tags.link;
import org.nova.html.tags.ul;
import org.nova.html.tags.script;
import org.nova.json.ObjectMapper;
import org.nova.html.tags.div;

//!!! Requires jquery

public class jsTree extends Element
{
    final private String id;
    final private div div;
    final private ul ul;
    
    static class Attributes
    {
        public String[] plugins;
    }
    
    public jsTree(Head head,String id,String sourcePath,String cssFile)
    {
        if (id==null)
        {
            id=Integer.toString(this.hashCode());
        }
        this.id=id;
        this.div=new div();
        div.id(id);
        
        script script=new script().src(sourcePath+"/jstree.min.js");
        link link=new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile);
        if (head!=null)
        {
            head.add(jsTree.class.getCanonicalName(),script);
            head.add(jsTree.class.getCanonicalName(),link);
        }
        else
        {
            this.div.addInner(script);
            this.div.addInner(link);
        }
        this.ul=this.div.returnAddInner(new ul());
    }

    public jsTree(Head head)
    {
        this(head,null,"/resources/html/widgets/jsTree","/themes/default/style.min.css");
    }
    
    public jsTree(Head head,String id)
    {
        this(head,id,"/resources/html/widgets/jsTree","/themes/default/style.min.css");
    }
    
    
    public jsTree add(TreeNode treeNode)
    {
        this.ul.addInner(treeNode);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(div);
        script script=new script();
        script.addInner(loadScript());
        composer.render(script);
    }

    private String loadScript()
    {
//        return "$('#"+this.id+"').jstree({\"plugins\" : [ \"wholerow\"]});"; 
        return "$('#"+this.id+"').jstree();"; 
    }
}
