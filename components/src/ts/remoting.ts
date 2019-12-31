//<reference path="path/to/jqueryui-1.9.d.ts"/>
//import * as bootstrap from "bootstrap";
declare function scriptClick0();
declare function scriptClick1(): any;
declare function scriptClick2(): any;
declare function scriptClick3(): any;

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

    class EventListener
    {
        id:string;
        event:string;
        script:string;
    }
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
        result:string;
        location:string;
    }

    type resultCallback<T> = (T)=> any;

    export function get<T>(pathAndQuery:string,result:resultCallback<T>=null) 
    {
        run("GET",pathAndQuery,result);
    }

    export function post<T>(pathAndQuery:string,result:resultCallback<T>=null)
    {
        run("POST",pathAndQuery,null);
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
        var timerId=window.setInterval(function(){run(type,pathAndQuery,null);},interval);
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
    
    function run<T>(type:string,pathAndQuery:string,result:resultCallback<T> )
    {
        $.ajax(
            {url:pathAndQuery,
            type:type,
            dataType:"json",
            cache: false,
            success:function(response:Response)
            {
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
                if (response.eventListeners!=null)
                {
                    response.eventListeners.forEach(item => {
                        document.getElementById(item.id).addEventListener(item.event, scriptClick0); 
                    });                    
            
                }
                if (response.script!=null)
                {
                    try
                    {
                        eval(response.script);
                    }
                    catch (ex)
                    {
   //                     alert(ex);
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
}

