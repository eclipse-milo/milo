#!/usr/bin/env python3
"""Copy the tutorial POM and complete programs into a standalone validation project."""

import argparse
import shutil
import xml.etree.ElementTree as ET
from pathlib import Path

parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument("destination", type=Path)
args = parser.parse_args()
module = Path(__file__).resolve().parent.parent
source = module / "src/main/java/org/eclipse/milo/examples/wiki"
args.destination.mkdir(parents=True, exist_ok=False)
ET.parse(module / "consumer/pom.xml")
shutil.copy(module / "consumer/pom.xml", args.destination / "pom.xml")
target = args.destination / "src/main/java/org/eclipse/milo/examples/wiki"
target.mkdir(parents=True)
for name in ("FirstClient.java", "FirstServer.java"):
    shutil.copy(source / name, target / name)
print(args.destination)
