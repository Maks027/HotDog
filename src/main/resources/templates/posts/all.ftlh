<#import "*/parts/common.ftlh" as comm>
<#import "*/parts/navbar.ftlh" as navbar>

<@comm.page>
    <@navbar.nav/>
    <#include "*/parts/postsList.ftlh"/>
</@comm.page>