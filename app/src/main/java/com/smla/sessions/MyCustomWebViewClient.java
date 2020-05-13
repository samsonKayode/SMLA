package com.smla.sessions;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by SAMSON KAYODE on 12-Sep-2017.
 */

public class MyCustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
