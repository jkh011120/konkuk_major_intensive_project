package com.example.konkuk_major_intensive_project.ViewModel

import androidx.lifecycle.ViewModel
import com.example.konkuk_major_intensive_project.model.FacilityDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FacilityViewModel : ViewModel() {
    private val _facilities = MutableStateFlow<List<FacilityDetail>>(emptyList())
    val facilities: StateFlow<List<FacilityDetail>> = _facilities

    private val FIREBASE_DATABASE_URL =
        "https://konkuk-major-intensive-project-default-rtdb.firebaseio.com"

    init {
        android.util.Log.d("FacilityVM", "init called")   // 🔴 1번 로그
        fetchFacilities()
    }


    private fun fetchFacilities() {
        android.util.Log.d("FacilityVM", "fetchFacilities start") // 🔴 2번 로그
        val database = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
        val facilitiesRef = database.getReference("facilities")

        facilitiesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                android.util.Log.d("FacilityVM", "onDataChange called") // 🔴 3번 로그

                val list = mutableListOf<FacilityDetail>()
                for (facilitySnapshot in snapshot.children) {
                    val facility = facilitySnapshot.getValue(FacilityDetail::class.java)
                    if (facility != null) {
                        list.add(facility)
                        android.util.Log.d(
                            "FacilityVM",
                            "facility: id=${facility.id}, name=${facility.name}, " +
                                    "lat=${facility.latitude}, lng=${facility.longitude}"
                        )
                    }
                }
                android.util.Log.d("FacilityVM", "loaded facilities size=${list.size}")
                _facilities.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                android.util.Log.e("FacilityVM", "fetch cancelled: ${error.message}")
            }
        })
    }
}
