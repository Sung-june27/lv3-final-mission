package finalmission.global.error.dto;

public record ErrorResponse(
        String message
) {

    public static ErrorResponse from(String message) {
        return new ErrorResponse(message);
    }
}
