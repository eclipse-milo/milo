#!/usr/bin/env python3
"""Copy the Wiki's code samples from this module's sources.

A Wiki code block backed by this module has a marker line directly above it:

    <!-- snippet: snippets/ClientSnippets.java#read -->

The path is relative to the module's org/eclipse/milo/examples/wiki source directory. With
"#name", the block holds the region between "// snippet:name:start" and "// snippet:name:end",
without its common indentation. Without a name, the block holds the whole file after its license
header.

usage: sync_snippets.py [--check] WIKI_DIR
"""

import argparse
import pathlib
import re
import sys
import textwrap

SOURCE_ROOT = (
    pathlib.Path(__file__).resolve().parent.parent
    / "src/main/java/org/eclipse/milo/examples/wiki"
)
MARKER = re.compile(r"^<!-- snippet: (?P<path>[^#\s]+)(?:#(?P<name>[\w-]+))? -->$", re.M)
BLOCK = re.compile(r"```java\n(?P<body>.*?)^```$", re.S | re.M)


def sample(path, name):
    text = (SOURCE_ROOT / path).read_text()
    if name is None:
        return re.sub(r"\A/\*.*?\*/\s*", "", text, flags=re.S)
    region = re.search(
        rf"^[ \t]*// snippet:{re.escape(name)}:start\n(.*?)^[ \t]*// snippet:{re.escape(name)}:end$",
        text,
        re.S | re.M,
    )
    if region is None:
        raise ValueError(f"{path} has no snippet region named {name}")
    return textwrap.dedent(region.group(1)).strip("\n") + "\n"


def sync_page(page, check):
    text = page.read_text()
    stale, parts, position = [], [], 0
    for marker in MARKER.finditer(text):
        label = f"{page.name}: {marker['path']}" + (f"#{marker['name']}" if marker["name"] else "")
        block = BLOCK.match(text, marker.end() + 1)
        if block is None:
            raise ValueError(f"{label} is not followed by a java code block")
        want = sample(marker["path"], marker["name"])
        if block["body"] != want:
            stale.append(label)
        parts += [text[position : block.start("body")], want]
        position = block.end("body")
    updated = "".join(parts) + text[position:]
    if not check and updated != text:
        page.write_text(updated)
    return stale


def main():
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("wiki", type=pathlib.Path, help="a local clone of the Milo Wiki")
    parser.add_argument("--check", action="store_true", help="report stale blocks without editing")
    args = parser.parse_args()

    stale = []
    try:
        for page in sorted(args.wiki.glob("*.md")):
            stale += sync_page(page, args.check)
    except ValueError as e:
        print(f"error: {e}", file=sys.stderr)
        return 2
    for label in stale:
        print(("stale: " if args.check else "updated: ") + label)
    return 1 if args.check and stale else 0


if __name__ == "__main__":
    sys.exit(main())
