package com.yourorg.stackspike;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {
    @Test
    void verifyModularStructure() {
        ApplicationModules modules = ApplicationModules.of(StackSpikeApplication.class);
        modules.verify();
        modules.forEach(System.out::println);
    }
}