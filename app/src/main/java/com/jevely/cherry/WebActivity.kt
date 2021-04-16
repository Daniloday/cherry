package com.jevely.cherry

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WebActivity : AppCompatActivity() {
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mCapturedImageURI: Uri? = null
    private var mCameraPhotoPath: String? = null
    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILECHOOSER_RESULTCODE = 1
    private var URL = ""

    lateinit var webView : WebView
    var webViewBundle : Bundle? = null
    lateinit var history : History
    // lateinit var sPref : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("start","webactivity")
        supportActionBar?.hide()
        history = History(this)
        //sPref = getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)
        webView = findViewById(R.id.webView)
        turnWebViewSettings()
        URL = intent.getStringExtra("applink").toString()
//        webView.settings.javaScriptEnabled = true
//        webView.isFocusable = true
//        webView.isFocusableInTouchMode = true
//        webView.settings.javaScriptEnabled = true
//        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
//        webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
//        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
//        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//
//        webView.settings.domStorageEnabled = true
//        webView.settings.setAppCacheEnabled(true)
//        webView.settings.databaseEnabled = true
//        webView.settings.setSupportMultipleWindows(false)


        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.e("Web", "url loading")
                val url = request?.url.toString()
                webView.loadUrl(url)
                history.goForward(url)
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        setChromeClient()
        if (history.history.isEmpty()){
            //FIRST LINK
            webView.loadUrl(URL)
        }
        else webView.loadUrl(history.history.last())

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //return super.onKeyDown(keyCode, event)
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Log.e("Web", "Back")
            history.goBack()?.let { webView.loadUrl(it) }
            return true
        } else return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
        Log.e("Web", "Pause")
        history.saveHistory()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    fun turnWebViewSettings() {
        webView.getSettings().setJavaScriptEnabled(true)
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        webView.getSettings().setGeolocationEnabled(true)
        webView.getSettings().setUseWideViewPort(true)
        webView.getSettings().setLoadWithOverviewMode(true)
        webView.getSettings().setAllowContentAccess(true)
        webView.getSettings().setDatabaseEnabled(true)
        webView.getSettings().setLoadsImagesAutomatically(true)
        webView.setFocusable(true)
        webView.setFocusableInTouchMode(true)
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
        webView.getSettings().setDomStorageEnabled(true)
        webView.getSettings().setAppCacheEnabled(true)
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        webView.getSettings().setSupportZoom(true)
        webView.getSettings().setUserAgentString(System.getProperty("http.agent"))
        webView.getSettings().setMixedContentMode(0)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null
            // Check that the response is a good one
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage) {
                    return
                }
                var result: Uri? = null
                try {
                    result = if (resultCode != RESULT_OK) {
                        null
                    } else {
                        // retrieve from the private variable if the intent is null
                        if (data == null) mCapturedImageURI else data.data
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "activity :$e",
                        Toast.LENGTH_LONG).show()
                }
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
        return
    }

    fun setChromeClient(){
        webView.webChromeClient = object : WebChromeClient() {
            @Throws(IOException::class)
            private fun createImageFile(): File? {
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "JPEG_" + timeStamp + "_"
                val storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES)
                return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir /* directory */
                )
            }

            override fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
                // Double check that we don't have any existing callbacks
                mFilePathCallback?.onReceiveValue(null)
                mFilePathCallback = filePath
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent!!.resolveActivity(packageManager) != null) {
                    // Create the File where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Log.e("Web", "Unable to create Image File", ex)
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.absolutePath
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile))
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "image/*"
                val intentArray: Array<Intent?>
                intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                return true
            }

            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?) {
                mUploadMessage = uploadMsg
                // Create AndroidExampleFolder at sdcard
                val imageStorageDir = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "AndroidExampleFolder")
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs()
                }
                // Create camera captured image file path and name
                val file = File(
                    imageStorageDir.toString() + File.separator + "IMG_"
                            + System.currentTimeMillis().toString() + ".jpg")
                mCapturedImageURI = Uri.fromFile(file)
                // Camera capture image intent
                val captureIntent = Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE)
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                // Create file chooser intent
                val chooserIntent = Intent.createChooser(i, "Image Chooser")
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent))
                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
            }

            fun openFileChooser(uploadMsg: ValueCallback<Uri>,
                                acceptType: String?,
                                capture: String?) {
                openFileChooser(uploadMsg, acceptType)
            }
        }
    }
}