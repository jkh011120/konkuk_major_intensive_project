package com.example.konkuk_major_intensive_project.Util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.konkuk_major_intensive_project.R
import com.example.konkuk_major_intensive_project.model.FacilityDetail


@Composable
fun FacilityInfoPanel(
    facility: FacilityDetail,
    onToggleFavorite: () -> Unit,
    onCallPhone: (String) -> Unit,
    navController: NavController? = null,
    isLoggedIn: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = facility.name ?: "이름 정보 없음",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = facility.address ?: "주소 정보 없음",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Text(
                        text = facility.phoneNumber ?: "전화번호 정보 없음",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
                        Text(
                            text = "${facility.averageRating ?: 0.0f}",
                            fontSize = 16.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${facility.reviewCount ?: 0}개 리뷰", fontSize = 16.sp)
                        TextButton(
                            onClick = {
                                facility.id?.let { id ->
                                    navController?.navigate("review/$id")
                                }
                            }
                        ) {
                            Text("리뷰 보기", fontSize = 14.sp, color = Color.Blue)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .size(120.dp, 100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                // 즐겨찾기
                IconButton(
                    onClick = {
                        if (isLoggedIn) onToggleFavorite()
                        else
                            android.widget.Toast.makeText(
                                context,
                                "로그인을 해주세요",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (facility.isFavorite)
                                R.drawable.baseline_star_24
                            else
                                R.drawable.baseline_star_outline_24
                        ),
                        contentDescription = "즐겨찾기",
                        tint = if (facility.isFavorite) Color(0xFFFFD700) else Color.Gray
                    )
                }

                // 전화
                IconButton(onClick = {
                    facility.phoneNumber?.let { onCallPhone(it) }
                }) {
                    Icon(Icons.Default.Phone, contentDescription = "전화", tint = Color.Gray)
                }

                // 길찾기
                IconButton(onClick = {
                    val lat = facility.latitude
                    val lng = facility.longitude

                    if (lat != null && lng != null) {
                        navController?.navigate("route/$lat/$lng/${facility.name ?: "장소"}")
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_directions_bus_24),
                        contentDescription = "교통 정보",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}
