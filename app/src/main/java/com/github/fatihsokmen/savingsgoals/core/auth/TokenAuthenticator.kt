package com.github.fatihsokmen.savingsgoals.core.auth

import okhttp3.Authenticator
import okhttp3.Route
import javax.inject.Inject

/**
 * Authenticator does not work with 403 which Starling sandbox api responds by default for non-valid
 * access tokens, so I am skipping this approach and will use an interceptor to handle however
 * this is not the right way tho since there could be multiple parallel calls and all will fail
 * at the same time independently
 */
class TokenAuthenticator @Inject constructor(
) : Authenticator {

    override fun authenticate(route: Route?, response: okhttp3.Response): okhttp3.Request? {
        return null
    }

}