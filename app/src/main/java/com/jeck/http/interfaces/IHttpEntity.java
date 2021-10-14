package com.jeck.http.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHttpEntity {
    boolean isRepeatable();

    boolean isChunked();

    long getContentLength();
//
//    Header getContentType();
//
//    Header getContentEncoding();

    InputStream getContent() throws IOException, IllegalStateException;

    void writeTo(OutputStream var1) throws IOException;

    boolean isStreaming();

    void consumeContent() throws IOException;
}