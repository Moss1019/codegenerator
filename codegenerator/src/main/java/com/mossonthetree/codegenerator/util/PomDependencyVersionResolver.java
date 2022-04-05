package com.mossonthetree.codegenerator.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

public class PomDependencyVersionResolver {
    private static final String uri = "https://search.maven.org/solrsearch/select?q=%s&rows=1&wt=json";
    private static final Function<String, URI> getUri = (String query) -> URI.create(String.format(uri, query));
    private static final Map<DependencyInfo, CachedDependency> cache = new HashMap<>();

    private final HttpClient httpClient;

    public PomDependencyVersionResolver() {
        httpClient = HttpClient.newHttpClient();
    }

    public String resolveVersion(DependencyInfo dependencyInfo) {
        String version = resolveFromCache(dependencyInfo);
        if (version == null) {
            version = resolveFromMavenCentral(dependencyInfo);
        }
        if (version == null) {
            version = resolveHardCoded(dependencyInfo);
        }
        return version;
    }

    private String resolveFromCache(DependencyInfo dependencyInfo) {
        if (cache.containsKey(dependencyInfo)) {
            CachedDependency cachedDependency = cache.get(dependencyInfo);
            Date lastCached = cachedDependency.timeResolved;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -1);
            if (lastCached.after(cal.getTime())) {
                return cache.get(dependencyInfo).version;
            }
        }
        return null;
    }

    private String resolveFromMavenCentral(DependencyInfo dependencyInfo) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(getUri.apply(String.format("%s+%s", dependencyInfo.getGroupId(), dependencyInfo.getArtifactId())))
                .build();
        String version = "";
        try {
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                return null;
            }
            Map<String, Object> map = new ObjectMapper().readValue(res.body(), HashMap.class);
            Map<String, Object> response = (Map<String, Object>) map.get("response");
            List<Map<String, Object>> docs = (List<Map<String, Object>>) response.get("docs");
            version = docs.get(0).get("latestVersion").toString();

            cache.put(dependencyInfo, new CachedDependency(version));
        } catch (Exception ignored) {
            return null;
        }
        return version;
    }

    private String resolveHardCoded(DependencyInfo dependencyInfo) {
        if (dependencyInfo.getArtifactId().equals("elasticsearch-rest-high-level-client")) {
            return "7.15.1";
        }
        if (dependencyInfo.getArtifactId().equals("kafka-clients")) {
            return "3.0.0";
        }
        if (dependencyInfo.getArtifactId().equals("jackson-databind")) {
            return "2.11.1";
        }
        if (dependencyInfo.getArtifactId().equals("spring-boot-starter-web")) {
            return "2.6.0";
        }
        if (dependencyInfo.getArtifactId().equals("spring-boot-starter-data-jpa")) {
            return "2.6.0";
        }
        if (dependencyInfo.getArtifactId().equals("mysql-connector-java")) {
            return "8.0.27";
        }
        if (dependencyInfo.getArtifactId().equals("spring-boot-starter-parent")) {
            return "2.6.0";
        }
        return "";
    }

    private static class CachedDependency {
        public String version;
        public Date timeResolved;

        public CachedDependency(String version) {
            this.version = version;
            timeResolved = Date.from(Instant.now());
        }
    }
}
