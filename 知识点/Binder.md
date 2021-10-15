# Binder

Android应用程序是由Activity、Service、Broascast Receiver 和 Content provider 四大组件的一个或多个组成的。有时这些组件运行在同一进程，有时候运行在不同的进程。这些进程间的通信就依赖于Binder的IPC机制。不仅如此Android系统对应用层提供的各种服务如：ActivityManagerService、PackageManagerService等都是基于Binder IPC 机制来是实现的。Binder机制在Android的位置非常重要。

#### 在Android中为什么使用Binder?

性能：Socket作为通信接口，传输效率低，开销大，主要用在跨网络的进程通信和本机上进程通信等低速通信。消息队列、管道采用存储-转发的方式，即数据先发现的缓存拷贝到内核开辟的缓存区中，然后在从内核缓冲区拷贝到到接收缓冲中，至少有两次拷贝过程。共享内存虽然无需拷贝，但控制复杂，难以使用。Binder只需一次数据拷贝，性能仅次于共享内存。

稳定性：Binder基于C/S架构，客户端（client）有什么需求就丢给服务端（Server）去完成，架构清晰，职责明确又相互独立，自然稳定性好。共享内存虽无需拷贝，但是控制复杂，难以使用。从稳定性的角度讲，Binder机制是优于共享内存的。

安全性：传统的IPC只能依赖于上层协议来确保。传统的IPC无法获得对方可靠的进程用户ID/进程ID，从未无法鉴别对方身份。Android为每个安装好的APP分配了自己的UID，故而进程的UID是鉴别进程身份的重要标志。同时Binder既支持实名Binder支持匿名Binder,安全性高。



##### Binder的定义

从进程间通信的角度看，Binder 是一种进程间通信的机制； 从 Server 进程的角度看，Binder 指的是 Server 中的 Binder 实体对象； 从 Client 进程的角度看，Binder 指的是对 Binder 代理对象，是 Binder 实体对象的一个远程代理 从传输过程的角度看，Binder 是一个可以跨进程传输的对象；Binder 驱动会对这个跨越进程边界的对象对一点点特殊处理，自动完成代理对象和本地对象之间的转换。