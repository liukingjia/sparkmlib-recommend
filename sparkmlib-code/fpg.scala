import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.fpm.{ FPGrowth, FPGrowthModel }

object fpg {

  def main(args: Array[String]) {
    //0 ����Spark����
    val conf = new SparkConf().setAppName("fpg")
    val sc = new SparkContext(conf)
    Logger.getRootLogger.setLevel(Level.WARN)

    //1 ��ȡ��������
    val data_path = "/home/jb-huangmeiling/sample_fpgrowth.txt"
    val data = sc.textFile(data_path)
    val examples = data.map(_.split(" ")).cache()

    //2 ����ģ��
    val minSupport = 0.2
    val numPartition = 10
    val model = new FPGrowth().
      setMinSupport(minSupport).
      setNumPartitions(numPartition).
      run(examples)

    //3 ��ӡ���
    println(s"Number of frequent itemsets: ${model.freqItemsets.count()}")
    model.freqItemsets.collect().foreach { itemset =>
      println(itemset.items.mkString("[", ",", "]") + ", " + itemset.freq)
    }

  }
}