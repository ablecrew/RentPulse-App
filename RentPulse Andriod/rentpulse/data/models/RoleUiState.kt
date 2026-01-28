package com.rentpulse.data.models

data class RoleUiState(
    val selectedRole: String? = null,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
