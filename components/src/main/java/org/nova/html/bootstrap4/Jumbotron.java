package org.nova.html.bootstrap4;

public class Jumbotron extends StyleComponent<Jumbotron>
{
    public Jumbotron(String src,String alt)
    {
        super("div","jumbotron");
    }


    public Jumbotron jumbotron_fluid()
    {
        addClass("jumbotron-fluid");
        return this;
    }


}
