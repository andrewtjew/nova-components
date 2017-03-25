package org.nova.http.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;

import org.nova.core.Utils;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentParam;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.CookieParam;
import org.nova.http.server.annotations.DELETE;
import org.nova.http.server.annotations.DefaultValue;
import org.nova.http.server.annotations.Filters;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.HEAD;
import org.nova.http.server.annotations.OPTIONS;
import org.nova.http.server.annotations.PATCH;
import org.nova.http.server.annotations.POST;
import org.nova.http.server.annotations.PUT;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.PathParam;
import org.nova.http.server.annotations.QueryParam;
import org.nova.http.server.annotations.StateParam;
import org.nova.http.server.annotations.TRACE;
import org.nova.tracing.Trace;

//TODO!!! Resolve Consumes and ContentReaders just like produces and contentwriters

class RequestHandlerMap
{
	static class Map extends HashMap<String, Node>
	{
		private static final long serialVersionUID = -4908433415114984382L;
	}

	static class Node
	{
		final Map map;
		RequestHandler requestHandler;

		Node()
		{
			map = new Map();
		}
	}

	final private Map patchMap;
	final private Map putMap;
	final private Map getMap;
	final private Map postMap;
	final private Map deleteMap;
	final private Map optionsMap;
	final private Map headMap;
	final private Map traceMap;
	final private HashMap<String, RequestHandler> requestHandlers;

	RequestHandlerMap()
	{
		this.putMap = new Map();
		this.getMap = new Map();
		this.postMap = new Map();
		this.deleteMap = new Map();
		this.optionsMap = new Map();
		this.headMap = new Map();
		this.traceMap = new Map();
		this.patchMap = new Map();
		this.requestHandlers = new HashMap<>();
	}

	public RequestHandler[] getRequestHandlers()
	{
		return this.requestHandlers.values().toArray(new RequestHandler[this.requestHandlers.size()]);
	}

	public RequestHandler getRequestHandler(String key)
	{
		return this.requestHandlers.get(key);
	}

	private Map getMap(String method)
	{
		switch (method)
		{
		case "DELETE":
			return deleteMap;
		case "GET":
			return getMap;
		case "HEAD":
			return headMap;
		case "OPTION":
			return optionsMap;
		case "POST":
			return postMap;
		case "PUT":
			return putMap;
		case "TRACE":
			return traceMap;
		case "PATCH":
			return patchMap;
		}
		return null;
	}

	void register(String root, Object object, Transformers transformers) throws Exception
	{
		ClassAnnotations classAnnotations = new ClassAnnotations();
		for (Class<?> classType=object.getClass();classType!=null;classType=classType.getSuperclass())
		{
		    if (Modifier.isPublic(classType.getModifiers())==false)
		    {
                throw new Exception("Class must be public. Site=" + classType.getCanonicalName());
		    }
    		for (Annotation annotation : classType.getAnnotations())
    		{
    			Class<?> type = annotation.annotationType();
    			if (type == ContentReaders.class)
    			{
    				classAnnotations.contentReaders = (ContentReaders) annotation;
    			}
    			else if (type == ContentWriters.class)
    			{
    				classAnnotations.contentWriters = (ContentWriters) annotation;
    			}
    			else if (type == ContentEncoders.class)
    			{
    				classAnnotations.contentEncoders = (ContentEncoders) annotation;
    			}
    			else if (type == ContentDecoders.class)
    			{
    				classAnnotations.contentDecoders = (ContentDecoders) annotation;
    			}
    			else if (type == Filters.class)
    			{
    				classAnnotations.filters = (Filters) annotation;
    			}
    		}
		}
		for (Method method : object.getClass().getMethods())
		{
			register(root, object, method, classAnnotations, transformers);
		}
	}

	void register(String root, Object object, Method method, Transformers transformers) throws Exception
	{
		register(root, object, method, new ClassAnnotations(), transformers);
	}

	private boolean isSimpleParameterType(Class<?> type)
	{
		return (type == int.class) || (type == Integer.class) || (type == long.class) || (type == Long.class) || (type == short.class) || (type == short.class)
				|| (type == float.class) || (type == Float.class) || (type == double.class) || (type == Double.class) || (type == boolean.class)
				|| (type == Boolean.class) || (type == String.class) || type.isEnum();
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	private Object getDefaultValue(Method method, DefaultValue default_, Class<?> type) throws Exception
	{
		if (default_ == null)
		{
			return null;
		}
		try
		{
			if ((type == int.class) || (type == Integer.class))
			{
				return Integer.parseInt(default_.value());
			}
			else if ((type == long.class) || (type == Long.class))
			{
				return Long.parseLong(default_.value());
			}
			else if ((type == short.class) || (type == short.class))
			{
				return Short.parseShort(default_.value());
			}
			else if ((type == float.class) || (type == Float.class))
			{
				return Float.parseFloat(default_.value());
			}
			else if ((type == double.class) || (type == Double.class))
			{
				return Double.parseDouble(default_.value());
			}
			else if ((type == boolean.class) || (type == Boolean.class))
			{
				String value = default_.value().toLowerCase();
				if (value.equals("true"))
				{
					return true;
				}
				if (value.equals("false"))
				{
					return false;
				}
			}
			else if (type == String.class)
			{
				return default_.value();
			}
			else if (type.isEnum())
			{
				return Enum.valueOf((Class<Enum>) type, default_.value());
			}
		}
		catch (Throwable t)
		{
		}
		throw new Exception("Unable to parse @Default value. Value=" + default_.value() + ". Site=" + method.getName());
	}

	static class DistanceContentWriter
	{
	    int distance;
	    ContentWriter<?> contentWriter;
	    DistanceContentWriter(int distance,ContentWriter<?> contentWriter)
	    {
	        this.distance=distance;
	        this.contentWriter=contentWriter;
	    }
	}
	private void storeClosestDistanceWriter(HashMap<String,DistanceContentWriter> writers,String mediaType,int distance,ContentWriter<?> writer)
	{
	    DistanceContentWriter closest=writers.get(mediaType);
	    if ((closest==null)||(closest.distance>distance))
	    {
	        writers.put(mediaType,new DistanceContentWriter(distance, writer));
	    }
	}
	
	private void register(String root, Object object, Method method, ClassAnnotations classAnnotations, Transformers transformers) throws Exception
	{
		ContentWriters contentWriters = null;
		ContentReaders contentReaders = null;
		ContentDecoders contentDecoders = null;
		ContentEncoders contentEncoders = null;
		Filters filters = classAnnotations.filters;

		Path path = null;
		String httpMethod = null;
		int verbs = 0;

		for (Annotation annotation : method.getAnnotations())
		{
			Class<?> type = annotation.annotationType();
			if (type == ContentReaders.class)
			{
				contentReaders = (ContentReaders) annotation;
			}
			else if (type == ContentWriters.class)
			{
				contentWriters = (ContentWriters) annotation;
			}
			else if (type == ContentEncoders.class)
			{
				contentEncoders = (ContentEncoders) annotation;
			}
			else if (type == ContentDecoders.class)
			{
				contentDecoders = (ContentDecoders) annotation;
			}
			else if (type == Filters.class)
			{
				filters = (Filters) annotation;
			}
			else if (type == GET.class)
			{
				httpMethod = "GET";
				verbs++;
			}
			else if (type == PUT.class)
			{
				httpMethod = "PUT";
				verbs++;
			}
			else if (type == DELETE.class)
			{
				httpMethod = "DELETE";
				verbs++;
			}
			else if (type == POST.class)
			{
				httpMethod = "POST";
				verbs++;
			}
			else if (type == PATCH.class)
			{
				httpMethod = "PATCH";
				verbs++;
			}
			else if (type == HEAD.class)
			{
				httpMethod = "HEAD";
				verbs++;
			}
			else if (type == OPTIONS.class)
			{
				httpMethod = "OPTION";
				verbs++;
			}
			else if (type == TRACE.class)
			{
				httpMethod = "TRACE";
				verbs++;
			}
			else if (type == Path.class)
			{
				path = (Path) annotation;
			}
		}
		if (contentWriters == null)
		{
			contentWriters = classAnnotations.contentWriters;
		}
		if (contentReaders == null)
		{
			contentReaders = classAnnotations.contentReaders;
		}
		if (contentDecoders == null)
		{
			contentDecoders = classAnnotations.contentDecoders;
		}
		if (contentEncoders == null)
		{
			contentEncoders = classAnnotations.contentEncoders;
		}
		if (filters == null)
		{
			filters = classAnnotations.filters;
		}

		if (verbs == 0)
		{
			return;
		}
		if (verbs > 1)
		{
			throw new Exception("Multiple Http verbs. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
		}
		if (path == null)
		{
			throw new Exception("Missing @Path annotation. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
		}

		// filters
		ArrayList<Filter> handlerFilters = new ArrayList<Filter>();
		if (transformers.bottomFilters != null)
		{
			for (Filter filter : transformers.bottomFilters)
			{
				handlerFilters.add(filter);
			}
		}
		if (filters != null)
		{
			for (Class<? extends Filter> type: filters.value())
			{
				Filter filter = transformers.getFilter(type);
				if (filter == null)
				{
					throw new Exception("No instance of requested filter " + type.getName()+ ". Site=" + object.getClass().getCanonicalName() + "." + method.getName());
				}
				handlerFilters.add(filter);
			}
		}
		if (transformers.topFilters != null)
		{
			for (Filter filter : transformers.topFilters)
			{
				handlerFilters.add(filter);
			}
		}

		// parameters
		ArrayList<ParameterInfo> parameterInfos = new ArrayList<ParameterInfo>();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] annotations = method.getParameterAnnotations();

		for (int parameterIndex = 0; parameterIndex < parameterTypes.length; parameterIndex++)
		{
			Class<?> parameterType = parameterTypes[parameterIndex];
			if (parameterType == Trace.class)
			{
				parameterInfos.add(new ParameterInfo(ParameterSource.TRACE, null, parameterIndex, parameterType, null));
				continue;
			}

			// Process parameter annotations
			Annotation[] parameterAnnotations = annotations[parameterIndex];

			DefaultValue default_ = null;
			ContentParam contentParam = null;
			CookieParam cookieParam = null;
			HeaderParam headerParam = null;
			PathParam pathParam = null;
			QueryParam queryParam = null;
			StateParam stateParam = null;

			for (Annotation annotation : parameterAnnotations)
			{
				Class<?> type = annotation.annotationType();
				if (type == DefaultValue.class)
				{
					default_ = (DefaultValue) annotation;
				}
				else if (type == ContentParam.class)
				{
					if (contentReaders == null)
					{
						throw new Exception("Need @ContentReaders for @ContentParam." + object.getClass().getCanonicalName() + "." + method.getName());
					}
					contentParam = (ContentParam) annotation;
				}
				else if (type == CookieParam.class)
				{
					cookieParam = (CookieParam) annotation;
				}
				else if (type == HeaderParam.class)
				{
					headerParam = (HeaderParam) annotation;
				}
				else if (type == PathParam.class)
				{
					pathParam = (PathParam) annotation;
				}
				else if (type == QueryParam.class)
				{
					queryParam = (QueryParam) annotation;
				}
				else if (type == StateParam.class)
				{
					stateParam = (StateParam) annotation;
				}
			}

			// Check if there are multiple param annotations
			ArrayList<Annotation> params = new ArrayList<Annotation>();
			if (contentParam != null)
			{
				params.add(contentParam);
			}
			if (cookieParam != null)
			{
				params.add(cookieParam);
			}
			if (pathParam != null)
			{
				params.add(pathParam);
			}
			if (headerParam != null)
			{
				params.add(headerParam);
			}
			if (queryParam != null)
			{
				params.add(queryParam);
			}
			if (stateParam != null)
			{
				params.add(stateParam);
			}
			if (params.size() > 1)
			{
				throw new Exception("Only one param annotation allowed. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
			}
			else if (params.size() == 0)
			{
				if (parameterType != Context.class)
				{
					throw new Exception("No param annotation. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
				}
			}

			// No multiple param annotations. Check each and add.
			if (parameterType == Context.class)
			{
				if (default_ != null)
				{
					throw new Exception("@Default annotation not allowed parameter of type RequestState. Site=" + object.getClass().getCanonicalName() + "."
							+ method.getName());
				}
				parameterInfos.add(new ParameterInfo(ParameterSource.CONTEXT, null, parameterIndex, parameterType, null));
			}
			else if (contentParam != null)
			{
				if (default_ != null)
				{
					throw new Exception("@Default annotation not allowed for @ContentParam parameter. Site=" + method.getName());
				}
				parameterInfos.add(new ParameterInfo(ParameterSource.CONTENT, null, parameterIndex, parameterType, null));
			}
			else if (stateParam != null)
			{
				if (default_ != null)
				{
					throw new Exception("@Default annotation not allowed for @StateParam parameter. Site=" + method.getName());
				}
				parameterInfos.add(new ParameterInfo(ParameterSource.STATE, null, parameterIndex, parameterType, null));
			}
			else if (cookieParam != null)
			{
				if (parameterType == Cookie.class)
				{
					if (default_ != null)
					{
						throw new Exception("@Default annotation not allowed for @CookieParam parameter with type Cookie. Site="
								+ object.getClass().getCanonicalName() + "." + method.getName());
					}
				}
				else if (isSimpleParameterType(parameterType) == false)
				{
					throw new Exception("Only simple types allowed for parameter. Site=" + method.getName());
				}
				parameterInfos.add(new ParameterInfo(ParameterSource.COOKIE, cookieParam.value(), parameterIndex, parameterType,
						getDefaultValue(method, default_, parameterType)));
			}
			else if (pathParam != null)
			{
				if (isSimpleParameterType(parameterType) == false)
				{
					throw new Exception("Only simple types allowed for parameter. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
				}
				parameterInfos.add(new ParameterInfo(ParameterSource.PATH, pathParam.value(), parameterIndex, parameterType,
						getDefaultValue(method, default_, parameterType)));
			}
			else if (queryParam != null)
			{
				if (isSimpleParameterType(parameterType) == false)
				{
					throw new Exception("Only simple types allowed for parameter. Site=" + method.getName());
				}
				parameterInfos.add(new ParameterInfo(ParameterSource.QUERY, queryParam.value(), parameterIndex, parameterType,
						getDefaultValue(method, default_, parameterType)));
			}
			else if (headerParam != null)
			{
				if (isSimpleParameterType(parameterType) == false)
				{
					throw new Exception("Only simple types allowed for parameter. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
				}
				parameterInfos.add(new ParameterInfo(ParameterSource.HEADER, headerParam.value(), parameterIndex, parameterType,
						getDefaultValue(method, default_, parameterType)));
			}
		}

		// ContentReaders and writers
		HashMap<String, ContentWriter<?>> contentWriterMap = new HashMap<>();
		Type returnType=method.getReturnType();
		if (contentWriters == null)
		{
			if (returnType != void.class)
			{
				throw new Exception("Method return type is not void. @ContentWriters annotation missing. Site="
						+ object.getClass().getCanonicalName() + "." + method.getName());
			}
		}
		else 
		{
		    if (returnType==Response.class)
		    {
		        Type genericReturnType=method.getGenericReturnType();
		        if (genericReturnType instanceof ParameterizedType)
		        {
		            returnType=((ParameterizedType)genericReturnType).getActualTypeArguments()[0];
		        }
		        else
		        {
		            returnType=Object.class;
		        }
		    }
		    
		    if ((returnType!=void.class)&&(returnType!=Void.class))
		    {
		        HashMap<String,DistanceContentWriter> closestDistanceWriters=new HashMap<>();
    			for (Class<? extends ContentWriter<?>> type : contentWriters.value())
    			{
    				ContentWriter<?> writer=transformers.getContentWriter(type);
    				if (writer==null)
    				{
                        throw new Exception("Need to register ContentWriter: "+type.getName()+", Site="+ object.getClass().getCanonicalName() + "." + method.getName());
    				}
				    ParameterizedType writerType=(ParameterizedType)writer.getClass().getGenericSuperclass();
				    Type writerContentType=writerType.getActualTypeArguments()[0];

				    int distance=Utils.getAncestorDistance((Class<?>)returnType, (Class<?>)writerContentType);
				    if (distance>=0)
				    {
    				    String mediaType=writer.getMediaType();
    				    storeClosestDistanceWriter(closestDistanceWriters, mediaType, distance, writer);
    					String anySubType = org.nova.http.server.WsUtils.toAnySubMediaType(mediaType);
                        storeClosestDistanceWriter(closestDistanceWriters, anySubType, distance, writer);
                        storeClosestDistanceWriter(closestDistanceWriters, "*/*", distance, writer);
				    }
    			}
    			for (Entry<String, DistanceContentWriter> entry:closestDistanceWriters.entrySet())
    			{
    			    contentWriterMap.put(entry.getKey(), entry.getValue().contentWriter);
    			}
    			
    			if (contentWriterMap.size()==0)
    			{
                    throw new Exception("No suitable ContentWriter found. "
                            + object.getClass().getCanonicalName() + "." + method.getName());
    			}
		    }
		}

		HashMap<String, ContentReader<?>> contentReaderMap = new HashMap<>();
		if (contentReaders!=null) 
		{
			for (Class<? extends ContentReader<?>> type : contentReaders.value())
			{
				ContentReader<?> reader=transformers.getContentReader(type);
				if (reader!=null)
				{
					contentReaderMap.put(reader.getMediaType(), reader);
				}
			}
		}
		
		HashMap<String, ContentDecoder> contentDecoderMap = new HashMap<>();
		if (contentDecoders != null)
		{
			for (Class<? extends ContentDecoder> type: contentDecoders.value())
			{
				ContentDecoder decoder=transformers.getContentDecoder(type);
				if (decoder==null)
				{
					throw new Exception(
							"No ContentEncoder of type "+type.getName()+" is supplied. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
					
				}
				contentDecoderMap.put(type.getName(),decoder);
			}
		}

		HashMap<String, ContentEncoder> contentEncoderMap = new HashMap<>();
		if (contentEncoders!= null)
		{
			for (Class<? extends ContentEncoder> type: contentEncoders.value())
			{
				ContentEncoder encoder=transformers.getContentEncoder(type);
				if (encoder==null)
				{
					throw new Exception(
							"No ContentDecoder of type "+type.getName()+" is supplied. Site=" + object.getClass().getCanonicalName() + "." + method.getName());
					
				}
                contentEncoderMap.put(type.getName(),encoder);
			}
		}

		// register the request handler
		String fullPath = root != null ? root + path.value() : path.value();
		RequestHandler requestHandler = new RequestHandler(object, method, httpMethod, fullPath, handlerFilters.toArray(new Filter[handlerFilters.size()]),
				parameterInfos.toArray(new ParameterInfo[parameterInfos.size()]), contentDecoderMap, contentEncoderMap, contentReaderMap, contentWriterMap,
				true);
		add(httpMethod, fullPath, requestHandler);
	}

	void add(String method, String path, RequestHandler requestHandler) throws Exception
	{
		Map map = getMap(method);
		if (map == null)
		{
			throw new Exception("Invalid http method=" + method + ". Site=" + requestHandler.getMethod().getName());
		}
		this.requestHandlers.put(requestHandler.getKey(), requestHandler);
		String[] fragments = Utils.split(path, '/');
		if (fragments[0].length() != 0)
		{
			throw new Exception("Path must start with root / character. Site=" + requestHandler.getMethod().getName());
		}
		Node node = null;
		HashMap<String, Integer> indexMap = new HashMap<>();
		int index = 0;
		for (int i = 1; i < fragments.length; i++)
		{
			String fragment = fragments[i];
			String key = fragment;
			if ((fragment.length() > 0) && (fragment.charAt(0) == '{') && (fragment.charAt(fragment.length() - 1) == '}'))
			{
				String name = fragment.substring(1, fragment.length() - 1);
				if (indexMap.containsKey(name))
				{
					throw new Exception("Path parameter names must be unique. Path=" + path + ", site=" + requestHandler.getMethod().getName());
				}
				indexMap.put(name, index++);
				if (PathParam.AT_LEAST_ONE_SEGMENT.equals(name))
				{
					if (i != fragments.length - 1)
					{
						throw new Exception("Rest of path must be last path parameter. Path=" + path + ", site=" + requestHandler.getMethod().getName());
					}
					key = PathParam.AT_LEAST_ONE_SEGMENT;
				}
				else
				{
					key = "/";
				}
			}
			node = map.get(key);
			if (node == null)
			{
				node = new Node();
				map.put(key, node);
			}
			if (PathParam.AT_LEAST_ONE_SEGMENT.equals(key))
			{
				break;
			}
			map = node.map;
		}
		if (node.requestHandler != null)
		{
			throw new Exception(
					"Path conflict. Existing Path=" + node.requestHandler.getPath() + ", existing requestHandler=" + node.requestHandler.getMethod().getName()
							+ ", new Path" + requestHandler.getPath() + ", new request handler=" + requestHandler.getMethod().getName());
		}
		ParameterInfo[] parameterInfos = requestHandler.getParameterInfos();
		for (int i = 0; i < parameterInfos.length; i++)
		{
			ParameterInfo parameterInfo = parameterInfos[i];
			if (parameterInfo.getSource() == ParameterSource.PATH)
			{
				Integer pathIndex = indexMap.get(parameterInfo.getName());
				if (pathIndex == null)
				{
					throw new Exception("PathParam name not found in Path. Path=" + path + ", PathParam name=" + parameterInfo.getName());
				}
				parameterInfos[i] = new ParameterInfo(parameterInfo, pathIndex);
			}
		}
		node.requestHandler = requestHandler;
	}

	RequestHandlerWithParameters resolve(String method, String path)
	{
		Map map = getMap(method);
		if (map == null)
		{
			return null;
		}
		String[] fragments = Utils.split(path, '/');
		Node node = null;
		String[] parameters = new String[fragments.length];
		int index = 0;
		for (int i = 1; i < fragments.length; i++)
		{
			String fragment = fragments[i];
			node = map.get(fragment);
			if (node == null)
			{
				node = map.get(PathParam.AT_LEAST_ONE_SEGMENT);
				if (node != null)
				{
					StringBuilder sb = new StringBuilder();
					sb.append(fragments[i]);
					for (int j = i + 1; j < fragments.length; j++)
					{
						sb.append("/" + fragments[j]);
					}
					parameters[index++] = sb.toString();
					break;
				}
				node = map.get("/");
				if (node != null)
				{
					parameters[index++] = fragment;
					map = node.map;
					continue;
				}
				return null;
			}
			map = node.map;
		}
		if (node.requestHandler == null)
		{
			return null;
		}
		return new RequestHandlerWithParameters(node.requestHandler, parameters);
	}
}
