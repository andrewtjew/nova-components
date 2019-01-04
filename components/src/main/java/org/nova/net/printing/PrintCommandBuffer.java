package org.nova.net.printing;

import java.util.ArrayList;

import org.nova.json.ObjectMapper;

public class PrintCommandBuffer
{
    final private ArrayList<Command> commands;
    
    public PrintCommandBuffer()
    {
        this.commands=new ArrayList<>();
    }
    public void add(Command command)
    {
        this.commands.add(command);
    }
    public String toString()
    {
        try
        {
            SerializedCommand[] serializedCommands=new SerializedCommand[this.commands.size()];
            for (int i=0;i<serializedCommands.length;i++)
            {
                Command command=this.commands.get(i);
                String text=ObjectMapper.writeObjectToString(command);
                serializedCommands[i]=new SerializedCommand(command.code, text);
            }
            return ObjectMapper.writeObjectToString(serializedCommands);
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }
    
}
