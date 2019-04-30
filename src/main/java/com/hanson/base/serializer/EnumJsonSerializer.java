package com.hanson.base.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hanson.base.enums.EnumType;

/**
 * Create by hanlin on 2017年11月8日
 * 此注解可以将输出枚举类序列化为以下结构，输入默认已经支持code或者name形式进行更新。
 * {
 * 		"code":"200",
 * 		"name":"SUCCESS", 
 *  	"text":"成功",
 * }
 **/
public class EnumJsonSerializer extends JsonSerializer<EnumType>{  
    @Override  
    public void serialize(EnumType obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {  
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("code");
        jsonGenerator.writeNumber(obj.code());
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString(obj.name());
        jsonGenerator.writeFieldName("text");
        jsonGenerator.writeString(obj.text());
        jsonGenerator.writeEndObject();
    }  
}  
