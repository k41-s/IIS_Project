package hr.algebra.iisweatherservice.grpc.impl;

import hr.algebra.iisweatherservice.grpc.CityWeather;
import hr.algebra.iisweatherservice.grpc.WeatherRequest;
import hr.algebra.iisweatherservice.grpc.WeatherResponse;
import hr.algebra.iisweatherservice.grpc.WeatherServiceGrpc;
import hr.algebra.iisweatherservice.service.WeatherDataService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.RequiredArgsConstructor;

@GrpcService
@RequiredArgsConstructor
public class WeatherGrpcServiceImpl extends WeatherServiceGrpc.WeatherServiceImplBase {

    private final WeatherDataService weatherDataService;

    @Override
    public void getTemperature(WeatherRequest request, StreamObserver<WeatherResponse> responseObserver) {
        var matches = weatherDataService.getWeatherData(request.getCityName());

        WeatherResponse.Builder responseBuilder = WeatherResponse.newBuilder();

        for (var city : matches) {
            responseBuilder.addResults(CityWeather.newBuilder()
                    .setCityName(city.name())
                    .setTemp(city.temp())
                    .build());
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}