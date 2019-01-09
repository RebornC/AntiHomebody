# AntiHomebody 「非宅」

假如你属于消极、颓丧、饱受懒癌或拖延症困扰的宅人群体，那么「非宅」欢迎您的到来。

## 项目内容

一款自我管理类的APP，主要帮助用户重建积极向上的人生态度。

使用初时，界面中央是一个颓丧的小人形象，用户通过浇灌能量，使得小人逐渐成长，形象也逐渐变得开朗活泼。但是当系统检测到用户获取的能量大幅度下降或停止时，小人形象又会慢慢变得消极。这是一个由系统监控的动态过程，同时用户可与小人进行一些互动。

能量的获取主要有以下三个途径：

- [x] 逃离温巢

      鼓励用户积极外出、跑步、运动、健身。
      
      通过Android4.4以上提供的计步传感器来统计用户每天步行次数，根据次数获得能量奖励。

- [x] 具化目标，养成优秀习惯

      制定每天的计划表，并生成桌面Widget。
      
      在当天24:00前进行计划打卡，根据完成度获得能量奖励。
      
      根据每天的完成情况，将数据可视化，生成统计图表或者每日报告。
      
- [x] 监督机制

      用户加入小组进行互相监督，或者组成战队进行PK。
      
      建立排行榜奖励机制。

## 关键技术

- 搭建后台服务端，实现用户数据的网络存储与交互。

- Android动画绘制：https://github.com/yangchong211/YCBlogs/tree/master/android/%E5%8A%A8%E7%94%BB%E6%9C%BA%E5%88%B6

- Android传感器监测原理：https://developer.android.google.cn/reference/android/hardware/Sensor

- 数据可视化

- 待补充 ...

## 进度计划

![](https://github.com/RebornC/AntiHomebody/blob/master/docs/process.png?raw=true)

## 会议记录

- 2018/10/16 第一次会议

  在会议上，我主要展示了之前所阅读的两篇论文，对其内容做了细致讲解，同时提出了我的毕设构思。归纳如下：
  
  - 论文1：《[Augmented Vehicular Reality](http://winlab.rutgers.edu/~gruteser/papers/hotmobile17-final28.pdf)》
  
    **[PPT](https://github.com/RebornC/AntiHomebody/blob/master/docs/Augmented%20Vehicular%20Reality.ppt)**
    
    由于现今的无人自动驾驶汽车存在着视线的局限性，为了提高可视性，进而提高安全性，该论文探索了一种称为车辆增强现实（AVR）的系统，初步评估了在附近汽车之间传递合并视觉信息的可行性。AVR通过使车辆与其他附近车辆共享视觉信息，从而扩大车辆的视觉范围，但需要仔细的技术来对准坐标参考框架，并检测动态物体。
    
  - 论文2：《[Ledig_Photo-Realistic_Single_Image](http://202.116.81.74/cache/6/03/openaccess.thecvf.com/ccd7f70b234f953c4d87ed9c116b2242/Ledig_Photo-Realistic_Single_Image_CVPR_2017_paper.pdf)》
  
    **[PPT](https://github.com/RebornC/AntiHomebody/blob/master/docs/Ledig_Photo-Realistic_Single_Image.pptx)**
  
    论文提出了将生成式对抗网络（GAN）用于单一图像超分辨率重建（SR）问题。其出发点是因为传统的方法一般处理的是较小的放大倍数，当图像的放大倍数在4以上时，很容易使得到的结果显得过于平滑，而缺少一些细节上的真实感。因此SRGAN使用GAN来生成图像中的细节。它对代价函数做了改进，其中第一部分是基于内容的代价函数，它除了像素空间的最小均方差以外，又包含了一个基于特征空间的最小均方差，该特征是利用VGG网络提取的图像高层次特征（以预训练19层VGG网络的ReLU激活层为基础的VGG loss，对VGG19的层卷积的结果求方差），第二部分是基于对抗学习的代价函数，它主要是基于判别器输出的概率。这样使得重建的高分辨率图像与真实的高分辨率图像无论是低层次的像素值上，还是高层次的抽象特征上，在整体概念和风格上都比较接近。
    
  - 毕设构思
  
    **[PPT](https://github.com/RebornC/AntiHomebody/blob/master/docs/%E6%AF%95%E8%AE%BE%E6%9E%84%E6%80%9D%EF%BC%88%E5%88%9D%E7%89%88%EF%BC%89.pptx)**
    
    具体可参见[项目内容](#项目内容)，导师已同意该构思。

- 2018/10/30 第二次会议

  导师对我的毕设构思暂无修改意见。
  
  认真记录会议内容，对毕设项目的时间规划进行了大致的安排。
  
  
## 工作总结

### 2018/11/03 ~ 11/15

这期间主要是查阅了相关的技术资料，同时进行项目的架构设计与模块划分。具体文档正在整理中，待上传。

### 2018/11/15 ~ 11/30

开始进入编码阶段，完成布局主框架的设计。

### 2018/12/01 ~ 12/15

完成用户数据后端同步功能与计步器模块。

### 2018/12/16 ~ 12/31

完成计划打卡功能模块，同时搞定了该应用的一系列数值设计。
