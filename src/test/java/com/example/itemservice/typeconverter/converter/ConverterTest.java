package com.example.itemservice.typeconverter.converter;

import com.example.itemservice.typeconverter.type.IpPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

    @Test
    void stringToInteger() {
        var converter = new StringToIntegerConverter();
        var result = converter.convert("10");
        assertThat(result).isEqualTo(10);
    }

    @Test
    void IntegerToString() {
        var converter = new IntegerToStringConverter();
        var result = converter.convert(10);
        assertThat(result).isEqualTo("10");
    }

    @Test
    void ipPortToString() {
        var converter = new IpPortToStringConverter();
        var source = new IpPort("127.0.0.1", 8080);
        var result = converter.convert(source);
        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void StringToIpPort() {
        var converter = new StringToIpPortConverter();
        var source = "127.0.0.1:8080";
        var result = converter.convert(source);
        assert result != null;
        assertThat(result.getIp()).isEqualTo("127.0.0.1");
        assertThat(result.getPort()).isEqualTo(8080);
    }
}
