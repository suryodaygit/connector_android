

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Custom API Callback class to handle response and failure from APIs.
 *
 * @param <T> Custom Model class which are created for handling response of the API.
 * @author Komal Ardekar
 */
abstract class APICallbacks<T> : Callback<T> {

    /**
     * @param call     Instance of Call interface.
     * @param response Response of the API to determine if it indicates success.
     */
    override fun onResponse(call: Call<T>, response: Response<T>) {

        if ( response.body() != null) {

            if (response.code() == 200 || response.code()==201)
                onSuccess(response.body())
            else
                response.errorBody()
        } else onFailure(response.message(),response.message())
    }

    protected abstract fun onSuccess(response: T?)

/**
 * @param generalErrorMsg UserData defined error message.
 * @param systemErrorMsg  System defined error message.
 */
    protected abstract fun onFailure(generalErrorMsg: String, systemErrorMsg: String)

  /*  *//**
     * When status us equal to o and error code is equal to 99 then trigger forced logout.
     *//*
    protected abstract fun onForcedLogout()

    *//**
     * @param response If not null and status is equal to 0 then sends response to the activity.
     *//*
    protected abstract fun onError(response: T?)

    *//**
     * @param generalErrorMsg UserData defined error message.
     * @param systemErrorMsg  System defined error message.
     *//*
    protected abstract fun onFailure(generalErrorMsg: String, systemErrorMsg: String)*/
}
