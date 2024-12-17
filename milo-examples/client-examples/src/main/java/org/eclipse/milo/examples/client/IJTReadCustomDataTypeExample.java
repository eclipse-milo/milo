package org.eclipse.milo.examples.client;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.sdk.core.types.json.JsonCodecFactory;
import org.eclipse.milo.sdk.core.types.json.JsonStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IJTReadCustomDataTypeExample implements ClientExample {

    public static void main(String[] args) throws Exception {
        var example = new IJTReadCustomDataTypeExample();

        new ClientExampleRunner(example, false).run();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        client.setDataTypeManagerInitializer(new OpcUaClient.DefaultDataTypeManagerInitializer(JsonCodecFactory::create));

        client.connect();

        NodeId nodeId = NodeId.parse("ns=1;s=TighteningSystem/ResultManagement/Results/Result");
        DataValue value = client.readValue(0, TimestampsToReturn.Both, nodeId);

        if (value.getValue().getValue() instanceof ExtensionObject xo) {
            JsonStruct struct = (JsonStruct) xo.decode(client.getDynamicEncodingContext());

            System.out.println(struct);
        }

        future.complete(client);
    }

    @Override
    public String getEndpointUrl() {
        return "opc.tcp://10.211.55.3:40451";
    }

    @Override
    public SecurityPolicy getSecurityPolicy() {
        return SecurityPolicy.None;
    }

}
