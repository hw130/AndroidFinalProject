package com.example.auctioninginfoapp.result

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auctioninginfoapp.database.DatabaseModule
import com.example.auctioninginfoapp.model.FreshData
import com.example.auctioninginfoapp.model.FreshWrapper
import com.example.auctioninginfoapp.model.Fruits
import com.example.auctioninginfoapp.model.SaveItem
import com.example.auctioninginfoapp.network.NetworkModule
import com.example.auctioninginfoapp.util.SingleLiveEvent
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gildor.coroutines.okhttp.await

class ResultViewModel() : ViewModel() {

    val moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    val notificationMsg: SingleLiveEvent<String> = SingleLiveEvent()

    private val resultList: SingleLiveEvent<List<FreshData>> = SingleLiveEvent()

    fun getResultList(): LiveData<List<FreshData>> = resultList

    fun saveResult(context: Context, saveName: String){
        viewModelScope.launch(Dispatchers.IO){
            DatabaseModule.getDatabase(context).freshDao().insertSave(
                SaveItem(id = null, saveTitle = saveName)
            ).run {
                resultList.value?.let { datas ->
                    datas.forEach { it.saveId = this }

                    Log.i("SAVERESULT", "datas: $datas")
                    DatabaseModule.getDatabase(context).freshDao().insertFresh(datas)
                }
            }
        }
    }

    private val errorHandler = CoroutineExceptionHandler{ _, exception ->
        exception.message?.let { Log.e("error", it) }
        notificationMsg.postValue(exception.message)
    }

    fun loadDataFromURL(
        selectDate: String,
        selectFruit: String,
        resultAmount: String
    ){
        val httpUrl = NetworkModule.makeHttprequest(
            NetworkModule.makeHttpUrl(
                scode = Fruits.valueOf(selectFruit).scode,
                date = selectDate,
                amout = resultAmount
            )
        )
        Log.i("HTTP", httpUrl.toString())

        viewModelScope.launch(Dispatchers.IO + errorHandler){
            val response = NetworkModule.client.newCall(httpUrl).await()
            Log.d("RESPONSE", "$response")

            /** 응답 결과(response, Json 형식)를 List<FreshData> 형식의 객체로 변환 */
            val freshData = response.body?.string()?.let{ mappingStringToFresh(it)} ?: emptyList()


            if(freshData.isEmpty()){
                notificationMsg.postValue("데이터가 존재하지 않습니다.")
            }
        }
    }

    private fun mappingStringToFresh(jsonBody: String): List<FreshData>{
        val freshStringToJsonAdapter = moshi.adapter(FreshWrapper::class.java)

        val freshResponse = freshStringToJsonAdapter.fromJson(jsonBody)
        Log.i("LIST", "$freshResponse")

        if(freshResponse?.errorCode != null){
            throw Error(freshResponse.errorCode!!.message)
        }

        return freshResponse?.list?.row?: emptyList()
    }

}