# SimpleBedrockModel — Fabric 1.21.1

Unofficial Fabric 1.21.1 port of
[SimpleBedrockModel](https://github.com/MCModderAnchor/SimpleBedrockModel),
based on [Sh1roCu's Fabric port](https://github.com/Sh1roCu/SimpleBedrockModel-Fabric).
Blockfield maintains this fork for use with Superb Warfare.

## Build and checks

Requires JDK 21 (`JAVA_HOME`), Python 3.12+, Node.js 22 and Just 1.57.0.
Tools are cached inside the project; the commands work on Linux and Windows.

```sh
just setup
just check
just build
```

`just format` applies formatting. The mod JAR is written to `build/libs/`.

## Releases

After committing to `main`, run `scripts/bump-fork.sh simplebedrockmodel` from
[blockfield-client](https://github.com/Blockfield/blockfield-client). It creates a
`bfN` tag, waits for the build and pins the released JAR. Passing an existing `bfN`
as the second argument only updates the pin. Do not change the mod version by hand.
Update the server pin as well when the JAR is shared, and use a coordinated server/client release.

## История форка

История переписана 2026-09-27: из неё удалены JAR `libs/touhoulittlemaid-*.jar` и `libs/oculus-*.jar` из истории upstream. Хеши коммитов больше не совпадают с upstream, деревья — совпадают: тег `upstream-1.21.1` содержит то же дерево, что `Sh1roCu/SimpleBedrockModel-Fabric@23b1a23` (ветка `1.21.1`). Обновление с upstream: `git fetch upstream && git rebase --onto upstream/1.21.1 upstream-1.21.1 main`, затем перенести тег `upstream-1.21.1`. Старые клоны: `git fetch --force --tags --prune --prune-tags origin && git reset --hard origin/main` или клонировать заново.

License: [GNU LGPL v3](LICENSE).
