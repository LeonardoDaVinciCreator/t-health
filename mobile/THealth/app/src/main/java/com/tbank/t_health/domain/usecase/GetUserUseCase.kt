package com.tbank.t_health.domain.usecase

import com.tbank.t_health.data.local.UserPrefs
import com.tbank.t_health.data.model.UserData
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userPrefs: UserPrefs
) {
    operator fun invoke(): UserData? {
        return userPrefs.getUser()
    }
}