import org.apache.spark.sql.SparkSession
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue}
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.fs.{FileSystem, Path}

object Bulkload {
  def main(args: Array[String]) {

    val csvFileName = args{0}
    val period = args{1}
    val tableName = args{2}
    val path = args{3}
    val id = args{4}
    val colFamily = "d"

    val ss = SparkSession.builder().master("local").appName("appName").getOrCreate()

    val df = ss.read
      .option("header", true)
      .option("ignoreLeadingWhiteSpace", true)
      .option("escape","\"")
      .csv(csvFileName)
      .sort(id)

    val sortedCol = df.columns.sorted

    val pairs = df.rdd.flatMap(line => {
      val rowKey = s"$period~${line.getAs(id)}"
      for (i <- sortedCol) yield {
        val value = if (line.getAs[String](i) == null) "" else line.getAs[String](i)
        (new ImmutableBytesWritable(rowKey.getBytes()), new KeyValue(rowKey.getBytes(), colFamily.getBytes(), i.getBytes(), value.getBytes()))
      }
    })

    val conf = HBaseConfiguration.create()
    conf.set(TableOutputFormat.OUTPUT_TABLE, tableName)
    conf.setInt("hbase.mapreduce.bulkload.max.hfiles.perRegion.perFamily", 500)

    val connection = ConnectionFactory.createConnection(conf)
    val table = connection.getTable(TableName.valueOf(tableName))
    val fs = FileSystem.get(conf)

    val job = Job.getInstance(conf)
    job.setMapOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setMapOutputValueClass(classOf[KeyValue])
    HFileOutputFormat2.configureIncrementalLoadMap(job, table)

    pairs.saveAsNewAPIHadoopFile(path, classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat2])
    new LoadIncrementalHFiles(conf).doBulkLoad(new Path(path),connection.getAdmin, table,connection.getRegionLocator(TableName.valueOf(tableName)))
    fs.delete(new Path(path))
    ss.stop()
  }
}