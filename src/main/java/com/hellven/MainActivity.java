package com.hellven;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.rgb;


public class MainActivity extends AppCompatActivity {

    int fish=0;

    private int versioncode = 1; //어플내 버전

    private ProgressDialog dialog;
    private ProgressBar mPBar;

    String xml;
    WebView mWebView;

    private static final int INPUT_FILE_REQUEST_CODE = 1;

    private static final String TAG = MainActivity.class.getSimpleName();
    private ValueCallback<Uri> mUploadMessage;
    private static final String TYPE_IMAGE = "image/*";

    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("save", 0);
        fish = pref.getInt("fish", 0);

        if(fish==0){
            AlertDialog.Builder builder = new
                    AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("공지사항");
            builder.setMessage("이 어플은 공식어플이 아닙니다.\n" +
                    "본 앱을 사용한 피해는 모두 본인 책임입니다.\n" +
                    "자동업데이트 기능은 보안상의 이유로 추가하지 않았습니다.\n" +
                    "공지는 앱을 받고 처음 실행시 한번만 뜹니다.");
            builder.setPositiveButton("확인", new
                    DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            fish=1;
                            SharedPreferences pref=getSharedPreferences("save", 0);
                            SharedPreferences.Editor edit=pref.edit();

                            edit.putInt("fish", fish);
                            edit.commit();
                        }});
            builder.show();}

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.reload);

        mSwipeRefreshLayout.setColorSchemeColors(rgb(255, 102, 0));
// 당기면 새로고침 리스너를 호출
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                mWebView.reload();
                mSwipeRefreshLayout.setRefreshing(false);
            }});



        //폴더생성성
        String str = Environment.getExternalStorageState();
        if (str.equals(Environment.MEDIA_MOUNTED))

        {

            String dirPath = "/sdcard/Hellven";
            File file = new File(dirPath);
            if (!file.exists())  // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
        } else
            Toast.makeText(MainActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();

        mWebView = (WebView) findViewById(R.id.wb);

        mPBar = (ProgressBar) findViewById(R.id.p);

        // Enable pinch to zoom without the zoom buttons
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            mWebView.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            mWebView.getSettings().setTextZoom(100);

        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setUseWideViewPort(true);


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);




        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("https://hellven.net");
        mWebView.setWebChromeClient(new FullscreenableChromeClient(this));
        mWebView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onCloseWindow(WebView w) {
                super.onCloseWindow(w);
                finish();
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                // return true or false after performing the URL request
                WebView newWebView = new WebView(MainActivity.this);
                view.addView(newWebView);
// Url 문자열을 가져옴.
                WebView.HitTestResult result = view.getHitTestResult();
                String url = result.getExtra();

                if (url != null && url.indexOf("_blank") > -1) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    return true;
                }


                final WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setAllowContentAccess(true);
                view.setWebChromeClient(this);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                });
                return super.onCreateWindow(view, dialog, userGesture, resultMsg);

            }


            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                // TODO Auto-generated method stub
                //return super.onJsAlert(view, url, message, result);
                new AlertDialog.Builder(view.getContext())
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message,
                                       final JsResult result) {
                // TODO Auto-generated method stub
                //return super.onJsConfirm(view, url, message, result);
                new AlertDialog.Builder(view.getContext())
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton("네",
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton("아니오",
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }


            public void onProgressChanged(WebView view, int progress) {
                mPBar.setProgress(progress);
                if (progress == 100) {
                    mPBar.setVisibility(View.GONE);

                } else {
                    mPBar.setVisibility(View.VISIBLE);

                }
            }

            // For Android Version < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                //System.out.println("WebViewActivity OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU), n=1");
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(TYPE_IMAGE);
                startActivityForResult(intent, INPUT_FILE_REQUEST_CODE);
            }

            // For 3.0 <= Android Version < 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                //System.out.println("WebViewActivity 3<A<4.1, OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU,aT), n=2");
                openFileChooser(uploadMsg, acceptType, "");
            }

            // For 4.1 <= Android Version < 5.0
            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                Log.d(getClass().getName(), "openFileChooser : " + acceptType + "/" + capture);
                mUploadMessage = uploadFile;
                imageChooser();
            }

            // For Android Version 5.0+
            // Ref: https://github.com/GoogleChrome/chromium-webview-samples/blob/master/input-file-example/app/src/main/java/inputfilesample/android/chrome/google/com/inputfilesample/MainFragment.java
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                System.out.println("WebViewActivity A>5, OS Version : " + Build.VERSION.SDK_INT + "\t onSFC(WV,VCUB,FCP), n=3");
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;
                imageChooser();
                return true;
            }

            private void imageChooser() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(getClass().getName(), "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType(TYPE_IMAGE);

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
            }

        });

        // 롱클릭 활성화
        mWebView.setLongClickable(true);

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                WebView webView = (WebView) v;
                WebView.HitTestResult hr = webView.getHitTestResult();
                int type = hr.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            MainActivity.this);

                    alertDialog.setTitle("저장");

                    alertDialog.setMessage("이미지를 저장하시겠습니까?");

                    alertDialog.setPositiveButton("저장",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    WebView webView = (WebView) v;
                                    WebView.HitTestResult hr = webView.getHitTestResult();

                                    int type = hr.getType();
                                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                                        String imageUrl = hr.getExtra();

                                        if (!imageUrl.startsWith("http")) {

                                        }


                                        File file = new File(Environment.getExternalStorageDirectory() + "/Hellven/img", getFilenameFromURL(imageUrl));

                                        DownloadManager downloadManager = (DownloadManager) MainActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
                                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                                        request.setDestinationUri(Uri.fromFile(file));
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        downloadManager.enqueue(request);

                                        Toast.makeText(MainActivity.this, "이미지를 다운로드 중입니다.", Toast.LENGTH_LONG).show();

                                    }


                                }

                                protected String getFilenameFromURL(URL url) {
                                    return getFilenameFromURL(url.getFile());
                                }

                                protected String getFilenameFromURL(String url) {
                                    String[] p = url.split("/");
                                    String s = p[p.length - 1];
                                    if (s.indexOf("?") > -1) {
                                        return s.substring(0, s.indexOf("?"));
                                    }
                                    return s;
                                }

                            });


                    // Setting Negative "NO" Button

                    alertDialog.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    dialog.cancel();
                                }
                            });
                    // Showing Alert Message
                    alertDialog.show();

                    return true;

                } else {

                }
                return false;
            }
        });





        mWebView.setDownloadListener(new DownloadListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDownloadStart(final String url, String userAgent, final String contentDisposition, String mimetype, long contentLength) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        MainActivity.this);

                alertDialog.setTitle("다운");

                alertDialog.setMessage("파일을 다운로드 하시겠습니까?");

                alertDialog.setPositiveButton("다운",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                MimeTypeMap mtm = MimeTypeMap.getSingleton();
                                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri downloadUri = Uri.parse(url);
                                // 파일 이름을 추출한다. contentDisposition에 filename이 있으면 그걸 쓰고 없으면 URL의 마지막 파일명을 사용한다.
                                String fileName = downloadUri.getLastPathSegment();
                                int pos = 0;
                                if ((pos = contentDisposition.toLowerCase().lastIndexOf("filename=")) >= 0) {
                                    fileName = contentDisposition.substring(pos + 9);
                                    pos = fileName.lastIndexOf(";");
                                    if (pos > 0) {
                                        fileName = fileName.substring(0, pos - 1);
                                    }
                                }
                                // MIME Type을 확장자를 통해 예측한다.
                                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
                                String mimeType = mtm.getMimeTypeFromExtension(fileExtension);
                                String cookie = CookieManager.getInstance().getCookie(url);
                                // Download 디렉토리에 저장하도록 요청을 작성
                                fileName=fileName.replaceAll("\"", "");
                                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                                request.setTitle(fileName);
                                request.setDescription(url);
                                request.setMimeType(mimeType);
                                request.setDestinationInExternalPublicDir("/Download", fileName);
                                Environment.getExternalStoragePublicDirectory("/Download").mkdirs();
                                request.addRequestHeader("Cookie", cookie);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                // 다운로드 매니저에 요청 등록
                                downloadManager.enqueue(request);

                            }
                        });
                alertDialog.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                dialog.cancel();
                            }
                        });
                // Showing Alert Message
                alertDialog.show();



            }

        });





    }

    private BroadcastReceiver completeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Resources res = context.getResources();
// 다운로드 완료 토스트 출력
            Toast.makeText(context, res.getString(R.string.download_complete), Toast.LENGTH_SHORT).show();
// 다운로드 완료 창으로 이동
            //startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
        }
    };


    protected void onPause() {
        super.onPause();
// 앱이 중단 되면 리시버 등록 해제
        unregisterReceiver(completeReceiver);
    }

    protected void onResume() {
// 앱이 실행되면 리시버 등록
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(completeReceiver, completeFilter);
        super.onResume();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        backButtonHandler();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);


        // Setting Dialog Title
        // Setting Dialog Message

        alertDialog.setTitle("종료");

        // I've included a simple dialog icon in my project named "dialog_icon", which's image file is copied and pasted in all "drawable" folders of "res" folders of the project. You can include any dialog image of your wish and rename it to dialog_icon.

        alertDialog.setMessage("종료하시겠습니까?");

        // Setting Icon to Dialog
        // Setting Positive "Yes" Button

        alertDialog.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        // Setting Negative "NO" Button

        alertDialog.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }


    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mFilePathCallback == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri[] results = new Uri[]{getResultUri(data)};

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            } else {
                if (mUploadMessage == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri result = getResultUri(data);

                Log.d(getClass().getName(), "openFileChooser : "+result);
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else {
            if (mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
            if (mUploadMessage != null) mUploadMessage.onReceiveValue(null);
            mFilePathCallback = null;
            mUploadMessage = null;
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Uri getResultUri(Intent data) {
        Uri result = null;
        if(data == null || TextUtils.isEmpty(data.getDataString())) {
            // If there is not data, then we may have taken a photo
            if(mCameraPhotoPath != null) {
                result = Uri.parse(mCameraPhotoPath);
            }
        } else {
            String filePath = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                filePath = data.getDataString();
            } else {
                filePath = "file:" + RealPathUtil.getRealPath(this, data.getData());
            }
            result = Uri.parse(filePath);
        }

        return result;
    }


}


