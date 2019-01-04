package org.nova.html.jsTree;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.enums.link_rel;
import org.nova.html.ext.Head;
import org.nova.html.tags.link;
import org.nova.html.tags.ul;
import org.nova.html.tags.script;
import org.nova.html.tags.div;

//!!! Requires jquery

public class Tree extends Element
{
    final private String id;
    final private div div;
    final private ul ul;
    
    static class Attributes
    {
        public String[] plugins;
    }
    
    public Tree(Head head,String id,String sourcePath,String cssFile)
    {
        if (id==null)
        {
            id="_"+Integer.toString(this.hashCode());
        }
        this.id=id;
        this.div=new div();
        this.div.id(id);
        
        script script=new script().src(sourcePath+"/jstree.min.js");
        link link=new link().rel(link_rel.stylesheet).type("text/css").href(sourcePath+cssFile);
        if (head!=null)
        {
            head.add(Tree.class.getCanonicalName(),script);
            head.add(Tree.class.getCanonicalName(),link);
        }
        this.ul=this.div.returnAddInner(new ul());
    }

    public Tree(Head head)
    {
        this(head,null,"/resources/html/widgets/jsTree","/themes/default/style.min.css");
    }
    
    public Tree(Head head,String id)
    {
        this(head,id,"/resources/html/widgets/jsTree","/themes/default/style.min.css");
    }
    
    
    public Tree add(TreeNode treeNode)
    {
        this.ul.addInner(treeNode);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.compose(div);
        script script=new script();
        script.addInner(loadScript());
        composer.compose(script);
    }

    private String loadScript()
    {
//        return "$('#"+this.id+"').jstree({\"plugins\" : [ \"wholerow\"]});"; 
        return "$('#"+this.id+"').jstree();"; 
    }
}
