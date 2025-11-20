package com.example.konkuk_major_intensive_project.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.konkuk_major_intensive_project.model.FacilityDetail
import com.example.konkuk_major_intensive_project.repository.FacilityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel : ViewModel() {

    private val repository = FacilityRepository.getInstance()

    data class UiState(
        val facility: FacilityDetail? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // ID로 단건 불러오는 경우 (repo 안에서 처리)
    fun loadFacilityDetail(facilityId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            repository.getFacilityDetail(facilityId)
                .onSuccess { facility ->
                    _uiState.value = _uiState.value.copy(
                        facility = facility,
                        isLoading = false
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "데이터를 불러올 수 없습니다"
                    )
                }
        }
    }

    // 이미 불러온 리스트에서 찾는 경우 (id nullable 대응)
    fun loadFacilityDetailFromList(id: String, allFacilities: List<FacilityDetail>) {
        viewModelScope.launch {
            val facility = allFacilities.find { it.id == id }
            _uiState.value = _uiState.value.copy(facility = facility)
        }
    }

    // 즐겨찾기 토글
    fun toggleFavorite() {
        val currentFacility = _uiState.value.facility ?: return

        // 🔴 id 가 null이면 토글 불가 → 에러 메시지 세팅하고 종료
        val facilityId = currentFacility.id
        if (facilityId.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "시설 ID가 없어 즐겨찾기를 설정할 수 없습니다"
            )
            return
        }

        viewModelScope.launch {
            repository.toggleFavorite(facilityId)
                .onSuccess { newFavoriteStatus ->
                    _uiState.value = _uiState.value.copy(
                        facility = currentFacility.copy(isFavorite = newFavoriteStatus)
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        error = "즐겨찾기 설정에 실패했습니다"
                    )
                }
        }
    }
}
