# Android Framework 知识点整理



## zygote进程



zygote进程，是由Init进程通过解析init.rc文件fork生成的。zygote主要包括：

- 加载zygoteInit类型，注册Zygote服务端socket套接字。
- 加载虚拟机
- preloadClass
- preloadResources


System server进程是由Zygote进程fork而来的，System server是Zygote孵化的第一个进程，System server负责启动和管理整个java framework，包含AcitviManager,PowerManager等服务。

Medis Server进程，是由init进程fork而来，负责启动和管理真个c++ framework,包含AudioFinger,Camera Service等服务。

------



## App层

zygote第一个孵化出的第一个app进程是launcher,这就是用户看到的桌面。

zygote还会创建broswer,phone ,email等app进程，每个app至少运行在一个进程上。

------



## Syscall && JNI

native和kernel之间有一层（syscall）层，见linux系统调用(Syscall)原理；

java层和Native（c/c++）层之间的纽带JNI,见Android JNI 原理分析。



------

通信方式

Linux ipc通信方式，linux有管道，消息队列，共享内存，套接字，信号量，信号这些ipc机制。

Android 还有额为的通信方式，比如binder,handler.

### 为何Android要采用Binder作为ipc机制

1. 管道在创建的时候会分配一个page大小的内存，缓存区大小比较有限；
2. 消息队列：信息复制两次，额外的cpu消耗；不适合频繁或信息量大的通信；
3. 共享内存：无需复制，共享内存直接附加到进程的虚拟地址空间，速度快；但进程间的同步问题操作系统无法实现，必须各进程利用同步工具解决；
4. 套接字：作为更通用的接口，传输效率低，主要用于不同机器或跨网络的通信；
5. 信号量：常用的一中锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段。
6. 信号：不适用与信息交换，更适用于中断控制，比如非法内存访问，杀死某个进程等；



## AMS

AMS启动

- 在System server进程中启动；
- SystemServer.java\#startBootstrapServices()；
- 创建Ams实例对象，创建Android Runtime,ActivityThread和Context对象；
- setSysetmProcess：注册Ams，meminfo、cpuinfo等服务到ServiceManager;
- installSystemProviders，加载settingProvider;
- 启动SystemUiService,在调用一系列服务的systemReady()方法；
- 发布binder服务

| 服务名      | 类名                   | 功能           |
| :---------- | ---------------------- | -------------- |
| activity    | ActivityManagerService | AMS            |
| procstats   | ProcessStatsService    | 进程统计       |
| meminfo     | MemBinder              | 内存           |
| gfxinfo     | GraphicsBinder         | 图像信息       |
| dbinfo      | DbBinder               | 数据库         |
| cpuinfo     | CpuBinder              | CPU            |
| permission  | PermissionController   | 权限           |
| processinfo | ProcessInfoService     | 进程服务       |
| usagestats  | UsageStatsService      | 应用的使用情况 |



![img](https://github.com/checkming/AndroidAdvancedNotes/raw/main/FrameWork/AMS/img/android-arch1.png)

