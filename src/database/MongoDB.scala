package database

import java.util.concurrent.TimeUnit

import app.AppConfig
import com.google.inject.{Inject, Singleton}
import com.mongodb.Block
import com.mongodb.connection.ClusterSettings
import models._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.mongodb.scala.bson.codecs.{DEFAULT_CODEC_REGISTRY, Macros}
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCollection, MongoCredential, MongoDatabase, ServerAddress, WriteConcern}
import utils.AppSystem

import scala.collection.JavaConverters._

@Singleton
class MongoDB @Inject()(appConfig: AppConfig) extends AppSystem {

  val METADATA_COLLECTION = "collection"

  val clusterSettings: ClusterSettings = ClusterSettings.builder()
    .hosts(appConfig.mongoServers.map(address => ServerAddress(address)).asJava)
    .build()

  val blockClusterSettings = new Block[ClusterSettings.Builder] {
    override def apply(b: ClusterSettings.Builder): Unit = b.applySettings(clusterSettings)
  }

  val templateModelCodecProvider: CodecProvider = Macros.createCodecProviderIgnoreNone[TestDB]()

  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(), DEFAULT_CODEC_REGISTRY)

  lazy val writeConcernSettings: WriteConcern = WriteConcern.MAJORITY.withWTimeout(2,TimeUnit.MINUTES)

  lazy val mongoCredential = MongoCredential.createCredential(appConfig.mongoUserName, appConfig.mongoDatabase, appConfig.mongoPassword.toCharArray)

  val mongoDefaultClientSettings = MongoClientSettings.builder()
    .applyToClusterSettings(blockClusterSettings)
    .codecRegistry(codecRegistry)
    .retryWrites(true)

  val mongoClientSettings: MongoClientSettings = appConfig.mongoAuthEnabled match {
    case true => mongoDefaultClientSettings
      .credential(mongoCredential)
      .writeConcern(writeConcernSettings)
      .build()
    case false => mongoDefaultClientSettings.build()
  }
  val mongoClient: MongoClient = MongoClient(mongoClientSettings)

  val database: MongoDatabase = mongoClient.getDatabase(appConfig.mongoDatabase).withCodecRegistry(codecRegistry)

  val metadataCollection: MongoCollection[TestDB] = database.getCollection[TestDB](METADATA_COLLECTION)

}
