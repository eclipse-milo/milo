#!/usr/bin/env python3
"""Copy the Wiki's code samples from this module's sources.

A Wiki code block backed by this module has a marker line directly above it:

    <!-- snippet: snippets/ClientSnippets.java#read -->

The path is relative to the module's org/eclipse/milo/examples/wiki source directory. With
"#name", the block holds the region between "// snippet:name:start" and "// snippet:name:end",
without its common indentation. Without a name, the block holds the whole file after its license
header, minus the "// snippet:name:start" and "// snippet:name:end" lines, so a page can show a
file whole while other pages show its regions.

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
REGION_MARKER_LINE = re.compile(r"^[ \t]*// snippet:[\w-]+:(?:start|end)\n", re.M)


def source_file(path):
    file = (SOURCE_ROOT / path).resolve()
    if not file.is_relative_to(SOURCE_ROOT.resolve()):
        raise ValueError(f"{path} is outside the snippet source directory")
    return file


def sample(path, name):
    text = source_file(path).read_text()
    if name is None:
        body = re.sub(r"\A/\*.*?\*/\s*", "", text, flags=re.S)
        return REGION_MARKER_LINE.sub("", body)
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

    pages = sorted(args.wiki.glob("*.md"))
    if not pages:
        print(f"error: {args.wiki} contains no Wiki pages", file=sys.stderr)
        return 2

    stale = []
    try:
        for page in pages:
            stale += sync_page(page, args.check)
    except ValueError as e:
        print(f"error: {e}", file=sys.stderr)
        return 2
    for label in stale:
        print(("stale: " if args.check else "updated: ") + label)
    return 1 if args.check and stale else 0


if __name__ == "__main__":
    sys.exit(main())
