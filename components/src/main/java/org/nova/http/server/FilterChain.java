package org.nova.http.server;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.tracing.Trace;

public class FilterChain
{
	private int filterIndex=0;
	private final RequestHandlerWithParameters methodResult;
	private final Filter[] filters;
	private final DecoderContext decoderContext;
	FilterChain(RequestHandlerWithParameters methodResult,DecoderContext decoderContext)
	{
		this.methodResult=methodResult;
		this.filters=methodResult.requestHandler.getFilters();
		this.decoderContext=decoderContext;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object buildParameter(ParameterInfo parameterInfo,String value) throws Exception 
	{
		if (value==null)
		{
			if (parameterInfo.getDefaultValue()!=null)
			{
				return parameterInfo.getDefaultValue();
			}
			throw new Exception("Parameter "+parameterInfo.getName()+" is null.");
		}
		Class<?> type=parameterInfo.getType();
		if (type==String.class)
		{
			return value;
		}
		else if ((type==int.class)||(type==Integer.class))
		{
			return Integer.parseInt(value);
		}
		if ((type==long.class)||(type==Long.class))
		{
			return Long.parseLong(value);
		}
		if ((type==short.class)||(type==Short.class))
		{
			return Short.parseShort(value);
		}
		if ((type==float.class)||(type==Float.class))
		{
			return Float.parseFloat(value);
		}
		if ((type==double.class)||(type==Double.class))
		{
			return Double.parseDouble(value);
		}
		if ((type==boolean.class)||(type==Boolean.class))
		{
            if ("on".equals(value))
            {
                return true;
            }
		    return !("false".equals(value));
		}
        if (type.isEnum())
        {
            return Enum.valueOf((Class<Enum>)type, value);
        }
        if (type==BigDecimal.class)
        {
            return new BigDecimal(value);
        }
        throw new Exception("Cannot parse parameter "+parameterInfo.getName());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Response<?> next(Trace trace,Context context) throws Throwable
	{
		if (this.filterIndex<this.filters.length)
		{
			return filters[this.filterIndex++].executeNext(trace,context,this);
		}
		RequestHandler requestHandler=this.methodResult.requestHandler;
		String[] pathParameters=this.methodResult.parameters;
		ParameterInfo[] parameterInfos=requestHandler.getParameterInfos();
		Object[] parameters=new Object[parameterInfos.length];
		HttpServletRequest request=context.getHttpServletRequest();
		
		ContentReader<?> reader=null;
		Object content=null;
		
		for (int i=0;i<parameterInfos.length;i++)
		{
			ParameterInfo parameterInfo=parameterInfos[i];
			switch (parameterInfo.getSource())
			{
			case CONTENT:
				if (reader==null)
				{
					reader=context.getContentReader();
					if (reader!=null)
					{
						content=reader.read(context,this.decoderContext.getInputStream(),parameterInfo.getType());
					}
					else
					{
						throw new AbnormalException(Abnormal.NO_READER);
					}
				}
				parameters[i]=content;
				break;
			case COOKIE:
				try
				{
					Cookie cookie=null;
					for (Cookie c:request.getCookies())
					{
						if (parameterInfo.getName().equals(c.getName()))
						{
							cookie=c;
							break;
						}
					}
					if (parameterInfo.getType()==Cookie.class)
					{
						parameters[i]=cookie;
					}
					else if (cookie!=null)
					{
						parameters[i]=buildParameter(parameterInfo,cookie.getValue());
					}
					else
					{
						parameters[i]=buildParameter(parameterInfo,null);
					}
				}
				catch (Throwable t)
				{
					throw new AbnormalException(Abnormal.BAD_COOKIE,t);
				}
				break;
			case HEADER:
				try
				{
					parameters[i]=buildParameter(parameterInfo,request.getHeader(parameterInfo.getName()));
				}
				catch (Throwable t)
				{
					throw new AbnormalException(Abnormal.BAD_HEADER,t);
				}
				break;
			case PATH:
				try
				{
					parameters[i]=buildParameter(parameterInfo,pathParameters[parameterInfo.getPathIndex()]);
				}
				catch (Throwable t)
				{
					throw new AbnormalException(Abnormal.BAD_PATH,t);
				}
				break;
			case QUERY:
				try
				{
					parameters[i]=buildParameter(parameterInfo,request.getParameter(parameterInfo.getName()));
				}
				catch (Throwable t)
				{
					throw new AbnormalException(Abnormal.BAD_QUERY,parameterInfo.getName(),t);
				}
				break;
			case CONTEXT:
				parameters[i]=context;
				break;
			case STATE:
				parameters[i]=context.getState();
				break;
			case TRACE:
				parameters[i]=trace;
				break;
            case QUERIES:
                parameters[i]=new Queries(request);
                break;
            case CHECK:
                parameters[i]=request.getParameter(parameterInfo.getName())!=null;
                break;
			default:
				break;
			}
		}
		
		
		try
		{
			Object result=requestHandler.getMethod().invoke(requestHandler.getObject(), parameters);
			if (context.isHandled()==false)
			{
				if (result==null)
				{
					return new Response(context.getHttpServletResponse().getStatus());
				}
				else if (result.getClass()==Response.class)
				{
					return (Response<?>)result;
				}
				return new Response(HttpStatus.OK_200,result);
			}
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
            throw e;
		}
		catch (InvocationTargetException e)
		{
			throw e.getTargetException();
		}
        catch (Throwable e)
        {
            throw e;
        }
		return null;
	}
}
