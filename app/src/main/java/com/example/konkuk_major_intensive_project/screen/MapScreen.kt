package com.example.konkuk_major_intensive_project.screen

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.konkuk_major_intensive_project.ViewModel.FacilityViewModel
import com.example.konkuk_major_intensive_project.model.FacilityDetail
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.compose.rememberMarkerState

enum class BottomTab(val label: String) {
    NURSING("수유실"),
    SMOKING("흡연실"),
    TOILET("화장실"),
    FAVORITES("즐겨찾기"),
    MYINFO("내 정보")
}

@OptIn(ExperimentalNaverMapApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavController,
    facilityViewModel: FacilityViewModel = viewModel()   // ⭐ Firebase ViewModel 사용
) {
    val context = LocalContext.current
    var path by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    // ▼ 하단 탭 상태
    var selectedTab by remember { mutableStateOf(BottomTab.NURSING) }

    // 권한
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LaunchedEffect(Unit) { permissionsState.launchMultiplePermissionRequest() }
    val granted = permissionsState.permissions.any { it.status.isGranted }

    val cameraPositionState = rememberCameraPositionState()
    val locationSource = rememberFusedLocationSource()

    // ⭐ Firebase에서 받아온 시설 목록
    val facilities by facilityViewModel.facilities.collectAsState()

    // 선택된 시설
    var selectedFacility by remember { mutableStateOf<FacilityDetail?>(null) }

    // 마커 클릭하면 지도 중앙 이동
    LaunchedEffect(selectedFacility) {
        selectedFacility?.let { info ->
            val lat = info.latitude ?: return@LaunchedEffect
            val lng = info.longitude ?: return@LaunchedEffect
            cameraPositionState.animate(CameraUpdate.scrollTo(LatLng(lat, lng)))
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == BottomTab.NURSING,
                    onClick = { selectedTab = BottomTab.NURSING },
                    icon = { Icon(Icons.Default.Face, contentDescription = null) },
                    label = { Text("수유실") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.SMOKING,
                    onClick = { selectedTab = BottomTab.SMOKING },
                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                    label = { Text("흡연실") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.TOILET,
                    onClick = { selectedTab = BottomTab.TOILET },
                    icon = { Icon(Icons.Default.Place, contentDescription = null) },
                    label = { Text("화장실") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.FAVORITES,
                    onClick = { selectedTab = BottomTab.FAVORITES },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text("즐겨찾기") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.MYINFO,
                    onClick = { selectedTab = BottomTab.MYINFO },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("내 정보") }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 🔴 디버그: 현재 불러온 시설 개수 화면에 띄우기
            Text(
                text = "시설 개수: ${facilities.size}",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
            // ▼ 수유실 탭에서만 지도 표시 (필요시 다른 탭 분기도 가능)
            if (selectedTab == BottomTab.NURSING) {
                NaverMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    locationSource = locationSource,
                    properties = MapProperties(
                        locationTrackingMode =
                            if (granted) LocationTrackingMode.Face else LocationTrackingMode.None
                    ),
                    uiSettings = MapUiSettings(isLocationButtonEnabled = true)
                ) {

                    // ⭐ Firebase에서 가져온 마커 표시
                    facilities.forEach { facility ->
                        val lat = facility.latitude
                        val lng = facility.longitude

                        if (lat != null && lng != null) {
                            key(facility.id) {
                                Marker(
                                    state = rememberMarkerState(
                                        position = LatLng(lat, lng)
                                    ),
                                    captionText = facility.name,
                                    onClick = {
                                        selectedFacility = facility
                                        true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // ▼ 선택된 시설 카드 표시
            selectedFacility?.let { info ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 94.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(info.name ?: "이름 정보 없음", color = Color.Black)
                        Text(info.address ?: "주소 정보 없음", color = Color.DarkGray)
                        val phone = info.phoneNumber
                        if (!phone.isNullOrBlank()) {
                            Text(text = "전화번호: $phone", color = Color.DarkGray)
                        }
                        // ⭐ 평점 표시
                        if (info.averageRating != null)
                            Text("⭐ ${String.format("%.1f", info.averageRating)}", color = Color(0xFFFFC107))
                        else
                            Text("아직 리뷰 없음", color = Color.Gray)

                        if (info.reviewCount > 0)
                            Text("리뷰 ${info.reviewCount}개")
                    }
                }
            }

            // 권한 안내
            if (!granted) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("위치 권한이 필요합니다.", color = Color.Red)
                }
            }
        }
    }
}
