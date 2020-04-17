

代码演示customer调用 Eureka注册实例的接口
微服务：

Spring Cloud提供了微服务解决的一整套方案，而Eureka是其重要组件，所以先要了解什么是“微服务”。
在大型系统架构中，会拆分多个子系统。这些系统往往都有这几个功能：提供接口，调用接口，以及该子系统自身的业务功能。
这样的一个子系统就称为一个“微服务”。

Eureka的管理：

基于以上概念，使用Eureka管理时会具备几个特性：

→服务需要有一个统一的名称（或服务ID）并且是唯一标识，以便于接口调用时各个接口的区分。
并且需要将其注册到Eureka Server中，其他服务调用该接口时，也是根据这个唯一标识来获取。

→服务下有多个实例，每个实例也有一个自己的唯一实例ID。因为它们各自有自己的基础信息如：不同的IP。
所以它们的信息也需要注册到Eureka Server中，其他服务调用它们的服务接口时，可以查看到多个该服务的实例信息，
根据负载策略提供某个实例的调用信息后，调用者根据信息直接调用该实例。

Eureka Server：

提供服务注册：各个微服务启动时，会通过Eureka Client向Eureka Server进行注册自己的信息（例如服务信息和网络信息），Eureka Server会存储该服务的信息。

提供服务信息提供：服务消费者在调用服务时，本地Eureka Client没有的情况下，会到Eureka Server拉取信息。

提供服务管理：通过Eureka Client的Cancel、心跳监控、renew等方式来维护该服务提供的信息以确保该服务可用以及服务的更新。

信息同步：每个Eureka Server同时也是Eureka Client，多个Eureka Server之间通过P2P复制的方式完成服务注册表的同步。同步时，被同步信息不会同步出去。也就是说有3个Eureka Server，Server1有新的服务信息时，同步到Server2后，Server2和Server3同步时，Server2不会把从Server1那里同步到的信息同步给Server3，只能由Server1自己同步给Server3。

每个可用区有一个Eureka集群，并且每个可用区至少有一个eureka服务器来处理区内故障。为了实现高可用，一般一个可用区中由三个Eureka Server组成。

Eureka Client

Eureka Client是一个Java客户端，用于简化与Eureka Server的交互。并且管理当前微服务，同时为当前的微服务提供服务提供者信息。

Eureka Client会拉取、更新和缓存Eureka Server中的信息。即使所有的Eureka Server节点都宕掉，服务消费者依然可以使用缓存中的信息找到服务提供者。

Eureka Client在微服务启动后，会周期性地向Eureka Server发送心跳（默认周期为30秒）以续约自己的信息。如果Eureka Server在一定时间内没有接收到某个微服务节点的心跳，Eureka Server将会注销该微服务节点（默认90秒）。

Eureka Client包含服务提供者Applicaton Service和服务消费者Application Client

Applicaton Service：服务提供者，提供服务给别个调用。

Application Client：服务消费者，调用别个提供的服务。

往往大多数服务本身既是服务提供者，也是服务消费者。

其它动作：

Register：服务注册

当Eureka客户端向Eureka Server注册时，它提供自身的元数据，比如IP地址、端口，运行状况指示符URL，主页等。

Renew：服务续约

Eureka Client会每隔30秒发送一次心跳来续约。 通过续约来告知Eureka Server该Eureka客户仍然存在，没有出现问题。 正常情况下，如果Eureka Server在90秒没有收到Eureka客户的续约，它会将实例从其注册表中删除。 建议不要更改续约间隔。

Fetch Registries：获取注册列表信息

Eureka客户端从服务器获取注册表信息，并将其缓存在本地。客户端会使用该信息查找其他服务，从而进行远程调用。该注册列表信息定期（每30秒钟）更新一次。每次返回注册列表信息可能与Eureka客户端的缓存信息不同， Eureka客户端自动处理。如果由于某种原因导致注册列表信息不能及时匹配，Eureka客户端则会重新获取整个注册表信息。 Eureka服务器缓存注册列表信息，整个注册表以及每个应用程序的信息进行了压缩，压缩内容和没有压缩的内容完全相同。Eureka客户端和Eureka 服务器可以使用JSON / XML格式进行通讯。在默认的情况下Eureka客户端使用压缩JSON格式来获取注册列表的信息。

Cancel：服务下线

Eureka客户端在程序关闭时向Eureka服务器发送取消请求。 发送请求后，该客户端实例信息将从服务器的实例注册表中删除。该下线请求不会自动完成，它需要调用以下内容：

DiscoveryManager.getInstance().shutdownComponent()；

Eviction 服务剔除

在默认的情况下，当Eureka客户端连续90秒没有向Eureka服务器发送服务续约，即心跳，Eureka服务器会将该服务实例从服务注册列表删除，即服务剔除。
如果Eureka Server在一定时间内（默认90秒）没有接收到某个微服务实例的心跳，
Eureka Server将会移除该实例。但是当网络分区故障发生时，微服务与Eureka Server之间无法正常通信，
而微服务本身是正常运行的，此时不应该移除这个微服务，所以引入了自我保护机制。


优化底层请求策略
连接管理
	PoolingHttpClientConnectionManager 连接池管理
	KeepAlive策略
超时设置
	connectTimeout/readTimeout
SSL校验
	证书检查策略
