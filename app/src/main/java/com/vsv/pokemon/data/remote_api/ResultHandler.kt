package com.vsv.pokemon.data.remote_api

import com.squareup.moshi.JsonDataException
import com.vsv.pokemon.domain.model.Result
import com.vsv.pokemon.domain.model.errors.RemoteError
import kotlinx.coroutines.ensureActive
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> T
): Result<T, RemoteError> {
    return try {
        Result.Success(execute.invoke())
    } catch (e: HttpException) {
        return when (e.code()) {
            404 -> {
                Result.Error(RemoteError.NOT_FOUND)
            }
            in 500..599 -> {
                Result.Error(RemoteError.SERVER)
            }
            else -> {
                Result.Error(RemoteError.UNKNOWN)
            }
        }
    } catch (e: SocketTimeoutException) {
        return Result.Error(RemoteError.TIMEOUT)
    } catch (e: UnresolvedAddressException) {
        return Result.Error(RemoteError.NO_INTERNET)
    } catch (e: UnknownHostException) {
        return Result.Error(RemoteError.NO_INTERNET)
    } catch (e: JsonDataException) {
        e.printStackTrace()
        return Result.Error(RemoteError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(RemoteError.UNKNOWN)
    }
}