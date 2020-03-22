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

