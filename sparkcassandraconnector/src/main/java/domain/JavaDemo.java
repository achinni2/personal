package domain;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

public class JavaDemo implements Serializable {
    private transient SparkConf conf;

    private JavaDemo(SparkConf conf) {
        this.conf = conf;
    }

    private void run() {
        JavaSparkContext sc = new JavaSparkContext(conf);
        generateData(sc);
        sc.stop();
    }

    private void generateData(JavaSparkContext sc) {
        CassandraConnector connector = CassandraConnector.apply(sc.getConf());
            Session session = connector.openSession();
            session.execute("DROP KEYSPACE IF EXISTS java_api");
            session.execute("CREATE KEYSPACE java_api WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");
            session.execute("CREATE TABLE java_api.products (id INT PRIMARY KEY, name TEXT, parents LIST<INT>)");
            session.execute("CREATE TABLE java_api.sales (id UUID PRIMARY KEY, product INT, price DECIMAL)");
            session.execute("CREATE TABLE java_api.summaries (product INT PRIMARY KEY, summary DECIMAL)");

            session.close();

        List<Product> products = Arrays.asList(
                new Product(0, "All products", Collections.<Integer>emptyList()),
                new Product(1, "Product A", Arrays.asList(0)),
                new Product(4, "Product A1", Arrays.asList(0, 1)),
                new Product(5, "Product A2", Arrays.asList(0, 1)),
                new Product(2, "Product B", Arrays.asList(0)),
                new Product(6, "Product B1", Arrays.asList(0, 2)),
                new Product(7, "Product B2", Arrays.asList(0, 2)),
                new Product(3, "Product C", Arrays.asList(0)),
                new Product(8, "Product C1", Arrays.asList(0, 3)),
                new Product(9, "Product C2", Arrays.asList(0, 3))
        );

        JavaRDD<Product> productsRDD = sc.parallelize(products);
        javaFunctions(sc).cassandraTable("java_api","products",mapRowTo(Product.class));

        JavaRDD<Sale> salesRDD = productsRDD.filter(new org.apache.spark.api.java.function.Function<Product, Boolean>() {
            public Boolean call(Product product) throws Exception {
                return product.getParents().size() == 2;
            }
        }).flatMap(new FlatMapFunction<Product, Sale>() {
            public Iterator<Sale> call(Product product) throws Exception {
                Random random = new Random();
                List<Sale> sales = new ArrayList<Sale>(1000);
                for (int i = 0; i < 1000; i++) {
                    sales.add(new Sale(UUID.randomUUID(), product.getId(), BigDecimal.valueOf(random.nextDouble())));
                }
                return sales.iterator();
            }
        });

        javaFunctions(salesRDD).writerBuilder("java_api","sales",mapToRow(Sale.class)).saveToCassandra();

        JavaPairRDD<Integer, Product> pRDD = javaFunctions(sc)
                .cassandraTable("java_api","products",mapRowTo(Product.class))
                .keyBy(new org.apache.spark.api.java.function.Function<Product,Integer>() {
                    public Integer call(Product product) throws Exception {
                        return product.getId();
                    }
                });

        JavaPairRDD<Integer, Sale> sRDD = javaFunctions(sc)
                .cassandraTable("java_api","sales",mapRowTo(Sale.class))
                .keyBy(new org.apache.spark.api.java.function.Function<Sale,Integer>() {
                    public Integer call(Sale sale) throws Exception {
                        return sale.getProduct();
                    }
                });

        JavaPairRDD<Integer, Tuple2<Sale,Product>> joinedRDD = sRDD.join(pRDD);

        JavaPairRDD<Integer,BigDecimal> allSalesRDD = joinedRDD.flatMapToPair(new PairFlatMapFunction<Tuple2<Integer, Tuple2<Sale, Product>>, Integer, BigDecimal>() {
            public Iterator<Tuple2<Integer, BigDecimal>> call(Tuple2<Integer, Tuple2<Sale, Product>> input) throws Exception {
                Tuple2<Sale, Product> saleWithProduct = input._2();
                List<Tuple2<Integer, BigDecimal>> allSales = new ArrayList<Tuple2<Integer, BigDecimal>>(saleWithProduct._2().getParents().size() + 1);
                allSales.add(new Tuple2<Integer, BigDecimal>(saleWithProduct._1().getProduct(), saleWithProduct._1().getPrice()));
                for (Integer parentProduct : saleWithProduct._2().getParents()) {
                    allSales.add(new Tuple2<Integer, BigDecimal>(parentProduct, saleWithProduct._1().getPrice()));
                }
                return allSales.iterator();
            }
        });

        JavaRDD<Summary> summariesRDD = allSalesRDD.reduceByKey(new Function2<BigDecimal, BigDecimal, BigDecimal>() {
            public BigDecimal call(BigDecimal v1, BigDecimal v2) throws Exception {
                return v1.add(v2);
            }
        }).map(new org.apache.spark.api.java.function.Function<Tuple2<Integer, BigDecimal>, Summary>() {
            public Summary call(Tuple2<Integer, BigDecimal> input) throws Exception {
                return new Summary(input._1(), input._2());
            }
        });

        javaFunctions(summariesRDD).writerBuilder("java_api","summaries",mapToRow(Summary.class)).saveToCassandra();

        JavaPairRDD<Integer, Summary> summariesRdd = javaFunctions(sc)
                .cassandraTable("java_api", "summaries", mapRowTo(Summary.class))
                .keyBy(new org.apache.spark.api.java.function.Function<Summary, Integer>() {
                    public Integer call(Summary summary) throws Exception {
                        return summary.getProduct();
                    }
                });

        JavaPairRDD<Integer, Product> productsRdd = javaFunctions(sc)
                .cassandraTable("java_api", "products", mapRowTo(Product.class))
                .keyBy(new org.apache.spark.api.java.function.Function<Product, Integer>() {
                    public Integer call(Product product) throws Exception {
                        return product.getId();
                    }
                });

        Iterator<Tuple2<Product, Optional<Summary>>> results = productsRdd.leftOuterJoin(summariesRdd).values().toLocalIterator();

        if (results.hasNext()) {
            System.out.println(results.next());
        }


    }
    

    public static void main(String[] args) {

        SparkConf conf = new SparkConf();
        conf.setAppName("Java API demo");
        conf.setMaster("local[4]");
        conf.set("spark.cassandra.connection.host", "127.0.0.1");
        conf.set("spark.cassandra.connection.connections_per_executor_max","10");

        JavaDemo app = new JavaDemo(conf);
        app.run();
    }
}
