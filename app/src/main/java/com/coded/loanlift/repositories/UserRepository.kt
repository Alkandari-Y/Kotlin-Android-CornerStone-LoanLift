package com.coded.loanlift.repositories

import com.coded.loanlift.data.response.auth.UserInfoDto
import com.coded.loanlift.data.response.kyc.KYCResponse

object UserRepository {
    var userInfo: UserInfoDto? = null
    var kyc: KYCResponse? = null
}