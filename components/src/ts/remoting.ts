//<reference path="path/to/jqueryui-1.9.d.ts"/>
//import * as bootstrap from "bootstrap";
// declare function scriptClick0();
// declare function scriptClick1(): any;
// declare function scriptClick2(): any;
// declare function scriptClick3(): any;

namespace org.nova.html.remoting
{
    class HtmlResult
    {
        id:string;
        html:string;
    }

    class ModalResult
    {
        id:string;
        option:Bootstrap.ModalOption;
    }

    class ValResult
    {
        id:string;
        val:any;
    }

    class RemoveClassResult
    {
        id:string;
        class_:string;
    }

    class AddClassResult
    {
        id:string;
        class_:string;
    }

    class PropResult
    {
        id:string;
        prop:string;
        value:any;
    }

    class ClearTimerCommand
    {
        timerName:string;
    }

    // class EventListener
    // {
    //     id:string;
    //     event:string;
    //     script:string;
    // }
    class Response
    {
        htmlResults:HtmlResult[];
        appendResults:HtmlResult[];
        modalResults:ModalResult[];
        valResults:ValResult[];
        removeClassResults:RemoveClassResult[];
        addClassResults:AddClassResult[];
        propResults:PropResult[];
        clearTimerCommands:ClearTimerCommand[];
        eventListeners:EventListener[];
        script:string;
        startScript:string;
        result:string;
        location:string;
    }

    type resultCallback<T> = (T)=> any;

    export function get<T>(pathAndQuery:string,result:resultCallback<T>=null) 
    {
        call("GET",pathAndQuery,null,result);
    }

    export function post<T>(pathAndQuery:string,result:resultCallback<T>=null)
    {
        call("POST",pathAndQuery,null,null);
    }

    export function scheduleGet(pathAndQuery:string,timerName:string,interval:number)
    {
        scheduleRun("GET",pathAndQuery,timerName,interval);
    }

    export function schedulePost(pathAndQuery:string,timerName:string,interval:number)
    {
        scheduleRun("POST",pathAndQuery,timerName,interval);
    }
    
    var timers:number[]=[];
    
    function scheduleRun(type:string,pathAndQuery,timerName:string,interval:number)
    {
        clearTimer[timerName];
        var timerId=window.setInterval(function(){call(type,pathAndQuery,null,null);},interval);
        timers[timerName]=timerId;
    }

    function clearTimer(timerName:string)
    {
        var timerId=timers[timerName];
        if (timerId!=null)
        {
            window.clearInterval(timerId);
        }
        timers[timerName]=null;
    }
    
    export function call<T>(type:string,pathAndQuery:string,data:object,result:resultCallback<T> )
    {
        $.ajax(
            {url:pathAndQuery,
            type:type,
            async:false,
            dataType:"json",
            data:data,
            cache: false,
            success:function(response:Response)
            {
                if (response.startScript!=null)
                {
                    try
                    {
                        eval(response.startScript);
                    }
                    catch (ex)
                    {
                        alert(ex);
                    }
                }
                if (response.clearTimerCommands!=null)
                {
                    response.clearTimerCommands.forEach(item => {
                        clearTimer(item.timerName);
                    });
                }
                if (response.htmlResults!=null)
                {
                    response.htmlResults.forEach(item => {
                        $("#"+item.id).html(item.html);
                    });
                }
                if (response.appendResults!=null)
                {
                    response.appendResults.forEach(item => {
                        var template=document.createElement("template");
                        template.innerHTML=item.html;
//                        document.getElementById(item.id).append(template.content.firstChild);
                    });
                }
                
                if (response.modalResults!=null)
                {
                    response.modalResults.forEach(item => {
                        $("#"+item.id).modal(item.option);
                    });
                }
                if (response.valResults!=null)
                {
                    response.valResults.forEach(item => {
                        $("#"+item.id).val(item.val);
                    });
                }
                if (response.removeClassResults!=null)
                {
                    response.removeClassResults.forEach(item => {
                        $("#"+item.id).removeClass(item.class_);
                    });
                }
                if (response.addClassResults!=null)
                {
                    response.addClassResults.forEach(item => {
                        $("#"+item.id).addClass(item.class_);
                    });
                }
                if (response.propResults!=null)
                {
                    response.propResults.forEach(item => {
                        if (item.value!=null)
                        {
                            $("#"+item.id).prop(item.prop,item.value);
                        }
                        else
                        {
                            $("#"+item.id).prop(item.prop);
                        }
                    });
                }
                // if (response.eventListeners!=null)
                // {
                //     response.eventListeners.forEach(item => {
                //         document.getElementById(item.id).addEventListener(item.event, scriptClick0); 
                //     });                    
            
                // }
                if (response.script!=null)
                {
                    try
                    {
                        eval(response.script);
                    }
                    catch (ex)
                    {
                        alert(ex);
                    }
                }
                if (response.location!=null)
                {
                    document.location.href=response.location;
                }
                if (result!=null)
                {
                    if (response.result!=null)
                    {
                        result(JSON.parse(response.result) as T)

                    }
                    else
                    {
                        result(null);
                    }

                }
            },
            error:function(xhr)
            {
                alert("Error: "+xhr.status+" "+xhr.statusText);
            }
        }); 
    }
export class CallBuilder
{
    public path:string;
    private data:object;
    public constructor(path:string)
    {
        this.path=path;
        this.data=new Object();
    }

    public inputValue(id:string)
    {
        var input=document.getElementById(id) as HTMLInputElement;
        this.stringValue(input.name,input.value);
    }

    public stringValue(name:string,value:string)
    {
        this.data[name]=value;
    }
    public numberValue(name:string,value:number)
    {
        this.data[name]=value;
    }
    public selectValue(id:string)
    {
        var select=document.getElementById(id) as HTMLSelectElement;
        this.stringValue(select.name,select.value);
    }

    public pathAndQuery()
    {
        return this.path;
    }

    // public call<T>(type:string,result:resultCallback<T>)
    // {
    //     org.nova.html.remoting.call(type,this.path,result);
    // }
    public post()
    {
        org.nova.html.remoting.call("POST",this.path,this.data,null);
    }
}
}

class Instruction
{
    trace:boolean;
    command:string;
    parameters:string[];
}

class Input 
{
    name:string;
    id:string;
    inputType:string;
}

class Remoting
{

    static toPlacement(placement:string):"top"|"bottom"|"left"|"right"|"auto"
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
    public static openEditBox(
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
                placement:Remoting.toPlacement(placement)
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

    public static openDialogPopover(
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
                placement:Remoting.toPlacement(placement),
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
            var data=Remoting.toData(text);
            $.ajax(
                {url:action,
                type:"POST",
                async:true,
                dataType:"json",
                cache: false,
                data:data,
                success:function(instructions:Instruction[])
                {
                    close();
                    Remoting.run(instructions);
                },
                error:function(xhr)
                {
                    close();
                    alert("Error: "+xhr.status+" "+xhr.statusText);
                }
            }); 
            }

    }    
    static toData(text:string):object
    {
        var data=new Object();
        var inputs=JSON.parse(text) as Input[];
        inputs.forEach(input=>
        {
            switch (input.inputType)
            {
                case "value":
                data[input.name]=(document.getElementById(input.id) as HTMLInputElement).value;
                break;
                
                case "checked":
                data[input.name]=(document.getElementById(input.id) as HTMLInputElement).checked;
                break;
                
                case "select":
                {
                    var select=document.getElementById(input.id) as HTMLSelectElement;
                    data[input.name]=select.options[select.selectedIndex].value;
                }
                break;

                case "radio":
                var radio=document.getElementById(input.id) as HTMLInputElement;
                if (radio.checked)
                {
                    data[input.name]=(document.getElementById(input.id) as HTMLInputElement).value;
                }
                break;

                case "constant":
                data[input.name]=input.id;
                break;
            }
        }
        );
        return data;
    }

    public static post(action:string,text:string,async:boolean)
    {
        var data=Remoting.toData(text);
        this.call("POST",action,data,async);
    }
    public static get(action:string,text:string,async:boolean)
    {
        var inputs=JSON.parse(text) as Input[];
        var seperator="?";
        inputs.forEach(input=>
        {
            switch (input.inputType)
            {
                case "value":
                action+=seperator+input.name+"="+encodeURIComponent((document.getElementById(input.id) as HTMLInputElement).value);
                break;
                
                case "checked":
                action+=seperator+input.name+"="+encodeURIComponent((document.getElementById(input.id) as HTMLInputElement).checked);
                break;
                
                case "select":
                var select=document.getElementById(input.id) as HTMLSelectElement;
                action+=seperator+input.name+"="+encodeURIComponent(select.options[select.selectedIndex].value);
                break;

                case "radio":
                var radio=document.getElementById(input.id) as HTMLInputElement;
                if (radio.checked)
                {
                    action+=seperator+input.name+"="+encodeURIComponent(radio.value);
                }
                break;

                case "constant":
                action+=seperator+input.name+"="+encodeURIComponent(input.id);
                break;
            }
            seperator="&";
        }
        );
        this.call("GET",action,null,async);
    }

    static call(type:string,pathAndQuery:string,data:object,async:boolean)
    {
        $.ajax(
            {url:pathAndQuery,
            type:type,
            async:async,
            dataType:"json",
            cache: false,
            data:data,
            success:function(instructions:Instruction[])
            {
                Remoting.run(instructions);
            },
            error:function(xhr)
            {
                alert("Error: "+xhr.status+" "+xhr.statusText);
            }
        }); 
    }

    static run(instructions:Instruction[])
    {
        if (instructions!=null)
        {
            instructions.forEach(instruction=>
                {
                    var parameters=instruction.parameters;
                    if (instruction.trace)
                    {
                        console.log("ActionResponse:"+instruction.command);
                        console.log("parameters:"+parameters);
                    }
                    try
                    {
                        switch (instruction.command)
                        {
                            case "innerHTML":
                                document.getElementById(parameters[0]).innerHTML=parameters[1];
                                break;
                                
                            case "outerHTML":
                                document.getElementById(parameters[0]).outerHTML=parameters[1];
                                break;

                            case "innerText":
                            document.getElementById(parameters[0]).innerText=parameters[1];
                            break;
                                    
                            case "alert":
                            alert(parameters[0]);
                            break;
                                    
                            case "log":
                            console.log(parameters[0]);
                            break;
                                    
                            case "location":
                            document.location.href=parameters[0];
                            break;
                                    
                            case "script":
                                eval(parameters[0]);
                                break;

                        }
                    }
                    catch (ex)
                    {
                        alert("ActionResponse Exception:"+ex);
                    }
                }
                );
            }
    }
}

