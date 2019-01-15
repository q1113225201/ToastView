# ToastView
系统自带的Toast由于使用了INotificationManager来显示，5.0以上系统只要关闭通知权限将不能显示（也有一些例外）。针对这个问题有以下几种解决方案：

## 提醒用户打开通知权限
优点：有了通知权限其他都不是问题；

缺点：是否打开权限取决于用户；

## 让系统认定为系统Toast（推荐）
优点：对原先使用那套ToastUtil改动最小；

缺点：国内厂商对Android系统定制化严重，可能需要做机型兼容，不过暂时只发现华为P20；

## 使用Dialog或PopupWindow实现Toast（推荐）
缺点：需要传当前Activity的上下文；

优点：无需权限在页面上显示完全没问题；

[详见博客](https://blog.csdn.net/q1113225201/article/details/86484463)

## 依赖接入

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```
	dependencies {
	        implementation 'com.github.q1113225201:ToastView:1.0.0'
	}
```
