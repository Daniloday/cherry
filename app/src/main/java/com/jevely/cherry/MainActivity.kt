package com.jevely.cherry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.WindowManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.appsflyer.AFLogger
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerLibCore
import java.lang.invoke.MethodHandles.Lookup.PACKAGE
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("config","call")
        configAppsflyer()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun configAppsflyer(){
        Log.e("config", "AF")
        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: MutableMap<String, Any>) {
                val status: String =
                    Objects.requireNonNull(conversionData["af_status"]).toString()
                Log.e("Status", status)
                if (status == "Non-organic") {
                    if (Objects.requireNonNull(conversionData["is_first_launch"]).toString()
                            .equals("true")
                    ) {
                        Log.e(AppsFlyerLibCore.LOG_TAG, "First Launch.")
                        Log.d(AppsFlyerLibCore.LOG_TAG, "Conversion: First Launch")
                        val data = conversionData.map {
                            if(it.value == null) Pair(it.key, "")
                            else Pair(it.key, it.value.toString())
                        }.toMap()
                        Log.e("Data", data.toString())
                        configLink(data)
                    }
                    else{
                        Log.e(AppsFlyerLibCore.LOG_TAG, "Not First Launch.")
                        next(App.BASE_URL)
                    }
                }
                else{
                    Log.e(AppsFlyerLibCore.LOG_TAG, "Conversion: This is an organic install.")
                }
            }

            override fun onConversionDataFail(errorMessage: String) {
                Log.d("LOG_TAG", "error getting conversion data: $errorMessage")
            }

            override fun onAppOpenAttribution(attributionData: MutableMap<String, String>) {
                for (attrName in attributionData.keys) {
                    Log.d(
                        "LOG_TAG",
                        "attribute: " + attrName + " = " + attributionData[attrName]
                    )
                }
            }

            override fun onAttributionFailure(errorMessage: String) {
                Log.d("LOG_TAG", "error onAttributionFailure : $errorMessage")
            }
        }
        AppsFlyerLib.getInstance().setLogLevel(AFLogger.LogLevel.VERBOSE)
        AppsFlyerLib.getInstance().init(App.AF_DEV_KEY, conversionListener, applicationContext)
        AppsFlyerLib.getInstance().start(this)
    }

    fun configLink(data : Map<String, String?>){
        val host = App.BASE_URL //"https://edems.site/6qLFXKCL"
        val c =  if(data.containsKey("campaign")) data["campaign"].toString() else ""
        val endLink = naming(c)
        val afId = AppsFlyerLib.getInstance().getAppsFlyerUID(this@MainActivity)
        val appName = App.PACKAGE
        val cId = if(data.containsKey("campaign_id")) data["campaign_id"].toString() else "null"
        val afAdset = if(data.containsKey("adset")) data["adset"].toString() else "null"
        val afAdsetId = if(data.containsKey("adset_id")) data["adset_id"].toString() else "null"
        val afAd = if(data.containsKey("af_ad")) data["af_ad"].toString() else "null"
        val afAdId = if(data.containsKey("af_ad_id")) data["af_ad_id"].toString() else "null"
        val link = "$host?af_id=$afId&app_name=$appName&с=$c&af_c_id=$cId&af_adset=$afAdset&af_adset_id=$afAdsetId&af_ad=$afAd&af_ad_id=$afAdId&$endLink"
        runOnUiThread {
            next(link)
        }
        Log.e("Link", link)
    }

    fun next(url : String){
//        Log.e("Deep", url)
//        sPref?.edit()?.putString(NAME, url)?.apply()
//        val builder = CustomTabsIntent.Builder()
//        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
//        val customTabsIntent = builder.build()
//        customTabsIntent.launchUrl(this, Uri.parse(url))
//        finish()
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("applink", url)
        startActivity(intent)
    }


    fun naming(c : String) =
        if(c != "") c.split("|^|").joinToString("&") else c
}