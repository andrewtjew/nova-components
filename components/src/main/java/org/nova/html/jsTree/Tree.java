/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
