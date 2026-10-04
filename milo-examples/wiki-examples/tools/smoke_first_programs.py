#!/usr/bin/env python3
"""Run both tutorial entry points and assert their output, exit status, and port cleanup.

Build the project and target/classpath.txt first. This script invokes Java only; Maven
execution belongs to the repository's Maven command runner.
"""

import argparse
import json
import os
import re
import socket
import subprocess
import time
from pathlib import Path

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument("project", type=Path)
parser.add_argument("evidence", type=Path)
parser.add_argument("--wiki", type=Path,
                    help="Execute the exact shell blocks from these local tutorial drafts")
args = parser.parse_args()
project = args.project.resolve()
args.evidence.mkdir(parents=True, exist_ok=True)
classpath = str(project / "target/classes") + ":" + (project / "target/classpath.txt").read_text().strip()
java = ["mise", "exec", "--", "java", "-cp", classpath]
commands = {name: java + ["org.eclipse.milo.examples.wiki." + name]
            for name in ("FirstServer", "FirstClient")}
environment = None
cwd = None
if args.wiki:
    # Select the repository's JDK before moving into the standalone consumer directory.
    selected_java = subprocess.run(["mise", "which", "java"], check=True,
                                   capture_output=True, text=True).stdout.strip()
    environment = dict(os.environ)
    environment["PATH"] = str(Path(selected_java).parent) + os.pathsep + environment["PATH"]
    cwd = project
    for name, page in (("FirstServer", "First-Server.md"), ("FirstClient", "First-Client.md")):
        text = (args.wiki / page).read_text()
        shell_blocks = re.findall(r"```sh\n(.*?)```", text, re.S)
        if len(shell_blocks) != 1:
            raise ValueError(f"Expected exactly one shell block in {page}")
        commands[name] = ["bash", "-c", shell_blocks[0]]
log = args.evidence / "server.log"
with log.open("w") as output:
    server = subprocess.Popen(commands["FirstServer"], cwd=cwd, env=environment,
                              stdin=subprocess.PIPE, stdout=output,
                              stderr=subprocess.STDOUT, text=True)
    try:
        deadline = time.monotonic() + 25
        while "Listening on opc.tcp://127.0.0.1:12686/wiki" not in log.read_text():
            if server.poll() is not None:
                raise RuntimeError("Server exited before readiness; inspect server.log")
            if time.monotonic() >= deadline:
                raise TimeoutError("Server readiness timed out")
            time.sleep(0.1)
        client = subprocess.run(commands["FirstClient"], cwd=cwd, env=environment,
                                capture_output=True, text=True, timeout=30)
        (args.evidence / "client.log").write_text(client.stdout + client.stderr)
        if client.returncode != 0 or "Temperature: 21.5" not in client.stdout:
            raise AssertionError("Client failed or returned unexpected output; inspect client.log")
        server.communicate("\n", timeout=20)
        if server.returncode != 0:
            raise AssertionError(f"Server exited with {server.returncode}")
        with socket.socket() as rebound:
            rebound.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            rebound.bind(("127.0.0.1", 12686))
        result = {"project": str(project), "java_commands": list(commands.values()),
                  "exact_wiki_shell_blocks": bool(args.wiki),
                  "client_exit": client.returncode, "server_exit": server.returncode,
                  "output": "Temperature: 21.5", "server_port_released": True}
        (args.evidence / "result.json").write_text(json.dumps(result, indent=2) + "\n")
        print("PASS: Temperature: 21.5; both processes exited 0; server port released")
    finally:
        if server.poll() is None:
            server.kill()
            server.wait(timeout=5)
