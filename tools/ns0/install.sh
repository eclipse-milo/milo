#!/usr/bin/env bash
set -euo pipefail
milo_dir=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.." && pwd)
output=${1:?expected generated output directory}
# unit:package pairs; the package tail also selects the owning sdk-<module> source root.
units=(
  'methods:core/model/methods'
  'server:server/model'
  'server:server/model/objects'
  'server:server/model/variables'
  'client:client/model'
  'client:client/model/objects'
  'client:client/model/variables'
)
# Check every unit and source package before replacing any installed files.
for unit in methods server client; do
  [[ -f "$output/$unit/.opcua-codegen-manifest" ]] || {
    echo "missing compiler ownership manifest: $unit" >&2; exit 1;
  }
done
for entry in "${units[@]}"; do
  [[ -f "$output/${entry%%:*}/org/eclipse/milo/opcua/sdk/${entry#*:}/package-info.java" ]] || {
    echo "missing generated package: ${entry#*:}" >&2; exit 1;
  }
done
# The client compiler binds to Milo's intrinsic client base classes instead of emitting them.
preserved() {
  case $1 in
    client/model/objects) printf '%s\n' BaseObjectType.java BaseObjectTypeNode.java ;;
    client/model/variables)
      printf '%s\n' BaseVariableType.java BaseVariableTypeNode.java \
        BaseDataVariableType.java BaseDataVariableTypeNode.java ;;
  esac
}
for entry in "${units[@]}"; do
  package=${entry#*:}
  module=${package%%/*}
  source_dir="$output/${entry%%:*}/org/eclipse/milo/opcua/sdk/$package"
  target_dir="$milo_dir/opc-ua-sdk/sdk-$module/src/main/java/org/eclipse/milo/opcua/sdk/$package"
  mkdir -p "$target_dir"
  keep=()
  while IFS= read -r file; do keep+=(! -name "$file"); done < <(preserved "$package")
  # Apart from preserved intrinsic bases these packages are entirely generated, including the
  # server's three base interface and node pairs.
  find "$target_dir" -maxdepth 1 -type f -name '*.java' "${keep[@]}" -delete
  cp -- "$source_dir"/*.java "$target_dir/"
done
printf '%s\n' 'Installed ns0 server and client sources and shared descriptors. Run the Milo formatter and verification.'
