#!/usr/bin/env bash
set -euo pipefail
milo_dir=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.." && pwd)
compiler_dir=$(cd -- "${1:?expected Model Tools checkout and output directory}" && pwd)
output=${2:?expected absolute output directory}
[[ "$output" = /* ]] || { echo 'output must be absolute' >&2; exit 1; }
: "${MILO_REPOSITORY:?set MILO_REPOSITORY to artifacts installed from this Milo branch}"
(
  cd "$compiler_dir"
  mise exec -- ./gradlew --console=plain --refresh-dependencies :codegen:writeRuntimeClasspath
  mise exec -- java --class-path "$(cat codegen/build/runtime-classpath.txt)" \
    "$milo_dir/tools/ns0/GenerateNs0Model.java" \
    uanodeset-core/src/main/resources/com/digitalpetri/opcua/uanodeset/Opc.Ua.NodeSet2.xml \
    "$milo_dir/tools/ns0/model-roots.tsv" "$output"
)
# Include new files as well as tracked edits. Missing tracked files represent deletions.
source_manifest() {
  local checkout=$1 destination=$2
  (
    cd "$checkout"
    while IFS= read -r -d '' source_file; do
      if [[ -f "$source_file" ]]; then sha256sum -- "$source_file"; fi
    done < <(git ls-files --cached --others --exclude-standard -z | sort -zu)
  ) > "$destination"
}
source_manifest "$milo_dir" "$output/milo-source-hashes.txt"
source_manifest "$compiler_dir" "$output/compiler-source-hashes.txt"
{
  printf 'Milo base revision: '; git -C "$milo_dir" rev-parse HEAD
  printf 'Milo source manifest SHA-256: '; sha256sum "$output/milo-source-hashes.txt"
  printf 'Milo tracked diff SHA-256: '; git -C "$milo_dir" diff --binary HEAD | sha256sum
  printf 'Compiler base revision: '; git -C "$compiler_dir" rev-parse HEAD
  printf 'Compiler source manifest SHA-256: '; sha256sum "$output/compiler-source-hashes.txt"
  printf 'Compiler tracked diff SHA-256: '; git -C "$compiler_dir" diff --binary HEAD | sha256sum
  printf 'Milo artifact repository: %s\n' "$MILO_REPOSITORY"
  find "$MILO_REPOSITORY/org/eclipse/milo" -type f \( -name '*.jar' -o -name '*.pom' \) -print0 | sort -z | xargs -0 sha256sum
} > "$output/build-identities.txt"
printf 'Generated sources and provenance: %s\n' "$output"
