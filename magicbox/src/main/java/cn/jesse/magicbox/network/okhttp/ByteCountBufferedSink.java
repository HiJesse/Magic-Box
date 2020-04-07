package cn.jesse.magicbox.network.okhttp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

/**
 * 按 byteCount 批次写入
 *
 * @author jesse
 */
public class ByteCountBufferedSink implements BufferedSink {
    private final long mByteCount;
    private final Sink originalSink;
    private final BufferedSink delegate;

    public ByteCountBufferedSink(Sink sink, long byteCount) {
        this.originalSink = sink;
        this.delegate = Okio.buffer(originalSink);
        this.mByteCount = byteCount;
    }

    @Override
    public long writeAll(Source source) throws IOException {
        if (source == null) throw new IllegalArgumentException("source == null");
        long totalBytesRead = 0;
        for (long readCount; (readCount = source.read(buffer(), mByteCount)) != -1; ) {
            totalBytesRead += readCount;
            emitCompleteSegments();
        }
        return totalBytesRead;
    }

    @Override
    public BufferedSink write(byte[] source, int offset, int byteCount) throws IOException {
        if (!isOpen()) throw new IllegalStateException("closed");
        //计算出要写入的次数
        long count = (long) Math.ceil((double) source.length / mByteCount);
        for (int i = 0; i < count; i++) {
            //让每次写入的字节数精确到mByteCount 分多次写入
            long newOffset = i * mByteCount;
            long writeByteCount = Math.min(mByteCount, source.length - newOffset);
            buffer().write(source, (int) newOffset, (int) writeByteCount);
            emitCompleteSegments();
        }
        return this;
    }

    @Override
    public BufferedSink emitCompleteSegments() throws IOException {
        final Buffer buffer = buffer();
        originalSink.write(buffer, buffer.size());
        return this;
    }

    @Override
    public Buffer buffer() {
        return delegate.buffer();
    }

    @Override
    public BufferedSink write(ByteString byteString) throws IOException {
        return delegate.write(byteString);
    }

    @Override
    public BufferedSink write(byte[] source) throws IOException {
        return delegate.write(source);
    }

    @Override
    public BufferedSink write(Source source, long byteCount) throws IOException {
        return delegate.write(source, byteCount);
    }

    @Override
    public BufferedSink writeUtf8(String string) throws IOException {
        return delegate.writeUtf8(string);
    }

    @Override
    public BufferedSink writeUtf8(String string, int beginIndex, int endIndex) throws IOException {
        return delegate.writeUtf8(string, beginIndex, endIndex);
    }

    @Override
    public BufferedSink writeUtf8CodePoint(int codePoint) throws IOException {
        return delegate.writeUtf8CodePoint(codePoint);
    }

    @Override
    public BufferedSink writeString(String string, Charset charset) throws IOException {
        return delegate.writeString(string, charset);
    }

    @Override
    public BufferedSink writeString(String string, int beginIndex, int endIndex, Charset charset) throws IOException {
        return delegate.writeString(string, beginIndex, endIndex, charset);
    }

    @Override
    public BufferedSink writeByte(int b) throws IOException {
        return delegate.writeByte(b);
    }

    @Override
    public BufferedSink writeShort(int s) throws IOException {
        return delegate.writeShort(s);
    }

    @Override
    public BufferedSink writeShortLe(int s) throws IOException {
        return delegate.writeShortLe(s);
    }

    @Override
    public BufferedSink writeInt(int i) throws IOException {
        return delegate.writeInt(i);
    }

    @Override
    public BufferedSink writeIntLe(int i) throws IOException {
        return delegate.writeIntLe(i);
    }

    @Override
    public BufferedSink writeLong(long v) throws IOException {
        return delegate.writeLong(v);
    }

    @Override
    public BufferedSink writeLongLe(long v) throws IOException {
        return delegate.writeLongLe(v);
    }

    @Override
    public BufferedSink writeDecimalLong(long v) throws IOException {
        return delegate.writeDecimalLong(v);
    }

    @Override
    public BufferedSink writeHexadecimalUnsignedLong(long v) throws IOException {
        return delegate.writeHexadecimalUnsignedLong(v);
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public BufferedSink emit() throws IOException {
        return delegate.emit();
    }

    @Override
    public OutputStream outputStream() {
        return delegate.outputStream();
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return delegate.write(src);
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        delegate.write(source, byteCount);
    }

    @Override
    public Timeout timeout() {
        return delegate.timeout();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}