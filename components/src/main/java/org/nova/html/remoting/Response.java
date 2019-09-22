package org.nova.html.remoting;

public class Response
{
    public HtmlResult[] htmlResults;
    public HtmlResult[] appendResults;
    public ModalResult[] modalResults;
    public ValResult[] valResults;
    public RemoveClassResult[] removeClassResults;
    public AddClassResult[] addClassResults;
    public PropResult[] propResults;
    public ClearTimerCommand[] clearTimerCommands;
    public String script;
    public String result;
    public String location;
}
