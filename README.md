# Minecraft用

[![GitHub Actions](https://badgen.net/github/checks/Monster2408/repo.mlserver.xyz/master?label=build&icon=github)](https://github.com/Monster2408/repo.mlserver.xyz/actions)
[![GitHub Release](https://badgen.net/github/release/Monster2408/repo.mlserver.xyz/master?label=release&icon=github)](https://github.com/Monster2408/repo.mlserver.xyz/releases/latest)
[![GitHub Issues](https://badgen.net/github/issues/Monster2408/repo.mlserver.xyz/master?label=issues&icon=github)](https://github.com/Monster2408/repo.mlserver.xyz/issues)
[![Twitter](https://badgen.net/twitter/follow/meoto2408?icon=twitter)](https://twitter.com/meoto2408)
[![Discord](https://discord.com/api/guilds/556844677115150366/widget.png)](https://discord.mlserver.xyz)

```xml
<project>
    <repositories>
        <repository>
            <id>github</id>
            <name>NKLab repository</name>
            <url>https://maven.pkg.github.com/Monster2408/repo.mlserver.xyz</url>
        </repository>
    </repositories>
    
    <dependencies>
        <!-- 新バージョン -->
        <dependency>
            <groupId>xyz.mlserver</groupId>
            <artifactId>minecraft</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</project>
```