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

import org.nova.html.Attribute;
import org.nova.html.HtmlWriter;
import org.nova.html.operator.Menu;
import org.nova.html.operator.OperationResult;
import org.nova.html.operator.OperatorResultWriter;
import org.nova.html.widgets.AjaxButton;
import org.nova.html.widgets.AjaxQueryResult;
import org.nova.html.widgets.AjaxQueryResultWriter;
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

@ContentWriters(OperatorResultWriter.class)
public class OperatorPages
{
	final private OperatorVariableManager variableManager;
	static public DateTimeFormatter DATETIME_FORMAT=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); 

	public static String formatDateTime(long dateTime)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), TimeZone.getDefault().toZoneId()).format(DATETIME_FORMAT);
	}
	
	public OperatorPages(OperatorVariableManager container,Menu menu) throws Exception
	{
		this.variableManager=container;
		menu.add("Variables|View","/operator/variables/view");
		menu.add("Variables|Modify","/operator/variables/modify");
	}
	
	@GET
	@Path("/operator/variables/view")
	public Response<OperationResult> list() throws Throwable
	{
		HtmlWriter writer=new HtmlWriter();

		List<String> list=Arrays.asList(variableManager.getCategories());
		Collections.sort(list);

		for (String category:list)
		{
			List<VariableInstance> instances=Arrays.asList(this.variableManager.getInstances(category));
//			Collections.sort(variables);
	
			writer.h2(category);
			writer.begin_sortableTable(1);
			writer.begin_tr();
			writer.th("Name");
			writer.th("Type");
            writer.th("Validator");
			writer.th("Default");
            writer.th("Value");
			writer.th("Modified");
			writer.th("Description");
	
			for (VariableInstance instance:instances)
			{
				writer.next_tr();
				Field field=instance.getField();
				OperatorVariable variable=instance.getOperatorVariable();
				writer.td(instance.getName());
                writer.td(field.getType().getSimpleName());
                writer.td(variable.validator().getSimpleName());
                writer.td(instance.getDefaultValue());
				writer.td(instance.getValue());
				writer.td(instance.getModified()==0?"":formatDateTime(instance.getModified()));
				writer.td(variable.description());
			}
			writer.end_tr();
			writer.end_table();
		}
		
		return OperationResult.respond(writer, "View Operator Variables");
	}

	@GET
	@Path("/operator/variables/modify")
	public Response<OperationResult> modify() throws Throwable
	{
		HtmlWriter writer=new HtmlWriter();

		List<String> list=Arrays.asList(this.variableManager.getCategories());
		Collections.sort(list);

		for (String category:list)
		{
			List<VariableInstance> instances=Arrays.asList(this.variableManager.getInstances(category));
//			Collections.sort(variables);
	
			writer.h2(category);
			writer.begin_sortableTable(1);
			writer.begin_tr();
			writer.th("Name");
			writer.th("Type");
			writer.th("Default");
			writer.th("Value");
			writer.th("Min","Minimum");
			writer.th("Max","Maximum");
            writer.th("\u2205","Null String");
            writer.th("New Value");
			writer.th("Action");
			writer.th("Result");
	
			int textSize=10;
            Attribute inputStyleAttribute=new Attribute("style","width:100%;background-color:#FFFFCF");
            Attribute stringStyleAttribute=new Attribute("style","background-color:#FFFFCF");
			for (VariableInstance instance:instances)
			{
				writer.next_tr();
				Field field=instance.getField();
				OperatorVariable variable=instance.getOperatorVariable();
				String name=instance.getName();
				Class<?> type=field.getType();
				Object value=instance.getValue();
                String resultKey=(category+name+"Result").replace('.', '_');
                String valueKey=(category+name+"Value").replace('.', '_');

				writer.td(name,variable.description());
				writer.td(type.getSimpleName());
                writer.td(instance.getDefaultValue());
                writer.td(instance.getValue(),new Attribute("id",valueKey));
				String buttonKey=(category+name+"Button").replace('.', '_');
				String[] options=variable.options();
				if (options[0].length()!=0)
				{
                    writer.td().td().td();
                    writer.begin_td();
                    writer.begin_select(name,inputStyleAttribute);
                    for (String option:options)
                    {
                        writer.option(option,new Attribute("value",option));
                    }
                    writer.end_select();
                    writer.end_td();
                    AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
                    button.async(false).val("value",name);
                    writer.td(writer.inner().writeObject(button));
				    
				}
				else if (type==boolean.class)
				{
					writer.td().td().td();
					writer.td(writer.inner().input_checkbox(name, null, (boolean)value));
					AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
					button.async(false).prop("value",name,"checked");
					writer.td(writer.inner().writeObject(button));
				}
				else if (type.isEnum())
				{
					writer.td().td().td();
					writer.begin_td();
					writer.begin_select(name,inputStyleAttribute);
					for (Object enumConstant:field.getType().getEnumConstants())
					{
						String option=enumConstant.toString();
						writer.option(option,new Attribute("value",option));
					}
					writer.end_select();
					writer.end_td();
					AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
					button.async(false).val("value",name);
					writer.td(writer.inner().writeObject(button));
				}
				else if (type==String.class) 
				{
					writer.td().td();
					if (value!=null)
					{
	                    writer.td(writer.inner().input_checkbox("nullString", null, false));
					    writer.td(writer.inner().input_text(textSize,name, value.toString(),stringStyleAttribute));
					}
					else
					{
                        writer.td(writer.inner().input_checkbox("nullString", null, true));
                        writer.td(writer.inner().input_text(textSize,name, "",stringStyleAttribute));
					}
					AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
					button.async(false).val("value",name).prop("nullString","nullString","checked");
					writer.td(writer.inner().writeObject(button));
				}
				else
				{
					writer.td(variable.minimum()).td(variable.maximum()).td();
					writer.td(writer.inner().input_text(textSize,name, value==null?"":value.toString(),inputStyleAttribute));
					AjaxButton button=new AjaxButton(buttonKey, "Update", "/operator/variable/"+category+"/"+name);
					button.async(false).val("value",name);
					writer.td(writer.inner().writeObject(button));
				}
				writer.td(writer.inner().div(null, new Attribute("id",resultKey)));
			}
			writer.end_tr();
			writer.end_table();
		}
		
		return OperationResult.respond(writer, "Modify Operator Variables");
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
		VariableInstance instance=this.variableManager.getInstance(category, name);
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
