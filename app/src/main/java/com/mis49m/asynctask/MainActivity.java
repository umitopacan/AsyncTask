package com.mis49m.asynctask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button btn;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.tv_result);

        btn=(Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] urls = new String[2];
                urls[0] = "url1";
                urls[1] = "url2";

                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute(urls);
            }
        });
    }

    public class BackgroundTask extends AsyncTask<String, Integer, String >{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cancel(true);
                    progressDialog.dismiss();
                }
            });

            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);
        }

        @Override
        protected String doInBackground(String... params) {
            int size = params.length;
            progressDialog.setMax(size);

            for(int i=0; i<size; i++){
                try {
                    if(isCancelled())
                        return "Canceled!";

                    publishProgress(i);

                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "Download completed!";
        }

        @Override
        protected void onPostExecute(String s) {
            tvResult.setText(s);
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
            progressDialog.dismiss();
            tvResult.setText(result);
        }
    }
}
