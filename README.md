> **Blockfield:** этот форк попадает в сборки так: закоммитить в `main`, затем в `blockfield-client` (и при необходимости в `blockfield-server`) выполнить `scripts/bump-fork.sh simplebedrockmodel`. Скрипт сам ставит тег `bfN`, ждёт сборку и закрепляет релиз. Версию руками не менять.
>
> История переписана 2026-09-27: из неё удалены JAR `libs/touhoulittlemaid-*.jar` и `libs/oculus-*.jar` из истории upstream. Хеши коммитов больше не совпадают с upstream, деревья — совпадают: тег `upstream-1.21.1` содержит то же дерево, что `Sh1roCu/SimpleBedrockModel-Fabric@23b1a23` (ветка `1.21.1`). Обновление с upstream: `git fetch upstream && git rebase --onto upstream/1.21.1 upstream-1.21.1 main`, затем перенести тег `upstream-1.21.1`. Старые клоны: `git fetch --force --tags --prune --prune-tags origin && git reset --hard origin/main` или клонировать заново.

An unofficial Fabric port of [SimpleBedrockModel](https://github.com/MCModderAnchor/SimpleBedrockModel)

## Developer checks

Install Python 3.12+, Node.js 22 and Just 1.57.0, native JDK 21 (`JAVA_HOME`) on Linux or Windows. Quality tools stay in the project cache.

```sh
just setup
just check
just format
```

`just --list` lists supported build and application commands.
