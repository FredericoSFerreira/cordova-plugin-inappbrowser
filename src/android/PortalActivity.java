package br.com.softbox.inappbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

public class PortalActivity extends AppCompatActivity {

    private ValueCallback<Uri> mRespostaUpload;
    private ValueCallback<Uri[]> mRespostaArquivo;

    public ValueCallback getRespostaUpload() {
        return this.mRespostaUpload;
    }

    public void setRespostaUpload(ValueCallback v) {
        this.mRespostaUpload = v;
    }

    public ValueCallback getRespostaArquivo() {
        return this.mRespostaArquivo;
    }

    public void setRespostaArquivo(ValueCallback v) {
        this.mRespostaArquivo = v;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadWebview();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != PortalWebClient.INPUT_FILE_REQUEST_CODE || mRespostaArquivo == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mRespostaArquivo.onReceiveValue(results);
            mRespostaArquivo = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != PortalWebClient.FILECHOOSER_RESULTCODE || mRespostaUpload == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == PortalWebClient.FILECHOOSER_RESULTCODE) {
                if (null == this.mRespostaUpload) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),  ""+e,
                            Toast.LENGTH_LONG).show();
                }
                mRespostaUpload.onReceiveValue(result);
                mRespostaUpload = null;
            }
        }
        return;
    }

    public WebView mView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadWebview() {
        setContentView(R.layout.activity_portal);

//        WebView mWebView = (WebView) findViewById(R.id.webview);
//        WebSettings mWebSettings = mWebView.getSettings();
//        mWebSettings.setJavaScriptEnabled(true);
//        WebViewClient mWebViewClient = new WebViewClient();
//        mWebView.setWebViewClient(mWebViewClient);
//        mWebView.loadUrl("https://www.execucaounilever.com.br/#/login/");
//        mWebView.loadUrl("http://69.164.219.138/#/login/");
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView browser = (WebView) findViewById(R.id.webview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE))
            { WebView.setWebContentsDebuggingEnabled(true); }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        browser.addJavascriptInterface(new PortalJavascript(this), "portalmobile");
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setAllowContentAccess(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setAllowFileAccess(true);
        browser.getSettings().setAppCacheEnabled(true);
        browser.clearCache(false);
        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        browser.setWebChromeClient(new PortalWebClient(this));
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setAllowFileAccessFromFileURLs(true);
        browser.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //browser.loadUrl("https://www.execucaounilever.com.br/#/login/");
//        browser.loadUrl("http://69.164.219.138/#/login/");
//        browser.loadUrl("https://www.execucaounilever.com.br/#/login/");
        browser.loadUrl("https://hmlexecucaounilever.softbox.com.br/#/login/");
//        browser.loadUrl("https://69.164.219.138/#/login/");
        browser.clearView();
        browser.measure(100, 100);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setLoadWithOverviewMode(true);
        mView = browser;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mView.canGoBack()) {
                        mView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // limpando memória
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mView.removeAllViews();
        mView.destroy();
    }



}
