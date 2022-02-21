package com.example.itemservice.typeconverter.converter;

import com.example.itemservice.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {
        log.info("Convert source = {}", source);

        // "127.0.0.1:8080"
        var split = source.split(":");
        var ip = split[0];
        var port = Integer.parseInt(split[1]);
        return new IpPort(ip, port);
    }
}
