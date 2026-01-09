package webserver.interceptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InterceptorRegistry {
    private final List<InterceptorRegistration> registrations = new ArrayList<>();

    // 캐시 저장소: <요청 경로, 해당 경로에 적용될 인터셉터 리스트>
    private final Map<String, List<Interceptor>> pathCache = new ConcurrentHashMap<>();

    public InterceptorRegistration addInterceptor(Interceptor interceptor) {
        InterceptorRegistration registration = new InterceptorRegistration(interceptor);
        registrations.add(registration);
        return registration;
    }

    public List<Interceptor> getInterceptorsForPath(String path) {
        // 캐시에 있으면 즉시 반환, 없으면 calculateInterceptors 실행 후 캐시에 저장
        return pathCache.computeIfAbsent(path, this::calculateInterceptors);
    }

    private List<Interceptor> calculateInterceptors(String path) {
        return registrations.stream()
                .filter(reg -> reg.isPathApplicable(path))
                .map(reg -> reg.interceptor)
                .collect(Collectors.toList());
    }

    // 내부 설정 클래스
    public static class InterceptorRegistration {
        private final Interceptor interceptor;
        private final List<String> includePatterns = new ArrayList<>();
        private final List<String> excludePatterns = new ArrayList<>();

        public InterceptorRegistration(Interceptor interceptor) { this.interceptor = interceptor; }

        public InterceptorRegistration addPathPatterns(String... patterns) {
            this.includePatterns.addAll(Arrays.asList(patterns));
            return this;
        }

        public InterceptorRegistration excludePathPatterns(String... patterns) {
            this.excludePatterns.addAll(Arrays.asList(patterns));
            return this;
        }

        public boolean isPathApplicable(String path) {
            for (String pattern : excludePatterns) {
                if (pathMatch(pattern, path)) return false;
            }
            for (String pattern : includePatterns) {
                if (pathMatch(pattern, path)) return true;
            }
            return includePatterns.isEmpty(); // include가 없으면 모든 경로 다 검사
        }

        private boolean pathMatch(String pattern, String path) {
            if (pattern.endsWith("/**")) {
                return path.startsWith(pattern.replace("/**", ""));
            }
            return pattern.equals(path);
        }
    }
}
