package kz.bitlab.trelloG142.exception.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ErrorResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("code")
    private HttpStatus status;

    @JsonProperty("details")
    private String details;
}
