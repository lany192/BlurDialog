[![](https://jitpack.io/v/lany192/BlurDialog.svg)](https://jitpack.io/#lany192/BlurDialog)
# BlurDialog
这是一个使用renderscript库实现的模糊背景的对话框

![image](https://github.com/lany192/BlurDialog/raw/master/Screenshot/pic1.png)
![image](https://github.com/lany192/BlurDialog/raw/master/Screenshot/pic2.png)
## Join to the project
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
    defaultConfig {
        ...
        renderscriptTargetApi 27
        renderscriptSupportModeEnabled true
        ...
    }

    dependencies {
        ...
        implementation 'com.github.lany192:BlurDialog:+'
    }

## usage

    public class SampleDialogFragment extends BlurDialogFragment {
        ...
    }
    
    public class SampleBottomDialogFragment extends BlurBottomDialogFragment {
        ...
    }
## thanks
tvbarthel https://github.com/tvbarthel/BlurDialogFragment

Dmitry Saviuk https://github.com/Dimezis/BlurView
