## IK-Analyzer-solr

> 源码下载地址:
https://www.oschina.net/news/34059/ikanalyzer-2012-ff-for-lucene-4-0


### 1. solrCloud

>  1.1 启动zookeeper

```shell
docker run --name zk -d -p 2181:2181 zookeeper:3.5
```
>  1.2 solrCloud 模式启动solr

```shell
## solr数据文件目录
mkdir -p /data/solrdata && chown -R 8983:8983 /data/solrdata

## solrCloud 启动solr1
docker run --name=solr1 -d -p 8983:8983 -v /data/solrdata:/data/solrdata \
--privileged=true solr:7.7-alpine \
bash -c '/opt/solr/bin/solr start -f -h 192.168.198.128 -p 8983 -z 192.168.198.128:2181 -t /data/solrdata'

## solrCloud 启动solr2
docker run --name=solr2 -d -p 8984:8984 -v /data/solrdata:/data/solrdata \
--privileged=true solr:7.7-alpine \
bash -c '/opt/solr/bin/solr start -f -h 192.168.198.128 -p 8984 -z 192.168.198.128:2181 -t /data/solrdata'
```

### 2. 配置
>  2.1编辑 /opt/solr/example/files/conf/managed-schema

```xml
     ......
<field name="orderNo" type="string" indexed="true" stored="true"/>
<field name="orderFee" type="string" indexed="true" stored="true"/>
     ......
<fieldType name="text_ik" class="solr.TextField">
    <analyzer type="index" isMaxWordLength="false" class="org.wltea.analyzer.lucene.IKAnalyzer"/>   
    <analyzer type="query" isMaxWordLength="true" class="org.wltea.analyzer.lucene.IKAnalyzer"/>
</fieldType>
```

>  2.2 查看配置

```shell
docker run -it --rm --link zk:zookeeper zookeeper:3.5 zkCli.sh -server zookeeper
```

```shell
[zk: zookeeper(CONNECTED) 0] ls /
[configs, zookeeper]
```

### 3. 中文分词

>   3.1 IKAnalyzer中文分词

  下载地址： 
  https://www.oschina.net/news/34059/ikanalyzer-2012-ff-for-lucene-4-0

>   3.2 拷贝中文分词jar

```shell
docker cp IK-Analyzer-solr-5.5.jar solr:/opt/solr/server/solr-webapp/webapp/WEB-INF/lib
docker cp ext.dic solr:/opt/solr/server/solr-webapp/webapp/WEB-INF/lib
docker cp stopword.dic solr:/opt/solr/server/solr-webapp/webapp/WEB-INF/lib
docker cp IKAnalyzer.cfg.xml solr:/opt/solr/server/solr-webapp/webapp/WEB-INF/lib
```
>  3.3 授权

```shell
docker exec -u root -it solr chown -R solr:solr /opt/solr/server/solr-webapp/webapp/WEB-INF/lib/
```


https://www.cnblogs.com/shoufeng/p/10615693.html
https://www.w3cschool.cn/solr_doc/solr_doc-t3642fkr.html

### 4. 创建collection

```
/solr/admin/collections?action=CREATE&name=collection1&router.name=implicit&shards=shard1&collection.configName=_defuat
```

### 5.创建分片 shard

>  动态新增shard,需要collection创建时指定 router=implicit

```
/solr/admin/collections?action=CREATESHARD&shard=shard2&collection=collection1
```


### 6.创建副本 Replica

```
/solr/admin/collections?action=ADDREPLICA&collection=test2&shard=shard2&node=192.167.1.2:8983_solr
```
