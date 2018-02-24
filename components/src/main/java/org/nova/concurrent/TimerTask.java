package org.nova.concurrent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Timer;

import org.nova.concurrent.TimerScheduler.Key;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;

public class TimerTask
{
	static public enum TimeBase
	{
		FIXED,
		FREE,
		MINUTE,
        HOUR,
        HALF_HOUR,
        QUARTER_HOUR,
		DAY,
		WEEK,
		MONTH,
		YEAR,
	}
	
	final private TimerScheduler timerScheduler;
	final private TimerRunnable executable;
	final private String category;
	final private long period;
	final private long offset;
	final private long number;
	final private long created;
	
	final TimeBase schedulingMode;
	private long due;
	private long totalDuration;
	private long attempts;
	private long successes;
	private long throwables;
	private Throwable lastThrowable;
	private TimerScheduler.Key key;
	private TaskStatus executeStatus;
	private long misses;
	private boolean cancel;
	
	
	TimerTask(long number,String category,TimerScheduler timerScheduler,TimeBase schedulingMode,long offset,long period,TimerRunnable executable)
	{
		this.created=System.currentTimeMillis();
		this.number=number;
		this.offset=offset;
		this.category=category;
		this.timerScheduler=timerScheduler;
		this.executable=executable;
		this.period=period;
		this.executeStatus=TaskStatus.READY;
		this.schedulingMode=schedulingMode;
		this.key=new Key(this.due, this.number);
	}
	
	TimerScheduler.Key getKey()
	{
		return this.key;
	}
	
	Key execute()
	{
		try (Trace trace=new Trace(this.timerScheduler.traceManager,this.category))
		{
			try
			{
				synchronized(this)
				{
					if (this.cancel)
					{
						this.executeStatus=TaskStatus.COMPLETED;
						return null;
					}
					this.executeStatus=TaskStatus.EXECUTING;
				}
				this.attempts++;
				this.executable.run(trace, this);
				this.successes++;
			}
			catch (Throwable t)
			{
				this.lastThrowable=t;
				this.throwables++;
				trace.close(t);
				Logger logger=this.timerScheduler.getLogger();
				if (logger!=null)
				{
				    logger.log(trace);
				}
			}
			this.totalDuration+=trace.getDurationNs();
			synchronized(this)
			{
				if (this.cancel)
				{
					this.executeStatus=TaskStatus.COMPLETED;
					return null;
				}
				if (period<=0)
				{
					this.executeStatus=TaskStatus.COMPLETED;
					return null;
				}
				this.executeStatus=TaskStatus.READY;
			}
			return getSchedule();
		}
	}
	
	Key getSchedule()
	{
        switch (this.schedulingMode)
        {
        case FIXED:
        {
            long now=System.currentTimeMillis();
            if (this.due==0)
            {
                this.due=now+this.offset;
            }
            else
            {
                long duration=now-this.due;
                long misses=duration/period;
                this.misses+=misses;
                this.due=this.due+this.period*(misses+1);
            }
        }   
            break;
        
        case FREE:
        {
            long now=System.currentTimeMillis();
            if (this.due==0)
            {
                this.due=now+this.offset;
            }
            else
            {
                long duration=now-this.due;
                this.misses+=duration/period;
                this.due=now+this.period;
            }
        }   
            break;

        case YEAR:
        {
           ZonedDateTime now=ZonedDateTime.now(ZoneOffset.UTC);
           long currentTimeMillis=System.currentTimeMillis();
           int days=now.getDayOfYear()+1;
           ZonedDateTime next=now.minusDays(days).minusHours(now.getHour()).minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano());
           
           this.due=next.toInstant().toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusYears(1);
               this.due=next.toInstant().toEpochMilli()+this.offset;
           }
        }
           break;

        case MONTH:
        {
            ZonedDateTime now=ZonedDateTime.now(ZoneOffset.UTC);
           long currentTimeMillis=System.currentTimeMillis();
           int days=now.getDayOfMonth()-1;
           ZonedDateTime next=now.minusDays(days).minusHours(now.getHour()).minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano());
           
           this.due=next.toInstant().toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusMonths(1);
               this.due=next.toInstant().toEpochMilli()+this.offset;
           }
        }
           break;

        case WEEK:
        {
           ZonedDateTime now=ZonedDateTime.now(ZoneOffset.UTC);
           long currentTimeMillis=System.currentTimeMillis();
           int day=now.getDayOfWeek().getValue();
           ZonedDateTime next=now.minusHours(now.getHour()).minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano()).minusDays(day);
    
           this.due=next.toInstant().toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusDays(7);
               this.due=next.toInstant().toEpochMilli()+this.offset;
           }
        }
           break;

        case DAY:
        {
           ZonedDateTime now=ZonedDateTime.now(ZoneOffset.UTC);
           long currentTimeMillis=System.currentTimeMillis();
           ZonedDateTime next=now.minusHours(now.getHour()).minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano());
           this.due=next.toInstant().toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusDays(1);
               this.due=next.toInstant().toEpochMilli()+this.offset;
           }
        }
           break;
           
        case HOUR:
        {
           ZonedDateTime now=ZonedDateTime.now(ZoneOffset.UTC);
           long currentTimeMillis=System.currentTimeMillis();
           ZonedDateTime next=now.minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano());
           this.due=next.toInstant().toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusHours(1);
               this.due=next.toInstant().toEpochMilli()+this.offset;
           }
        }
           break;
           
        case HALF_HOUR:
        {
           ZonedDateTime now=ZonedDateTime.now(ZoneOffset.UTC);
           long currentTimeMillis=System.currentTimeMillis();
           int add=(now.getMinute()/30)*30;
           ZonedDateTime next=now.minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano()).plusMinutes(add);
           this.due=next.toInstant().toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusMinutes(30);
               this.due=next.toInstant().toEpochMilli()+this.offset;
           }
        }
           break;

        case QUARTER_HOUR:
        {
           ZonedDateTime now=ZonedDateTime.now(ZoneOffset.UTC);
           long currentTimeMillis=System.currentTimeMillis();
           int add=(now.getMinute()/15)*15;
           ZonedDateTime next=now.minusMinutes(now.getMinute()).minusSeconds(now.getSecond()).minusNanos(now.getNano()).plusMinutes(add);
           this.due=next.toInstant().toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusMinutes(15);
               this.due=next.toInstant().toEpochMilli()+this.offset;
           }
        }
           break;
           
        case MINUTE:
        {
           LocalDateTime now=LocalDateTime.now();
           long currentTimeMillis=System.currentTimeMillis();
           LocalDateTime next=now.minusSeconds(now.getSecond()).minusNanos(now.getNano());
           this.due=next.toInstant(ZoneOffset.UTC).toEpochMilli()+this.offset;
           if (this.due<currentTimeMillis)
           {
               next=next.plusMinutes(1);
               this.due=next.toInstant(ZoneOffset.UTC).toEpochMilli()+this.offset;
           }
        }
           break;
           
        }
        this.key=new Key(this.due, this.number);
        return this.key;
	    
	}
	
	public void cancel()
	{
		synchronized (this)
		{
			this.cancel=true;
			this.timerScheduler.cancel(this.key);
		}
	}

	public String getCategory()
	{
		return category;
	}

	public TimeBase getShedulingMode()
	{
		return this.schedulingMode;
	}
	public long getPeriod()
	{
		return period;
	}

	public long getNumber()
	{
		return number;
	}

	public long getCreated()
	{
		return created;
	}

	public long getDue()
	{
		return due;
	}

	public long getDelay()
	{
		return offset;
	}

	public long getTotalDuration()
	{
		return totalDuration;
	}

	public long getAttempts()
	{
		return attempts;
	}

	public long getSuccesses()
	{
		return successes;
	}

	public long getThrowables()
	{
		return throwables;
	}

	public Throwable getLastThrowable()
	{
		return lastThrowable;
	}
	public TaskStatus getExecutableStatus()
	{
		return executeStatus;
	}
	public long getMisses()
	{
		return misses;
	}
}
