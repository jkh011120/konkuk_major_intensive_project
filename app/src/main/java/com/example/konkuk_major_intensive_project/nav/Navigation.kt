package com.example.konkuk_major_intensive_project.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavigationGraph(navController: NavHostController){
    NavHost(navController=navController, startDestination = "home") {
        composable("home") { }
        composable("review") { }
        composable("user_info") { }
    }
}