package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.Test;

class OpcUaClientResetMethodsTest {

  @Test
  void lazyBackedFieldsHavePublicResetMethods() throws Exception {
    List<String> resetMethods =
        List.of(
            "resetOperationLimits",
            "resetDynamicDataTypeManager",
            "resetDynamicEncodingContext",
            "resetDataTypeTree",
            "resetObjectTypeTree",
            "resetVariableTypeTree");

    for (String resetMethod : resetMethods) {
      Method method = OpcUaClient.class.getMethod(resetMethod);

      assertEquals(void.class, method.getReturnType());
      assertTrue(Modifier.isPublic(method.getModifiers()));
      assertEquals(0, method.getParameterCount());
    }
  }
}
