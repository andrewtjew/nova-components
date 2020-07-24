namespace nova.remote
{
    function toData(text:string):object
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


    export function post(action:string,text:string,async:boolean)
    {
        var data=toData(text);
        call("POST",action,data,async);
    }
    export function get(action:string,text:string,async:boolean)
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
        call("GET",action,null,async);
    }

    function call(type:string,pathAndQuery:string,data:object,async:boolean)
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
                run(instructions);
            },
            error:function(xhr)
            {
                alert("Error: "+xhr.status+" "+xhr.statusText);
            }
        }); 
    }

    function run(instructions:Instruction[])
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
                            case "value":
                                (document.getElementById(parameters[0]) as HTMLInputElement).value=parameters[1];
                                break;
                                
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
                                    
                            case "documentObject":
                            document[parameters[0]]=parameters[1];
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
