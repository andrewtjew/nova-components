package org.nova.operator;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.nova.frameworks.OperatorPage;
import org.nova.frameworks.ServerApplication;
import org.nova.frameworks.ServerApplicationPages.Level2Panel;
import org.nova.frameworks.ServerApplicationPages.WideTable;
import org.nova.html.Attribute;
import org.nova.html.HtmlWriter;
import org.nova.html.elements.Element;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.operator.AjaxButton;
import org.nova.html.operator.AjaxQueryResult;
import org.nova.html.operator.AjaxQueryResultWriter;
import org.nova.html.operator.SelectOptions;
import org.nova.html.tags.div;
import org.nova.html.tags.input_checkbox;
import org.nova.html.tags.input_text;
import org.nova.html.tags.p;
import org.nova.html.widgets.MenuBar;
import org.nova.html.widgets.Row;
import org.nova.http.server.Response;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.DefaultValue;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.PathParam;
import org.nova.http.server.annotations.QueryParam;
import org.nova.operations.OperatorVariable;
import org.nova.operations.OperatorVariableManager;
import org.nova.operations.Status;
import org.nova.operations.ValidationResult;
import org.nova.operations.VariableInstance;

import com.google.common.util.concurrent.AtomicDouble;

@ContentWriters(HtmlElementWriter.class)
public class OperatorPages
{
	final private ServerApplication serverApplication;
	static public DateTimeFormatter DATETIME_FORMAT=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); 

	public static String formatDateTime(long dateTime)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), TimeZone.getDefault().toZoneId()).format(DATETIME_FORMAT);
	}
	
	public OperatorPages(ServerApplication serverApplication) throws Exception
	{
		this.serverApplication=serverApplication;
		MenuBar menuBar=serverApplication.getMenuBar();
		menuBar.add("/operator/variables/view","Variables","View");
		menuBar.add("/operator/variables/modify","Variables","Modify");
	}
	
	@GET
	@Path("/operator/variables/view")
	public Element list() throws Throwable
	{
        OperatorPage page=this.serverApplication.buildOperatorPage("View Operator Variables"); 

		List<String> list=Arrays.asList(serverApplication.getOperatorVariableManager().getCategories());
		Collections.sort(list);

		for (String category:list)
		{
			List<VariableInstance> instances=Arrays.asList(this.serverApplication.getOperatorVariableManager().getInstances(category));
//			Collections.sort(variables);

			Level2Panel panel=page.content().returnAddInner(new Level2Panel(page.head(),category));
			page.content().addInner(new p());
			WideTable table=panel.returnAddInner(new WideTable(page.head()));
			
            table.setHeadRowItems("Name","Type","Validator","Default","Value","Modified","Description");

			for (VariableInstance instance:instances)
			{
                Field field=instance.getField();
                OperatorVariable variable=instance.getOperatorVariable();
                Row row=new Row();
			    row.add(instance.getName());
			    row.add(field.getType().getSimpleName());
			    row.add(variable.validator().getSimpleName());
			    row.add(instance.getDefaultValue());
			    row.add(instance.getValue());
			    row.add(instance.getModified()==0?"":formatDateTime(instance.getModified()));
			    row.add(variable.description());
			    table.addBodyRow(row);

			}
		}
		
		return page;
	}

	@GET
	@Path("/operator/variables/modify")
	public Element modify() throws Throwable
	{
        OperatorPage page=this.serverApplication.buildOperatorPage("Modify Operator Variables"); 

        List<String> list=Arrays.asList(serverApplication.getOperatorVariableManager().getCategories());
		Collections.sort(list);

		for (String category:list)
		{
            List<VariableInstance> instances=Arrays.asList(this.serverApplication.getOperatorVariableManager().getInstances(category));
//			Collections.sort(variables);
            Level2Panel panel=page.content().returnAddInner(new Level2Panel(page.head(),category));
            page.content().addInner(new p());
            WideTable table=panel.returnAddInner(new WideTable(page.head()));

            Row row=new Row();
            row.add("Name");
            row.add("Type");
            row.add("Default");
            row.add("Value");
            row.addWithTitle("Min","Minimum");
            row.addWithTitle("Max","Maximum");
            row.addWithTitle("\u2205","Null String");
            row.add("New Value");
            row.add("Action");
            row.add("Result");
            table.setHeadRow(row);
            int textSize=10;

            for (VariableInstance instance:instances)
            {
                Field field=instance.getField();
                OperatorVariable variable=instance.getOperatorVariable();
                String name=instance.getName();
                Class<?> type=field.getType();
                Object value=instance.getValue();
                String resultKey=(category+name+"Result").replace('.', '_');
                String valueKey=(category+name+"Value").replace('.', '_');

                row=new Row();
                row.addWithTitle(name,variable.description());
                row.add(type.getSimpleName());
                row.add(instance.getDefaultValue());
                row.add(instance.getValue());//,new Attribute("id",valueKey));
                String buttonKey=(category+name+"Button").replace('.', '_');
                String[] options=variable.options();
                if (options[0].length()!=0)
                {
                    row.add("","","");
                    
                    SelectOptions selectOptions=new SelectOptions();
                    for (String option:options)
                    {
                        selectOptions.add(option);
                    }
                    row.add(selectOptions);
                    AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
                    button.async(false).val("value",name);
                    row.add(button);
                    
                }
                else if (type==boolean.class)
                {
                    row.add("","","");
                    row.add(new input_checkbox().id(name).checked((boolean)value));
                    AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
                    button.async(false).prop("value",name,"checked");
                    row.add(button);
                }
                else if (type.isEnum())
                {
                    row.add("","","");
                    
                    SelectOptions selectOptions=new SelectOptions();
                    for (Object enumConstant:field.getType().getEnumConstants())
                    {
                        String option=enumConstant.toString();
                        selectOptions.add(option);
                    }
                    row.add(selectOptions);
                    AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
                    button.async(false).val("value",name);
                    row.add(button);
                }
                else if (type==String.class) 
                {
                    row.add("","");
                    if (value!=null)
                    {
                        row.add(new input_checkbox().id("nullString").checked(false));
                        row.add(new input_text().id(name).value(value.toString()).size(textSize));
                    }
                    else
                    {
                        row.add(new input_checkbox().id("nullString").checked(true));
                        row.add(new input_text().id(name).size(textSize));
                    }
                    AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
                    button.async(false).val("value",name).prop("nullString","nullString","checked");
                    row.add(button);
                }
                else
                {
                    row.add(variable.minimum(),variable.maximum(),"");
                    row.add(new input_text().size(textSize).id(name).value(value==null?"":value.toString()));
                    AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
                    button.async(false).val("value",name);
                    row.add(button);
                }
                row.add(new div().id(resultKey));
                table.addBodyRow(row);
            }
		}
		
		return page;
	}
	
	private String formatStringOutput(String s)
	{
	    if (s==null)
	    {
	        return "null";
	    }
	    return "\""+s+"\"";
	}
	
	
	@GET
	@Path("/operator/variable/{category}/{name}")
	@ContentWriters(AjaxQueryResultWriter.class)
	public AjaxQueryResult update(@PathParam("category") String category,@PathParam("name") String name,@QueryParam("value") String value,@QueryParam("nullString") @DefaultValue("") String nullString) throws Throwable
	{
		VariableInstance instance=this.serverApplication.getOperatorVariableManager().getInstance(category, name);
		OperatorVariable variable=instance.getOperatorVariable();
		AjaxQueryResult result=new AjaxQueryResult();
        String resultKey=(category+name+"Result").replace('.', '_');
        String resultValue=(category+name+"Value").replace('.', '_');
		if ("true".equals(nullString))
		{
		    value=null;
		}
		try
		{
		    
			Object old=instance.getValue();
			ValidationResult validationResult=instance.set(value);
			if (validationResult.getStatus()==Status.SUCCESS)
			{
    			if (instance.getField().getType()==String.class)
    			{
                    old=formatStringOutput((String)old);
                    value=formatStringOutput((String)value);
    			}
                result.put(resultKey, formatDateTime(instance.getModified())+": "+old+"\u21D2"+value);
                result.put(resultValue, value);
			}
			else
			{
	            result.put(resultKey, new HtmlWriter().tag("font",validationResult.getMessage(),new Attribute("color","red")).toString());
			}
		}
		catch (Throwable t)
		{
			result.put(resultKey, new HtmlWriter().tag("font",t.getMessage(),new Attribute("color","red")).toString());
		}
		return result;
	}
}
