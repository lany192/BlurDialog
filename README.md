# BlurDialog
这是一个使用renderscript库实现的模糊背景的对话框

![image](https://github.com/lany192/BlurDialog/raw/master/Screenshot/a1.png)
![image](https://github.com/lany192/BlurDialog/raw/master/Screenshot/a2.png)
## Join to the project

    defaultConfig {
        ...
        renderscriptTargetApi 25
        renderscriptSupportModeEnabled true
        ...
    }

    dependencies {
        ...
        compile 'com.github.lany192:BlurDialog:1.0.0'
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