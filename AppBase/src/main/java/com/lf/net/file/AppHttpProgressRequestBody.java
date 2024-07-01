package com.lf.net.file;

import android.os.Handler;
import android.os.Looper;

import com.lf.net.listener.AppHttpBaseNetListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * File descripition: 文件上传
 *
 * @author 宋宁
 * @date 2020/8/20
 */
public class AppHttpProgressRequestBody extends RequestBody {
    private File mFile;
    private String mPath;
    private String mMediaType;
    private AppHttpBaseNetListener mListener;

    private int mEachBufferSize = 1024;

    public AppHttpProgressRequestBody(final File file, String mediaType, AppHttpBaseNetListener ZYBHttpBaseNetListener) {
        mFile = file;
        mMediaType = mediaType;
        mListener = ZYBHttpBaseNetListener;
    }

    public AppHttpProgressRequestBody(final File file, String mediaType, int eachBufferSize, AppHttpBaseNetListener ZYBHttpBaseNetListener) {
        mFile = file;
        mMediaType = mediaType;
        mEachBufferSize = eachBufferSize;
        mListener = ZYBHttpBaseNetListener;
    }

    @Override
    public MediaType contentType() {
        // i want to upload only images
        return MediaType.parse(mMediaType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[mEachBufferSize];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {
                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));
                uploaded += read;
                sink.write(buffer, 0, read);

            }
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            mListener.onProgress((int) (100 * mUploaded / mTotal));
        }
    }
}
