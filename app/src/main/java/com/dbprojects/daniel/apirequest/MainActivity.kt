package com.dbprojects.daniel.apirequest

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Callback
import com.dbprojects.daniel.apirequest.FetchCompleteListener
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException



class MainActivity : AppCompatActivity(), FetchCompleteListener{

    override fun fetchComplete(){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var client = OkHttpClient()
        var request = OkHttpRequest(client)


        RequestButton.setOnClickListener {
            val stock = StockRequestText.text
            val url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=$stock&apikey=YLEIP3QYGME0JOD"
            ResponseText.text ="Requesting Price....."
            simpleProgressBar.setProgress(0)
            SystemClock.sleep(1000)
            simpleProgressBar.setProgress(25)
            request.GET(url, object: Callback {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call?, response: Response) {
                    val responseData = response.body()?.string()
                    simpleProgressBar.setProgress(50)
                    runOnUiThread{
                        try {
                            var json = JSONObject(responseData)
                            println("Request Successful!!")
                            println(json)
                            val responseObject = json.getJSONObject("Global Quote")
                            val symbol = responseObject.getString("01. symbol")
                            val price = responseObject.getString("05. price")
                            this@MainActivity.fetchComplete()
                            println("The $symbol stock price is now $price")
                            SystemClock.sleep(1000)
                            ResponseText.text = "The $symbol stock price is now $price"
                            simpleProgressBar.setProgress(100)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    println("Request Failure.")
                }
            })

            println("test")
        }
    }










}
