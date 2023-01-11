package com.guilherme.antonio.feature_photo

import com.guilherme.antonio.feature_photo.base.ApiResult
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import okhttp3.Request
import okio.Timeout
import retrofit2.Callback
import retrofit2.Response

object RetrofitConfig {

    fun getClient(): Retrofit {

        val h = HttpLoggingInterceptor { message ->
            Timber.d("Http: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(h)
            .build()

        val contentType = "application/json".toMediaType()

        val json = kotlinx.serialization.json.Json {
            // By default Kotlin serialization will serialize all of the keys present in JSON object and throw an
            // exception if given key is not present in the Kotlin class. This flag allows to ignore JSON fields
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(ApiResultAdapterFactory())
            .build()
    }

}

class ApiResultAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) return null
        check(returnType is ParameterizedType)

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != ApiResult::class.java) return null
        check(responseType is ParameterizedType)

        val successType = getParameterUpperBound(0, responseType)

        return ApiResultCallAdapter<Any>(successType)
    }
}

internal class ApiResultCallAdapter<T>(
    private val successType: Type,
) : CallAdapter<T, Call<ApiResult<T>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<ApiResult<T>> = ApiResultCall(call)
}

internal class ApiResultCall<T> constructor(
    private val callDelegate: Call<T>,
) : Call<ApiResult<T>> {

    @Suppress("detekt.MagicNumber")
    override fun enqueue(callback: Callback<ApiResult<T>>) =
        callDelegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                response.body()?.let {
                    when (response.code()) {
                        in 200..208 -> {
                            callback.onResponse(
                                this@ApiResultCall,
                                Response.success(ApiResult.Success(it))
                            )
                        }
                        in 400..409 -> {
                            callback.onResponse(
                                this@ApiResultCall,
                                Response.success(
                                    ApiResult.Error(
                                        response.code(),
                                        response.message()
                                    )
                                )
                            )
                        }
                    }
                } ?: callback.onResponse(
                    this@ApiResultCall,
                    Response.success(ApiResult.Error(123, "message"))
                )
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                callback.onResponse(
                    this@ApiResultCall,
                    Response.success(ApiResult.Exception(throwable))
                )
                call.cancel()
            }
        })

    override fun clone(): Call<ApiResult<T>> = ApiResultCall(callDelegate.clone())

    override fun execute(): Response<ApiResult<T>> =
        throw UnsupportedOperationException("ResponseCall does not support execute.")

    override fun isExecuted(): Boolean = callDelegate.isExecuted

    override fun cancel() = callDelegate.cancel()

    override fun isCanceled(): Boolean = callDelegate.isCanceled

    override fun request(): Request = callDelegate.request()

    override fun timeout(): Timeout = callDelegate.timeout()
}