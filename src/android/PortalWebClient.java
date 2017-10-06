package br.com.softbox.inappbrowser;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.File;
import portal.unilever.softbox.br.portalexecucaoandroid.PortalActivity;

public class PortalWebClient extends WebChromeClient {

    private PortalActivity mContext;

    public PortalWebClient(PortalActivity act) {
        mContext = act;
    }

    public static int INPUT_FILE_REQUEST_CODE = 123;
    public static int FILECHOOSER_RESULTCODE = 321;

    @Override
    public boolean onShowFileChooser(
            WebView webView, ValueCallback<Uri[]> filePathCallback,
            WebChromeClient.FileChooserParams fileChooserParams) {

        if (mContext.getRespostaArquivo() != null) {
            mContext.getRespostaArquivo().onReceiveValue(null);
        }
        mContext.setRespostaArquivo(filePathCallback);

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");


        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Selecione");

        mContext.startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
        return true;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

        Log.d("LogTag", message);
        result.confirm();
        return true;
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        mContext.setRespostaUpload(uploadMsg);
        Intent i = getChooserIntent(getGalleryIntent("image/*"));
        i.addCategory(Intent.CATEGORY_OPENABLE);
        mContext.startActivityForResult(Intent.createChooser(i, "Selecione"), FILECHOOSER_RESULTCODE);

    }

    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        mContext.setRespostaUpload(uploadMsg);
        Intent i = getChooserIntent( getGalleryIntent("*/*"));
        i.addCategory(Intent.CATEGORY_OPENABLE);
        mContext.startActivityForResult(
                Intent.createChooser(i, "Selecione"),
                FILECHOOSER_RESULTCODE);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mContext.setRespostaUpload(uploadMsg);
        Intent i = getChooserIntent(getGalleryIntent("image/*"));
        mContext.startActivityForResult(Intent.createChooser(i, "Selecione"), FILECHOOSER_RESULTCODE);

    }

    private Intent getGalleryIntent(String type) {
        final Intent galleryIntent = new Intent();
        galleryIntent.setType(type);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        return galleryIntent;
    }

    private Intent getChooserIntent(Intent galleryIntent) {

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Selecione");

        // Add the camera options.
        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents);

        return chooserIntent;
    }
}
