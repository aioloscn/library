package com.aiolos.library.config.xss;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对请求数据转义，防止XSS攻击
 * @author Aiolos
 * @date 2021/3/19 9:54 上午
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StrUtil.isEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (!StrUtil.isEmpty(value)) {
                    value = HtmlUtil.filter(value);
                }
                values[i] = value;
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = super.getParameterMap();
        Map<String, String[]> map = new LinkedHashMap<>();
        parameters.forEach((k, v) -> {
            for (int i = 0; i < v.length; i++) {
                String value = v[i];
                if (!StrUtil.isEmpty(value)) {
                    value = HtmlUtil.filter(value);
                }
                v[i] = value;
            }
            map.put(k, v);
        });
        return map;
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if (!StrUtil.isEmpty(header)) {
            header = HtmlUtil.filter(header);
        }
        return header;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream in = super.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuffer.append(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        inputStreamReader.close();
        in.close();

        Map<String, Object> map = JSONUtil.parseObj(stringBuffer.toString());
        Map<String, Object> result = new LinkedHashMap<>();
        map.forEach((k, v) -> {
            if (v instanceof String) {
                if (!StrUtil.isEmpty(v.toString())) {
                    result.put(k, HtmlUtil.filter(v.toString()));
                }
            } else {
                result.put(k, v);
            }
        });

        String jsonStr = JSONUtil.toJsonStr(result);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }
}
