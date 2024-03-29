# gradle 一些语法归纳

忽略某些so包

```bash
android {
    //...
    packagingOptions {
        exclude "lib/arm64-v8a/**"
        exclude "lib/x86_64/**"
    }
    //...
}
```

运行gradle dependencyInsight会显示匹配你输入的一个特定依赖的详细信息。



如下是一个例子：

```powershell
gradlew.bat startActivity:dependencyInsight --dependency appcompat --configuration androidTestCompile
#gradle3.0
gradlew.bat :app:dependencyInsight --configuration debugImplementation --dependency retrofit
gradlew.bat :commonModel:dependencyInsight --configuration debugImplementation --dependency coroutines
./gradlew.bat :app:dependencyInsight --configuration compile --dependency icu4j
```



这个任务在了解一个依赖关系，找出某个依赖来自于哪里以及为什么要选择某个版本是非常有用的，你可以查看DependencyInsightReportTask类的API文档来获取更多的信息。

内建的dependencyInsight 任务是help任务组的一部分。这个任务需要配置依赖和配置。输出报表依赖于你描述的某个依赖的某个配置。如果使用了Java相关的插件。
dependencyInsight 预先配置的是'compile' 配置，因为一般情况下他是我们感兴趣的编译依赖。你应该通过在命令行使用 '--dependency'来描述你感兴趣的依赖。
如果你不喜欢默认的配置你可以通过'--configuration' 选项来选择。查看 DependencyInsightReportTask 类的API来获取更多相关的信息。

#build.gradle 修改生成的apk的名字
 //修改生成的apk名字（已废弃）



```
    applicationVariants.all { variant ->
        variant.outputs.each { output ->
            def oldFile = output.outputFile
            def apkName;
            if (variant.productFlavors == null || variant.productFlavors.size() == 0)
                apkName = 'NativeAudio_' + variant.buildType.name + "_" + defaultConfig.versionName + '.apk'
            else apkName = 'NativeAudio_' + variant.productFlavors[0].name + "_" + variant.buildType.name + "_" + defaultConfig.versionName + '.apk'
            output.outputFile = new File(oldFile.parent, apkName)
            /*if (variant.buildType.name == 'debug') {
                def releaseApkName = 'NativeAudio_' + variant.productFlavors[0].name + "_" + variant.buildType.name + defaultConfig.versionName + '.apk'
                output.outputFile = new File(oldFile.parent, releaseApkName)
            }*/
        }
    }
```

// android Studio 3.5 修改了



```groovy
android.applicationVariants.all { ApplicationVariantImpl variant ->
	variant.variantData.outputScope.apkDatas.each {
        apkData ->
        def fileName = "BookCounterDemo_${variant.versionName}_${releaseTime()}_${apkData.fullName}.apk"
        apkData.outputFileName = fileName
        println apkData.getOutputFileName()
	} 
}
```

gradlew.bat app:dependencies --configuration debugCompileClasspath
