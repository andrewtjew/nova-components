package org.nebula.sqlserver;

import java.nio.charset.StandardCharsets;

import org.nova.sqldb.SqlServerConfiguration;

public class ConnectorAndMigrationConfiguration
{
    public SqlServerConfiguration connectorConfiguration;
    public String passwordKey;
    public String user;
    public String scriptFile;
    public String charSet=StandardCharsets.UTF_16LE.name();
    public boolean captureActivateStackTrace;
}
