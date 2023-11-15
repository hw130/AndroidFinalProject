package com.example.auctioninginfoapp.network

import com.example.auctioninginfoapp.BuildConfig
import com.example.auctioninginfoapp.model.Fruits
import com.google.api.Http
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/** object 키워드로 NetworkModule 객체를 생성
 - 공공DB 서버에 요청할 OkHttp Request 객체 생성
 */
object NetworkModule {
 /** OKhttp client(실제로 네트워크를 호출하는 부분) 생성
  * - 네트워크 타임아웃 시간 설정
  */
  val client: OkHttpClient by lazy {
   OkHttpClient.Builder()
    .connectTimeout( // Connection Timeout
     2,
     TimeUnit.MINUTES
    )
    .readTimeout(2, TimeUnit.MINUTES) //Read Timeout
    .build()
 }
 /** HttpUrl 객체 정의 함수 선언
  - HttpUrl.Builder()를 이용하여 HttpUrl 객체(URI 구조) 정의
  - OpenAPI 명세에 맞춰 선언
  */
 fun makeHttpUrl(scode: String, date: String, amout: String) : HttpUrl{
  //http://211.237.50.150:7080/openapi/
  return HttpUrl.Builder()
   .scheme("http")
   .host("211.237.50.150")
   .port(7080)
   .addPathSegment("openapi")
   .addPathSegment(BuildConfig.API_KEY) //API_KEY
   .addPathSegment("json") //요청파일 타입(json 또는 xml)
   .addPathSegment("Grid_20160624000000000348_1") //요청하는 API 종류(API URL)
   .addPathSegment("1") //페이지 번호(START_INDEX)
   .addPathSegment(amout) //한 페이지 결과 수(END-INDEX)
   .addQueryParameter("AUCNG_DE", date.replace("-", "")) //경락일(-를 삭제)
   .addQueryParameter("SPCIES_CD", scode) //품종코드(060103)
   .build()
 }
 /** OkHttp Request 객체 생성 함수
  - Request.Builder()를 사용하여 요청을 위한 Request 객체 생성
  - url: 요청서버 url
  - get/post: 전송방식
  */

 fun makeHttprequest(httpUrl: HttpUrl) : Request{
  return Request.Builder().url(httpUrl).get().build()
 }
}
