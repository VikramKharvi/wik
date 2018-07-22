package com.vikram984511gmail.hawk_browser;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    ProgressBar superProgressBar;
    LinearLayout superLinearLayout;
    WebView superWebView;
    SwipeRefreshLayout superSwipeLayout;
    String myCurrentUrl;
    Toolbar toolbar;
    ImageView superImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText Edittext = findViewById(R.id.edittext);
        registerForContextMenu(Edittext);
        superImageView = findViewById(R.id.myImageView);
        superLinearLayout = findViewById(R.id.myLinearLayout);
        superProgressBar = findViewById(R.id.myProgressBar);
        superSwipeLayout = findViewById(R.id.mySwipelayout);
        superWebView = findViewById(R.id.myWebView);

        superProgressBar.setMax(100);


        superWebView.loadUrl("https://www.google.com");
        superWebView.getSettings().setJavaScriptEnabled(true);
        superWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                superLinearLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                superLinearLayout.setVisibility(View.GONE);
                superSwipeLayout.setRefreshing(false);
                super.onPageFinished(view, url);
                myCurrentUrl = url;
            }
            });
            superWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                superImageView.setImageBitmap(icon);

            }
        });
        superSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                superWebView.reload();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenuInflater = getMenuInflater();
        myMenuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_forward:
                GoForward();
                break;

            case R.id.menu_refresh:
                superWebView.reload();
                break;

            case R.id.menu_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,myCurrentUrl);
                startActivity(Intent.createChooser(shareIntent,myCurrentUrl));
        }
        return super.onOptionsItemSelected(item);
    }

    private void GoForward() {
        if (superWebView.canGoForward()) {
            superWebView.goForward();
        } else {
            Toast.makeText(this, "Can't go further!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (superWebView.canGoBack()) {
            superWebView.goBack();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Exit App");
            dialog.setMessage("Browser has nothing to go back, so what next?");
            dialog.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.setNegativeButton("STAY HERE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
    }

}