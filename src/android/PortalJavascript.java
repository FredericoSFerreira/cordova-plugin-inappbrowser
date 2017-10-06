package br.com.softbox.inappbrowser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

/*
Essa classe magica é a interface entre o javascript rodando no webview e o android nativo
Vc pode chamar pelo objeto "portalmobile.mostraVideo" por exemplo lá do javascript
 */
public class PortalJavascript {

    private Context context;

    public PortalJavascript(Context context){
        this.context = context;
    }

    // pra chamar no javascript
    @JavascriptInterface
    public void mostraVideo(final String url, String formato){ // formato de mp4 eh "video/mp4"
        // vendo o video com player nativo
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setDataAndType(Uri.parse(url), formato);
        context.startActivity(intent);
    }
}
