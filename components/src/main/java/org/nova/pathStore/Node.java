package org.nova.pathStore;

import java.util.HashMap;

public class Node<A,V,AV extends AttributeValue<A,V>>
{
    HashMap<String,Node<A,V,AV>> nodes;
    AV attributeValue;
    Node()
    {
    }
}
