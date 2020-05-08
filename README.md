# EOS Blockchain Java SDK

> eosio java sdk with eos rpc-api



## dependency

### release version
[![](https://jitpack.io/v/CarilKing/jeos.svg)](https://jitpack.io/#CarilKing/jeos)

```xml
	<dependency>
	    <groupId>com.github.CarilKing</groupId>
	    <artifactId>jeos</artifactId>
	    <version>v0.9.17</version>
	</dependency>

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Gradle

> compile group: 'io.jafka', name: 'jeos', version: '0.9.15'


### latest snapshot version

```xml
<repositories>
  <repository>
    <id>oss.sonatype.org-snapshot</id>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
      <enabled>false</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>io.jafka</groupId>
    <artifactId>jeos</artifactId>
    <version>1.0-SNAPSHOT</version>
  </dependency>
</dependencies>

```

## Java samples with RPC

- [Transfer EOS with java](https://gist.github.com/adyliu/b35ec8551c05f82a1d7307395d4910da)
- [Create account with java](https://gist.github.com/adyliu/6d30650cf2f6d0a703d5b547db484d31)

## Java samples without RPC (Local Only)

- [Create EOS Key with java without RPC](https://gist.github.com/adyliu/63d93895d07678d3d80a52dfbcb18976)
- [Sign transaction offline](https://gist.github.com/adyliu/492503b94d0306371298f24e15481da4)

## RPC-API

- [create eos account with rpc-api](https://github.com/adyliu/jeos/wiki/create-eos-account-with-rpc-api)
- [create-eos-account-with-rpc-api.pdf](https://github.com/adyliu/jeos/wiki/create-eos-account-with-rpc-api.pdf)
