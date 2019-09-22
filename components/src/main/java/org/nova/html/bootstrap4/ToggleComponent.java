package org.nova.html.bootstrap4;

public abstract class ToggleComponent<ELEMENT extends ToggleComponent<ELEMENT>> extends StyleComponent<ELEMENT> 
{
    public ToggleComponent(String tag, String componentClass)
    {
        super(tag, componentClass);
    }

    @SuppressWarnings("unchecked")
    public ELEMENT active()
    {
        addClass("active");
        return (ELEMENT)this;
    }

    @SuppressWarnings("unchecked")
    public ELEMENT disabled()
    {
        addClass("disabled");
        attr("disabled");
        return (ELEMENT)this;
    }

    public ELEMENT toggleCollapse(Collapse collapse)
    {
        data("toggle","collapse");
        data("target","#"+collapse.id());
        return (ELEMENT)this;
    }
    public ELEMENT toggleCollapse(Collapse collapse,String collapseClass)
    {
        data("toggle","collapse");
        data("target","."+collapseClass);
        return (ELEMENT)this;
    }
    
    public ELEMENT toggleModal(Modal modal)
    {
        data("toggle","modal");
        data("target","#"+modal.id());
        return (ELEMENT)this;
    }    

    /*
    public ELEMENT toggleDropdownMenu(DropdownMenu dropdownMenu)
    {
        data("toggle","dropdown");
        addClass("dropdown-toggle");
        return (ELEMENT)this;
    } 
    */   

    public ELEMENT toggleTabContent(TabContent tabContent)
    {
        data("toggle","list");
        data("target","#"+tabContent.id());
        return (ELEMENT)this;
    }
    public ELEMENT toggleTabPane(TabPane tabPane)
    {
        String id=tabPane.id();
        data("toggle","tab");
        attr("href","#"+id);
        return (ELEMENT)this;
    }
}
