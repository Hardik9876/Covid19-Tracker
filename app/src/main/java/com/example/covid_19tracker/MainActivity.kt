package com.example.covid_19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var stateAdapter: StateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))

        fetchResults()

        button.setOnClickListener{
            fetchResults()

        }
    }

    private  fun fetchResults() {
        GlobalScope.launch {
            val response= withContext(Dispatchers.IO) { Client.api.execute()}

            if(response.isSuccessful)
          {
             val data= Gson().fromJson(response.body?.string(),com.example.covid_19tracker.response::class.java)
        
              launch(Dispatchers.Main) { 
                  bindData(data.statewise[0])
                  bindStateData(data.statewise.subList(0,data.statewise.size))
              }
          }
        }

    }

    private fun bindStateData(subList: List<StatewiseItem>) {
   stateAdapter=StateAdapter(subList)
        list.adapter=stateAdapter
    }

    private fun bindData(item: StatewiseItem) {
    val lastTime=item.lastupdatedtime
        val dateFormat=SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdateTv.text="Last Updated \n ${getTime(dateFormat.parse(lastTime))}"
       confirmedTv.text=item.confirmed
        recoverdTv.text=item.recovered
        deathsTv.text=item.deaths
        activeTv.text=item.active
    }


    private fun getTime(past:Date):String
    {
        val now = Date()
        val sec=TimeUnit.MILLISECONDS.toSeconds(now.time-past.time)
       val min=TimeUnit.MILLISECONDS.toMinutes(now.time-past.time)
       val hour= TimeUnit.MILLISECONDS.toHours(now.time-past.time)
        return when
        {
            sec<60 -> {
                "Few Sec Ago"
            }
            min<60 ->{
                "$min Min Ago"
            }
            hour<24 ->
            {
                "$hour Hr ${min%60} Min Ago"
            }

            else -> { "NA "}
        }
    }

}