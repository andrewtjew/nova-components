package org.nova.html.Chartjs;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.canvas;
import org.nova.html.tags.script;
import org.nova.html.widgets.Head;
import org.nova.json.ObjectMapper;

public class BarChart extends Element
{
    final private canvas canvas;
    final private String id;
    private BarType type;
    
    public BarChart(Head head,String id,int width,int height)
    {
        head.add("Chartjs",new script().src("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.min.js"));
        this.canvas=new canvas().id(id).width(width).height(height);
        this.id=id;
        this.type=new BarType();
    }
    
    public void setData(Data data)
    {
        this.type.data=data;
    }
    
    public String script() throws Throwable
    {
        String data=ObjectMapper.write(this.type);
        
        StringBuilder sb=new StringBuilder();
        String ctx=this.id+"_idx";
        sb.append("var "+ctx+"=document.getElementById('"+this.id+"');");
        sb.append("var "+this.id+"=new Chart("+ctx+",");
        sb.append(data);
        sb.append(");");
        return sb.toString();
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.canvas);
        script script=new script();
        script.addInner(script());
        composer.render(script);
    }
}
