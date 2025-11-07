package dev.ar.maxisandoval.consultarcuitonlineapirestjson.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.Map;
import dev.ar.maxisandoval.consultarcuitonlineapirestjson.service.CuitHtmlParser;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api")
public class CuitProxyController {

    private final WebClient http;

    public CuitProxyController(WebClient.Builder b) {
        this.http = b.build();
    }

    @GetMapping("/cuit")
    public Mono<ResponseEntity<Object>> get(@RequestParam(name = "cuit", required = false) String cuitParam,
                                            @RequestParam(name = "cuilt", required = false) String cuiltAlias,
                                            @RequestParam(name = "raw", defaultValue = "false") boolean raw) {
        String rawInput = (cuitParam != null ? cuitParam : (cuiltAlias != null ? cuiltAlias : ""));
        String cuit = rawInput.replaceAll("\\D", "");
        if (cuit.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Parámetro ?cuit (o ?cuilt) requerido")));
        }

        String target = "https://www.cuitonline.com/api/pigeon-content/search/" + cuit + "/anonymous/1";
        String referer = "https://www.cuitonline.com/search/" + cuit;

        return http.get()
                .uri(target)
                .header(HttpHeaders.REFERER, referer)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL)
                .retrieve()
                .toEntity(String.class)
                .map(resp -> {
                    String bodyStr = resp.getBody() == null ? "" : resp.getBody();

                    Object maybeJson = tryParseJson(bodyStr);
                    Object body;

                    if (raw) {
                        body = (maybeJson != null) ? maybeJson : Map.of("raw", bodyStr);
                    } else {
                        if (maybeJson instanceof Map<?, ?> m && m.containsKey("content")) {
                            String html = String.valueOf(m.get("content"));
                            body = CuitHtmlParser.parse(html);
                        } else if (maybeJson != null) {
                            body = maybeJson;
                        } else {
                            body = Map.of("raw", bodyStr);
                        }
                    }

                    return ResponseEntity.status(resp.getStatusCode())
                            .contentType(MediaType.APPLICATION_JSON)
                            .headers(h -> h.setAccessControlAllowOrigin("*"))
                            .body(body);
                })
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(h -> h.setAccessControlAllowOrigin("*"))
                                .body(Map.of("error", ex.toString()))));
    }

    private static Object tryParseJson(String s) {
        try {
            return com.fasterxml.jackson.databind.json.JsonMapper.builder()
                    .build()
                    .readValue(s, Object.class);
        } catch (Exception e) {
            return null;
        }
    }
}