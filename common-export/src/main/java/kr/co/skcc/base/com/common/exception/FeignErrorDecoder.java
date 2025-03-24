package kr.co.skcc.oss.com.common.exception;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static feign.FeignException.errorStatus;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        ApiError apiError;

        if (response.body() == null) {
            apiError = new ApiError(HttpStatus.valueOf(response.status()));
            apiError.setError(response.reason());
            List<String> details = new ArrayList<>();
            details.add(response.reason());
            apiError.setMessage(details);
        } else {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                String errorMessage;
                Reader reader = response.body().asReader();
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                errorMessage = builder.toString();
                apiError = mapper.readValue(errorMessage, ApiError.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return switch (response.status()) {
            case 400 -> new BadRequestException(apiError.getError(), apiError.getMessage());
            case 404 -> new NotFoundException(apiError.getError(), apiError.getMessage());
            case 409 -> new ServiceException(apiError.getError(), apiError.getMessage());
            default -> errorStatus(methodKey, response);
        };
    }
}