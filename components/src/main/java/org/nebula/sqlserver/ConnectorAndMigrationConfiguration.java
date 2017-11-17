package org.nebula.sqlserver;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.nova.sqldb.SqlServerConfiguration;

public class ConnectorAndMigrationConfiguration
{
    public SqlServerConfiguration connectorConfiguration;
    public String passwordKey;
    public String user;
    public String scriptFile;
    public Charset charSet=StandardCharsets.UTF_16LE;
}
