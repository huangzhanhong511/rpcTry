package com.richard.compress.gzip;

import com.richard.compress.Compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompress implements Compress {

    private static final int BUFFER_SIZE = 1024 * 4;
    @Override
    public byte[] compress(byte[] data) {
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(data);
            gzip.flush();
            gzip.finish();
            return baos.toByteArray();

        }catch (IOException e){
            throw new RuntimeException("gzip compress error: "+e);
        }
    }

    @Override
    public byte[] decompress(byte[] data) {
        if(data == null) {
            throw new NullPointerException("data is null");
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = gzip.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
