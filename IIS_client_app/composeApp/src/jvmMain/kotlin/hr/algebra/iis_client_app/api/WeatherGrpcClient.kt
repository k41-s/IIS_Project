package hr.algebra.iis_client_app.api

import hr.algebra.iis_client_app.weather.WeatherServiceGrpcKt
import hr.algebra.iis_client_app.weather.WeatherProto
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class WeatherGrpcClient {
    private val channel = ManagedChannelBuilder.forAddress("localhost", 9091)
        .usePlaintext()
        .executor(Dispatchers.Default.asExecutor())
        .build()

    private val stub = WeatherServiceGrpcKt.WeatherServiceCoroutineStub(channel)

    suspend fun getTemperature(cityName: String): List<WeatherProto.CityWeather> {
        val request = WeatherProto.WeatherRequest.newBuilder().setCityName(cityName).build()
        val response = stub.getTemperature(request)
        return response.resultsList
    }

    fun shutdown() {
        channel.shutdown()
    }
}
