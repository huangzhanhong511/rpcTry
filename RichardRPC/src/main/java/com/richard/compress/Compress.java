package com.richard.compress;

public interface Compress {
    /**
     *
     * @param data 这里是传入你要压缩的字节数组
     * @return 返回压缩之后的字节数组
     */
    public byte[] compress(byte[] data);
    public byte[] decompress(byte[] data);
}
