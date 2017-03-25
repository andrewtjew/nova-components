package org.nova.services;

import org.nova.http.server.Context;

public abstract class SessionRejectResponder
{
    abstract void respondToNoSession(SessionFilter sessionFilter,Context context);
    abstract void respondToAccessDenied(SessionFilter sessionFilter,Session session,Context context);
    abstract void respondToNoLock(SessionFilter sessionFilter,Session session,Context context);
    abstract String getAssociatedMediaType();
}
