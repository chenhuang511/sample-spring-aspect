package vn.psvm.demo.aspectlogging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CustomServiceAspect {

    private final Logger logger = LoggerFactory.getLogger(CustomServiceAspect.class);

    private final ObjectMapper objectMapper;

    public CustomServiceAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(vn.psvm.demo.aspectlogging.CustomServiceAnnotation)")
    public Object logProceed(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, Object> parameters = getParameters(joinPoint);
        logger.info("==> in service: arguments: {} ", objectMapper.writeValueAsString(parameters));
        Object proceed = joinPoint.proceed();
        System.out.println("Stupid proceeding: " + joinPoint.getSignature());
        return proceed;
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
