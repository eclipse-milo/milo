#!/usr/bin/env python3
"""Check the Wiki diagnostics CurrentTime Read with and without its matching key log.

Requires tshark with OPC UA decryption support (checked with 4.4.19). Paths are
interpreted by tshark, including when --tshark-prefix runs it in a container.
Only selected protocol fields are requested; key material is never printed.
"""

import argparse
import csv
import io
import json
import subprocess


FIELDS = [
    "frame.number", "tcp.stream", "tcp.srcport", "tcp.dstport",
    "opcua.servicenodeid.numeric", "opcua.RequestHandle", "opcua.ServiceResult",
    "opcua.nodeid.numeric", "opcua.datavalue.mask", "opcua.StatusCode",
    "opcua.variant.has_value", "opcua.DateTime", "_ws.malformed", "_ws.col.info",
]


def run(command):
    result = subprocess.run(command, capture_output=True, text=True, timeout=120)
    if result.returncode:
        raise RuntimeError(f"tshark exited with status {result.returncode}")
    return result.stdout


def require(condition, message):
    if not condition:
        raise RuntimeError(message)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--capture", required=True)
    parser.add_argument("--keylog", required=True)
    parser.add_argument("--port", type=int, default=12689)
    parser.add_argument("--tshark-prefix", nargs="+", default=["tshark"])
    args = parser.parse_args()
    require(1 <= args.port <= 65535, "The server port must be in 1..65535")
    commands = []

    def decode(keylog):
        # Decode As alone does not set the port preference used to select server keys.
        command = args.tshark_prefix + [
            "-r", args.capture, "-o", f"opcua.tcp.port:{args.port}",
            "-o", f"opcua.debug_file:{keylog}", "-Y", "opcua",
            "-T", "fields", "-E", "header=y",
        ]
        for field in FIELDS:
            command.extend(["-e", field])
        commands.append(command)
        return list(csv.DictReader(io.StringIO(run(command)), delimiter="\t"))

    with_keys = decode(args.keylog)
    without_keys = decode("")
    require(with_keys, "No OPC UA packets were decoded")
    require(not any(row["_ws.malformed"] for row in with_keys),
            "The keyed decode contains a malformed OPC UA packet")
    require(not any(row["_ws.malformed"] for row in without_keys),
            "The no-key control contains a malformed OPC UA packet")
    requests = [row for row in with_keys
                if row["opcua.servicenodeid.numeric"] == "631"
                and "2258" in row["opcua.nodeid.numeric"].split(",")
                and row["tcp.dstport"] == str(args.port)
                and "(decrypted)" in row["_ws.col.info"]]
    require(len(requests) == 1, "Expected one decrypted CurrentTime ReadRequest")
    request = requests[0]
    responses = [row for row in with_keys
                 if row["opcua.servicenodeid.numeric"] == "634"
                 and row["tcp.srcport"] == str(args.port)
                 and row["tcp.stream"] == request["tcp.stream"]
                 and row["opcua.RequestHandle"] == request["opcua.RequestHandle"]
                 and "(decrypted)" in row["_ws.col.info"]]
    require(len(responses) == 1, "Expected the matching decrypted ReadResponse")
    response = responses[0]
    require(response["opcua.ServiceResult"] == "0x00000000",
            "The ReadResponse service result is not Good")
    mask_text = response["opcua.datavalue.mask"]
    require(mask_text and "," not in mask_text, "Expected one DataValue result")
    mask = int(mask_text, 0)
    require(mask & 1 and response["opcua.variant.has_value"] == "0x0d"
            and response["opcua.DateTime"], "Expected a DateTime value")
    # An omitted DataValue StatusCode has the OPC UA Binary default of Good.
    require(not (mask & 2) or response["opcua.StatusCode"] == "0x00000000",
            "The DataValue operation status is not Good")
    controls = {row["frame.number"]: row for row in without_keys}
    for row in (request, response):
        control = controls.get(row["frame.number"])
        require(control is not None and not control["opcua.servicenodeid.numeric"]
                and "(encrypted)" in control["_ws.col.info"],
                "The no-key control unexpectedly exposes the service body")
    version_command = args.tshark_prefix + ["--version"]
    version = run(version_command).splitlines()[0]
    print(json.dumps({
        "analyzer": version,
        "request_frame": int(request["frame.number"]),
        "response_frame": int(response["frame.number"]),
        "request_handle": int(request["opcua.RequestHandle"]),
        "service_result": "Good",
        "operation_status": "Good" if mask & 2 else "Good (omitted default)",
        "value_type": "DateTime",
        "without_keys": "matching request and response remain encrypted",
        "malformed_packets": 0,
        "commands": commands + [version_command],
    }, indent=2))


if __name__ == "__main__":
    main()
