package com.project.appchat.AsyncTask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsyncTask extends AsyncTask<Void, Integer, Integer> {
    WeakReference<ProgressBar> wProgressBar;

    public SimpleAsyncTask(ProgressBar pro){
        this.wProgressBar = new WeakReference<>(pro);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        // Tao so nguyen ngau nhien tu 100 -> 5000
        int n = (new Random().nextInt(400) + 10) * 100;
//        int n = 100000;
        this.wProgressBar.get().setMax(n);
        // Cho ung dung ngu

        for(int i = 1; i <= n; i++){
            publishProgress(i);
        }
        // Tra ket qua ve
        return n;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        this.wProgressBar.get().setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.wProgressBar.get().setProgress(0);
    }
}
