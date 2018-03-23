package org.nova.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.nova.concurrent.MultiTaskScheduler;
import org.nova.concurrent.Synchronization;
import org.nova.tracing.Trace;

import com.nova.disrupt.DisruptorTraceContext;

public class PostContext implements AutoCloseable
{

    static class PostOutputStream extends OutputStream
    {
        final OutputStream outputStream;
        final PostContext context;
        public PostOutputStream(OutputStream outputStream,PostContext context)
        {
            this.outputStream=outputStream;
            this.context=context;
        }
    
        @Override
        public void write(int b) throws IOException
        {
            this.outputStream.write(b);
        }
    
        @Override
        public void write(byte b[], int off, int len) throws IOException 
        {
            this.outputStream.write(b, off, len);
        }
        
        @Override
        public void flush() throws IOException
        {
            this.outputStream.flush();
        }
    
        @Override
        public void close() throws IOException
        {
            this.outputStream.close();
            this.context.waitForResponse();
        }
    }

    private final HttpPost post;
    private PipedOutputStream pipedOutputStream;

    final private DisruptorTraceContext context;
    final private HttpClient client;
    private HttpResponse response;
    private boolean threadEnded;
    private IOException threadException;

    PostContext(Trace parent, String traceCategory, HttpClient client, MultiTaskScheduler scheduler, HttpPost post, DisruptorTraceContext context) throws IOException
    {
        this.client = client;
        this.post = post;
        this.context = context;
        scheduler.schedule(parent, scheduler.getClass().getSimpleName() + "@" + traceCategory, (trace) ->
        {
            execute();
        });
    }

    private void execute() throws Exception
    {
        HttpResponse response=null;
        try
        {
            PipedInputStream pipedInputStream=new PipedInputStream();
            PipedOutputStream pipedOutputStream = new PipedOutputStream();
            pipedOutputStream.connect(pipedInputStream);

            InputStreamEntity entity = new InputStreamEntity(pipedInputStream);
            post.setEntity(entity);
            synchronized(this)
            {
                this.pipedOutputStream=pipedOutputStream;
                this.notifyAll();
            }
            response = this.client.execute(this.post);
        }
        catch (IOException t)
        {
            synchronized (this)
            {
                this.threadException=t;
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            synchronized (this)
            {
                this.response = response;
                this.threadEnded=true;
                this.notifyAll();
            }
        }
    }

    public OutputStream getOutputStream()
    {
        synchronized(this)
        {
            Synchronization.waitForNoThrow(this,()->{return this.pipedOutputStream!=null;});
        }
        return new PostOutputStream(this.pipedOutputStream,this);
    }

    public int getStatusCode() throws Throwable
    {
        waitForResponse();
        return this.response.getStatusLine().getStatusCode();
    }
    
    public void waitForResponse() throws IOException
    {
        synchronized (this)
        {
            Synchronization.waitForNoThrow(this, () ->
            {
                return this.threadEnded;
            });
            if (this.threadException!=null)
            {
                throw this.threadException;
            }
            if (this.response==null)
            {
                throw new IOException("No response");
            }
        }
    }
    
    public InputStream getInputStream() throws Throwable
    {
        this.pipedOutputStream.close();
        waitForResponse();
        return this.response.getEntity().getContent();
    }

    @Override
    public void close() throws Exception
    {
        waitForResponse();
        if (this.response!=null)
        {
            this.response.getEntity().getContent().close();
        }
        this.context.close();
    }

}
