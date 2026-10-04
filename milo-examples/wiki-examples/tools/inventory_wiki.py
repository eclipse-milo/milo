#!/usr/bin/env python3
"""Inventory Markdown examples and check local Wiki destinations without network access.

Requires markdown-it-py. This reports evidence; it never marks a snippet validated or a
page reviewed. Keep the resulting work ledger outside the public Wiki repository.
"""

import argparse
import hashlib
import json
import re
import subprocess
from pathlib import Path
from urllib.parse import unquote, urlparse

from markdown_it import MarkdownIt


def digest(data):
    return hashlib.sha256(data).hexdigest()


def inventory(path):
    data = path.read_bytes()
    tokens = MarkdownIt("commonmark").parse(data.decode())
    anchors = set(re.findall(r'<a\s+(?:id|name)="([^"]+)"', data.decode()))
    headings = []
    blocks = []
    links = []
    counts = {}
    section = "(page introduction)"
    for i, token in enumerate(tokens):
        if token.type == "heading_open":
            title = tokens[i + 1]
            text = "".join(t.content for t in title.children or []
                           if t.type in ("text", "code_inline"))
            slug = re.sub(r"[^\w\- ]", "", text.lower()).replace(" ", "-")
            n = counts.get(slug, 0)
            counts[slug] = n + 1
            anchor = slug if n == 0 else f"{slug}-{n}"
            anchors.add(anchor)
            section = text
            headings.append({"title": text, "anchor": anchor, "line": token.map[0] + 1})
        if token.type in ("fence", "code_block"):
            blocks.append({"id": f"{path.stem}:{len(blocks) + 1}",
                           "section": section, "line": token.map[0] + 1,
                           "kind": token.type, "language": token.info.strip(),
                           "sha256": digest(token.content.encode()),
                           "content": token.content, "validation_status": "unvalidated"})
        for child in token.children or []:
            if child.type == "link_open":
                links.append(child.attrGet("href"))
    return {"page": path.name, "sha256": digest(data), "headings": headings,
            "anchors": sorted(anchors), "blocks": blocks, "links": links}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("wiki", type=Path)
    parser.add_argument("--source", type=Path)
    args = parser.parse_args()
    pages = [inventory(p) for p in sorted(args.wiki.glob("*.md"))]
    by_name = {Path(p["page"]).stem: p for p in pages}
    errors = []
    source_checks = {}
    for page in pages:
        for link in page["links"]:
            parsed = urlparse(link)
            if not parsed.scheme and not parsed.netloc:
                target = unquote(parsed.path) or Path(page["page"]).stem
                target = target.removesuffix(".md")
                if target not in by_name:
                    errors.append({"page": page["page"], "link": link, "error": "missing page"})
                elif parsed.fragment and unquote(parsed.fragment) not in by_name[target]["anchors"]:
                    errors.append({"page": page["page"], "link": link, "error": "missing anchor"})
            elif args.source and parsed.netloc == "github.com":
                match = re.match(r"/eclipse-milo/milo/blob/([0-9a-f]{40})/(.*)", parsed.path)
                if match and link not in source_checks:
                    revision, path = match.groups()
                    result = subprocess.run(["git", "cat-file", "-e", f"{revision}:{path}"],
                                            cwd=args.source, capture_output=True)
                    source_checks[link] = result.returncode == 0
                    if result.returncode:
                        errors.append({"page": page["page"], "link": link,
                                       "error": "source path absent at revision"})
    print(json.dumps({"pages": pages, "link_errors": errors,
                      "source_checks": source_checks}, indent=2))
    raise SystemExit(bool(errors))


if __name__ == "__main__":
    main()
