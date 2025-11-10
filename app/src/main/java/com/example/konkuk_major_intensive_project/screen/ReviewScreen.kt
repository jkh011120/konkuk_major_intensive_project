package com.example.konkuk_major_intensive_project.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.konkuk_major_intensive_project.ViewModel.ReviewViewModel
import com.example.konkuk_major_intensive_project.model.Review

//@Preview
@Composable
fun ReviewScreen(
    facilityId: String,
    viewModel: ReviewViewModel = viewModel()
) {
    val content by viewModel::content
    val rating by viewModel::rating
    val error by viewModel::errorMessage
    val userReviews by viewModel::userReviews

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            item {
                OutlinedTextField(
                    value = content,
                    onValueChange = { viewModel.content = it },
                    label = { Text("리뷰 내용을 작성하세요.")},
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                RatingBar(rating = rating, onRatingChanged = { viewModel.rating = it })
            }



            item {
                error?.let { Text(it, color = Color.Red) }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Button(
                    onClick = { viewModel.submitReview() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005500)),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("리뷰 제출", color = Color.White, fontSize = 16.sp)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(userReviews, key = { it.id }) { review ->
                ReviewItem(review)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("작성자: ${review.userId}", style = MaterialTheme.typography.labelSmall)
        Text("⭐".repeat(review.rating))
        Text(review.content)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        for (i in 1..5) {
            Text(
                text = if (i <= rating) "⭐" else "☆",
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onRatingChanged(i) },
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}