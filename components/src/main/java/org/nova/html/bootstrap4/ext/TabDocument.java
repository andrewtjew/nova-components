package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Nav;
import org.nova.html.bootstrap4.NavItem;
import org.nova.html.bootstrap4.NavLink;
import org.nova.html.bootstrap4.TabContent;
import org.nova.html.bootstrap4.TabPane;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;

public class TabDocument extends Element
{
    final private Nav nav;
    final private TabContent tabContent;
    
    
    public TabDocument(boolean fill)
    {
        this.nav=new Nav().tabs();
        if (fill)
        {
            this.nav.fill();
        }
//        this.nav.attr("role","tablist");
        this.tabContent=new TabContent();
    }
    
    public TabPane add(String label,boolean show,boolean active,boolean disabled)
    {
        NavItem navItem=this.nav.returnAddInner(new NavItem());
        TabPane tabPane=this.tabContent.returnAddInner(new TabPane()).fade();
        NavLink navLink=navItem.returnAddInner(new NavLink()).toggleTabPane(tabPane).addInner(label);
        if (active)
        {
            tabPane.active();
            navLink.active();
        }
        if (show)
        {
            tabPane.show();
        }
        if (disabled)
        {
           navLink.disabled();
        }
        return tabPane;
    }
    
    public TabPane add(String label,boolean activeAndShow)
    {
        return add(label,activeAndShow,activeAndShow,false);
    }

    public TabDocument add(NavLink navLink,TabPane tabPane)
    {
        NavItem navItem=this.nav.returnAddInner(new NavItem());
        navItem.addInner(navLink);
        navLink.toggleTabPane(tabPane);
        this.tabContent.addInner(tabPane);
        return this;
    }
    
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.nav.compose(composer);
        this.tabContent.compose(composer);
    }
    
}
