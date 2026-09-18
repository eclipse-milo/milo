/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

import com.digitalpetri.opcua.codegen.ClientGenerationRequest;
import com.digitalpetri.opcua.codegen.ClientGenerationResult;
import com.digitalpetri.opcua.codegen.GenerationRequest;
import com.digitalpetri.opcua.codegen.GenerationResult;
import com.digitalpetri.opcua.codegen.JavaClientTypeCompiler;
import com.digitalpetri.opcua.codegen.JavaDataTypeCompiler;
import com.digitalpetri.opcua.codegen.JavaServerTypeCompiler;
import com.digitalpetri.opcua.codegen.OwnedOutputWriter;
import com.digitalpetri.opcua.codegen.ServerGenerationRequest;
import com.digitalpetri.opcua.codegen.ServerGenerationResult;
import com.digitalpetri.opcua.codegen.TypePackages;
import com.digitalpetri.opcua.uanodeset.LinkedModel;
import com.digitalpetri.opcua.uanodeset.NodeSetLinker;
import com.digitalpetri.opcua.uanodeset.NodeSetReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

/**
 * Generates Milo's standard server and client libraries from an explicitly supplied, pinned NodeSet.
 *
 * <p>Method descriptors are generated once as a shared unit in {@code milo-sdk-core}; both SDK
 * libraries consume that catalog instead of emitting their own copies. The three units are written
 * to separate output roots so each keeps its own compiler ownership manifest.
 */
public final class GenerateNs0Model {
  private static final String INPUT_SHA256 =
      "d29ad5eb20884935a6b7ef8f9b70a6ad72417411a3fd3e4e704dac5762d394c8";

  private static final String METHODS = "org.eclipse.milo.opcua.sdk.core.model.methods";
  private static final String SERVER = "org.eclipse.milo.opcua.sdk.server.model";
  private static final String CLIENT = "org.eclipse.milo.opcua.sdk.client.model";

  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      throw new IllegalArgumentException("expected NodeSet path, model-roots.tsv, output directory");
    }
    Path input = Path.of(args[0]);
    String hash =
        HexFormat.of()
            .formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(input)));
    if (!INPUT_SHA256.equals(hash)) {
      throw new IllegalArgumentException("NodeSet SHA-256 mismatch: " + hash);
    }
    LinkedModel model = NodeSetLinker.suppliedOnly().link(NodeSetReader.read(input));
    Set<ExpandedNodeId> roots = new LinkedHashSet<>();
    Set<ExpandedNodeId> methodRoots = new LinkedHashSet<>();
    for (String line : Files.readAllLines(Path.of(args[1]))) {
      if (!line.isBlank() && !line.startsWith("#")) {
        NodeId root = NodeId.parse(line.split("\t")[0]);
        roots.add(root.expanded(model.namespaceTable()));
        // Only ObjectTypes declare Methods; VariableType roots are not descriptor roots.
        if (model.objectTypes().contains(root)) {
          methodRoots.add(root.expanded(model.namespaceTable()));
        }
      }
    }
    Path output = Path.of(args[2]);
    var writer = new OwnedOutputWriter();

    // The shared descriptors must exist before either SDK library binds to them.
    GenerationResult methods =
        new JavaDataTypeCompiler()
            .generate(
                model,
                new GenerationRequest(
                    Set.of(),
                    Set.of(),
                    Map.of(),
                    List.of(),
                    Map.of(),
                    methodRoots,
                    Map.of(Namespaces.OPC_UA, METHODS)));
    writer.write(output.resolve("methods"), "milo-ns0-methods", methods);

    var dataTypes = new GenerationRequest(Set.of(), Set.of(), Map.of(), List.of(methods.bindings()));
    ServerGenerationResult server =
        new JavaServerTypeCompiler()
            .generate(
                model,
                new ServerGenerationRequest(
                    dataTypes,
                    roots,
                    Map.of(Namespaces.OPC_UA, SERVER),
                    List.of(),
                    Map.of(),
                    true,
                    Map.of(
                        Namespaces.OPC_UA,
                        new TypePackages(SERVER + ".objects", SERVER + ".variables"))));
    writer.write(output.resolve("server"), "milo-ns0-server", server.output());

    ClientGenerationResult client =
        new JavaClientTypeCompiler()
            .generate(
                model,
                new ClientGenerationRequest(
                    dataTypes,
                    roots,
                    Map.of(Namespaces.OPC_UA, CLIENT),
                    List.of(),
                    Map.of(),
                    true,
                    Map.of(
                        Namespaces.OPC_UA,
                        new TypePackages(CLIENT + ".objects", CLIENT + ".variables"))));
    writer.write(output.resolve("client"), "milo-ns0-client", client.output());

    Files.write(output.resolve("server-diagnostics.txt"), server.diagnostics());
    Files.write(output.resolve("client-diagnostics.txt"), client.diagnostics());
    System.out.println(
        "Generated "
            + methods.sources().size()
            + " descriptor, "
            + server.output().sources().size()
            + " server and "
            + client.output().sources().size()
            + " client sources from "
            + roots.size()
            + " roots; input "
            + hash);
  }
}
