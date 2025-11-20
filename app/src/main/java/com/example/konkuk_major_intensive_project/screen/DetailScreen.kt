package com.example.konkuk_major_intensive_project.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.konkuk_major_intensive_project.R
import com.example.konkuk_major_intensive_project.Util.FacilityInfoPanel
import com.example.konkuk_major_intensive_project.ViewModel.DetailScreenViewModel
import com.example.konkuk_major_intensive_project.ViewModel.UserViewModel
import com.example.konkuk_major_intensive_project.model.FacilityDetail

fun makePhoneCall(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    facilityId: String,
    navController: NavController,
    userViewModel: UserViewModel,
    viewModel: DetailScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val favorites by remember { userViewModel::favorites }

    LaunchedEffect(facilityId) {
        viewModel.loadFacilityDetail(facilityId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("시설 상세 정보") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            uiState.facility?.let { facility ->

                // ★ id가 null이면 즐겨찾기 동작 불가 → false
                val isFavorite =
                    if (facility.id != null) favorites[facility.id] == true else false

                val isLoggedIn = userViewModel.isLoggedIn

                FacilityInfoPanel(
                    facility = facility.copy(isFavorite = isFavorite),
                    onToggleFavorite = {
                        facility.id?.let { safeId ->
                            userViewModel.toggleFavorite(safeId) {}
                        }
                    },
                    onCallPhone = { phoneNumber -> makePhoneCall(context, phoneNumber) },
                    navController = navController,
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }

}
