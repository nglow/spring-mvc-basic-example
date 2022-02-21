package com.example.itemservice.typeconverter;

import com.example.itemservice.typeconverter.converter.IntegerToStringConverter;
import com.example.itemservice.typeconverter.converter.IpPortToStringConverter;
import com.example.itemservice.typeconverter.converter.StringToIntegerConverter;
import com.example.itemservice.typeconverter.converter.StringToIpPortConverter;
import com.example.itemservice.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 주석처리 우선순위
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new IpPortToStringConverter());
        registry.addConverter(new StringToIpPortConverter());

        // 추가
        registry.addFormatter(new MyNumberFormatter());
    }
}
