#!/usr/bin/env python3
"""Run the local source examples in isolated JVMs and check observable output.

Build client-examples and provide its Maven dependency classpath first. This script
does not invoke Maven. Logs are private because KeyLogExample prints session keys;
only redacted logs and non-secret assertions are retained. Vendor examples require
separate fixtures and are deliberately not treated as passing by this script.
"""

import argparse
import hashlib
import json
import re
import socket
import subprocess
import tempfile
from pathlib import Path


CASES = {
    "ReadExample": {"StartTime=": 1, "State=Running": 1, "CurrentTime=": 1},
    "BrowseExample": {"Node=Objects": 1, "Node=Server": 1},
    "WriteExample": {"Wrote '": 10},
    "SubscriptionDataExample": {"subscription onDataReceived:": 2,
                                "monitoredItem onDataReceived:": 2},
    "MethodExample": {"sqrt(16)=4.0": 1},
    "SubscriptionEventExample": {"field[0]=": 3, "field[4]=": 3},
    "ReadWriteCustomDataTypeNodeExample": {"Decoded=": 2, "foobar": 1},
    "ReverseConnectExample": {"Connected to ": 1, "State=Running": 1,
                              "CurrentTime=": 1},
    "AliasNamesExample": {"FindAlias:": 1, "FindAliasVerbose:": 1,
                          "read Demo.": 1},
    "TriggeringExample": {"sampling item received value:": 1},
    "KeyLogExample": {"Connected with Basic256Sha256": 1, "ServerState = 0": 1,
                      "CurrentTime = ": 1, "Load either file": 1},
}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("classpath", type=Path)
    parser.add_argument("evidence", type=Path)
    parser.add_argument("--example", choices=list(CASES), action="append")
    args = parser.parse_args()
    args.evidence.mkdir(parents=True, exist_ok=True, mode=0o700)
    module = args.source / "milo-examples/client-examples"
    classpath = str(module / "target/classes") + ":" + args.classpath.read_text().strip()
    results = []
    for name in args.example or CASES:
        port = 4840 if name == "KeyLogExample" else 12686
        with socket.socket() as probe:
            probe.bind(("127.0.0.1", port))
        with tempfile.TemporaryDirectory(prefix="milo-wiki-example-") as temp:
            command = ["mise", "exec", "--", "java", "-ea", "-Djava.io.tmpdir=" + temp,
                       "-cp", classpath, "org.eclipse.milo.examples.client." + name]
            timed_out = False
            try:
                run = subprocess.run(command, cwd=args.source, capture_output=True,
                                     text=True, timeout=45)
                output, code = run.stdout + run.stderr, run.returncode
            except subprocess.TimeoutExpired as error:
                timed_out = True
                output = (error.stdout or b"").decode() + (error.stderr or b"").decode()
                code = None
            checks = {text: output.count(text) >= count for text, count in CASES[name].items()}
            checks["no logged ERROR"] = re.search(r"\bERROR\b", output) is None
            checks["process exited normally"] = code == 0 and not timed_out
            with socket.socket() as probe:
                probe.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
                try:
                    probe.bind(("127.0.0.1", port))
                    checks["listening port released"] = True
                except OSError:
                    checks["listening port released"] = False
            if name == "KeyLogExample":
                # Key lines contain only record labels and generated session material.
                output = re.sub(r"(?m)^.*(?:CLIENT_IV|SERVER_IV|CLIENT_KEY|SERVER_KEY|"
                                r"CLIENT_ENCRYPTION_KEY|SERVER_ENCRYPTION_KEY|"
                                r"CLIENT_SIGNATURE|SERVER_SIGNATURE|"
                                r"CLIENT_ENCRYPT|SERVER_ENCRYPT).*$",
                                "[session key record redacted]", output)
                # Conservatively redact long hexadecimal strings regardless of label.
                output = re.sub(r"(?i)\b[0-9a-f]{32,}\b", "[redacted hex]", output)
            log = args.evidence / (name + ".log")
            log.write_text(output)
            log.chmod(0o600)
            source = module / ("src/main/java/org/eclipse/milo/examples/client/" + name + ".java")
            result = {"example": name, "source_sha256": hashlib.sha256(source.read_bytes()).hexdigest(),
                      "command": command, "exit": code, "timed_out": timed_out,
                      "checks": checks, "pass": all(checks.values()), "log": str(log),
                      "cleanup": "Temporary identity/key directory removed after JVM exit"}
            results.append(result)
            (args.evidence / "results.json").write_text(json.dumps(results, indent=2) + "\n")
            print(name + ": " + ("PASS" if result["pass"] else "FAIL"), flush=True)
    raise SystemExit(not all(r["pass"] for r in results))


if __name__ == "__main__":
    main()
