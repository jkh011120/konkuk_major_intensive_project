package com.example.konkuk_major_intensive_project.Util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

suspend fun RequestRoute(
    startLng: Double,
    startLat: Double,
    destinationLat: Double,
    destinationLng: Double,
    option: String = "trafast",
    context: Context,
): List<LatLng> = withContext(Dispatchers.IO) {   // ★ 여기서 IO 스레드로 전환
    try {
        val start = "$startLng,$startLat"        // "lng,lat"
        val goal = "$destinationLng,$destinationLat"

        val baseUrl = "https://maps.apigw.ntruss.com/map-direction/v1/driving"
        val urlStr = baseUrl +
                "?start=" + URLEncoder.encode(start, "UTF-8") +
                "&goal=" + URLEncoder.encode(goal, "UTF-8") +
                "&option=" + URLEncoder.encode(option, "UTF-8")

        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = "GET"
        conn.connectTimeout = 10_000
        conn.readTimeout = 10_000

        // Manifest meta-data 에서 키 가져오기
        val pm = context.packageManager
        val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val meta = appInfo.metaData

        val clientId = meta.getString("com.naver.maps.map.NCP_KEY_ID") ?: ""
        val clientSecret = meta.getString("com.naver.maps.map.NCP_KEY_SECRET") ?: ""  // Manifest 에 이 키도 꼭 추가
        Log.i("clientId",clientId)
        Log.i("clientsecret",clientSecret)

        conn.setRequestProperty("x-ncp-apigw-api-key-id", clientId)
        conn.setRequestProperty("x-ncp-apigw-api-key", clientSecret)

        val responseCode = conn.responseCode
        val stream = if (responseCode == HttpURLConnection.HTTP_OK) {
            conn.inputStream
        } else {
            conn.errorStream
        }

        val responseText = stream.bufferedReader().use { it.readText() }
        Log.i("req_route", responseText)

        conn.disconnect()

        // JSON 파싱해서 path -> List<LatLng>
        ExtractPathList(responseText, option)
    } catch (e: Exception) {
        Log.e("req_route", "RequestRoute 실패", e)
        emptyList()   // 에러 땐 빈 리스트 리턴 (PathOverlay에서 size 체크해주면 크래시 안 남)
    }
}

// JSON 문자열에서 path만 뽑아서 LatLng 리스트로 변환
fun ExtractPathList(
    responseText: String,
    option: String = "trafast"   // request 할 때 쓴 option 이랑 동일하게
): List<LatLng> {
    val result = mutableListOf<LatLng>()

    val root = JSONObject(responseText)

    // code != 0 이면 실패
    if (root.optInt("code", -1) != 0) {
        return emptyList()
    }

    val routeObj = root.getJSONObject("route")

    // route 안의 option 이름(trafast, traoptimal 등)으로 접근
    val optionArray = routeObj.getJSONArray(option)

    // 보통 첫 번째 경로만 씀
    val firstRoute = optionArray.getJSONObject(0)

    val pathArray = firstRoute.getJSONArray("path")

    // path: [[lng, lat], [lng, lat], ...]
    for (i in 0 until pathArray.length()) {
        val point = pathArray.getJSONArray(i)
        val lng = point.getDouble(0)
        val lat = point.getDouble(1)

        // LatLng 은 (lat, lng) 순서니까 뒤집어서 넣기
        result.add(LatLng(lat, lng))
    }

    return result
}
