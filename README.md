# sparkload
An app that bulk loads data into hbase via spark job

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)]() [![Dependency Status](https://www.versioneye.com/user/projects/596f195e6725bd0027f25e93/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/596f195e6725bd0027f25e93)

## Environment Setup

* Java 8 or higher (https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html)
* SBT (http://www.scala-sbt.org/)

```shell
brew install sbt
```

## Running

To run the `sparkload`, run the following:

``` shell
start-hbase.sh
${sparksubmitdir}/spark-submit \
    --class Bulkload \
    target/scala-2.11/spark_loader_2.11-1.0.jar \
    csv filename \
    period of data being loaded (YYYYMM) \
    target hbase tablename \
    target directory for HFile \
    name of column in csv file to be used as the rowkey

/usr/local/Cellar/apache-spark/2.1.0/bin/spark-submit
```

## Package

To package the code without dependencies

``` shell
sbt package
```

## Assembly

To assemble the code + all dependancies into a fat .jar, run the following:

```shell
sbt assembly
```

## Deployment

After running the following command:
 
```shell
sbt clean compile "project api" universal:packageBin
```

## Testing

## Contributing

See [CONTRIBUTING](CONTRIBUTING.md) for details.

## License

Copyright ©‎ 2017, Office for National Statistics (https://www.ons.gov.uk)

Released under MIT license, see [LICENSE](LICENSE) for details.
