package com.example.classroommanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfviewer extends AppCompatActivity
{
    String url;//request.auth != null
    PDFView pdfView;
    TextView textView;
    ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        pdfView = findViewById(R.id.pdfview);
        textView = findViewById(R.id.appbartitle);
        progressBar = findViewById(R.id.progressbar);

        final String name = getIntent().getStringExtra("pdf");
        url = getIntent().getStringExtra("file");
        textView.setText(name);

        Log.d("url",getIntent().getStringExtra("file"));

//        progressBar.setVisibility(View.VISIBLE);
        new RetrivePDFStream(getApplicationContext()).execute(url);
//        progressBar.setVisibility(View.GONE);
    }

    @SuppressLint("StaticFieldLeak")
    class RetrivePDFStream extends AsyncTask<String, Void, InputStream> {

        Context context;
        String res;

        public RetrivePDFStream(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPostExecute(InputStream inputStream)
        {
            super.onPostExecute(inputStream);

            pdfView.fromStream(inputStream)
                    .enableAnnotationRendering(false)
                    .enableAntialiasing(true)
                    .enableDoubletap(true)
                    .swipeHorizontal(true)
                    .scrollHandle(null)
                    .enableSwipe(true)
                    .enableSwipe(true)
                    .password(null)
                    .defaultPage(0)
                    .spacing(0)
                    .load();

            System.out.println("Input Stream"+inputStream);
        }

        @Override
        protected InputStream doInBackground(String... strings)
        {
            InputStream inputStream = null;
            try
            {
                URL uri = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                //if (urlConnection.getResponseCode() == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                //}
            }
            catch (IOException e)
            {
                return null;
            }
            return inputStream;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //startActivity(new Intent(getApplicationContext(), uploadtimetable.class));
        //finish();
    }
}










//        try
//        {
//            openurl = "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(url, "ISO-8859-1");
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//
////        String link = Uri.encode(url);
////        openurl = "http://docs.google.com/viewer?url=" + link + "&embedded=true";
//
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        webView.setWebChromeClient(new WebChromeClient()
//        {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//
//                textView.setText("Loading...");
//                if(newProgress==100)
//                {
//                    progressBar.setVisibility(View.GONE);
//                    textView.setText(name);
//                } //url loading is complete
//            }
//        });
//        webView.loadUrl(url);
//        checkPageFinished();
//    public void checkPageFinished()
//    {
//
//        if (webView.getContentHeight() == 0)
//        {
//            //Run off main thread to control delay
//            webView.postDelayed(new Runnable()
//            {
//                @Override
//                public void run() {
//                    webView.loadUrl(url);
//                }
//                //Set 1s delay to give the view a longer chance to load before
//                // setting the view (or more likely to display blank)
//            }, 1000);
////            //Set the view with the selected pdf
////            setContentView(webView);
//
//            webView.postDelayed(new Runnable()
//            {
//                @Override
//                public void run() {
//                    //If view is still blank:
//                    if (webView.getContentHeight() == 0) {
//                        //Loop until it works
//                        checkPageFinished();
//                    }
//                }
//                //Safely loop this function after 1.5s delay if page is not loaded
//            }, 1500);
//
//        } //If view is blank:
//        else
//        {
//            Toast.makeText(this, "Pdf Loaded", Toast.LENGTH_SHORT).show();
//        }
//    }