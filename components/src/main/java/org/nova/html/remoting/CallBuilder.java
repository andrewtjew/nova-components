package org.nova.html.remoting;

import org.nova.html.elements.Element;
import org.nova.html.elements.TagElement;
import org.nova.html.ext.FormQueryBuilder;
import org.nova.html.ext.Head;
import org.nova.html.tags.script;
import org.nova.http.client.PathAndQuery;

public class CallBuilder
{
    public CallBuilder(Head head)
    {
        head.add("_"+this.hashCode(), new script().src("/resources/html/js/remoting.js"));
    }
    public CallBuilder()
    {
    }

    /*
    public String generateGet(String path,FormQueryBuilder formQueryBuilder)
    {
        return "org.nova.html.remoting.get("+formQueryBuilder.generateFormQuery(path)+")";
    }
    public String generatePost(String path,FormQueryBuilder formQueryBuilder)
    {
        return "org.nova.html.remoting.post("+formQueryBuilder.generateFormQuery(path)+")";
    }
    */
    public String generateGet(PathAndQuery pathAndQuery,FormQueryBuilder formQueryBuilder)
    {
        return "org.nova.html.remoting.get("+formQueryBuilder.generateFormQuery(pathAndQuery)+")";
    }
    public String generatePost(PathAndQuery pathAndQuery,FormQueryBuilder formQueryBuilder)
    {
        return "org.nova.html.remoting.post("+formQueryBuilder.generateFormQuery(pathAndQuery)+")";
    }
    
    public String generateGet(PathAndQuery pathAndQuery)
    {
        return "org.nova.html.remoting.get('"+pathAndQuery.toString()+"')";
    }
    public String generatePost(PathAndQuery pathAndQuery)
    {
        return "org.nova.html.remoting.post('"+pathAndQuery.toString()+"')";
    }
//--
    /*
    public String generateScheduleGet(String path,FormQueryBuilder formQueryBuilder,String timerName,int intervalMs)
    {
        return "org.nova.html.remoting.scheduleGet("+formQueryBuilder.generateFormQuery(path)+",'"+timerName+"',"+intervalMs+")";
    }
    public String generateSchedulePost(String path,FormQueryBuilder formQueryBuilder,String timerName,int intervalMs)
    {
        return "org.nova.html.remoting.schedulePost("+formQueryBuilder.generateFormQuery(path)+",'"+timerName+"',"+intervalMs+")";
    }
    */
    public String generateScheduleGet(PathAndQuery pathAndQuery,FormQueryBuilder formQueryBuilder,String timerName,int intervalMs)
    {
        return "org.nova.html.remoting.scheduleGet("+formQueryBuilder.generateFormQuery(pathAndQuery)+",'"+timerName+"',"+intervalMs+")";
    }
    public String generateSchedulePost(PathAndQuery pathAndQuery,FormQueryBuilder formQueryBuilder,String timerName,int intervalMs)
    {
        return "org.nova.html.remoting.schedulePost("+formQueryBuilder.generateFormQuery(pathAndQuery)+",'"+timerName+"',"+intervalMs+")";
    }
    
    public String generateScheduleGet(PathAndQuery pathAndQuery,String timerName,int intervalMs)
    {
        return "org.nova.html.remoting.scheduleGet('"+pathAndQuery.toString()+"','"+timerName+"',"+intervalMs+")";
    }
    public String generateSchedulePost(PathAndQuery pathAndQuery,String timerName,int intervalMs)
    {
        return "org.nova.html.remoting.schedulePost('"+pathAndQuery.toString()+"','"+timerName+"',"+intervalMs+")";
    }


}
