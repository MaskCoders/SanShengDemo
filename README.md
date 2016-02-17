# SanShengDemo
说明：
1、定位使用 百度定位sdk 6.13
http://developer.baidu.com/map/index.php?title=android-locsdk
2、后台线程使用ground1.4 
https://github.com/telly/groundy
3、拍照保存  
位置：
同步update到系统media中，展示时直接调用系统图库即可。
4、图库如果需要则使用开源jar包'com.commit451:PhotoView:1.2.4'
https://github.com/lostghoul/PhotoView



1.命令要等发送并接受后再发下一条
2.需要封装个类，国亡主站支持多个设备并发，所以要把串口和socket封装成一个发送类，调用`
