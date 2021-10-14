package com.jeck.http.bean;

import com.jeck.http.interfaces.IHttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonHttpEntity implements IHttpEntity {



    InputStream wrappedStream;

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return wrappedStream;
    }

    @Override
    public void writeTo(OutputStream var1) throws IOException {

    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void consumeContent() throws IOException {

    }

    public void setWrappedStream(InputStream wrappedStream) {
        this.wrappedStream = wrappedStream;
    }
}
