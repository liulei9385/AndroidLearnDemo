# Kotlin1.6新增了哪些内容

**kotlin1.6 新的地方**

* 枚举、密封类、bool 类型 在 WHEN 语句中必须详尽的罗列所有取值。
* suspend 函数作为参数类型以及普通函数到 suspend 函数的转换。
* 改进了递归泛型类型的类型推断﻿。
* 支持对类类型参数的注释。
* 优化在给定 KProperty 实例上调用 get/set 的委托属性﻿。

```
class Box<T> {
    private var impl: T = ...

    var content: T by ::impl
}
```