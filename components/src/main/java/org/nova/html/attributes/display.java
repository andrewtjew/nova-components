package org.nova.html.attributes;

public enum display
{
    inline("inline"),
    block("block"),
    flex("flex"),
    inline_block("inline-block"),
    inline_flex("inline-flex"),
    inline_table("inline-table"),
    list_item("list-item"),
    run_in("run-in"),
    table("table"),
    table_caption("table-caption"),
    table_column_group("table-column-group"),
    table_header_group("table-header-group"),
    table_footer_group("table-footer-group"),
    table_row_group("table-row-group"),
    table_cell("table-cell"),
    table_column("table-column"),
    table_row("table-row"),
    none("none"),
    initial("initial"),
    inherit("inherit")
    ;
    final String value;
    display(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
