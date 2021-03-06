/*
Copyright © 2019 Salt Edge. https://saltedge.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.saltedge.sdk.webview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.saltedge.sdk.SaltEdgeSDK;
import com.saltedge.sdk.model.Saltbridge;
import com.saltedge.sdk.model.response.SaltbridgeResponse;
import com.saltedge.sdk.network.SEApiConstants;
import com.saltedge.sdk.network.SERestClient;
import com.saltedge.sdk.utils.SEConstants;
import com.saltedge.sdk.utils.UITools;

import org.jetbrains.annotations.NotNull;

public class SEWebViewTools {

    public static ValueCallback<Uri[]> uploadMessage;
    private static SEWebViewTools instance;
    private ProgressDialog progressDialog;
    private Activity activity;
    private WebViewRedirectListener webViewListener;
    private String returnUrl;

    public interface WebViewRedirectListener {
        void onConnectSessionSuccess(String connectionId, String connectionSecret, String stage);
        void onConnectSessionError(String stage);
        void onConnectionUpdate();
        void onConnectSessionStageChange(String connectionId, String connectionSecret, String stage, String apiStage);
    }

    public static SEWebViewTools getInstance() {
        if (instance == null) {
            instance = new SEWebViewTools();
        }
        return instance;
    }

    public void initializeWithUrl(Activity activity,
                                  WebView webView,
                                  String url,
                                  String returnUrl,
                                  WebViewRedirectListener listener) {
        this.activity = activity;
        this.webViewListener = listener;
        this.returnUrl = returnUrl;
        progressDialog = UITools.createProgressDialog(activity);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                uploadMessage = filePathCallback;
                startFilePicker();
                return true;
            }

        });
        webView.loadUrl(url);
    }

    public void clear() {
        activity = null;
        webViewListener = null;
    }

    private void startFilePicker() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("file/*");
        if (activity != null) activity.startActivityForResult(chooserIntent, SEConstants.FILECHOOSER_RESULT_CODE);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (SaltEdgeSDK.isLoggingEnabled()) {
                Log.d("SEWebViewTools", "load url: " + url);
            }
            if (view != null && urlIsSaltedgeRedirection(url)) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (activity != null && !activity.isFinishing()) {
                UITools.showProgress(progressDialog);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (activity != null && !activity.isFinishing()) {
                UITools.destroyProgressDialog(progressDialog);
            }
        }
    }

    private boolean urlIsSaltedgeRedirection(String url) {
        if (returnUrl != null && !returnUrl.isEmpty() && url.equals(returnUrl)) {
            if (webViewListener != null) webViewListener.onConnectionUpdate();
        } else if (url != null && url.contains(SEApiConstants.PREFIX_SALTBRIDGE)) {
            onStageChanged(extractSaltbridgeObjectFromUrl(url));
            return false;
        }
        return true;
    }

    private Saltbridge extractSaltbridgeObjectFromUrl(@NotNull String url) {
        SaltbridgeResponse response = null;
        try {
            String jsonData = url.substring(SEApiConstants.PREFIX_SALTBRIDGE.length());
            response = SERestClient.gson.fromJson(jsonData, SaltbridgeResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null ? null : response.getData();
    }

    private void onStageChanged(Saltbridge saltbridge) {
        if (webViewListener == null || saltbridge == null) return;
        String stage = saltbridge.getStage();
        String connectionId = saltbridge.getConnectionId();
        String connectionSecret = saltbridge.getSecret();
        switch (stage) {
            case SEConstants.STATUS_SUCCESS:
                webViewListener.onConnectSessionSuccess(connectionId, connectionSecret, stage);
                break;
            case SEConstants.STATUS_ERROR:
                webViewListener.onConnectSessionError(stage);
                break;
            case SEConstants.STATUS_FETCHING:
                webViewListener.onConnectSessionStageChange(connectionId, connectionSecret, stage, saltbridge.getApiStage());
                break;
        }
    }
}
