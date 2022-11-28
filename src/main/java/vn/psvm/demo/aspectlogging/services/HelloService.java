package vn.psvm.demo.aspectlogging.services;

import org.springframework.stereotype.Service;
import vn.psvm.demo.aspectlogging.LogExecutionTime;
import vn.psvm.demo.aspectlogging.CustomServiceAnnotation;

@Service
public class HelloService {
    @CustomServiceAnnotation
    @LogExecutionTime
    public String decorateInput(String input) {
        return input + "_with_service_processing";
    }
}
