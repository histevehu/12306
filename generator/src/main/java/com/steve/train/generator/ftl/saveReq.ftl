package com.steve.train.${module}.req;

<#--遍历传入的typeSet-->
<#--"#数据类型  数据集 as 数据个体"，相当于for循环-->
<#list typeSet as type>
    <#if type=='Date'>
        import java.util.Date;
        import com.fasterxml.jackson.annotation.JsonFormat;
    </#if>
    <#if type=='BigDecimal'>
        import java.math.BigDecimal;
    </#if>
</#list>

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * @author     : Steve Hu
 * @date       : ${DateAndTime}
 * @description: ${Domain}保存（新增、修改）请求封装类（FreeMarker生成）
 */
public class ${Domain}SaveReq {

<#--遍历字段，添加变量及相应注解-->
<#list fieldList as field>
    /**
    * ${field.comment}
    */
    <#if field.javaType=='Date'>
        <#if field.type=='time'>
            @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type=='date'>
            @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
        <#else>
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        </#if>
    </#if>
    <#if field.name!="id" && field.nameHump!="createdAt" && field.nameHump!="updatedAt">
        <#if !field.nullAble>
            <#if field.javaType=='String'>
                @NotBlank(message = "${field.nameCn}不能为空")
            <#else>
                @NotNull(message = "${field.nameCn}不能为空")
            </#if>
        </#if>
    </#if>
    private ${field.javaType} ${field.nameHump};

</#list>
<#--创建get和set方法-->
<#list fieldList as field>
    public ${field.javaType} get${field.nameBigHump}() {
    return ${field.nameHump};
    }

    public void set${field.nameBigHump}(${field.javaType} ${field.nameHump}) {
    this.${field.nameHump} = ${field.nameHump};
    }

</#list>
@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append(getClass().getSimpleName());
sb.append(" [");
sb.append("Hash = ").append(hashCode());
<#list fieldList as field>
    sb.append(", ${field.nameHump}=").append(${field.nameHump});
</#list>
sb.append("]");
return sb.toString();
}
}
