package vn.psvm.demo.aspectlogging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class RestControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(RestControllerAspect.class);

    private final ObjectMapper objectMapper;

    public RestControllerAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("within(vn.psvm.demo.aspectlogging.resources..*) " +
            "&& @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getPointcut() {
    }

    @Before("getPointcut()")
    public void logMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Map<String, Object> parameters = getParameters(joinPoint);
        GetMapping getMapping = signature.getMethod().getAnnotation(GetMapping.class);
        try {
            logger.info("==> path(s): {}, arguments: {} ",
                    getMapping.value(), objectMapper.writeValueAsString(parameters));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    @AfterReturning(pointcut = "getPointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, Object entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        GetMapping getMapping = signature.getMethod().getAnnotation(GetMapping.class);
        try {
            logger.info("<== path(s): {}, retuning: {}",
                    getMapping.value(), objectMapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }
}
