@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handle(Exception ex) {

        return Mono.just(
                ResponseEntity.status(500)
                        .body(Map.of(
                                "error", ex.getMessage(),
                                "status", 500
                        ))
        );
    }
}