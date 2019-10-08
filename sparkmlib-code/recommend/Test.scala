package recommend

import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.rdd.RDD

object ItemCF {
  def main(args: Array[String]) {

    //0 ����Spark����
    val conf = new SparkConf().setAppName("ItemCF")
    val sc = new SparkContext(conf)
    Logger.getRootLogger.setLevel(Level.WARN)

    //1 ��ȡ��������
    val data_path = "/home/jb-huangmeiling/sample_itemcf2.txt"
    val data = sc.textFile(data_path)
    val userdata = data.map(_.split(",")).map(f => (ItemPref(f(0), f(1), f(2).toDouble))).cache()

    //2 ����ģ��
    val mysimil = new ItemSimilarity()
    val simil_rdd1 = mysimil.Similarity(userdata, "cooccurrence")
    val recommd = new RecommendedItem
    val recommd_rdd1 = recommd.Recommend(simil_rdd1, userdata, 30)

    //3 ��ӡ���
    println(s"��Ʒ���ƶȾ���: ${simil_rdd1.count()}")
    simil_rdd1.collect().foreach { ItemSimi =>
      println(ItemSimi.itemid1 + ", " + ItemSimi.itemid2 + ", " + ItemSimi.similar)
    }
    println(s"�Ñ��Ƽ��б�: ${recommd_rdd1.count()}")
    recommd_rdd1.collect().foreach { UserRecomm =>
      println(UserRecomm.userid + ", " + UserRecomm.itemid + ", " + UserRecomm.pref)
    }    

  }
}
