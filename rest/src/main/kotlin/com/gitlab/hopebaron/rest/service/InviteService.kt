package com.gitlab.hopebaron.rest.service

import com.gitlab.hopebaron.rest.ratelimit.RequestHandler
import com.gitlab.hopebaron.rest.route.Route
import io.ktor.http.Parameters

class InviteService(requestHandler: RequestHandler) : RestService(requestHandler) {
    suspend fun getInvite(code: String, withCounts: Boolean) = call(Route.InviteGet) {
        keys[Route.InviteCode] = code
        parameters = Parameters.build {
            append("with_counts", "$withCounts")

        }
    }

    suspend fun deleteInvite(code: String) = call(Route.InviteDelete) {
        keys[Route.InviteCode] = code
    }
}