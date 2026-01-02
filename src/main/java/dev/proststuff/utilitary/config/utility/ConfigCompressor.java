package dev.proststuff.utilitary.config.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ConfigCompressor {
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data);
        }
        return bos.toByteArray();
    }

    public static byte[] decompress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data))) {
            gis.transferTo(bos);
        }

        return bos.toByteArray();
    }
}
