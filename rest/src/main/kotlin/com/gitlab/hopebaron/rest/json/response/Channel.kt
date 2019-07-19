package com.gitlab.hopebaron.rest.json.response

import com.gitlab.hopebaron.common.entity.ChannelType
import kotlinx.serialization.Serializable

@Serializable
data class PartialChannelResponse(val name: String, val type: ChannelType)