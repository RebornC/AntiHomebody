# AntiHomebody 「非宅」

假如你属于消极、颓丧、饱受懒癌或拖延症困扰的宅人群体，那么「非宅」欢迎您的到来。

项目安装包下载链接：https://github.com/RebornC/AntiHomebody/releases

或者您可以直接用安卓手机扫描下方的二维码进行apk下载

<img src="https://github.com/RebornC/AntiHomebody/blob/master/images/QrCode.png?raw=true" width="17%" height="17%">

## 目录

- [项目内容](#项目内容)
- [进度计划](#进度计划)
- [技术架构](#技术架构)
  - [整体架构](#整体架构)
  - [客户端架构](#客户端架构)
  - [服务端架构](#服务端架构)
- [功能模块](#功能模块)
- [会议记录](#会议记录)
- [工作总结](#工作总结)

---

## 项目内容

友情链接：[课题背景](https://github.com/RebornC/AntiHomebody/blob/master/docs/%E8%AF%BE%E9%A2%98%E8%83%8C%E6%99%AF.md)

这是一款自我管理类的 Android APP，主要帮助用户重建积极向上的人生态度。

使用初时，界面中央是一个颓丧的动画小人形象，代表用户当前状态。用户通过积攒并浇灌能量，使得该小人的等级逐渐增长，形象也相应地变得开朗活泼，类似于养卡游戏。当系统检测到用户获取的能量大幅度下降或停止时，会进行相应的扣罚处理，小人形象又会慢慢变得颓丧。这是一个数据监测的动态过程。

能量的获取主要有以下三个途径：

- [x] 逃离温巢，养成运动习惯

      鼓励用户积极外出、跑步、运动、健身。
      
      通过Android4.4以上提供的计步传感器来统计用户每天步行次数，根据次数获得能量奖励。
      
      根据每天的计步情况，将数据可视化，生成统计图表。

- [x] 具化目标，向拖延症说“不”

      制定每天的计划清单，在当天24:00前进行计划打卡，根据完成度百分比获得对应的能量。
      
- [x] 监督机制

      建立数据监测系统。用户每日首次登录时（在账号注册已满7天的情况下），该系统会根据近7天的能量均值和上升趋势，进行活跃度的评判，从而采取对应的能量奖罚措施。
      
<br/>

用户通过以上三个途径获取能量，一方面用以浇灌主界面的小人形象，使其得以成长；另一方面，在坚持完成任务的同时，用户也逐渐摆脱颓丧的现状，养成了更加积极向上的生活态度。这便是非宅APP的意义所在。

另外，在实现核心功能的基础上，非宅APP也加入了一些其他功能，例如“虫洞”，用以记录用户的当下思绪和想法，并以时间轴形式进行展示；用户也可编辑自己的资料、查看自己的各种能量动态。同时，非宅APP导入了一些第三方SDK，用以实现微信分享、消息推送等外部功能，使其成为一个更加丰富的应用。

<br/>

## 进度计划

非宅APP的开发流程历经两个半月，期间经历了完整的软件开发周期，根据进度规划按时完成各项迭代需求，最终成品得以发布。项目的一切工作皆由本人负责。

![](https://github.com/RebornC/AntiHomebody/blob/master/images/process_2.png?raw=true)

- 前期规划
  - 需求分析：定位目标用户群体，清晰地认识到产品主要是为了解决什么痛点、满足什么需求。对初期的抽象想法进行讨论、研究、梳理、可行性评估，然后拆分成一个个明确的功能需求点，同时进行各项数值规则的设计，最后整理出一份完善且合乎逻辑的需求文档。
  - 原型设计：根据需求文档，进行APP界面的设计，包括：功能的结构性布局、各个页面的具体设计、页面间的业务逻辑交互设计。设计过程中尽量保证原创性、不涉嫌商业侵权，最终输出具体的原型设计图。
  - 技术选型：在个人的能力范围内，进行技术方案的考核。重点关注Android客户端是选择纯原生开发，还是尝试近年流行的React Native或者混合开发？服务端是自主开发还是使用现成的后端云服务？经过仔细衡量，最终服务端选择了Bmob后端云，免去了自己学习后端开发的时间成本；Android客户端选择了自己最为熟悉的Java纯原生开发，这样能保证APP的性能体验最优。因为之前有过这方面的相关开发经验，所以有信心保证项目顺利完成。
  - 可行性报告：主要是总结前三个步骤的工作成果，进行毕设创意展示，输出开题报告，以获得导师签字同意。
- 项目实施
  - 架构设计：经过评审，确定选用的技术方案与设计模式，之后便通过Android Studio创建客户端项目工程，开始搭建整体架构，并导入依赖库与外部SDK。在进行数据层的搭建时，同时进行数据库设计。
  - 模块划分：一方面是将项目工程目录按模块进行划分，以便结构清晰、代码文件各司其职。另一方面，根据需求文档，进行功能模块的划分，以便分工明确。
  - 编码阶段：将之前的原型设计转换为代码，实现完整的逻辑功能。之所以在这个阶段耗费最长的时间，是因为本项目较少依赖开源库，大部分的布局和组件都是自定义设计并实现的，过程比较费时费力。
  - 测试优化：事实上，完成每个模块的开发后便会进行自测，包括功能测试、边界测试等等，然后记录不足之处，待项目完成所有的基本功能之后再进行优化，在进行功能改进的同时也要避免响应过慢、内存泄漏、滚动掉帧等性能问题。
- 项目上线
  - 发布：进行正式签名，打包出release版本的安装包，在平台上进行发布。
  - 维护：收集反馈信息，及时修复各种bug。

非宅APP的开发流程严格遵循以上进度规划，最终项目工程的代码量约一万行，产品性能良好。目前仅在Github的Releases上托管了v1.0.0安装包，用户数量有限，但所幸收集到了不少诚心的反馈信息并有所针对性地进行维护。

<br/>

## 技术架构

任何项目都需要架构作为支撑，以此来保证项目的平稳运行，而一个设计优良的架构，对项目的扩展性、健壮性、可维护性起着不言而喻的重要性。

### 整体架构

非宅APP总体分层如下图，每一层各司其职。

![](https://github.com/RebornC/AntiHomebody/blob/master/images/architecture.jpg?raw=true)

- 展示层：表现形式为APP UI界面，负责与用户进行功能交互，接受用户的数据请求并返回正确的响应。事实上，展示层需要考虑的包括但不限于界面布局、屏幕适配、文字属性、图片资源、提示信息等等，它同时要处理好与业务层的交互方式，避免代码变得臃肿或混乱。非宅APP采用了MVP设计模式，使得该展示层具有较好的可读性、健壮性、维护性、扩展性。
- 业务层：负责处理业务逻辑。调用数据层的接口获取数据，在UI中显示；从UI中获取用户指令和输入的数据，执行业务逻辑或通过DAO写入数据源。简而言之，业务层向上，与展示层交互，向下则与数据层交互。
- 数据层：进行数据存储，提供数据访问服务。非宅APP的数据服务主要分为本地存储和云端存储。本地存储主要使用Android自带的轻型数据库SQLite，实现对用户数据和每日能量动态的数据持久保存；云端存储使用Bmob后端平台，将本地数据同步更新于云端。
- 依赖层：本项目开发所使用到的Android组件和服务，以及为了更好地实现某些编程功能而导入的第三方开源框架。通过该依赖层，APP性能得到了更好的保证。
- 外部接口：在对应的开放平台中注册后并将第三方SDK引入自己的项目。在非宅APP中，Push SDK实现推送服务，WeChat SDK实现微信分享功能。

### 客户端架构

上面主要是从宏观的角度分析了非宅APP的整体架构。在具体的**Android**开发过程中，该项目采取了MVP设计模式，将视图逻辑与业务逻辑进行分离，在优化代码性能的同时也提高了开发效率。MVP主要分为以下三层：

- View：主要指代Activity和Fragment，负责处理UI视图。
- Presenter：业务处理层，既能调用UIluoji，又能请求数据。
- Model：包含数据源以及具体的数据请求。

MVP 把Activity/Fragment中的UI逻辑抽象成View接口，把业务逻辑抽象成Presenter接口，而Model类还是原来的 Model。这样子Activity/Fragment的工作变得简单了，只用来响应生命周期，其他工作都丢到Presenter中去完成。从图3-2可以看出，Presenter是Model和View之间的桥梁，为了让结构变得更加简单，View并不能直接对Model进行操作，这也是MVP与MVC最大的不同之处。在非宅APP中，MVP模式的具体使用步骤如下。

![](https://github.com/RebornC/AntiHomebody/blob/master/images/MVP_architecture.jpg?raw=true)

1. 创建IPresenter接口，把所有业务逻辑的接口都放在这里，并创建它的实现PresenterCompl（在这里可以方便地查看业务功能，由于接口可以有多种实现所以也方便写单元测试）。
2. 创建IView接口，把所有视图逻辑的接口都放在这里，实现类是当前的Activity/Fragment。
3. 由上图可以看出，Activity里包含了一个IPresenter，而PresenterCompl里又包含了一个IView并且依赖了Model。Activity里只保留对IPresenter的调用，其它工作全部留到PresenterCompl中实现。

非宅APP在进行编程开发时，遵循MVP设计模式，对每个功能模块都进行了视图逻辑和业务逻辑的分离，将它们分别抽离到View和Presenter接口中去，并由具体的实现类来完成，这样不仅能够降低耦合，同时也提高了代码的可阅读性。对于View层而言，Activity/Fragment只处理生命周期的任务，代码一下子变得简洁，不像MVC模式那么臃肿；对于Presenter层而言，由于被抽象成接口，可以有多种具体的实现，所以方便进行单元测试。另外，把业务逻辑抽到Presenter中去，可避免后台线程引用Activity导致 Activity的资源无法被系统回收从而引起内存泄露和OOM。

事实上，在非宅APP完成基本的迭代开发后，我在后期又断断续续为它加入了一些新功能与优化方案，由于采用了MVP模式，逻辑的分离使得扩展和维护都变得十分简单，基本不用改动View层（因为它并不直接与Model层交互），只需在对应的业务逻辑中进行改动即可。这就是MVP模式的低耦合优势。

另外，本项目的客户端工程，在Android Studio上进行搭建，采用Java语言进行开发。除各项配置文件之外，项目工程的主体分为res文件夹和java文件夹。res文件夹存放资源文件，而java文件夹则放置着最重要的功能源码，里面的目录划分如下。

| 文件夹名称 | 职责 |
| :------: | :------: |
| adapter | 各类适配器 |
| common | 全局配置，包括该项目的数值设计和数据监测类 |
| db | SQLite数据库与具体的访问类 |
| model | 各类数据实体 |
| presenter | view层与model层的桥梁，包含Impl接口与实现类文件 |
| service | 后台服务类 |
| ui | 界面文件（Activity/Fragment），还有Impl（callback）接口类 |
| utils | 一系列工具类 |
| views | 一系列自定义组件 |
| widget | 桌面窗口部件 |

### 服务端架构

对于本项目而言，服务端主要用于存储用户的相关数据，满足用户在不同时间或不同设备的情境下，登录同一账号时数据能实时保存、更新、同步。

关于服务端的技术选型， 在项目前期规划时，原本是想采取传统方式，购买服务器并进行后端工程的搭建。在进行规划排期时，考虑到自己本身并无后端开发的相关经历，需要耗费一定的时间成本去加以学习，而本项目的重点应放在Android客户端的开发。因此，我摒弃了原有的想法，选用了现有的MBaaS。

MBaaS（Mobile Backend as a Service），即“移动后端服务系统”，是近年来流行的分布式架构设计。它为移动应用（例如APP、小程序）提供了一整套后端即服务的高效云服务架构、标准化服务API接口和灵活管理能力。

国内流行的MBaaS有很多，基于服务性能以及价格的衡量，我最终选用了Bmob后端云。它提供了实时数据和文件存储功能，实现APP“云和端”的数据连通，另外还内置了用户系统等服务，让开发者以最少的配置和最简单的方式使用Bmob后端云平台提供的服务，进而完全消除开发者编写服务器代码以及维护服务器的操作。

目前本项目的Bmob后端云配置如下，各项服务指标皆能满足日常访问量需求。

|      配置      |      Bmob      |
| :------: | :------: |
| CPU | 1核 |
| 内存 | 1GB |
| I/O优化 | 支持 |
| 网络类型 | 基础网络 |
| API数 | 1000000 |
| 存储量 | 20G |
| 流量 | 20GB |

非宅APP与Bmob服务端交互的架构设计如下图。

![](https://github.com/RebornC/AntiHomebody/blob/master/images/backend_architecture.jpg?raw=true)

对于个人开发者而言，Bmob提供了轻量级的SDK，以及各类封装友好的接口并附有详细的文档教程，将SDK导入客户端工程后调用API即可。

<br/>

## 功能模块

![](https://github.com/RebornC/AntiHomebody/blob/master/images/function_intro.png?raw=true)

<br/>

## 会议记录

- **2018/10/16 第一次会议**

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

- **2018/10/30 第二次会议**

  导师对我的毕设构思暂无修改意见。
  
  认真记录会议内容，对毕设项目的时间规划进行了大致的安排。
  
- **2019/01/17 第三次会议**

  此时已完成大部分编程工作，在会议上进行了毕设作品展示。（形式：视频）
  
  导师提出质疑，主要是我没有简洁地表明该APP的功能与所用到的技术。
  
  最后导师给了85的暂定分。
  
  PS：后来自己思考了一番，主要是我的作品偏工程性，重点落在产品设计、架构搭建、模块划分、功能实现，整个研发过程其实是一个小型的“产品开发周期”，而并非是某种技术知识点的研究，所以很难在短时间内进行一个完整的展示。当然我的展示方式也有很多不足之处（过于拖沓，说不清楚重点），后期会以文档形式加以说明。希望导师能多多谅解。

<br/>
  
## 工作总结

**2018/11/03 - 11/15**

- 这期间主要是查阅了相关的技术资料，同时进行项目的架构设计与模块划分。

**2018/11/15 - 11/30**

- 开始进入编码阶段，完成布局主框架的设计。

**2018/12/01 - 12/15**

- 完成用户数据后端同步功能与计步器模块。

**2018/12/16 - 12/31**

- 完成计划打卡功能模块，同时搞定了该应用的一系列数值设计。

**2019/01/01 - 01/15**

- 完成能量互动模块与用户个人功能，基本完成第一阶段的迭代需求。

**2019/01/15 - /**

- 各种 debug
- 优化数据监控模式
- 增加桌面窗口 Widget
- 部分界面由文本展示形式改为网页形式
- 集成第三方消息推送
- 增加广告屏
- 支持将"小人形象"生成图片
- 增加微信/朋友圈分享功能

<br/>

**项目总结**

本课题基于实际项目的开发需求，针对“丧文化”背景下生活态度较消极的青年群体，设计并开发了基于Android的自我管理类APP，通过一套完善的功能机制来帮助用户摆脱颓废的现状，提高自我管理能力，重建积极向上的态度。

本课题的主要工作以及取得的成果如下：

1. 在前期的项目准备中，清晰地定位用户群体，分析产品所应实现的功能机制，研究一套合理的数值规则，梳理出一份详细的功能需求文档，并输出了具体的原型设计图。
2. 明确技术选型后，开始设计优良的项目架构。以分层模式来描述项目的整体架构，明确每一层所负责的角色和职务。项目的Android客户端基于MVP设计模式进行开发，在降低视图层和业务逻辑层的耦合度的同时也提高了项目的可拓展性和维护性，大幅度优化了开发效率。项目的服务端采用了Bmob后端云，实现了用户数据的网络存储与交互。另外，客户端通过SQLite搭建本地数据库，保证数据的持久化，同时也实现了本地数据与云端数据的一致性。
3. 根据需求文档，进行每个业务模块的开发，实现了用户系统、计步器、计划清单、成长模块等核心功能。在开发过程中，本人自主设计并实现了大部分界面布局和控件，在MVP设计模式上进行业务逻辑的编写，同时引入了不少开源库来保证APP的性能流畅。
4. 在后期优化时，不断进行调试改进，同时为项目集成了很多外部接口，使得APP的功能应用更加丰富。


