# OpenGl Es 学习



## 形状面和环绕

在 OpenGL 中，形状的面是由三维空间中的三个或更多点定义的表面。一个包含三个或更多三维点（在 OpenGL 中被称为顶点）的集合具有一个正面和一个背面。默认情况下，在 OpenGL 中，沿逆时针方向绘制的面为正面。

![三角形顶点处的坐标](D:\knowledgeNote\ccw-winding.png)

与 OpenGL 的“面剔除”这一常用功能有关。面剔除是 OpenGL 环境的一个选项，它允许渲染管道忽略（不计算或不绘制）形状的背面，从而节省时间和内存并缩短处理周期：

务必按照逆时针绘制顺序定义 OpenGL 形状的坐标

Android 框架支持 ETC1 压缩格式并将其作为标准功能，包括 `ETC1Util` 实用程序类和 `etc1tool` 压缩工具（位于 `<sdk>/tools/` 处的 Android SDK 内）。

<div style="background:#f8f9fa;padding:0px 20px 0px 20px">
    **注意**：ETC1 纹理压缩格式不支持具有透明度（Alpha 通道）的纹理。如果您的应用需要具有透明度的纹理，则应研究目标设备上可用的其他纹理压缩格式。
</div>


##### OpenGL ES 1.0/1.1 API 的编程明显不同于 OpenGL ES 2.0 和 3.0，因此开发者在开始借助这些 API 进行开发之前应仔细考虑以下因素：

- **设备兼容性** OpenGL ES 2.0 和 3.0性能会更好，硬件制造商对 OpenGL ES 图形管道的实现不同。
- **编码便利性** OpenGL ES 1.0/1.1 提供的API对初学者更友好。
- **图形控制力**  OpenGL ES 2.0 和 3.0提供可编程的管道，更具有控制力。
- **纹理支持** OpenGL ES 3.0 API支持纹理压缩。



# 构建 OpenGL ES 环境

一种比较直接的方式是同时实现 `GLSurfaceView` 和 `GLSurfaceView.Renderer`。(全屏的话比较推荐)

如果只是用在一小部分的布局上，可以用`textureView`. 对于动手能力强的，也可以使用`surfaceView`,代价会编写其他相当多的代码。

在AndroidManifest.xml中添加。

```xml
<uses-feature android:glEsVersion="0x00020000" android:required="true" />
```

如果使用了纹理压缩，还需添加支持的压缩格式。

```xml
<supports-gl-texture android:name="GL_OES_compressed_ETC1_RGB8_texture" />
<supports-gl-texture android:name="GL_OES_compressed_paletted_texture" />
```

如需详细了解纹理压缩格式，请参阅 [OpenGL](https://developer.android.com/guide/topics/graphics/opengl#textures) 开发者指南。

- `onSurfaceCreated()` - 调用一次以设置视图的 OpenGL ES 环境。
- `onDrawFrame()` - 每次重新绘制视图时调用。
- `onSurfaceChanged()` - 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用。

### 定义三角形

为坐标定义浮点数的顶点数组。为了最大限度地提高工作效率，您可以将这些坐标写入 `ByteBuffer` 中。



#### 应用投影和相机视图

对绘制对象坐标进行数学转换以更接近我们眼睛看到的实物。

- 投影-在openGL视图比例更改时。
- 相机视图- 根据虚拟相机的位置调整绘制对象的坐标。



一位北京的开发者写的使用openGL对视频添加滤镜的开源项目。

[VideoClipEditViewTest](https://github.com/shaopx/VideoClipEditViewTest)



#### 跨平台框架

- [learn openGL window](https://learnopengl.com/Getting-started/Hello-Window) 这是一个学习OpenGL比较号的教程，值得学习。
- [glfw]("https://github.com/glfw/glfw")是一个跨平台OpenGL的库。
- [Glad](https://glad.dav1d.de)

选择`OpenGL`版本，访问[https://glad.dav1d.de/](https://links.jianshu.com/go?to=https%3A%2F%2Fglad.dav1d.de%2F)，选择对应的版本和模式，然后点击**GENARATE**按钮，选择`glad.zip`即可。



github

[LearnOpenGL](https://github.com/JoeyDeVries/LearnOpenGL)

[GLSL_Shaders](https://developer.mozilla.org/zh-CN/docs/Games/Techniques/3D_on_the_web/GLSL_Shaders)

