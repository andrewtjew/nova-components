package org.nova.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils
{
    public static String readTextFile(String fileName, String charset) throws Exception
    {
        return readTextFile(fileName, Charset.forName(charset));
    }

    public static String readTextFile(String fileName) throws Exception
    {
        return readTextFile(fileName, StandardCharsets.UTF_8);
    }

    public static String readTextFile(String fileName, Charset charset) throws Exception
    {
        return new String(readFile(fileName), charset);
    }

    public static String toNativePath(String fileName)
    {
        return fileName.replace('\\', File.separatorChar).replace('/', File.separatorChar);
    }

    public static byte[] readFile(String fileName) throws Exception
    {
        fileName = toNativePath(fileName);
        File file = new File(fileName);
        if (file.isDirectory() == true)
        {
            throw new Exception("File is a directory. Filename=" + fileName);
        }

        long length = file.length();
        if (length > Integer.MAX_VALUE)
        {
            throw new Exception("File too big. Filename=" + fileName);
        }

        try (FileInputStream stream = new FileInputStream(file))
        {
            byte[] bytes = new byte[(int) file.length()];
            stream.read(bytes);
            return bytes;
        }
    }

    public static String readString(InputStream stream, int bufferSize, Charset charset) throws IOException
    {
        int count = 0;
        byte[] buffer = new byte[bufferSize];
        byte[] stringBuffer = new byte[bufferSize];
        for (int bytesRead = stream.read(buffer); bytesRead > 0; bytesRead = stream.read(buffer))
        {
            if (bytesRead + count > stringBuffer.length)
            {
                byte[] newStringBuffer = new byte[stringBuffer.length * 2];
                System.arraycopy(stringBuffer, 0, newStringBuffer, 0, count);
                stringBuffer = newStringBuffer;
            }
            System.arraycopy(buffer, 0, stringBuffer, count, bytesRead);
            count += bytesRead;
        }
        return new String(stringBuffer, 0, count, charset);
    }

    public static String readString(InputStream stream, Charset charset) throws IOException
    {
        return readString(stream, 65536, charset);
    }

    public static String readString(InputStream stream) throws IOException
    {
        return readString(stream, StandardCharsets.UTF_8);
    }

    public static void writeTextFile(String fileName, String text) throws Exception
    {
        writeTextFile(fileName, text, StandardCharsets.UTF_8);
    }

    public static void writeTextFile(String fileName, String text, Charset charset) throws Exception
    {
        File file = new File(fileName);
        if (file.isDirectory() == true)
        {
            throw new Exception("File is a directory. Filename=" + fileName);
        }

        try (OutputStream stream = new FileOutputStream(file))
        {
            stream.write(text.getBytes(charset));
        }
    }

    public static void writeTextFile(String fileName, String text, String encoding) throws Exception
    {
        File file = new File(fileName);
        if (file.isDirectory() == true)
        {
            throw new Exception("File is a directory. Filename=" + fileName);
        }

        try (OutputStream stream = new FileOutputStream(file))
        {
            stream.write(text.getBytes(encoding));
        }
    }

    public static String computeHashSHA256(File file, int buffer) throws Throwable
    {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file))
        {
            byte[] data = new byte[buffer];
            int read = 0;
            while ((read = fis.read(data)) != -1)
            {
                sha256.update(data, 0, read);
            }
        }
        byte[] hashBytes = sha256.digest();
        return TypeUtils.bigEndianByteArrayToHexString(hashBytes);
    }
 
    public static String computeHashSHA256(String file) throws Throwable
    {
        return computeHashSHA256(new File(file),4096);
    }

}
