namespace nova.remote
{
    function toPlacement(placement:string):"top"|"bottom"|"left"|"right"|"auto"
    {
        switch (placement)
        {
            case "top":
                return "top";
            case "bottom":
                return "bottom";
            case "left":
                return "left";
            case "right":
                return "right";
            case "auto":
                return "auto";
            default:
                return null;
        }
    }


    export function openEditBox(
        template:string
        ,backgroundId:string
        ,containerId:string
        ,acceptButtonId:string
        ,dismissButtonId:string
        ,editButtonId:string
        ,editInputId:string
        ,valueElementId:string
        ,action:string
        ,text:string
        ,content:string
        ,placement:string
        )
    {
        var background=document.getElementById(backgroundId);
        var container=document.getElementById(containerId);

        if (background!=null)
        {
            background.style.display="block";
        }

        //Need to create popover first to create the buttons.
        var pop=$('#'+containerId).popover(
            {
                trigger:"manual",
                container:'#'+containerId,
                template:template,
                html:true,
                content:content,
                sanitize:false,
                placement:toPlacement(placement)
            }
        );
        pop.popover("show");


        var acceptButton=document.getElementById(acceptButtonId);
        var dismissButton=document.getElementById(dismissButtonId);
        var editInput=document.getElementById(editInputId) as HTMLInputElement;
        var editButton=document.getElementById(editButtonId);
        var valueElement=document.getElementById(valueElementId);

        container.onmouseover=function(){
        }
        editButton.style.display="none";

        var close=function()
        {
            pop.popover("hide");     
            container.onmouseover=function(){
                editButton.style.display="block";
            }
            if (background!=null)
            {
                background.style.display="none";
            }
        }
        dismissButton.onclick=function()
        {
            close();
        }
    
        acceptButton.onclick=function()
        {
            var data=Remoting.toData(text);
            data[editInput.name]=editInput.value;
            $.ajax(
                {url:action,
                type:"POST",
                async:true,
                dataType:"json",
                cache: false,
                data:data,
                success:function(result:object)
                {
                    if (result!=null)
                    {
                        valueElement.innerText=result.toString();
                    }
                    close();
                },
                error:function(xhr)
                {
                    close();
                    alert("Error: "+xhr.status+" "+xhr.statusText);
                }
            }); 
            }
    }

    export function openDialogPopover(
        template:string
        ,backgroundId:string
        ,containerId:string
        ,acceptButtonId:string
        ,dismissButtonId:string
        ,action:string
        ,text:string
        ,content:string
        ,placement:string
        )
    {
        var background=document.getElementById(backgroundId);

        if (background!=null)
        {
            background.style.display="block";
        }
        //Need to create popover first to create the buttons.
        var pop=$('#'+containerId).popover(
            {
                trigger:"manual",
                placement:toPlacement(placement),
                template:template,
                html:true,
                content:content,
                sanitize:false,
            }
        );
        pop.popover("show");

        var acceptButton=document.getElementById(acceptButtonId);
        var dismissButton=document.getElementById(dismissButtonId);

        var close=function()
        {
            pop.popover("hide");     
            if (background!=null)
            {
                background.style.display="none";
            }
        }
        dismissButton.onclick=function()
        {
            close();
        }
    
        acceptButton.onclick=function()
        {
            nova.remote.post(action,text,false);
        }

    }    
}